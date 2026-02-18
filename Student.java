import java.util.Scanner;

public class Student {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] names = new String[5];
        int[] marks = new int[5];

        for (int i = 0; i < 5; i++) {
            System.out.print("Enter student name: ");
            names[i] = sc.nextLine();

            System.out.print("Enter marks: ");
            marks[i] = sc.nextInt();
            sc.nextLine();
        }
        System.out.println("\n--- Student Result ---");
        for(int i = 0; i < 5; i++) {
            char grade = 5;

            if(marks[i] >= 90)
                grade = 'A';
            else if(marks[i] >= 75)
                grade = 'B';
            else if(marks[i] >= 36)
                grade = 'C';
            else if(marks[i]<35)
                grade = 'F';

            System.out.println(names[i] +
                    " | Marks: " + marks[i] +
                    " | Grade: " + grade);
        }
    }
}
