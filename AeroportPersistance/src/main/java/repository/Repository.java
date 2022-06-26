package repository;

import java.util.Collection;

public interface Repository<Tid, T> {
    /**
     * Returns an entity by id
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    T findOne(Tid id);

    /**
     * Returns all entities
     *
     * @return all entities
     */
    Iterable<T> findAll();

    /**
     * Saves an entity
     *
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
    #* @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null
     */
    void save(T entity);

    /**
     * Removes an entity
     *
     * @param id - the id of entity that should be removed from list
     * @return null if the entity is removed
     * id if the entity is not in list (otherwhise)
     * @throws IllegalArgumentException if the given id is null
     */
    void delete(Tid id);

    // ID remove(ID id);
    void update(Tid id,T entity);

}
