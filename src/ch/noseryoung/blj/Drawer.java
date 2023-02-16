package ch.noseryoung.blj;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Drawer {
    public void draw(byte[][] currentGeneration, ArrayList<int[]> correctCells, int width, int height, boolean printSolution, int scale) {
        long startTime = System.currentTimeMillis();

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = Color.WHITE;

                if (currentGeneration[y][x] == CellType.PATH.getValue() || currentGeneration[y][x] == CellType.VISITED.getValue()) {
                    color = Color.WHITE;
                } else if (currentGeneration[y][x] == CellType.WALL.getValue()) {
                    color = Color.BLACK;
                } /*else {
                    color = new Color(54, 125, 47);
                }*/

                img.setRGB(x, y, color.getRGB());
            }
        }

        if (printSolution) {
            for (int[] cell : correctCells) {
                img.setRGB(cell[1], cell[0], 0x367D2F);
            }
        }

        //img.setRGB(width - 2, height - 2, Color.YELLOW.getRGB());


        String time = String.valueOf(LocalDateTime.now()).replaceAll("-", "").replaceAll(":", "").split("\\.")[0];


        //JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(img)));
        try {
            ImageIO.write(img, "png", new File("Maze_" + time + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (width * height < 1000000 && scale > 1) {
            try {
                System.out.println("resizing...");
                int imageType = BufferedImage.TYPE_INT_RGB;
                BufferedImage scaledBI = new BufferedImage(width * scale, height * scale, imageType);
                Graphics2D g = scaledBI.createGraphics();
                g.setComposite(AlphaComposite.Src);
                g.drawImage(img, 0, 0, width * scale, height * scale, null);
                g.dispose();

                try {
                    ImageIO.write(scaledBI, "png", new File("Maze_" + time + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception ignore) {

            }
        }

        System.out.println("Image Conversion");
        System.out.println("Elapsed Time = " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println();
    }

    final String FILE_DIRECTORY = "parts/";

    int imageCounter = 0;

    public void saveImage(byte[][] currentGeneration, ArrayList<int[]> correctCells, int width, int height, boolean printSolution) {
        long startTime = System.currentTimeMillis();

        if (imageCounter == 0) {
            File folder = new File(FILE_DIRECTORY);
            if (!folder.exists()) {
                folder.mkdir();
            }
            purgeDirectory(folder);
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = Color.WHITE;

                if (currentGeneration[y][x] == CellType.VISITED.getValue()) {
                    color = Color.GREEN;
                } else if (currentGeneration[y][x] == CellType.PATH.getValue()) {
                    color = Color.WHITE;
                } else if (currentGeneration[y][x] == CellType.WALL.getValue()) {
                    color = Color.BLACK;
                } /*else {
                    color = new Color(54, 125, 47);
                }*/

                img.setRGB(x, y, color.getRGB());
            }
        }

        if (printSolution) {
            for (int[] cell : correctCells) {
                img.setRGB(cell[1], cell[0], 0x367D2F);
            }
        }

        try {
            ImageIO.write(img, "png", new File(FILE_DIRECTORY + "Maze_" + imageCounter++ + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Image Conversion");
        System.out.println("Elapsed Time = " + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println();
    }

    public void mergeImages() {
        try {
//            Runtime.getRuntime().exec("ffmpeg -framerate 30 -i " + FILE_DIRECTORY + "Maze_%d.png -c:v libx264 -r 30 -pix_fmt yuv420p out.mp4");
            Runtime.getRuntime().exec("ffmpeg -framerate 30 -i " + FILE_DIRECTORY + "Maze_%d.png outfile.mp4");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void purgeDirectory(File dir) {
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory())
                purgeDirectory(file);
            file.delete();
        }
    }


    public byte[][] importImage(String path) {


        File input = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        byte[][] grid = new byte[image.getHeight()][image.getWidth()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                grid[y][x] = image.getRGB(x, y) == Color.BLACK.getRGB() ? CellType.WALL.getValue() : CellType.PATH.getValue();
            }
        }

        System.out.println("Height: " + image.getHeight());
        System.out.println("Width: " + image.getWidth());


        //draw(grid, new ArrayList<Cell>(), image.getWidth(), image.getHeight(), false);

        /*for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                System.out.print(grid[y][x].getState() + "   ");
            }
            System.out.println();
        }*/
        return grid;

    }

    public int[] getResolution(String path) {
        int[] resolution = new int[2];

        File folderInput = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(folderInput);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        resolution[0] = image.getHeight();
        resolution[1] = image.getWidth();

        return resolution;
    }
}
