package game;

import core.renderers.MeshRenderer;
import core.resources.Mesh;
import core.resources.Texture;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Mesh boardMesh = new Mesh();
    private Mesh figureMesh = new Mesh();
    private List<Texture> figureTextures = new ArrayList<Texture>();
    private Figure[][] field = new Figure[9][9];
    private List<Figure> capturedFiguresH;
    private List<Figure> capturedFiguresL;

    private static final Map<Figure.Type, Integer> figureTexturesMap = new HashMap<Figure.Type, Integer>();
    static {
        figureTexturesMap.put(Figure.Type.KING, 0);
        figureTexturesMap.put(Figure.Type.ROOK, 1);
        figureTexturesMap.put(Figure.Type.ROOK_INVERTED, 1);
        figureTexturesMap.put(Figure.Type.BISHOP, 2);
        figureTexturesMap.put(Figure.Type.BISHOP_INVERTED, 2);
        figureTexturesMap.put(Figure.Type.GOLD_GENERAL, 3);
        figureTexturesMap.put(Figure.Type.SILVER_GENERAL, 4);
        figureTexturesMap.put(Figure.Type.SILVER_GENERAL_INVERTED, 4);
        figureTexturesMap.put(Figure.Type.KNIGHT, 5);
        figureTexturesMap.put(Figure.Type.KNIGHT_INVERTED, 5);
        figureTexturesMap.put(Figure.Type.LANCE, 6);
        figureTexturesMap.put(Figure.Type.LANCE_INVERTED, 6);
        figureTexturesMap.put(Figure.Type.PAWN, 7);
        figureTexturesMap.put(Figure.Type.PAWN_INVERTED, 7);
    }


    private static final Vector2f cellSize = new Vector2f(1.69f, 1.86f);
    private static final Vector3f figuresOffset = new Vector3f(-4 * cellSize.x, 1, -4 * cellSize.y);

    public Board() {
        boardMesh.loadFromFile("shogiban.obj");
        boardMesh.getDiffuseTexture().loadFromFile("shogiban.png");

        figureMesh.loadFromFile("figure.obj");

        for (int i = 0; i < 9; ++i) {
            figureTextures.add(new Texture());
        }
        figureTextures.get(0).loadFromFile("figure_kingw.png");
        figureTextures.get(1).loadFromFile("figure_rook.png");
        figureTextures.get(2).loadFromFile("figure_bishop.png");
        figureTextures.get(3).loadFromFile("figure_gold.png");
        figureTextures.get(4).loadFromFile("figure_silver.png");
        figureTextures.get(5).loadFromFile("figure_knight.png");
        figureTextures.get(6).loadFromFile("figure_lance.png");
        figureTextures.get(7).loadFromFile("figure_pawn.png");
        figureTextures.get(8).loadFromFile("figure_kingb.png");
    }

    public void close() {
        boardMesh.close();
        figureMesh.close();

        for (Texture texture : figureTextures) {
            texture.close();
        }
    }

    public void initField() {
        // Black figures
        setFigure(0, 8, new Figure(Figure.Type.LANCE, Figure.Color.BLACK));
        setFigure(8, 8, new Figure(Figure.Type.LANCE, Figure.Color.BLACK));

        setFigure(1, 8, new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK));
        setFigure(7, 8, new Figure(Figure.Type.KNIGHT, Figure.Color.BLACK));

        setFigure(2, 8, new Figure(Figure.Type.SILVER_GENERAL, Figure.Color.BLACK));
        setFigure(6, 8, new Figure(Figure.Type.SILVER_GENERAL, Figure.Color.BLACK));

        setFigure(3, 8, new Figure(Figure.Type.GOLD_GENERAL, Figure.Color.BLACK));
        setFigure(5, 8, new Figure(Figure.Type.GOLD_GENERAL, Figure.Color.BLACK));

        setFigure(4, 8, new Figure(Figure.Type.KING, Figure.Color.BLACK));

        setFigure(1, 7, new Figure(Figure.Type.ROOK, Figure.Color.BLACK));
        setFigure(7, 7, new Figure(Figure.Type.BISHOP, Figure.Color.BLACK));

        for (int i = 0; i < 9; ++i) {
            setFigure(i, 6, new Figure(Figure.Type.PAWN, Figure.Color.BLACK));
        }

        // White figures
        setFigure(0, 0, new Figure(Figure.Type.LANCE, Figure.Color.WHITE));
        setFigure(8, 0, new Figure(Figure.Type.LANCE, Figure.Color.WHITE));

        setFigure(1, 0, new Figure(Figure.Type.KNIGHT, Figure.Color.WHITE));
        setFigure(7, 0, new Figure(Figure.Type.KNIGHT, Figure.Color.WHITE));

        setFigure(2, 0, new Figure(Figure.Type.SILVER_GENERAL, Figure.Color.WHITE));
        setFigure(6, 0, new Figure(Figure.Type.SILVER_GENERAL, Figure.Color.WHITE));

        setFigure(3, 0, new Figure(Figure.Type.GOLD_GENERAL, Figure.Color.WHITE));
        setFigure(5, 0, new Figure(Figure.Type.GOLD_GENERAL, Figure.Color.WHITE));

        setFigure(4, 0, new Figure(Figure.Type.KING, Figure.Color.WHITE));

        setFigure(1, 1, new Figure(Figure.Type.BISHOP, Figure.Color.WHITE));
        setFigure(7, 1, new Figure(Figure.Type.ROOK, Figure.Color.WHITE));

        for (int i = 0; i < 9; ++i) {
            setFigure(i, 2, new Figure(Figure.Type.PAWN, Figure.Color.WHITE));
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
                    if (figure.getColor() == Figure.Color.WHITE) {
                        yRotation = (float)Math.PI;
                    }
                    if (figure.isInverted()) {
                        zRotation = (float)Math.PI;
                    }

                    Vector3f position = new Vector3f(figuresOffset.x, figuresOffset.y, figuresOffset.z);
                    position.x += (8 - x) * cellSize.x;
                    position.z += y * cellSize.y;

                    figureMesh.setRotation(0, yRotation, zRotation);
                    figureMesh.setPosition(position);

                    if (figure.getColor() == Figure.Color.BLACK && figure.getType() == Figure.Type.KING) {
                        figureMesh.setDiffuseTexture(figureTextures.get(8));
                    }
                    else {
                        figureMesh.setDiffuseTexture(figureTextures.get(figureTexturesMap.get(figure.getType())));
                    }

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
