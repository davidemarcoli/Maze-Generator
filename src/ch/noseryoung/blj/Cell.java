package ch.noseryoung.blj;

public class Cell {
    private int y;
    private int x;
    private int state;

    public Cell(int y, int x, int state) {
        this.y = y;
        this.x = x;
        this.state = state;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
