package com.foodie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface FoodieBaseRepository<T extends FoodieBaseEntity, ID> extends JpaRepository<T, ID> {}