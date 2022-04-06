package game_shop.view;

import game_shop.entities.Game;

import java.math.BigDecimal;
import java.util.Scanner;

public class EditGameDialog implements EntityDialog<Game>{
    Scanner sc = new Scanner(System.in);

    @Override
    public Game input() {
        Game game = new Game();
        while (game.getId() < 1) {
            System.out.println("Id:");
            var ans = sc.nextLine();
            long n = Long.parseLong(ans);
            if (n < 1) {
                System.out.println("Error: The id should be bigger or equal to 1");
            } else {
                game.setId(n);
            }
        }
        while (game.getPrice() == null) {
            System.out.println("Price:");
            var ans = sc.nextLine();
            BigDecimal price = new BigDecimal(ans);

            game.setPrice(price);
        }

        while (game.getSize() == null) {
            System.out.println("Size:");
            var ans = sc.nextLine();
            BigDecimal size = new BigDecimal(ans);

            game.setSize(size);
        }
        return game;
    }
}
