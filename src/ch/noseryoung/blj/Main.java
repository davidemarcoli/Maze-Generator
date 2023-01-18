package ch.noseryoung.blj;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("Generieren (1) oder Lösen (2) ?");

        int choice = scan.nextInt();

        if (choice == 1) {
            System.out.println("Size (Width, Height): ");

            int width = scan.nextInt();
            int height = scan.nextInt();


            System.out.println("\n\nDo you want to print the solution?");
            scan.nextLine();
            String answer = scan.nextLine();

            System.out.print("\nDo you want to scale the Image? Input the scale: ");

            Generator generator;

            if (answer.split("")[0].equalsIgnoreCase("n")) {
                generator = new Generator(width, height, false, scan.nextInt());
            } else {
                generator = new Generator(width, height, true, scan.nextInt());
            }

            //for (int i = 0; i < 10; i++) {
            //Generator generator = new Generator(150, 150, false);
            generator.start();
            //}
        } else {
            System.out.print("Geben sie den Dateinamen ein: ");
            scan.nextLine();
            String path = scan.nextLine();

            Drawer drawer = new Drawer();

            byte[][] grid = drawer.importImage(path);

            int[] resolution = drawer.getResolution(path);

            /*for (int y = 0; y < resolution[0]; y++) {
                for (int x = 0; x < resolution[1]; x++) {
                    System.out.print(grid[y][x].getState() + "   ");
                }
                System.out.println();
            }*/

            Generator solver = new Generator(resolution[1], resolution[0], grid);
            solver.startSolver();
        }
    }
}
