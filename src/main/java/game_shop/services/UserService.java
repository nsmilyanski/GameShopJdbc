package game_shop.services;

import game_shop.entities.User;
import game_shop.exceptions.NonExistingEntityException;

public interface UserService {
    void registerUser(User user);

    void logInUser(String username, String password);

    User getLogInUser();

    void logOut();

    void addGamesToTheShoppingCard(String gameTitle);

    void removeGameFromCard(String gameTitle);

    void buyItem() throws NonExistingEntityException;

    void addShoppingCard(String number);
}
