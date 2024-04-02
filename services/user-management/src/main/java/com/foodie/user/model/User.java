package com.foodie.user.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foodie.FoodieBaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends FoodieBaseEntity {
  
  private String firstname;
  private String lastname;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @JsonIgnore
  private String password;

  @Column(name = "is_active", columnDefinition = "boolean default true")
  private Boolean isActive;

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;
}
