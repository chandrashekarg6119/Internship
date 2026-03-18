import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class EmailValidation{


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface emailValidation {
        String message() default "Invalid Email Address";
    }


    static class User {
        @emailValidation
        String email;

        User(String email) {
            this.email = email;
        }
    }

    public static void validate(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(emailValidation.class)) {
                field.setAccessible(true);
                String email = (String) field.get(obj);

                String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

                if (Pattern.matches(regex, email)) {
                    System.out.println("Valid Email: " + email);
                } else {
                    emailValidation ann = field.getAnnotation(emailValidation.class);
                    System.out.println(ann.message() + " -> " + email);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        User u1 = new User("test@gmail.com");
        User u2 = new User("Gcs1912@gmail.com");

        validate(u1);
        validate(u2);
    }
}