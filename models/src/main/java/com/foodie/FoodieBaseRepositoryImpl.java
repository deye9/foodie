package com.foodie;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public abstract class FoodieBaseRepositoryImpl<T extends FoodieBaseEntity, ID extends Serializable>
        implements FoodieBaseService<T, ID> {

    private final FoodieBaseRepository<T, ID> foodieBaseRepository;

    public FoodieBaseRepositoryImpl(FoodieBaseRepository<T, ID> foodieBaseRepository) {
        this.foodieBaseRepository = foodieBaseRepository;
    }

    @Override
    public T save(T entity) {
        return foodieBaseRepository.save(entity);
    }

    @Override
    public List<T> findAll() {
        return foodieBaseRepository.findAll();
    }

    @Override
    public Optional<T> findById(ID entityId) {
        return foodieBaseRepository.findById(entityId);
    }

    @Override
    public T update(T entity) {
        return foodieBaseRepository.save(entity);
    }

    @Override
    public T updateById(T entity, ID entityId) {
        return foodieBaseRepository.findById(entityId)
                .map(existingEntity -> foodieBaseRepository.save(entity))
                .orElse(null);
    }

    @Override
    public void delete(T entity) {
        foodieBaseRepository.delete(entity);
    }

    @Override
    public void deleteById(ID entityId) {
        foodieBaseRepository.deleteById(entityId);
    }

    @Override
    public Optional<T> findByIdAndDeletedAtIsNull(ID entityId) {
        return foodieBaseRepository.findById(entityId)
                .filter(entity -> entity.getDeletedAt() == null);
    }

    @Override
    public Page<T> findAllByDeletedAtIsNull(int page, int size, String sort) {
        if (page < 0 || size <= 0 || sort == null) {
            throw new IllegalArgumentException("Page, size, and sort parameters are invalid");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Streamable<T> lazyStreamable = foodieBaseRepository.findAll(pageable)
                .filter(entity -> entity.getDeletedAt() == null);

        List<T> content = lazyStreamable.toList();
        return new PageImpl<>(content, pageable, content.size());
    }

}