package game;

import core.Scene;
import core.SceneManager;
import core.resources.Texture;
import gui.Gui;
import gui.Label;
import gui.TextBox;
import gui.Widget;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

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
                //TODO: start game as black
                SceneManager.addScene(new Game(Figure.Color.BLACK));
            }
        });
        gui.addWidget(buttonHost);

        buttonConnect = gui.createButton("Connect");
        buttonConnect.bind(Widget.Action.PRESSED, (Widget self) -> {
            if (menuState == MenuState.MAIN) {
                setMenuState(MenuState.CONNECTION);
            }
            else if (menuState == MenuState.HOST) {
                // TODO: start game as white
                SceneManager.addScene(new Game(Figure.Color.WHITE));
            }
            else if (menuState == MenuState.CONNECTION) {
                //TODO: connect
            }
        });
        gui.addWidget(buttonConnect);

        buttonExit = gui.createButton("Exit");
        buttonExit.bind(Widget.Action.PRESSED, (Widget self) -> {
            if (menuState == MenuState.MAIN) {
                SceneManager.deleteScene();
            }
            else {
                setMenuState(MenuState.MAIN);
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
    }

    @Override
    public void onUpdate(float dt) {
        gui.update(dt);
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
