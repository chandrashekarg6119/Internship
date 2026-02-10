package com.example;

import java.util.Scanner;

class SquareCheck {

    // Function to find distance between two points
    static int dist(int x1, int y1, int x2, int y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Input four points
        System.out.print("Enter x1 y1: ");
        int x1 = sc.nextInt();
        int y1 = sc.nextInt();

        System.out.print("Enter x2 y2: ");
        int x2 = sc.nextInt();
        int y2 = sc.nextInt();

        System.out.print("Enter x3 y3: ");
        int x3 = sc.nextInt();
        int y3 = sc.nextInt();

        System.out.print("Enter x4 y4: ");
        int x4 = sc.nextInt();
        int y4 = sc.nextInt();

        // Calculate distances
        int d1 = dist(x1, y1, x2, y2);
        int d2 = dist(x1, y1, x3, y3);
        int d3 = dist(x1, y1, x4, y4);
        int d4 = dist(x2, y2, x3, y3);
        int d5 = dist(x2, y2, x4, y4);
        int d6 = dist(x3, y3, x4, y4);

        // Square condition check
        if (d1 > 0 &&
                d1 == d2 &&
                d2 == d3 &&
                d4 == d5 &&
                d1 == d4 &&
                d6 == 2 * d1) {

            System.out.println("Yes");
        } else {
            System.out.println("No");
        }

        sc.close();
    }
}