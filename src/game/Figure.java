package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class Movement {
    int directionX;
    int directionY;
    boolean finite;

    Movement(int directionX, int directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
        finite = false;
    }

    Movement(int directionX, int directionY, boolean finite) {
        this.directionX = directionX;
        this.directionY = directionY;
        this.finite = finite;
    }
}

public class Figure {
    public enum Type {
        KING,
        ROOK,
        ROOK_INVERTED,
        BISHOP,
        BISHOP_INVERTED,
        GOLD_GENERAL,
        SILVER_GENERAL,
        SILVER_GENERAL_INVERTED,
        KNIGHT,
        KNIGHT_INVERTED,
        LANCE,
        LANCE_INVERTED,
        PAWN,
        PAWN_INVERTED
    }
    public enum Color {
        BLACK,
        WHITE
    }

    private static final Map<Type, Type> invertationDictionary = new HashMap<Type, Type>();
    private static final Map<Type, Movement[]> movements = new HashMap<Type, Movement[]>();
    static {
        invertationDictionary.put(Type.KING, Type.KING);
        invertationDictionary.put(Type.ROOK, Type.ROOK_INVERTED);
        invertationDictionary.put(Type.BISHOP, Type.BISHOP_INVERTED);
        invertationDictionary.put(Type.GOLD_GENERAL, Type.GOLD_GENERAL);
        invertationDictionary.put(Type.SILVER_GENERAL, Type.SILVER_GENERAL_INVERTED);
        invertationDictionary.put(Type.KNIGHT, Type.KNIGHT_INVERTED);
        invertationDictionary.put(Type.LANCE, Type.LANCE_INVERTED);
        invertationDictionary.put(Type.PAWN, Type.PAWN_INVERTED);

        // Normal types

        movements.put(Type.KING, new Movement[] {
                new Movement(-1, -1, true),
                new Movement(-1, 0, true),
                new Movement(-1, 1, true),
                new Movement(0, 1, true),
                new Movement(1, 1, true),
                new Movement(1, 0, true),
                new Movement(1, -1, true),
                new Movement(0, -1, true)
        });

        movements.put(Type.ROOK, new Movement[] {
                new Movement(-1, 0),
                new Movement(0, 1),
                new Movement(1, 0),
                new Movement(0, -1)
        });

        movements.put(Type.BISHOP, new Movement[] {
                new Movement(-1, -1),
                new Movement(-1, 1),
                new Movement(1, 1),
                new Movement(1, -1)
        });

        movements.put(Type.GOLD_GENERAL, new Movement[] {
                new Movement(-1, 0, true),
                new Movement(-1, 1, true),
                new Movement(0, 1, true),
                new Movement(1, 1, true),
                new Movement(1, 0, true),
                new Movement(0, -1, true)
        });

        movements.put(Type.SILVER_GENERAL, new Movement[] {
                new Movement(-1, -1, true),
                new Movement(-1, 1, true),
                new Movement(0, 1, true),
                new Movement(1, 1, true),
                new Movement(1, -1, true)
        });

        movements.put(Type.KNIGHT, new Movement[] {
                new Movement(-1, 2, true),
                new Movement(1, 2, true)
        });

        movements.put(Type.LANCE, new Movement[] {
                new Movement(0, 1)
        });

        movements.put(Type.PAWN, new Movement[] {
                new Movement(0, 1, true)
        });

        // Inverted types

        movements.put(Type.ROOK_INVERTED, new Movement[] {
                new Movement(-1, -1, true),
                new Movement(-1, 0),
                new Movement(-1, 1, true),
                new Movement(0, 1),
                new Movement(1, 1, true),
                new Movement(1, 0),
                new Movement(1, -1, true),
                new Movement(0, -1)
        });

        movements.put(Type.BISHOP_INVERTED, new Movement[] {
                new Movement(-1, -1),
                new Movement(-1, 0, true),
                new Movement(-1, 1),
                new Movement(0, 1, true),
                new Movement(1, 1),
                new Movement(1, 0, true),
                new Movement(1, -1),
                new Movement(0, -1, true)
        });

        movements.put(Type.SILVER_GENERAL_INVERTED, movements.get(Type.GOLD_GENERAL));

        movements.put(Type.KNIGHT_INVERTED, movements.get(Type.GOLD_GENERAL));

        movements.put(Type.LANCE_INVERTED, movements.get(Type.GOLD_GENERAL));

        movements.put(Type.PAWN_INVERTED, movements.get(Type.GOLD_GENERAL));
    }

    private Type type;
    private Color color;
    private int positionX = 0;
    private int positionY = 0;
    private boolean highlighted = false;

    Figure(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    Figure(Type type, Color color, int positionX, int positionY) {
        this.type = type;
        this.color = color;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public Type getType() {
        return type;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setPosition(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionY() {
        return positionY;
    }

    public void invert() {
        if (isInverted()) {
            for (Map.Entry<Type, Type> entry : invertationDictionary.entrySet()) {
                if (Objects.equals(type, entry.getValue())) {
                    type = entry.getKey();
                    break;
                }
            }
        }
        else {
            type = invertationDictionary.get(type);
        }
    }

    public boolean isInverted() {
        return type == Type.ROOK_INVERTED || type == Type.BISHOP_INVERTED ||
                type == Type.SILVER_GENERAL_INVERTED || type == Type.KNIGHT_INVERTED ||
                type == Type.LANCE_INVERTED || type == Type.PAWN_INVERTED;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public Movement[] getPossibleMoves() {
        return movements.get(type);
    }
}
