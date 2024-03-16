package com.foodie.models.listener;

import com.foodie.FoodieBaseModel;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class RecordUpdateListener {
    @PrePersist
    protected void setCreateDate(FoodieBaseModel foodieBaseModel) {
        foodieBaseModel.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    protected void setUpdateDate(FoodieBaseModel foodieBaseModel) {
        foodieBaseModel.setUpdatedAt(LocalDateTime.now());
    }
}
