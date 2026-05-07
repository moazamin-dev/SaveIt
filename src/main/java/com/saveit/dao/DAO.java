package com.saveit.dao;

import java.util.List;

/**
 * @brief Generic Data Access Object (DAO) interface.
 *
 * This interface defines the standard operations to be performed on a
 * specific type of entity, following the Data Access Object pattern to
 * separate low-level data accessing API or operations from high-level
 * business services.
 *
 * @param <T> The type of the domain object (entity) this DAO manages.
 */
public interface DAO<T> {

    /**
     * @brief Persists the given entity to the data source.
     *
     * @param entity The object of type T to be saved.
     */
    void save(T entity);

    /**
     * @brief Retrieves all entities associated with a specific identifier.
     *
     * Typically used to fetch all records belonging to a specific user
     * or parent entity.
     *
     * @param id The unique identifier used to filter the records (e.g., user_id).
     * @return List<T> A list of entities found in the data source.
     */
    List<T> getAll(int id);

    /**
     * @brief Deletes an entity from the data source by its unique identifier.
     *
     * @param id The unique ID of the record to be removed.
     */
    void delete(int id);
}