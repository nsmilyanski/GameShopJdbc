package game_shop.view;

import game_shop.entities.Game;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class NewGameDialog implements EntityDialog<Game>{
    public static Scanner sc = new Scanner(System.in);


    @Override
    public Game input() {
        Game game = new Game();

        while (game.getTitle() == null) {
            System.out.println("Title:");
            var ans = sc.nextLine();
            if (ans.length() < 3) {
                System.out.println("Error: The title should be at least 3 characters long.");
            } else {
                game.setTitle(ans);
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

        while (game.getTrailer() == null) {
            System.out.println("Trailer:");
            var ans = sc.nextLine();

            game.setTrailer(ans);
        }

        while (game.getThumbnailUrl() == null) {
            System.out.println("ThumbnailUrl:");
            var ans = sc.nextLine();

            game.setThumbnailUrl(ans);
        }
        while (game.getDescription() == null) {
            System.out.println("Description:");
            var ans = sc.nextLine();

            game.setDescription(ans);
        }

        while (game.getYear() < 1) {
            System.out.println("Release Date:");
            System.out.println("Please enter in format yyyy:");
            var ans = sc.nextLine();
            game.setYear(Integer.parseInt(ans));
        }

        return game;
    }
}
