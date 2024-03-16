package com.foodie.models.users;

import java.io.Serializable;

import com.foodie.FoodieBaseModel;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends FoodieBaseModel {

    @NotBlank(message = "Role Name is mandatory")
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @NotBlank(message = "Role Description is mandatory")
    @Column(name = "role_description", nullable = false)
    private String roleDescription;
}
