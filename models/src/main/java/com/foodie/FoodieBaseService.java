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

    default T save(T entity) {
        return getRepository().save(entity);
    }

    default List<T> findAll() {
        return getRepository().findAll();
    }

    default Optional<T> findById(Key id) {
        return getRepository().findById(id);
    }

    default T update(T entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        return getRepository().save(entity);
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
    default void delete(T entity) {
        getRepository().findById((Key) entity.getId())
                .ifPresent(existingEntity -> {
                    existingEntity.setDeletedAt(LocalDateTime.now());
                    getRepository().save(existingEntity);
                });
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

    private boolean isFieldIgnored(Field field) {
        return field.getName().equals("id") 
            || field.isAnnotationPresent(JsonIgnore.class)
            || field.isAnnotationPresent(CreationTimestamp.class)
            || field.isAnnotationPresent(UpdateTimestamp.class);
    }
}