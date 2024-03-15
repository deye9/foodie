package com.foodie;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.foodie.models.listener.RecordUpdateListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(RecordUpdateListener.class)
public abstract class FoodieBaseModel implements Serializable {

    @Id
    // @JsonIgnore
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(insertable = false, columnDefinition = "TIMESTAMP", name = "deleted_at")
    private LocalDateTime deletedAt;
}
