package com.example;
import java.util.Scanner;
class ReverseNum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int num, reverse = 0, remainder;

        System.out.print("Enter a number: ");
        num = sc.nextInt();
        while (num != 0) {
            int digit = num % 10;
            reverse = reverse * 10 + digit;
            num = num / 10;
        }

        System.out.println("Reversed number = " + reverse);
    }
}
