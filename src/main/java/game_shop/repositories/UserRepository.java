package game_shop.repositories;

import game_shop.entities.User;

public interface UserRepository extends BaseRepository<Long, User> {
    User logInUser(String username, String password);


    boolean checkEmailIfExist(String email);
}
