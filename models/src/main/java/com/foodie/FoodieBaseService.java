package com.foodie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

public interface FoodieBaseService<T extends FoodieBaseEntity, ID> {

    FoodieBaseRepository<T, ID> getRepository();

    default T save(T entity) {
        return getRepository().save(entity);
    }

    default List<T> findAll() {
        return getRepository().findAll();
    }

    default Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @SuppressWarnings("unchecked")
    default T update(T entity) {
        entity.setUpdatedAt(LocalDateTime.now());

        return getRepository().findById((ID) entity.getId())
                .map(existingEntity -> getRepository().save(entity))
                .orElse(null);
    }

    default T updateById(T entity, ID id) {
        entity.setUpdatedAt(LocalDateTime.now());

        return getRepository().findById(id)
                .map(existingEntity -> getRepository().save(entity))
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    default void delete(T entity) {
        getRepository().findById((ID) entity.getId())
                .ifPresent(existingEntity -> {
                    existingEntity.setDeletedAt(LocalDateTime.now());
                    getRepository().save(existingEntity);
                });
    }

    default void deleteById(ID id) {
        getRepository().findById(id)
                .ifPresent(entity -> {
                    entity.setDeletedAt(LocalDateTime.now());
                    getRepository().save(entity);
                });
    }

    default void hardDelete(T entity) {
        getRepository().delete(entity);
    }

    default void hardDeleteById(ID id) {
        getRepository().deleteById(id);
    }

    default Optional<T> findByIdAndDeletedAtIsNull(ID id) {
        return getRepository().findById(id)
                .filter(entity -> entity.getDeletedAt() == null);
    }

    default Page<T> findAllByDeletedAtIsNull(int page, int size, String sort) {
        if (page < 0 || size <= 0 || sort == null) {
            throw new IllegalArgumentException("Page, size, and sort parameters are invalid");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Streamable<T> lazyStreamable = getRepository().findAll(pageable)
                .filter(entity -> entity.getDeletedAt() == null);

        List<T> content = lazyStreamable.toList();
        return new PageImpl<>(content, pageable, content.size());
    }
}
