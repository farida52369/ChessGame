package sample;

public enum MoveList {

    UP(-1, 0),
    RIGHT(0, 1),
    LEFT(0, -1),
    DOWN(1, 0),

    KNIGHT_UP_RIGHT(1, 2),
    KNIGHT_RIGHT_UP(2, 1),
    KNIGHT_UP_LEFT(-1, 2),
    KNIGHT_LEFT_UP(-2, 1),

    KNIGHT_DOWN_RIGHT(1, -2),
    KNIGHT_RIGHT_DOWN(2, -1),
    KNIGHT_DOWN_LEFT(-1, -2),
    KNIGHT_LEFT_DOWN(-2, -1),

    // For White Pawn For Example
    DIAGONAL_RIGHT_UP(-1, 1),
    DIAGONAL_LEFT_UP(-1, -1),

    // For Black Pawn For Example
    DIAGONAL_RIGHT_DOWN(1, 1),
    DIAGONAL_LEFT_DOWN(1, -1),

    // 0, 2
    DOUBLE_UP(2, 0),
    DOUBLE_DOWN(-2, 0);

    // X, Y are the possible moves
    private final int x, y;

    MoveList(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

}
