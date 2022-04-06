package game_shop.view;

import game_shop.entities.User;

import java.util.Scanner;

public class NewLogInUserDialog implements EntityDialog<User>{
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
        while (user.getPassword() == null) {
            System.out.println("Password:");
            var ans = sc.nextLine();
            if (ans.length() < 6) {
                System.out.println("Error: The username should be at least 6 characters long.");
            } else {
                user.setPassword(ans);
            }
        }
        return user;
    }

}
