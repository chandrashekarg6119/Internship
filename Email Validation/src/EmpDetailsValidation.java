import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class EmpDetailsValidation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Validate {
        String type();
        String message() default "Invalid Input";
    }


    static class Employee {

        @Validate(type="name", message="Invalid Name")
        String name;

        @Validate(type="email", message="Invalid Email")
        String email;

        @Validate(type="phone", message="Invalid Phone Number")
        String phone;

        Employee(String name, String email, String phone) {
            this.name = name;
            this.email = email;
            this.phone = phone;
        }
    }

    public static void validate(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Validate.class)) {

                field.setAccessible(true);
                String value = (String) field.get(obj);

                Validate v = field.getAnnotation(Validate.class);

                String regex = "";

                if (v.type().equals("name"))
                    regex = "^[A-Za-z ]+$";

                if (v.type().equals("email"))
                    regex = "^[A-Za-z0-9+_.-]+@(.+)$";

                if (v.type().equals("phone"))
                    regex = "^[0-9]{10}$";

                if (Pattern.matches(regex, value))
                    System.out.println(field.getName() + " is Valid: " + value);
                else
                    System.out.println(v.message() + " -> " + value);
            }
        }
    }


    public static void main(String[] args) throws Exception {

        Employee e1 = new Employee("Ravi Kumar", "ravi@gmail.com", "9876543210");
        Employee e2 = new Employee("Ravi123", "ravi@gmail", "12345");

        System.out.println("Employee 1 Validation:");
        validate(e1);

        System.out.println("\nEmployee 2 Validation:");
        validate(e2);
    }
}