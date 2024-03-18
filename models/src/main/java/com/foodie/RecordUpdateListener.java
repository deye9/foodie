package com.foodie;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class RecordUpdateListener {
    @PrePersist
    protected void setCreateDate(FoodieBaseEntity foodieBaseModel) {
        foodieBaseModel.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    protected void setUpdateDate(FoodieBaseEntity foodieBaseModel) {
        foodieBaseModel.setUpdatedAt(LocalDateTime.now());
    }
}
