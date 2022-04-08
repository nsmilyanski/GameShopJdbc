package game_shop.repositories.impl;

import game_shop.entities.Comment;
import game_shop.entities.Order;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class OrderRepositoryJdbc implements OrderRepository {
    private static final String INSERT_NEW_ORDER =
            "insert into `order` (`user_id`, `sum`) " +
                    "values (?, ?);";
    private static final String SELECT_ALL_ORDERS = "select *  from `order`;";
    private Connection connection;

    public OrderRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Order> findAll() throws EntityPersistenceException {
        try(var stmt = connection.prepareStatement(SELECT_ALL_ORDERS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toComments(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_ORDERS, ex);
        }
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public Order create(Order entity) {
        try(var stmt = connection.prepareStatement(INSERT_NEW_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setInt(1, entity.getBuyer().getId().intValue());
            stmt.setBigDecimal(2, entity.getSum());
            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating order failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
                else {
                    throw new EntityPersistenceException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_ORDER, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_ORDER, ex);
        }
    }

    @Override
    public void addAll(Collection<Order> entities) {

    }

    @Override
    public Order update(Order entity) throws NonExistingEntityException {
        return null;
    }

    @Override
    public Order deleteById(Long id) throws NonExistingEntityException {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
    public List<Order> toComments(ResultSet rs) throws SQLException {
        List<Order> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Order(
                    rs.getLong(1),
                    rs.getBigDecimal("sum")

            ));
        }
        return results;
    }
}
