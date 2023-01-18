package ch.noseryoung.blj;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Generator {
    private boolean solve;
    private int width;
    private int height;
    private int scale;
    private Cell[][] currentGeneration;
    private ArrayList<Cell> correctCells = new ArrayList<>();
    Drawer drawer = new Drawer();
    Random random = new Random();

    long startTime = 0;

    Stack<Cell> history = new Stack<>();

    public Generator(int width, int height, boolean solve, int scale) {
        this.scale = scale;
        this.solve = solve;
        this.width = width % 2 == 1 ? width : width + 1;
        this.height = height % 2 == 1 ? height : height + 1;
        currentGeneration = new Cell[this.height][this.width];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (y % 2 == 0 || x % 2 == 0) {
                    currentGeneration[y][x] = new Cell(y, x, 1);
                } else {
                    currentGeneration[y][x] = new Cell(y, x, 0);
                }
            }
        }
    }

    public Generator(int width, int height, Cell[][] grid) {
        this.solve = true;
        this.width = width % 2 == 1 ? width : width + 1;
        this.height = height % 2 == 1 ? height : height + 1;
        this.currentGeneration = grid;
    }


    public void start() {
        Cell currentCell = currentGeneration[0][0];
        try {
            currentCell = currentGeneration[1][1];
        } catch (Exception e) {
            System.out.println("Geben Sie eine Zahl grösser als 1 ein!");
            System.exit(0);
        }
        //currentCell.setState(0);

        startTime = System.currentTimeMillis();

        try {
            do {
                currentGeneration[currentCell.getY()][currentCell.getX()].setState(2);
                history.push(currentCell);

                while (getNeightborCount(currentCell, 2) < 1 && !history.empty()) {
                    history.pop();

                    if (!history.empty())
                        currentCell = history.peek();
                }

                Cell nextCell = currentCell;

                if (getNeightborCount(currentCell, 2) > 0) {
                    nextCell = getRandomNeighbor(currentCell, 2);
                }


                currentGeneration[(nextCell.getY() + currentCell.getY()) / 2][(nextCell.getX() + currentCell.getX()) / 2].setState(2);


                if (currentCell.equals(currentGeneration[height - 2][width - 2]) || currentCell.equals(currentGeneration[height - 1][width - 2])) {
                    saveStack(history);
                }

                currentCell = nextCell;
                currentCell.setState(0);
            } while (!(history.empty() || allVisited()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Maze Generation");
        System.out.println("Elapsed Time = " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println();

        currentGeneration[0][1].setState(2);

        for (int i = width - 1; i > 0; i--) {
            if (currentGeneration[height - 2][i].getState() != 1) {
                currentGeneration[height - 1][i].setState(2);
                break;
            }
        }

        System.out.println("Size correct Cells: " + correctCells.size());

        drawer.draw(currentGeneration, correctCells, width, height, solve, scale);
    }

    public void startSolver() {
        Cell currentCell = currentGeneration[0][0];
        try {
            currentCell = currentGeneration[1][1];
        } catch (Exception e) {
            System.out.println("Geben Sie eine Zahl grösser als 1 ein!");
            System.exit(0);
        }

        startTime = System.currentTimeMillis();

        try {
            do {
                currentGeneration[currentCell.getY()][currentCell.getX()].setState(2);
                history.push(currentCell);

                while (getNeightborCount(currentCell, 1) < 1 && !history.empty()) {
                    history.pop();

                    if (!history.empty())
                        currentCell = history.peek();
                }

                Cell nextCell = currentCell;

                if (getNeightborCount(currentCell, 1) > 0) {
                    nextCell = getRandomNeighbor(currentCell, 1);
                }

                if (currentCell.equals(currentGeneration[height - 2][width - 2]) || currentCell.equals(currentGeneration[height - 1][width - 2])) {
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

        currentGeneration[0][1].setState(2);

        for (int i = width - 1; i > 0; i--) {
            if (currentGeneration[height - 2][i].getState() != 1) {
                currentGeneration[height - 1][i].setState(2);
                break;
            }
        }

        System.out.println("Size correct Cells: " + correctCells.size());

        drawer.draw(currentGeneration, correctCells, width, height, solve, scale);
    }

    public void saveStack(Stack<Cell> history) {

        Stack<Cell> path = (Stack<Cell>) history.clone();

        ArrayList<Cell> correctCells = new ArrayList<>();

        while (!path.empty()) {
            correctCells.add(path.pop());
        }

        int size = correctCells.size();

        try {
            for (int i = 0; i < size - 1; i++) {
                correctCells.add(new Cell((correctCells.get(i).getY() + correctCells.get(i + 1).getY()) / 2, (correctCells.get(i).getX() + correctCells.get(i + 1).getX()) / 2, 3));
                correctCells.get(i).setState(3);
                correctCells.get(i + 1).setState(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        correctCells.add(new Cell(0, 1, 3));
        correctCells.add(new Cell(this.height - 1, this.width - 2, 3));

        System.out.println("Saved Stack");

        this.correctCells = correctCells;
    }

    public int getNeightborCount(Cell currentCell, int offset) {
        int neighbours = 0;

        try {
            if (currentGeneration[currentCell.getY() - offset][currentCell.getX()].getState() == 0)
                neighbours++;
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell.getY()][currentCell.getX() + offset].getState() == 0)
                neighbours++;
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell.getY() + offset][currentCell.getX()].getState() == 0)
                neighbours++;
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell.getY()][currentCell.getX() - offset].getState() == 0)
                neighbours++;
        } catch (Exception ignore) {

        }

        return neighbours;
    }

    public Cell[] availableCells(Cell currentCell, int offset) {
        Cell[] neighbours = new Cell[getNeightborCount(currentCell, offset)];
        int index = 0;

        try {
            if (currentGeneration[currentCell.getY() - offset][currentCell.getX()].getState() == 0) {
                neighbours[index] = currentGeneration[currentCell.getY() - offset][currentCell.getX()];
                index++;
            }
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell.getY()][currentCell.getX() + offset].getState() == 0) {
                neighbours[index] = currentGeneration[currentCell.getY()][currentCell.getX() + offset];
                index++;
            }
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell.getY() + offset][currentCell.getX()].getState() == 0) {
                neighbours[index] = currentGeneration[currentCell.getY() + offset][currentCell.getX()];
                index++;
            }
        } catch (Exception ignore) {

        }

        try {
            if (currentGeneration[currentCell.getY()][currentCell.getX() - offset].getState() == 0) {
                neighbours[index] = currentGeneration[currentCell.getY()][currentCell.getX() - offset];
            }
        } catch (Exception ignore) {

        }

        return neighbours;
    }

    public Cell getRandomNeighbor(Cell cell, int offset) {

        Cell[] neighbors = availableCells(cell, offset);

        return neighbors[randomMinMax(0, Math.max(neighbors.length - 1, 0))];
    }

    public int randomMinMax(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    public boolean allVisited() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (currentGeneration[i][j].getState() != 2) {
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

    public Cell[][] getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(Cell[][] currentGeneration) {
        this.currentGeneration = currentGeneration;
    }
}
