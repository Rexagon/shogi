package game;

import core.renderers.MeshRenderer;
import core.resources.Mesh;
import core.resources.Shader;
import core.resources.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
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

    private Shader highlightShader = new Shader();
    private Mesh boardMesh = new Mesh();
    private Mesh figureMesh = new Mesh();
    private List<Texture> figureTextures = new ArrayList<Texture>();
    private Figure[][] field = new Figure[9][9];
    private boolean[][] possibleMoves = new boolean[9][9];
    private List<Figure> capturedFiguresH;
    private List<Figure> capturedFiguresL;
    private Figure hoveredFigure;
    private int hoveredX;
    private int hoveredY;
    private Figure selectedFigure;
    private Figure.Color currentTerm = Figure.Color.BLACK;

    public Board() throws IOException {
        highlightShader.loadFromFile("highlight.vert", "highlight.frag");
        highlightShader.setAttribute(0, "vPosition");
        highlightShader.setAttribute(1, "vTextureCoords");
        highlightShader.setAttribute(2, "vNormal");

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

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                Figure figure = field[x][y];
                if (figure != null) {
                    prepareMesh(figure);

                    if (figure == hoveredFigure || figure == selectedFigure || figure.isHighlighted()) {
                        GL11.glStencilMask(0xFF);
                    }
                    else {
                        GL11.glStencilMask(0x00);
                    }

                    MeshRenderer.draw(figureMesh);
                }
                else if (possibleMoves[x][y]){
                    figure = new Figure(Figure.Type.KING, selectedFigure.getColor());
                    figure.setPosition(x, y);
                    prepareMesh(figure);

                    highlightShader.bind();

                    float alpha = 0.5f;
                    if (x == hoveredX && y == hoveredY) {
                        alpha = 0.8f;
                    }
                    highlightShader.setUniform("color", 1.0f, 1.0f, 0.0f, alpha);
                    highlightShader.unbind();

                    GL11.glEnable(GL11.GL_BLEND);
                    MeshRenderer.draw(figureMesh, highlightShader);
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
        }

        GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        GL11.glStencilMask(0x00);

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                Figure figure = field[x][y];
                if (figure != null) {
                    if (figure == hoveredFigure || figure == selectedFigure || figure.isHighlighted()) {
                        prepareMesh(figure);
                        highlightShader.bind();

                        if (figure == hoveredFigure) {
                            highlightShader.setUniform("color", 0.0f, 1.0f, 0.0f, 0.4f);
                        }
                        else if (figure == selectedFigure) {
                            highlightShader.setUniform("color", 0.3f, 1.0f, 0.0f, 1.0f);
                        }
                        else if (figure.isHighlighted()) {
                            highlightShader.setUniform("color", 1.0f, 0.2f, 0.0f, 0.4f);
                        }

                        highlightShader.unbind();

                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDepthMask(false);
                        MeshRenderer.draw(figureMesh, highlightShader);
                        GL11.glDepthMask(true);
                        GL11.glDisable(GL11.GL_BLEND);
                    }
                }
            }
        }

        GL11.glStencilMask(0xFF);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    public void setFigure(int x, int y, Figure figure) {
        if (isPositionValid(x, y)) {
            if (figure != null) {
                figure.setPosition(x, y);
            }

            field[x][y] = figure;
        }
    }

    public Figure getFigure(int x, int y) {
        if (isPositionValid(x, y)) {
            return field[x][y];
        }
        else {
            return null;
        }
    }

    public void handleMouseMove(Vector3f coords) {
        int x = 8 - (int)Math.ceil((double)(coords.x - cellSize.x / 2.0f - figuresOffset.x) / cellSize.x);
        int y = (int)Math.ceil((double)(coords.z - cellSize.y / 2.0f - figuresOffset.z) / cellSize.y);

        hoveredFigure = getFigure(x, y);
        hoveredX = x;
        hoveredY = y;
    }

    public void handleMouseClick(Vector3f coords, Figure.Color color) {
        if (currentTerm != color) return;

        int x = 8 - (int) Math.ceil((double) (coords.x - cellSize.x / 2.0f - figuresOffset.x) / cellSize.x);
        int y = (int) Math.ceil((double) (coords.z - cellSize.y / 2.0f - figuresOffset.z) / cellSize.y);

        Figure clickedFigure = getFigure(x, y);

        if (selectedFigure != null && isPositionValid(x, y) && possibleMoves[x][y]) {
            setFigure(selectedFigure.getPositionX(), selectedFigure.getPositionY(), null);
            setFigure(x, y, selectedFigure);
            selectedFigure = null;

            if (currentTerm == Figure.Color.BLACK) {
                currentTerm = Figure.Color.WHITE;
            }
            else {
                currentTerm = Figure.Color.BLACK;
            }
        }
        else {
            if (clickedFigure == null || clickedFigure.getColor() == currentTerm) {
                selectedFigure = clickedFigure;
            }
        }

        updateField();
    }

    private void prepareMesh(Figure figure) {
        float yRotation = 0;
        float zRotation = 0;
        if (figure.getColor() == Figure.Color.WHITE) {
            yRotation = (float)Math.PI;
        }
        if (figure.isInverted()) {
            zRotation = (float)Math.PI;
        }

        Vector3f position = new Vector3f(figuresOffset.x, figuresOffset.y, figuresOffset.z);
        position.x += (8 - figure.getPositionX()) * cellSize.x;
        position.z += figure.getPositionY() * cellSize.y;

        figureMesh.setRotation(0, yRotation, zRotation);
        figureMesh.setPosition(position);

        if (figure.getColor() == Figure.Color.BLACK && figure.getType() == Figure.Type.KING) {
            figureMesh.setDiffuseTexture(figureTextures.get(8));
        }
        else {
            figureMesh.setDiffuseTexture(figureTextures.get(figureTexturesMap.get(figure.getType())));
        }
    }

    private void updateField() {
        for (int x = 0; x < 9; ++x) {
            for (int y = 0; y < 9; ++y) {
                Figure figure = getFigure(x, y);
                if (figure != null) {
                    figure.setHighlighted(false);
                }
                possibleMoves[x][y] = false;
            }
        }

        if (selectedFigure != null) {
            Movement[] movements = selectedFigure.getPossibleMoves();

            int sign = selectedFigure.getColor() == Figure.Color.BLACK ? -1 : 1;

            for (Movement movement : movements) {
                int mX = selectedFigure.getPositionX() + sign * movement.directionX;
                int mY = selectedFigure.getPositionY() + sign * movement.directionY;

                while (isPositionValid(mX, mY)) {
                    Figure figure = getFigure(mX, mY);

                    if (figure != null) {
                        if (figure.getColor() != selectedFigure.getColor()) {
                            figure.setHighlighted(true);
                            possibleMoves[mX][mY] = true;
                        }
                        break;
                    }

                    possibleMoves[mX][mY] = true;

                    if (movement.finite) {
                        break;
                    }

                    mX += sign * movement.directionX;
                    mY += sign * movement.directionY;
                }
            }
        }
    }

    static boolean isPositionValid(int x, int y) {
        return x >= 0 && x <= 8 && y >= 0 && y <= 8;
    }
}
