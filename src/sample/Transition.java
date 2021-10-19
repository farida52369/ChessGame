package sample;

public class Transition {

    private int oldX;
    private int oldY;
    private int newX;
    private int newY;

    public Transition(int oldX, int oldY, int newX, int newY) {
        this.oldX = oldX;
        this.oldY = oldY;
        this.newX = newX;
        this.newY = newY;
    }

    //  Getters For oldX, oldY, newX, newY
    public int getNewX() {
        return this.newX;
    }

    public int getNewY() {
        return this.newY;
    }

    public int getOldX() {
        return this.oldX;
    }

    public int getOldY() {
        return this.oldY;
    }

    //  Setters For oldX, oldY, newX, newY
    public void setNewX(int newX) {
        this.newX = newX;
    }

    public void setNewY(int newY) {
        this.newY = newY;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    // Gap between X and Y
    public int getGapBetweenX() {
        return this.newX - this.oldX;
    }

    public int getGapBetweenY() {
        return this.newY - this.oldY;
    }
}

