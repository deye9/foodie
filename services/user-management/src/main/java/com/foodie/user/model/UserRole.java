package com.foodie.user.model;

import com.foodie.FoodieBaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_roles")
public class UserRole extends FoodieBaseEntity {

    @NotBlank(message = "User is mandatory")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @NotBlank(message = "Role is mandatory")
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
