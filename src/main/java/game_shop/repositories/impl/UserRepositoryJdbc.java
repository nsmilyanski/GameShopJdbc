package game_shop.repositories.impl;

import game_shop.entities.Game;
import game_shop.entities.User;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class UserRepositoryJdbc implements UserRepository {
    private static final String SELECT_ALL_USERS = "select * from `user`;";
    private static final String FIND_BY_ID = "select * from `user` where id = ?;";
    private static final String CHECK_USERS = "select u.full_name, u.`password`, u.`email`, u.`is_admin` from `user` as u " +
            " where u.full_name = ? and u.`password` = ?;";
    public static final String UPDATE_GAME = "update user set `email` = ?,  `password` = ?, full_name = ?, is_admin = ? " +
            "where id = ?; ";
    private static final String CHECK_EMAIL = "select count(*) as cou from user where email = ? ;";
    private static final String SELECT_COUNT_USERS = "select count(*) as count from `user`;";
    private static final String INSERT_NEW_USER =
            "insert into `user` (`email`, `password`, `full_name`, `is_admin`) " +
                    "values (?, ?, ?, ?);";

    private Connection connection;

    public UserRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<User> findAll() throws EntityPersistenceException {
        try(var stmt = connection.prepareStatement(SELECT_ALL_USERS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toUsers(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_USERS, ex);
        }
    }

    private Collection<User> toUsers(ResultSet rs) throws SQLException {
        List<User> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new User(
                    rs.getLong(1),
                    rs.getString("email"),
                    rs.getString("full_name")

            ));
        }
        return results;
    }

    @Override
    public User findById(Long id) {
        try(var stmt = connection.prepareStatement(FIND_BY_ID)) {
            // 4. Set params and execute SQL query
            stmt.setInt(1, id.intValue());
            var rs = stmt.executeQuery();
            rs.next();
            // 5. Transform ResultSet to Book
            return toUsers(rs).iterator().next();
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_BY_ID, ex);
        }
    }

    @Override
    public User create(User entity) {
        try(var stmt = connection.prepareStatement(INSERT_NEW_USER, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getFullName());
            stmt.setBoolean(4, entity.getAdmin());
            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
                else {
                    throw new EntityPersistenceException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_USER, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_USER, ex);
        }
    }

    @Override
    public void addAll(Collection<User> entities) {

    }

    @Override
    public User update(User entity) throws NonExistingEntityException {
        User user = null;
        try(var stmt = connection.prepareStatement(UPDATE_GAME)) {

            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getFullName());
            stmt.setBoolean(4, entity.getAdmin());
            stmt.setInt(5, entity.getId().intValue());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + UPDATE_GAME, ex);
        }
        return user;

    }

    @Override
    public User deleteById(Long id) throws NonExistingEntityException {
        return null;
    }

    @Override
    public long count() {
        try(var stmt = connection.prepareStatement(SELECT_COUNT_USERS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return rs.getInt("count");

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_COUNT_USERS, ex);
        }
    }

    @Override
    public User logInUser(String username, String password) {
        try(var stmt = connection.prepareStatement(CHECK_USERS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, username);
            stmt.setString(2, password);
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return new User(rs.getString("email"), rs.getString("password"),
                  rs.getString("full_name"), rs.getBoolean("is_admin"));

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + CHECK_USERS, ex);
        }
    }

    @Override
    public boolean checkEmailIfExist(String email) {
        boolean hasEmail = false;

        try(var stmt = connection.prepareStatement(CHECK_EMAIL)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, email);
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();

            int count = rs.getInt("cou");

            if (count > 0) {
                hasEmail = true;
            }

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + CHECK_USERS, ex);
        }
        return hasEmail;
    }
}
