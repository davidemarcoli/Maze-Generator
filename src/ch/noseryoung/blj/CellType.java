package ch.noseryoung.blj;

public enum CellType {
    PATH(0),
    WALL(1),
    VISITED(2),
    CORRECT(3);

    private final int value;

    CellType(int value) {
        this.value = value;
    }

    public byte getValue() {
        return (byte) value;
    }
}
