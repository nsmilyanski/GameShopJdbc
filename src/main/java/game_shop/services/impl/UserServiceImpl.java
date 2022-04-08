package game_shop.services.impl;

import game_shop.entities.Comment;
import game_shop.entities.Game;
import game_shop.entities.ShoppingCard;
import game_shop.entities.User;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.*;
import game_shop.services.OrderService;
import game_shop.services.UserService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final OrderService orderService;
    private final ShoppingCardRepository shoppingCardRepository;
    private final CommentRepository commentRepository;
    private static User logInUser;


    public UserServiceImpl(GameRepository gameRepository, UserRepository userRepository,
                           OrderService orderService, ShoppingCardRepository shoppingCardRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.orderService = orderService;
        this.shoppingCardRepository = shoppingCardRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void registerUser(User user) {
        if (userRepository.checkEmailIfExist(user.getEmail())){
            System.out.printf("Email %s already exist!%n", user.getEmail());
            return;
        }else if (user.getFullName().length() < 3){
            System.out.println("Your username is too short");
            return;
        }else if (passwordIsNotCorrect(user.getPassword())){
            System.out.println("Password you enter is not correct, your password should be at least 6 symbols " +
                    "and should have at least 1 digit and 1 upper case!");
            return;
        }
        if (userRepository.count() == 0){
            user.setAdmin(true);
        }
        userRepository.create(user);

        System.out.println("You successfully register " + user.getFullName());
    }



    @Override
    public void logInUser(String username, String password) {
        User inUser = null;
        try {
        inUser = userRepository.logInUser(username, password);
        }catch (EntityPersistenceException e) {
            System.out.println(e.getMessage());
        }


        if (inUser != null){
            ShoppingCard card = null;
            try {
             card = shoppingCardRepository.findById(inUser.getId());

            }catch (EntityPersistenceException exception) {
                System.out.println(exception.getMessage());
            }
            if (card == null) {
                System.out.println("You do not have a shopping card!");
                System.out.println("You have to add shopping card!");
            }else {
                inUser.setShoppingCard(card);
                System.out.println("You already add shopping card, and you can have shopping!");
            }
            logInUser = inUser;
            System.out.println(inUser.getFullName() + " successfully logIn");
        }else {
            System.out.println("User does not exist please enter correct username and password");
        }

    }

    @Override
    public User getLogInUser() {
        return logInUser;
    }

    @Override
    public void logOut() {
        if (logInUser != null){
            System.out.printf("%s logout%n", logInUser.getFullName());
        }else {
            System.out.println("No logged in user!");
        }
        logInUser = null;
    }

    @Override
    public void addGamesToTheShoppingCard(String gameTitle) {
        if (logInUser == null){
            System.out.println("You are not log in!");
            return;
        }else if (logInUser.getShoppingCard() == null){
            System.out.println("You have to add shopping card!");
            return;
        }

        Game game = gameRepository.findByGameTitle(gameTitle);

        if (game == null){
            System.out.println("We do not have game with name: " + gameTitle);
            return;
        }
        logInUser.getShoppingCard().addItems(game);

        System.out.printf("You successfully add Game(%s) to your card%n", game.getTitle());

    }

    @Override
    public void removeGameFromCard(String gameTitle) {
        if (logInUser == null){
            System.out.println("You are not log in!");
            return;
        }else if (logInUser.getShoppingCard() == null){
            System.out.println("You have to add shopping card!");
            return;
        }else if (logInUser.getShoppingCard().getItems().isEmpty()){
            System.out.println("You have no items in your shopping card");
            return;
        }

        Game game = gameRepository.findByGameTitle(gameTitle);

        logInUser.getShoppingCard().removeItemFromShoppingCard(game);

        System.out.printf("You successfully remove Game(%s) from your card%n", game.getTitle());
    }

    @Override
    public void buyItem() throws NonExistingEntityException {
        if (logInUser == null){
            System.out.println("You are not log in!");
            return;
        }else if (logInUser.getShoppingCard() == null){
            System.out.println("You have to add shopping card!");
            return;
        }else if (logInUser.getShoppingCard().getItems().isEmpty()){
            System.out.println("You have no items in your shopping card");
            return;
        }

        System.out.println("Your bought games are:");

        logInUser.getShoppingCard().getItems()
                .forEach( game -> System.out.printf(" - Game: %s  Price: %s%n", game.getTitle(), game.getPrice()));

        BigDecimal sum = BigDecimal.valueOf(logInUser.getShoppingCard().getItems().stream().map(Game::getPrice).mapToDouble(BigDecimal::doubleValue)
                .sum());

        orderService.addOrder(logInUser, sum);

        logInUser.getShoppingCard().setItems(new HashSet<>());
        System.out.println("Thank you! You successfully bought games");


    }

    @Override
    public void addShoppingCard(String number) {
        if (logInUser == null){
            System.out.println("No logged in user, first you should log in!");
            return;
        }
        ShoppingCard shoppingCard = new ShoppingCard(number);
        shoppingCard.setUser(logInUser);
        shoppingCardRepository.create(shoppingCard);
        logInUser.setShoppingCard(shoppingCard);
        try {
            userRepository.update(logInUser);
        } catch (NonExistingEntityException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Successfully add shopping card!");
    }

    @Override
    public void makeComment(String description, String gameTitle) {
        if (logInUser == null){
            System.out.println("No logged in user, first you should log in!");
            return;
        }
        Game byGameTitle = null;
        try {
        byGameTitle = gameRepository.findByGameTitle(gameTitle);

        }catch (EntityPersistenceException exception) {
            System.out.println("NO game with this title!");
            return;
        }

        Comment comment = new Comment();
        comment.setDescription(description);
        comment.setUser(logInUser);
        comment.setGame(byGameTitle);

        commentRepository.create(comment);

        System.out.printf("%s successfully add comment%n", logInUser.getFullName());



    }

    @Override
    public void findAllComments() {
        Collection<Comment> all = commentRepository.findAll();
        all.stream().forEach(System.out::println);
    }

    @Override
    public void updateComment() {
        if (logInUser == null){
            System.out.println("No logged in user, first you should log in!");
            return;
        }

        List<Comment> allComments = null;

        try {

           allComments =
                    commentRepository.findAllCommentsFromCurrentUser(logInUser.getId());
        }catch (EntityPersistenceException e) {
            System.out.println("No comments from you!");
            return;
        }

        if (allComments.size() == 1) {
            System.out.println("you have gust one comment");
            System.out.println("Edit comment:");
            Comment comment = allComments.get(0);
            Scanner scanner = new Scanner(System.in);
            String description = scanner.nextLine();
            comment.setDescription(description);
            try {
                commentRepository.update(comment);
            } catch (NonExistingEntityException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("You successfully edit comment");
        }else {
            System.out.printf("You have %d comment, which one you wont edit", allComments.size());
            int index = 1;
            for (Comment comment : allComments) {
                index++;
                System.out.printf("%d - %s%n", index, comment);
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the comment you have shoosen:");
            int value = Integer.parseInt(scanner.nextLine());
            Comment comment = allComments.get(value);
            System.out.println("Enter the new comment:");
            String description = scanner.nextLine();

            comment.setDescription(description);


            try {
                commentRepository.update(comment);
            } catch (NonExistingEntityException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    @Override
    public void findAllOrders() {
        if (logInUser == null){
            System.out.println("First you have to log in!");
            return;
        }else if (!logInUser.getAdmin()){
            System.out.println("Just admin can add games to Database");
            return;
        }
        orderService.findAll();
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
