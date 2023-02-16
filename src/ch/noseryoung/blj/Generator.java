package ch.noseryoung.blj;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Generator {
    private boolean solve;
    private int width;
    private int height;
    private int scale;
    private byte[][] currentGeneration;
    private ArrayList<int[]> correctCells = new ArrayList<>();
    Drawer drawer = new Drawer();
    Random random = new Random();

    long startTime = 0;

    Stack<int[]> history = new Stack<>();

    public Generator(int width, int height, boolean solve, int scale) {
        this.scale = scale;
        this.solve = solve;
        this.width = width % 2 == 1 ? width : width + 1;
        this.height = height % 2 == 1 ? height : height + 1;
        currentGeneration = new byte[this.height][this.width];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (y % 2 == 0 || x % 2 == 0) {
                    currentGeneration[y][x] = CellType.WALL.getValue();
                } else {
                    currentGeneration[y][x] = CellType.PATH.getValue();
                }
            }
        }
    }

    public Generator(int width, int height, byte[][] grid) {
        this.solve = true;
        this.width = width % 2 == 1 ? width : width + 1;
        this.height = height % 2 == 1 ? height : height + 1;
        this.currentGeneration = grid;
    }


    public void start() {
        int[] currentCell = new int[]{0, 0};
        try {
            currentCell = new int[]{1, 1};
        } catch (Exception e) {
            System.out.println("Geben Sie eine Zahl grösser als 1 ein!");
            System.exit(0);
        }
        //currentCell.setState(0);

        startTime = System.currentTimeMillis();

        try {
            do {
                currentGeneration[currentCell[0]][currentCell[1]] = CellType.VISITED.getValue();
                history.push(currentCell);

                while (getNeightborCount(currentCell, 2) < 1 && !history.empty()) {
                    history.pop();

                    if (!history.empty())
                        currentCell = history.peek();
                }

                int[] nextCell = currentCell;

                if (getNeightborCount(currentCell, 2) > 0) {
                    nextCell = getRandomNeighbor(currentCell, 2);
                }


                currentGeneration[(nextCell[0] + currentCell[0]) / 2][(nextCell[1] + currentCell[1]) / 2] = CellType.VISITED.getValue();


                if (currentCell[0] == height - 2 && currentCell[1] == width - 2 || currentCell[0] == height - 1 && currentCell[1] == width - 2) {
                    saveStack(history);
                }

                currentCell = nextCell;
                currentGeneration[currentCell[0]][currentCell[1]] = CellType.PATH.getValue();

                drawer.saveImage(currentGeneration, correctCells, width, height, solve);
            } while (!(history.empty() || allVisited()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Maze Generation");
        System.out.println("Elapsed Time = " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println();

        currentGeneration[0][1] = CellType.VISITED.getValue();

        for (int i = width - 1; i > 0; i--) {
            if (currentGeneration[height - 2][i] != CellType.WALL.getValue()) {
                currentGeneration[height - 1][i] = CellType.VISITED.getValue();
                break;
            }
        }

        System.out.println("Size correct Cells: " + correctCells.size());

        drawer.draw(currentGeneration, correctCells, width, height, solve, scale);
        drawer.mergeImages();
    }

    public void startSolver() {
        int[] currentCell = new int[]{0, 0};
        try {
            currentCell = new int[]{1, 1};
        } catch (Exception e) {
            System.out.println("Geben Sie eine Zahl grösser als 1 ein!");
            System.exit(0);
        }

        startTime = System.currentTimeMillis();

        try {
            do {
                currentGeneration[currentCell[0]][currentCell[1]] = CellType.VISITED.getValue();
                history.push(currentCell);

                while (getNeightborCount(currentCell, 1) < 1 && !history.empty()) {
                    history.pop();

                    if (!history.empty())
                        currentCell = history.peek();
                }

                int[] nextCell = currentCell;

                if (getNeightborCount(currentCell, 1) > 0) {
                    nextCell = getRandomNeighbor(currentCell, 1);
                }

                if (currentCell[0] == height - 2 && currentCell[1] == width - 2 || currentCell[0] == height - 1 && currentCell[1] == width - 2) {
                    saveStack(history);
                    break;
                }

                currentCell = nextCell;

                drawer.saveImage(currentGeneration, correctCells, width, height, solve);
            } while (true);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println("Maze Generation");
        System.out.println("Elapsed Time = " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println();

        currentGeneration[0][1] = CellType.VISITED.getValue();

        for (int i = width - 1; i > 0; i--) {
            if (currentGeneration[height - 2][i] != CellType.WALL.getValue()) {
                currentGeneration[height - 1][i] = CellType.VISITED.getValue();
                break;
            }
        }

        System.out.println("Size correct Cells: " + correctCells.size());

        drawer.draw(currentGeneration, correctCells, width, height, solve, scale);
        drawer.mergeImages();
    }

    public void saveStack(Stack<int[]> history) {

        Stack<int[]> path = (Stack<int[]>) history.clone();

        ArrayList<int[]> correctCells = new ArrayList<>();

        while (!path.empty()) {
            correctCells.add(path.pop());
        }

        int size = correctCells.size();

        try {
            for (int i = 0; i < size - 1; i++) {
                correctCells.add(new int[]{(correctCells.get(i)[0] + correctCells.get(i + 1)[0]) / 2, (correctCells.get(i)[1] + correctCells.get(i + 1)[1]) / 2});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        correctCells.add(new int[]{0, 1});
        correctCells.add(new int[]{this.height - 1, this.width - 2, 3});

        System.out.println("Saved Stack");

        this.correctCells = correctCells;
    }

    public int getNeightborCount(int[] currentCell, int offset) {
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

    public int[][] availableCells(int[] currentCell, int offset) {
        int[][] neighbours = new int[getNeightborCount(currentCell, offset)][2];
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

    public int[] getRandomNeighbor(int[] cell, int offset) {

        int[][] neighbors = availableCells(cell, offset);

        return neighbors[randomMinMax(0, Math.max(neighbors.length - 1, 0))];
    }

    public int randomMinMax(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    public boolean allVisited() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (currentGeneration[i][j] == CellType.VISITED.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte[][] getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(byte[][] currentGeneration) {
        this.currentGeneration = currentGeneration;
    }
}
