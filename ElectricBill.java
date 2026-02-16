//write a program to electricity bill calculation.
// First 100units = 5rs/unit , after that next 100units = 7rs/per unit, above 200units = 10rs/per unit.

import java.util.Scanner;

public class ElectricBill {
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter no of units used: ");
        int units = sc.nextInt();
        double bill=0;
        if(units <=100){
            bill = units * 5;
        }
        if(units <=200){
            bill = (100*5)+(units-100)*7;
        }
        if(units >200){
            bill = (100*5)+(100*7)+(units-200)*10;
        }
        System.out.println("Total Electricity Bill = Rs."+bill);
    }
}
