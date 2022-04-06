package game_shop.repositories;


import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;

import java.util.Collection;

public interface BaseRepository<K, V> {

    Collection<V> findAll() throws EntityPersistenceException;

    V findById(K id);

    V  create(V entity) ;

    void addAll(Collection<V> entities);

    V update(V entity) throws NonExistingEntityException;

    V deleteById(K id) throws NonExistingEntityException;

    long count();
}
