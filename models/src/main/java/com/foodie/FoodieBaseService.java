package com.foodie;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface FoodieBaseService<T extends FoodieBaseEntity, Key> {

    FoodieBaseRepository<T, Key> getRepository();
    Logger logger = LoggerFactory.getLogger(FoodieBaseService.class);

    default List<T> findAll() {
        return getRepository().findAll();
    }

    default Optional<T> findById(Key id) {
        return getRepository().findById(id);
    }

    default T save(T entity) {
        return getRepository().save(entity);
    }

    default List<T> saveAll(List<T> entity) {
        return getRepository().saveAll(entity);
    }

    default T update(T entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        return getRepository().save(entity);
    }

    default List<T> updateAll(List<T> entities) {
        entities.forEach(entity -> entity.setUpdatedAt(LocalDateTime.now()));

        return getRepository().saveAll(entities);
    }

    default T updateById(T entity, Key id) {
        Optional<T> existingEntity = findByIdAndDeletedAtIsNull(id);
        return existingEntity.map(retrievedEntity -> {
            updateFields(entity, retrievedEntity);
            retrievedEntity.setUpdatedAt(LocalDateTime.now());
            return getRepository().save(retrievedEntity);
        }).orElse(null);
    }

    @SuppressWarnings("unchecked")
    default List<T> updateAllById(List<T> entities) {
        entities.forEach(entity -> {
            Optional<T> existingEntity = findByIdAndDeletedAtIsNull((Key) entity.getId());
            existingEntity.ifPresent(retrievedEntity -> {
                updateFields(entity, retrievedEntity);
                retrievedEntity.setUpdatedAt(LocalDateTime.now());
            });
        });
        return getRepository().saveAll(entities);
    }

    default void delete(T entity) {
        entity.setDeletedAt(LocalDateTime.now());
        getRepository().save(entity);
    }

    default void deleteById(Key id) {
        getRepository().findById(id)
                .ifPresent(entity -> {
                    entity.setDeletedAt(LocalDateTime.now());
                    getRepository().save(entity);
                });
    }

    default void hardDelete(T entity) {
        getRepository().delete(entity);
    }

    default void hardDeleteById(Key id) {
        getRepository().deleteById(id);
    }

    default Optional<T> findByIdAndDeletedAtIsNull(Key id) {
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

    private boolean isFieldIgnored(Field field) {
        return field.getName().equalsIgnoreCase("id")
            || field.isAnnotationPresent(JsonIgnore.class)
            || field.isAnnotationPresent(CreationTimestamp.class)
            || field.isAnnotationPresent(UpdateTimestamp.class);
    }

    private void updateFields(T sourceEntity, T targetEntity) {
        Class<?> clazz = sourceEntity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            if (!isFieldIgnored(field)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(sourceEntity);
                    if (value != null) {
                        field.set(targetEntity, value);
                    }
                } catch (IllegalAccessException e) {
                    logger.error("Error updating field: " + field.getName() + " in entity: " + clazz.getName() + ". " + e.getMessage());
                }
            }
        }
    }
}