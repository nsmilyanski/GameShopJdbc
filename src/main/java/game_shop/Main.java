package game_shop;

import game_shop.controllers.BaseController;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.jdbc.JdbcSimpleDemo;
import game_shop.repositories.GameRepository;
import game_shop.repositories.OrderRepository;
import game_shop.repositories.UserRepository;
import game_shop.repositories.impl.GameRepositoryJdbc;
import game_shop.repositories.impl.OrderRepositoryJdbc;
import game_shop.repositories.impl.UserRepositoryJdbc;
import game_shop.services.GameService;
import game_shop.services.OrderService;
import game_shop.services.UserService;
import game_shop.services.impl.GameServiceImpl;
import game_shop.services.impl.OrderServiceImpl;
import game_shop.services.impl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

;import static game_shop.util.JdbcUtils.createDbConnection;


public class Main {
    public static void main(String[] args) throws IOException, NonExistingEntityException, SQLException, ClassNotFoundException {

        Properties props = new Properties();

        String dbConfigPath = JdbcSimpleDemo.class.getClassLoader()
                .getResource("jdbc.properties").getPath();

        props.load(new FileInputStream(dbConfigPath));

        Connection connection = createDbConnection(props);

        GameRepository gameRepository = new GameRepositoryJdbc(connection);
        UserRepository userRepository = new UserRepositoryJdbc(connection);
        OrderRepository orderRepository = new OrderRepositoryJdbc(connection);


        OrderService orderService = new OrderServiceImpl(orderRepository);
        UserService userService = new UserServiceImpl(gameRepository, userRepository, orderService);
        GameService gameService = new GameServiceImpl(gameRepository, userService);




        BaseController baseController = new BaseController(gameService, userService);

        baseController.init();

//        while (true){
//            System.out.println("Enter your commands");
//
//            String[] commands = bufferedReader.readLine().split("\\|");
//
//            switch (commands[0]){
//                case "RegisterUser":
//                    userService.registerUser(new User(commands[1], commands[2], commands[3]));
//                    break;
//
//                case "LoginUser":
//                    userService.logInUser(commands[1],commands[2]);
//                    break;
//                case "Logout":
//                    userService.logOut();
//                    break;
//                case "AddGame":
//                    gameService.addGame(new Game(commands[1], new BigDecimal(commands[2]),
//                            new BigDecimal(commands[3]),
//                            commands[4], commands[5], commands[6],
//                            LocalDate.parse(commands[7], DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
//                    break;
//
//                case "EditGame":
//                    String price = commands[2].replaceAll("price=", "");
//                    String size = commands[3].replaceAll("size=", "");
//                    gameService.editGame(Long.parseLong(commands[1]), new BigDecimal(price),
//                            new BigDecimal(size));
//                    break;
//                case "DeleteGame":
//                    gameService.deleteGame(Long.parseLong(commands[1]));
//                    break;
//                case "AllGames":
//                    gameService.findAllGames();
//                    break;
//                case "DetailGame":
//                    gameService.findGameByTitle(commands[1]);
//                    break;
//                case "OwnedGames":
//                    gameService.getOwnedGamesFromLoggedInUser();
//                    break;
//                case "AddShoppingCard":
//                    userService.addShoppingCard(commands[1]);
//                    break;
//                case "AddItem":
//                    userService.addGamesToTheShoppingCard(commands[1]);
//                    break;
//                case "RemoveItem":
//                    userService.removeGameFromCard(commands[1]);
//                    break;
//                case "BuyItem":
//                    userService.buyItem();
//                    break;
//            }
//        }
    }
}
