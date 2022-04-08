package game_shop.view;

import game_shop.entities.Comment;
import game_shop.entities.Game;

import java.util.Scanner;

public class NewCommentDialog implements EntityDialog<Comment> {
    public static Scanner sc = new Scanner(System.in);

    @Override
    public Comment input() {
        Comment comment = new Comment();

        while (comment.getDescription() == null) {
            System.out.println("Enter your comment:");
            var ans = sc.nextLine();
            if (ans.length() < 6) {
                System.out.println("Error: The title should be at least 6 characters long.");
            } else {
                comment.setDescription(ans);
            }
        }

        while (comment.getGame() == null) {
            System.out.println("Enter the game title you wont comment:");
            var ans = sc.nextLine();
            if (ans.length() < 6) {
                System.out.println("Error: The title should be at least 6 characters long.");
            } else {
                Game game = new Game();
                game.setTitle(ans);
                comment.setGame(game);
            }
        }
        return comment;
    }
}
