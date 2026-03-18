import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class StudentMarksAnalyzer {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\Lenovo\\Desktop\\Internship\\Read File\\src\\marks.txt";

        int total = 0;
        int count = 0;
        int highest = Integer.MIN_VALUE;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = br.readLine()) != null) {
                int mark = Integer.parseInt(line.trim());
                total += mark;
                count++;

                if (mark > highest) {
                    highest = mark;
                }
            }

            br.close();

            double average = (double) total / count;

            System.out.println("Total Marks = " + total);
            System.out.println("Average Marks = " + average);
            System.out.println("Highest Mark = " + highest);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}