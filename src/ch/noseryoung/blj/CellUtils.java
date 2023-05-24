package ch.noseryoung.blj;

import java.util.Random;

public abstract class CellUtils {

    private static final Random random = new Random();

    public static int getNeighborCount(byte[][] currentGeneration, int[] currentCell, int offset) {
        int neighbours = 0;

        try {
            if (currentGeneration[currentCell[0] - offset][currentCell[1]] == CellType.PATH.getValue())
                neighbours++;
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell[0]][currentCell[1] + offset] == CellType.PATH.getValue())
                neighbours++;
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell[0] + offset][currentCell[1]] == CellType.PATH.getValue())
                neighbours++;
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell[0]][currentCell[1] - offset] == CellType.PATH.getValue())
                neighbours++;
        } catch (Exception ignore) {

        }

        return neighbours;
    }

    public static int[][] getAvailableCells(byte[][] currentGeneration, int[] currentCell, int offset) {
        int[][] neighbours = new int[getNeighborCount(currentGeneration, currentCell, offset)][2];
        int index = 0;

        try {
            if (currentGeneration[currentCell[0] - offset][currentCell[1]] == CellType.PATH.getValue()) {
                neighbours[index] = new int[]{currentCell[0] - offset, currentCell[1]};
                index++;
            }
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell[0]][currentCell[1] + offset] == CellType.PATH.getValue()) {
                neighbours[index] = new int[]{currentCell[0], currentCell[1] + offset};
                index++;
            }
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell[0] + offset][currentCell[1]] == CellType.PATH.getValue()) {
                neighbours[index] = new int[]{currentCell[0] + offset, currentCell[1]};
                index++;
            }
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell[0]][currentCell[1] - offset] == CellType.PATH.getValue()) {
                neighbours[index] = new int[]{currentCell[0], currentCell[1] - offset};
            }
        } catch (Exception ignore) {

        }

        return neighbours;
    }

    public static int[] getRandomNeighbor(byte[][] currentGeneration, int[] cell, int offset) {

        int[][] neighbors = getAvailableCells(currentGeneration, cell, offset);

        return neighbors[randomMinMax(0, Math.max(neighbors.length - 1, 0))];
    }

    public static int randomMinMax(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }
}
