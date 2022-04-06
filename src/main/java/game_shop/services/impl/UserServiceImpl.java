package game_shop.services.impl;

import game_shop.entities.Game;
import game_shop.entities.ShoppingCard;
import game_shop.entities.User;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.GameRepository;
import game_shop.repositories.OrderRepository;
import game_shop.repositories.UserRepository;
import game_shop.services.OrderService;
import game_shop.services.UserService;

import java.util.HashSet;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final OrderService orderService;
    private static User logInUser;


    public UserServiceImpl(GameRepository gameRepository, UserRepository userRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.orderService = orderService;
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

        orderService.addOrder(logInUser, logInUser.getShoppingCard().getItems());
        logInUser.setGames(logInUser.getShoppingCard().getItems());

        userRepository.update(logInUser);

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
        logInUser.setShoppingCard(shoppingCard);
        try {
            userRepository.update(logInUser);
        } catch (NonExistingEntityException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Successfully add shopping card!");
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
