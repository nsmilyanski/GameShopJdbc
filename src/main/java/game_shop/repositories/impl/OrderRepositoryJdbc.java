package game_shop.repositories.impl;

import game_shop.entities.Order;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.OrderRepository;

import java.sql.Connection;
import java.util.Collection;

public class OrderRepositoryJdbc implements OrderRepository {
    private Connection connection;

    public OrderRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Order> findAll() throws EntityPersistenceException {
        return null;
    }

    @Override
    public Order findById(Long id) {
        return null;
    }

    @Override
    public Order create(Order entity) {
        return null;
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
}
