package com.foodie.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.foodie.FoodieBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends FoodieBaseEntity {

    @NotBlank(message = "Permission Name is mandatory")
    @Column(name = "permission_name", nullable = false, unique = true)
    private String permissionName;

    @NotBlank(message = "Permission Description is mandatory")
    @Column(name = "permission_description", nullable = false)
    private String permissionDescription;

    @Column(name = "can_create")
    private boolean canCreate;

    @Column(name = "can_read")
    private boolean canRead;

    @Column(name = "can_update")
    private boolean canUpdate;

    @Column(name = "can_delete")
    private boolean canDelete;
}