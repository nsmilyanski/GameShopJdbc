package game_shop.view;

import game_shop.entities.User;

import java.util.Scanner;

public class NewUserDialog implements EntityDialog<User> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public User input() {
        User user = new User();
        while (user.getFullName() == null) {
            System.out.println("Username:");
            var ans = sc.nextLine();
            if (ans.length() < 3) {
                System.out.println("Error: The username should be at least 3 characters long.");
            } else {
                user.setFullName(ans);
            }
        }
        while (user.getEmail() == null) {
            System.out.println("Email:");
            var ans = sc.nextLine();
            if (!ans.contains("@") || !ans.contains(".") || ans.length() < 6) {
                System.out.println("Error: The email should contains @ and . and  should be at least 6 characters long");
            } else {
                user.setEmail(ans);
            }
        }
        while (user.getPassword() == null) {
            System.out.println("Password:");
            var ans = sc.nextLine();
            if (passwordIsNotCorrect(ans)) {
                System.out.println("Password you enter is not correct, your password should be at least 6 symbols " +
                        "and should have at least 1 digit and 1 upper case!");
            } else {
                user.setPassword(ans);
            }
        }
        return user;
    }
    private boolean passwordIsNotCorrect(String password) {
        if (password.length() < 6){
            return true;
        }
        boolean hasDigit = false;
        boolean isUpperCase = false;
        for (int i = 0; i < password.length(); i++) {
            char symbol = password.charAt(i);
            if (Character.isDigit(symbol)){
                hasDigit = true;
            }else if (Character.isUpperCase(symbol)){
                isUpperCase = true;
            }
        }

        if (!hasDigit || !isUpperCase){
            return true;
        }

        return false;
    }
}
