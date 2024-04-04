package com.foodie.user.utils;

import com.foodie.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.UUID;

public class FoodieUserDetails implements UserDetails {

    private User user;

    public FoodieUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
    public UUID getId() {
        return user.getId();
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public static FoodieUserDetails fromUserEntityToCustomUserDetails(User user) {
        return new FoodieUserDetails(user);
    }
}