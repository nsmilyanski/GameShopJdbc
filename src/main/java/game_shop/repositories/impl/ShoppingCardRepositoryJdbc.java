package game_shop.repositories.impl;

import game_shop.entities.ShoppingCard;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.ShoppingCardRepository;

import java.util.Collection;

public class ShoppingCardRepositoryJdbc implements ShoppingCardRepository {
    @Override
    public Collection<ShoppingCard> findAll() throws EntityPersistenceException {
        return null;
    }

    @Override
    public ShoppingCard findById(Long id) {
        return null;
    }

    @Override
    public ShoppingCard create(ShoppingCard entity) {
        return null;
    }

    @Override
    public void addAll(Collection<ShoppingCard> entities) {

    }

    @Override
    public ShoppingCard update(ShoppingCard entity) throws NonExistingEntityException {
        return null;
    }

    @Override
    public ShoppingCard deleteById(Long id) throws NonExistingEntityException {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }
}
