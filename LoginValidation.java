//write a program to check login validation. user name = admin , password = 1234.
import java.util.Scanner;
public class LoginValidation {
    public static void main(String[]args){
        Scanner sc = new Scanner(System.in);

        int attempts =0;

        while (attempts<3){
            System.out.print("Enter User_ID:" );
                    String User_ID = sc.nextLine();
            System.out.print("Enter Password:" );
            String Password=sc.nextLine();
            if(User_ID.equals("admin")&& Password.equals("1234")) {
                System.out.print("Login Successfully");
                return;
            }else{
                    attempts++;
                    System.out.println("User Blocked"+(3-attempts));
                }
            }

        }
    }


