package repository;
import model.*;

/**
 * CRUD operations repository interface
 *
 * @param <ID> - type E must have an attribute of type ID
 * @param <E>  -  type of entities saved in repository
 */
public interface Repo<ID, E extends Entity<ID>> {

    /**
     * Returns an entity by id
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    E findOne(ID id);

    /**
     * Returns all entities
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * Saves an entity
     *
     * @param entity entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     #* @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null
     */
    void save(E entity);

    /**
     * Removes an entity
     *
     * @param id - the id of entity that should be removed from list
     * @return null if the entity is removed
     * id if the entity is not in list (otherwhise)
     * @throws IllegalArgumentException if the given id is null
     */
    void delete(ID id);

    // ID remove(ID id);
    void update(ID id,E entity);
}
