package com.foodie;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

/**
 * The FoodieBaseService interface provides methods for performing CRUD operations on entities that extend the FoodieBaseEntity class.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's ID
 */
public interface FoodieBaseService<T extends FoodieBaseEntity, ID extends Serializable> {

    T save(T entity);

    List<T> findAll();

    Optional<T> findById(ID entityId);

    T update(T entity);

    T updateById(T entity, ID entityId);

    void delete(T entity);

    void deleteById(ID entityId);

    Optional<T> findByIdAndDeletedAtIsNull(ID entityId);

    Page<T> findAllByDeletedAtIsNull(int page, int size, String sort);
}