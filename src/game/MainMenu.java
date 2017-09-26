package game;

import core.Scene;
import core.SceneManager;
import core.resources.Texture;
import core.gui.Gui;
import core.gui.Label;
import core.gui.TextBox;
import core.gui.Widget;
import game.events.ConnectionEvent;
import game.events.GameEvent;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainMenu extends Scene {
    private enum MenuState {
        MAIN,
        CONNECTION,
        CONNECTION_WAIT,
        HOST,
        HOST_WAIT
    }

    private MenuState menuState = MenuState.MAIN;
    private Gui gui = new Gui();
    private Widget imageLogo;
    private Label buttonHost;
    private Label buttonConnect;
    private Label buttonExit;
    private Label labelInfo;
    private TextBox inputIP;
    private Figure.Color selectedColor;

    private ServerSocket connectionListener;

    @Override
    public void onInit() {
        Texture logoTexture = new Texture();
        logoTexture.loadFromFile("logo.png");
        imageLogo = new Widget();
        imageLogo.setTexture(logoTexture);
        imageLogo.setSize(logoTexture.getSize());
        gui.addWidget(imageLogo);

        buttonHost = gui.createButton("Host game");
        buttonHost.bind(Widget.Action.PRESSED, (Widget self) -> {
            if (menuState == MenuState.MAIN) {
                setMenuState(MenuState.HOST);
            }
            else if (menuState == MenuState.HOST) {
                hostGame(Figure.Color.BLACK);
            }
        });
        gui.addWidget(buttonHost);

        buttonConnect = gui.createButton("Connect");
        buttonConnect.bind(Widget.Action.PRESSED, (Widget self) -> {
            if (menuState == MenuState.MAIN) {
                setMenuState(MenuState.CONNECTION);
            }
            else if (menuState == MenuState.HOST) {
                hostGame(Figure.Color.WHITE);
            }
            else if (menuState == MenuState.CONNECTION) {
                connectToHost(inputIP.getText());
            }
        });
        gui.addWidget(buttonConnect);

        buttonExit = gui.createButton("Exit");
        buttonExit.bind(Widget.Action.PRESSED, (Widget self) -> {
            if (menuState == MenuState.MAIN) {
                SceneManager.deleteScene();
            }
            else {
                resetAll();
            }
        });
        gui.addWidget(buttonExit);

        labelInfo = gui.createLabel("Just a tiny coursework made by Ivan Kalinin. Version 0.8");
        labelInfo.setTextAlign(Label.HorizontalAlign.RIGHT, Label.VerticalAlign.BOTTOM);
        labelInfo.setFontSize(12);
        gui.addWidget(labelInfo);

        inputIP = gui.createTextBox();
        inputIP.setMaxTextLength(15);
        gui.addWidget(inputIP);

        setMenuState(MenuState.MAIN);
        updateLayout();
    }

    @Override
    public void onClose() {
        gui.close();
        Network.close();
    }

    @Override
    public void onUpdate(float dt) {
        gui.update(dt);

        GameEvent event;
        while ((event = Network.popEvent()) != null) {
            switch (event.getType()) {
                case CONNECTION:
                    if (connectionListener != null) {
                        SceneManager.addScene(new Game(selectedColor));
                    }
                    else {
                        SceneManager.addScene(new Game(((ConnectionEvent)event).getSelectedColor()));
                    }
                    break;

                case EXIT:
                    resetAll();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        gui.draw(dt);
    }

    @Override
    public void onResize(int width, int height) {
        updateLayout();
    }

    @Override
    public void onReturn() {
        resetAll();
    }

    private void hostGame(Figure.Color hostColor) {
        try {
            setMenuState(MenuState.HOST_WAIT);

            if (connectionListener != null) {
                connectionListener.close();
            }
            connectionListener = new ServerSocket(7742);

            selectedColor = hostColor;

            new Thread(() -> {
                try {
                    Network.init(connectionListener.accept());

                    Figure.Color oppositeColor = Figure.Color.BLACK;
                    if (hostColor == Figure.Color.BLACK) {
                        oppositeColor = Figure.Color.WHITE;
                    }

                    Network.send(new ConnectionEvent(oppositeColor));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToHost(String host) {
        setMenuState(MenuState.CONNECTION_WAIT);

        new Thread(() -> {
            try {
                Network.init(new Socket(host, 7742));

                Network.send(new ConnectionEvent(Figure.Color.BLACK)); // only the fact of packet send is important
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void resetAll() {
        try {
            if (connectionListener != null) {
                connectionListener.close();
                connectionListener = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Network.close();
        setMenuState(MenuState.MAIN);
        updateLayout();
    }

    private void updateLayout() {
        float screenWidth = Display.getWidth();
        float screenHeight = Display.getHeight();

        float lineHeight = 40.0f;

        Vector2f formSize = new Vector2f(300.0f, 7.0f * lineHeight);
        Vector2f formPosition = new Vector2f((screenWidth - formSize.x) / 2.0f, (screenHeight - formSize.y) / 2.0f);

        imageLogo.setPosition((screenWidth - imageLogo.getSize().x) / 2.0f, formPosition.y - imageLogo.getSize().y);

        inputIP.setPosition(formPosition.x, formPosition.y + lineHeight);
        inputIP.setSize(formSize.x, lineHeight);

        buttonHost.setPosition(formPosition.x, formPosition.y + lineHeight);
        buttonHost.setSize(formSize.x, lineHeight);

        buttonConnect.setPosition(formPosition.x, formPosition.y + 3 * lineHeight);
        buttonConnect.setSize(formSize.x, lineHeight);

        buttonExit.setPosition(formPosition.x, formPosition.y + 5 * lineHeight);
        buttonExit.setSize(formSize.x, lineHeight);

        labelInfo.setPosition(0, Display.getHeight() - lineHeight);
        labelInfo.setSize(Display.getWidth(), lineHeight);
    }

    private void setMenuState(MenuState menuState) {
        this.menuState = menuState;

        switch (menuState) {
            case MAIN:
                inputIP.setVisible(false);
                buttonHost.setVisible(true);
                buttonHost.setText("Host");
                buttonConnect.setVisible(true);
                buttonConnect.setText("Connect");
                buttonExit.setText("Exit");
                break;

            case CONNECTION:
                inputIP.setVisible(true);
                gui.focusWidget(inputIP);
                buttonHost.setVisible(false);
                buttonConnect.setVisible(true);
                buttonConnect.setText("Connect to this ip");
                buttonExit.setText("Back");
                break;

            case CONNECTION_WAIT:
                inputIP.setVisible(false);
                buttonHost.setVisible(false);
                buttonConnect.setVisible(true);
                buttonConnect.setText("Please wait...");
                buttonExit.setText("Back");
                break;

            case HOST:
                inputIP.setVisible(false);
                buttonHost.setVisible(true);
                buttonHost.setText("Host as black");
                buttonConnect.setVisible(true);
                buttonConnect.setText("Host as white");
                buttonExit.setText("Back");
                break;

            case HOST_WAIT:
                inputIP.setVisible(false);
                buttonHost.setVisible(true);
                buttonHost.setText("Please wait for a player...");
                buttonConnect.setVisible(false);
                buttonExit.setText("Back");
                break;

            default:
                break;
        }
    }
}
