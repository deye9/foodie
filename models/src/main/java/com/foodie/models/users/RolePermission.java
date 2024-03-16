package com.foodie.models.users;

import com.foodie.FoodieBaseModel;

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
@Table(name = "role_permissions")
public class RolePermission extends FoodieBaseModel{

    @NotBlank(message = "Role is mandatory")
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @NotBlank(message = "Permission is mandatory")
    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

}
