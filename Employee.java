class Employee {
    int salary = 10000;
}
class Manager extends Employee {
    int bonus = 5000;
    void showSalary() {
        int totalSalary = salary + bonus;
        System.out.println("Total Salary: " + totalSalary);
    }
}
public class Main {
    public static void main(String[] args) {
        Manager m = new Manager();
        m.showSalary();
    }
}
