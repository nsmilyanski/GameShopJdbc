package game_shop.view;

import game_shop.entities.ShoppingCard;

import java.util.Scanner;

public class AddShoppingCardDialog implements EntityDialog<ShoppingCard> {
    public static Scanner sc = new Scanner(System.in);
    @Override
    public ShoppingCard input() {
        ShoppingCard shoppingCard = new ShoppingCard();
        while (shoppingCard.getNumber() == null) {
            System.out.println("Shopping card Number:");
            var ans = sc.nextLine();
            if (ans.length() < 6 && !containsDigit(ans)) {
                System.out.println("Error: The number should be at least 6 characters long and should contains at least " +
                        "1 number");
            } else {
                shoppingCard.setNumber(ans);
            }
        }
        return null;
    }

    private boolean containsDigit(String ans) {
        for (int i = 0; i < ans.length(); i++) {
            char symbol = ans.charAt(i);
            if (Character.isDigit(symbol)){
                return true;
            }
        }

        return false;
    }
}
