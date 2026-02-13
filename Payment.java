import java.util.Scanner;
class Payment {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double walletBalance = 10000;
        int choice;
        double amount;
        do {
            System.out.println("\nWallet Balance = " + walletBalance);
            System.out.println("Payment Method");
            System.out.println("1. UPI 2. Card 3. Cash 4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            if (choice == 4) {
                break;
            }
            System.out.print("Enter amount to pay: ");
            amount = sc.nextDouble();
            if (amount <= walletBalance) {
                walletBalance = walletBalance - amount;
                System.out.println("Payment Successful");
                System.out.println("Updated Balance = " + walletBalance);
            } else {
                System.out.println("Insufficient Balance");
            }
        } while (true);
        sc.close();
    }
}
