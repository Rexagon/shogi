package game;

import core.renderers.MeshRenderer;
import core.resources.Mesh;
import core.resources.Texture;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Mesh boardMesh = new Mesh();
    private Mesh figureMesh = new Mesh();
    private List<Texture> figureTextures = new ArrayList<Texture>();
    private Figure[][] field = new Figure[9][9];
    private List<Figure> capturedFiguresH;
    private List<Figure> capturedFiguresL;

    private static final Vector2f cellSize = new Vector2f(1.69f, 1.86f);
    private static final Vector3f figuresOffset = new Vector3f(-4 * cellSize.x, 1, -4 * cellSize.y);

    public Board() {
        boardMesh.loadFromFile("shogiban.obj");
        boardMesh.getDiffuseTexture().loadFromFile("shogiban.png");

        figureMesh.loadFromFile("figure.obj");
        //temp solution
        figureMesh.getDiffuseTexture().loadFromFile("figure.png");

        setFigure(0, 0, new Figure(Figure.Type.LANCE, Figure.Color.BLACK));
        setFigure(1, 0, new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK));
        setFigure(2, 0, new Figure(Figure.Type.SILVER_GENERAL, Figure.Color.BLACK));
        setFigure(3, 0, new Figure(Figure.Type.GOLD_GENERAL, Figure.Color.BLACK));
        setFigure(5, 0, new Figure(Figure.Type.LANCE, Figure.Color.BLACK));
        setFigure(6, 0, new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK));
        setFigure(7, 0, new Figure(Figure.Type.SILVER_GENERAL, Figure.Color.BLACK));
        setFigure(8, 0, new Figure(Figure.Type.GOLD_GENERAL, Figure.Color.BLACK));
    }

    public void close() {
        boardMesh.close();
        figureMesh.close();

        for (Texture texture : figureTextures) {
            texture.close();
        }
    }

    public void draw(float dt) {
        MeshRenderer.draw(boardMesh);

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                Figure figure = field[x][y];
                if (field[x][y] != null) {
                    float yRotation = 0;
                    float zRotation = 0;
                    if (figure.getColor() == Figure.Color.BLACK) {
                        yRotation = (float)Math.PI;
                    }
                    if (figure.isInverted()) {
                        zRotation = (float)Math.PI;
                    }

                    Vector3f position = new Vector3f(figuresOffset.x, figuresOffset.y, figuresOffset.z);
                    position.x += x * cellSize.x;
                    position.z += (8 - y) * cellSize.y;

                    figureMesh.setPosition(position);
                    figureMesh.setRotation(0, yRotation, zRotation);

                    //TODO: bind texture
                    MeshRenderer.draw(figureMesh);
                }
            }
        }
    }

    public void setFigure(int x, int y, Figure figure) {
        figure.setPosition(x, y);
        field[x][y] = figure;
    }
}
