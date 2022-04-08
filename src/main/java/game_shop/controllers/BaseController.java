package game_shop.controllers;

import game_shop.exceptions.NonExistingEntityException;
import game_shop.services.GameService;
import game_shop.services.UserService;
import game_shop.view.*;

import java.util.List;
import java.util.Scanner;

public class BaseController {
    private GameService gameService;
    private UserService userService;


    public BaseController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    public void init() {
        var menu = new Menu("Game Menu", List.of(
                new Menu.Option("Register User:", () -> {
                    var user = new NewUserDialog().input();
                    userService.registerUser(user);
                    return String.format("User %s register successfully!", user.getFullName());
                }),
                new Menu.Option("Log in User", () -> {
                    var user = new NewLogInUserDialog().input();
                    userService.logInUser(user.getFullName(), user.getPassword());
                    return "You welcome";
                }),
                new Menu.Option("LogOut", () -> {
                    userService.logOut();
                    return "See you again...";
                }),
                new Menu.Option("AddGame", () -> {
                    var game = new NewGameDialog().input();
                    gameService.addGame(game);
                    return "";
                }),
                new Menu.Option("Edit Game", () -> {
                    var game =new EditGameDialog().input();
                    gameService.editGame(game.getId(), game.getPrice(), game.getSize());
                    return "";
                }),
                new Menu.Option("Delete Game", () -> {
                    System.out.println("Please enter the id:");
                    Scanner scanner = new Scanner(System.in);
                    gameService.deleteGame(Long.parseLong(scanner.nextLine()));
                    return "";
                }),
                new Menu.Option("Add shopping card", () -> {
                    var user = new NewLogInUserDialog().input();
                    Scanner scanner = new Scanner(System.in);
                    userService.addShoppingCard(scanner.nextLine());
                    return "Have fun!";
                }),
                new Menu.Option("ShowAllGames", () -> {
                    System.out.println("Choose your favorite game:");
                    gameService.findAllGames();
                    return "";
                }),
                new Menu.Option("Detail for current game", () -> {
                    System.out.println("Please enter the game title:");
                    Scanner scanner = new Scanner(System.in);
                    gameService.findGameByTitle(scanner.nextLine());
                    return "";
                }),
                new Menu.Option("Add Item to the Shopping Card", () -> {
                    System.out.println("Please enter the game title:");
                    Scanner scanner = new Scanner(System.in);
                    userService.addGamesToTheShoppingCard(scanner.nextLine());
                    return "";
                }),
                new Menu.Option("Remove Item from Shopping card", () -> {
                    System.out.println("Please enter the game title:");
                    Scanner scanner = new Scanner(System.in);
                    userService.removeGameFromCard(scanner.nextLine());
                    return "";
                }),
                new Menu.Option("Buy items", () -> {
                    System.out.println("Than you:");
                    try {
                        userService.buyItem();
                    } catch (NonExistingEntityException e) {
                        System.out.println(e.getMessage());
                    }
                    return "";
                }),
                new Menu.Option("Comment game", () -> {
                    var comment = new NewCommentDialog().input();
                    userService.makeComment(comment.getDescription(), comment.getGame().getTitle());
                    return "";
                }),
                new Menu.Option("ShowAllComments", () -> {
                    userService.findAllComments();
                    return "That is all comments";
                }),
                new Menu.Option("Edit comments from you", () -> {
                    userService.updateComment();
                    return "";
                }),
                new Menu.Option("ShowAllOrders(just for admin)", () -> {
                    userService.findAllOrders();
                    return "";
                })

        ));
        menu.show();
    }

}
