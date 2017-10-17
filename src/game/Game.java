package game;

import core.*;
import core.gui.Gui;
import core.gui.Label;
import core.gui.Widget;
import core.renderers.MeshRenderer;
import core.renderers.SkyboxRenderer;
import core.renderers.TextRenderer;
import core.resources.Mesh;
import core.gui.Text;
import game.events.ExitEvent;
import game.events.GameEvent;
import game.events.MouseClickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

import java.io.IOException;

public class Game extends Scene {
    private Figure.Color color;
    private Figure.Color oppositeColor;

    private Gui gui = new Gui();
    private Label labelTurn;
    private Widget formMessage;
    private Label labelMessageTitle;
    private Label buttonMessageYes;
    private Label buttonMessageNo;
    private Board board;
    private Mesh table;
    private Mesh island;
    private Mesh figure;
    private boolean messageVisible = false;

    public Game(Figure.Color color) {
        this.color = color;
        oppositeColor = Figure.Color.BLACK;
        if (color == Figure.Color.BLACK) {
            oppositeColor = Figure.Color.WHITE;
        }

        CameraController.resetCamera(color);
    }

    @Override
    public void onInit() {
        labelTurn = new Label("");
        labelTurn.setFontSize(22);
        labelTurn.setColor(new Vector4f(0.3f, 0.3f, 0.3f, 1.0f));
        gui.addWidget(labelTurn);

        labelMessageTitle = gui.createLabel("Do you want to flip figure?");
        labelMessageTitle.setColor(0.3f, 0.3f, 0.3f, 1.0f);
        gui.addWidget(labelMessageTitle);

        buttonMessageYes = gui.createButton("Yes");
        buttonMessageYes.bind(Widget.Action.PRESSED, (Widget widget) -> {
            if (messageVisible) {
                Figure figure = board.getSelectedFigure();
                if (figure != null) {
                    boolean inverted = board.canBeInverted(color, board.getSelectedFigure());
                    board.invertFigure(color, figure);
                    board.endTurn();
                    Network.send(new MouseClickEvent(figure.getPositionX(), figure.getPositionY(), inverted));
                    setMessageVisible(false);
                }
            }
        });
        gui.addWidget(buttonMessageYes);

        buttonMessageNo = gui.createButton("No");
        buttonMessageNo.bind(Widget.Action.PRESSED, (Widget widget) -> {
            if (messageVisible) {
                Figure figure = board.getSelectedFigure();
                if (figure != null) {
                    board.endTurn();
                    Network.send(new MouseClickEvent(figure.getPositionX(), figure.getPositionY(), false));
                    setMessageVisible(false);
                }
            }
        });
        gui.addWidget(buttonMessageNo);

        formMessage = new Widget();
        formMessage.setColor(0.3f, 0.3f, 0.3f, 1.0f);
        formMessage.setActive(false);
        gui.addWidget(formMessage);

        setMessageVisible(false);

        // Loading table
        table = new Mesh();
        table.loadFromFile("table.obj");
        table.getDiffuseTexture().loadFromFile("table.png");
        table.getDiffuseTexture().setFlitering(GL11.GL_LINEAR, GL11.GL_LINEAR);

        // Loading island
        island = new Mesh();
        island.loadFromFile("island.obj");
        island.getDiffuseTexture().loadFromFile("island.png");
        island.setPosition(0, -10, 0);

        figure = new Mesh();
        figure.loadFromFile("figure.obj");
        figure.getDiffuseTexture().loadFromFile("figure_kingb.png");

        try {
            board = new Board();
        } catch (IOException e) {
            e.printStackTrace();
        }
        board.initField();

        onResize(Display.getWidth(), Display.getHeight());
    }

    @Override
    public void onClose() {
        gui.close();
        board.close();
        table.close();
        island.close();

        Network.send(new ExitEvent(ExitEvent.ExitType.DISCONNECT));
    }

    @Override
    public void onUpdate(float dt) {
        gui.update(dt);
        CameraController.update(dt);

        if (Input.isKeyDown(Keyboard.KEY_ESCAPE)) {
            SceneManager.deleteScene();
            return;
        }

        if (!messageVisible) {
            float mouseX = (float) Mouse.getX() / ((float) Display.getWidth() * 0.5f) - 1.0f;
            float mouseY = (float) Mouse.getY() / ((float) Display.getHeight() * 0.5f) - 1.0f;

            Matrix4f projection = CameraController.getMainCamera().getProjection();
            Matrix4f view = CameraController.getMainCamera().getTransformation();
            Matrix4f invVP = Matrix4f.invert(Matrix4f.mul(projection, view, null), null);

            Vector4f screenPos = new Vector4f(mouseX, mouseY, 1.0f, 1.0f);
            Vector4f worldPos = Matrix4f.transform(invVP, screenPos, null);
            Vector3f rayOrigin = CameraController.getMainCamera().getPosition();
            Vector3f ray = new Vector3f(worldPos.x / worldPos.w, worldPos.y / worldPos.w, worldPos.z / worldPos.w);
            ray = Vector3f.sub(ray, rayOrigin, null);
            ray.normalise(ray);

            Vector3f planeNormal = new Vector3f(0, 1, 0);

            if (Vector3f.dot(planeNormal, ray) != 0) {
                Vector3f intersection = getIntersection(new Vector3f(0, 1, 0), planeNormal, rayOrigin, ray);

                Vector2f cellSize = Board.getCellSize();
                Vector3f figuresOffset = Board.getFiguresOffset();

                int x = 8 - (int) Math.ceil((double) (intersection.x - cellSize.x / 2.0f - figuresOffset.x) / cellSize.x);
                int y = (int) Math.ceil((double) (intersection.z - cellSize.y / 2.0f - figuresOffset.z) / cellSize.y);

                if (!Board.isPositionValid(x, y)) {
                    intersection = getIntersection(new Vector3f(0, 0, 0), planeNormal, rayOrigin, ray);
                    y = (int) Math.ceil((double) (intersection.z / 1.1f - cellSize.y / 2.0f - figuresOffset.z) / cellSize.y);
                }

                board.handleMouseMove(x, y);
                if (Input.isMouseButtonPressed(0) && board.getCurrentTerm() == color) {
                    board.handleMouseClick(x, y, color);
                    Figure figure = board.getSelectedFigure();
                    if (figure != null) {
                        if (board.canBeInverted(color, board.getSelectedFigure())) {
                            setMessageVisible(true);
                        } else {
                            board.endTurn();
                            Network.send(new MouseClickEvent(figure.getPositionX(), figure.getPositionY(), false));
                        }
                    }
                }
            }
        }

        GameEvent event;
        while ((event = Network.popEvent()) != null) {
            switch (event.getType()) {
                case EXIT:
                    SceneManager.deleteScene();
                    break;

                case MOUSE_CLICK:
                    MouseClickEvent mouseClick = ((MouseClickEvent)event);
                    board.handleMouseClick(mouseClick.getX(), mouseClick.getY(), oppositeColor);
                    if (mouseClick.isInverted()) {
                        board.invertFigure(oppositeColor, board.getSelectedFigure());
                    }
                    board.endTurn();
                    break;
            }
        }
    }

    @Override
    public void onDraw(float dt) {
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        SkyboxRenderer.draw();

        MeshRenderer.draw(island);
        MeshRenderer.draw(table);
        MeshRenderer.draw(figure);

        board.draw(dt);

        labelTurn.setText(board.getCurrentTerm() == color ? "Your turn" : "Opponent's turn");
        gui.draw(dt);
    }

    @Override
    public void onResize(int screenWidth, int screenHeight) {
        float lineHeight = 40.0f;

        Vector2f labelSize = new Vector2f(256.0f, lineHeight);

        labelTurn.setPosition((screenWidth - labelSize.x) / 2.0f, 0.0f);
        labelTurn.setSize(labelSize);

        Vector2f formSize = new Vector2f(400.0f, 3.0f * lineHeight);
        Vector2f formPosition = new Vector2f((screenWidth - formSize.x) / 2.0f, (screenHeight - formSize.y) / 2.0f);

        formMessage.setPosition(formPosition);
        formMessage.setSize(formSize);

        labelMessageTitle.setPosition(formPosition.x, formPosition.y);
        labelMessageTitle.setSize(formSize.x, lineHeight);

        buttonMessageYes.setPosition(formPosition.x + 20.0f, formPosition.y + lineHeight * 1.5f);
        buttonMessageYes.setSize(100.0f, lineHeight);

        buttonMessageNo.setPosition(formPosition.x + formSize.x - 120.0f, formPosition.y + lineHeight * 1.5f);
        buttonMessageNo.setSize(100.0f, lineHeight);
    }

    private Vector3f getIntersection(Vector3f planeOrigin, Vector3f planeNormal, Vector3f rayOrigin, Vector3f ray) {
        float u = Vector3f.dot(planeNormal, Vector3f.sub(planeOrigin, rayOrigin, null)) / Vector3f.dot(planeNormal, ray);
        return Vector3f.add(rayOrigin, new Vector3f(ray.x * u, ray.y * u, ray.z * u), null);
    }

    private void setMessageVisible(boolean visible) {
        messageVisible = visible;
        formMessage.setVisible(visible);
        labelMessageTitle.setVisible(visible);
        buttonMessageYes.setVisible(visible);
        buttonMessageNo.setVisible(visible);
    }
}
