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
            byte valueAccessTest = currentGeneration[1][1];
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

                while (CellUtils.getNeighborCount(currentGeneration, currentCell, 2) < 1 && !history.empty()) {
                    history.pop();

                    if (!history.empty())
                        currentCell = history.peek();
                }

                int[] nextCell = currentCell;

                if (CellUtils.getNeighborCount(currentGeneration, currentCell, 2) > 0) {
                    nextCell = CellUtils.getRandomNeighbor(currentGeneration, currentCell, 2);
                }


                currentGeneration[(nextCell[0] + currentCell[0]) / 2][(nextCell[1] + currentCell[1]) / 2] = CellType.VISITED.getValue();


                if (currentCell[0] == height - 2 && currentCell[1] == width - 2 || currentCell[0] == height - 1 && currentCell[1] == width - 2) {
                    System.out.println("Reached histpry dumop");
                    saveStack(history);
                }

                currentCell = nextCell;
                currentGeneration[currentCell[0]][currentCell[1]] = CellType.PATH.getValue();
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

                while (CellUtils.getNeighborCount(currentGeneration, currentCell, 1) < 1 && !history.empty()) {
                    history.pop();

                    if (!history.empty())
                        currentCell = history.peek();
                }

                int[] nextCell = currentCell;

                if (CellUtils.getNeighborCount(currentGeneration, currentCell, 1) > 0) {
                    nextCell = CellUtils.getRandomNeighbor(currentGeneration, currentCell, 1);
                }

                if (currentCell[0] == height - 2 && currentCell[1] == width - 2 || currentCell[0] == height - 1 && currentCell[1] == width - 2) {
                    saveStack(history);
                    break;
                }

                currentCell = nextCell;
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

    public boolean allVisited() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (currentGeneration[i][j] == CellType.VISITED.getValue()) {
                    return false;
                }
            }
        }
        System.out.println("All Cells visited");
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
