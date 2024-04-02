package com.foodie.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodie.user.contracts.ChangePasswordRequest;
import com.foodie.user.model.Role;
import com.foodie.user.model.User;
import com.foodie.user.repositories.RoleRepository;
import com.foodie.user.repositories.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByIdAndDeletedAtIsNull(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id);
    }

    public User validateAndGetUser(UUID userId) {
        return findByIdAndDeletedAtIsNull(userId).orElse(null);
    }

    public Collection<? extends GrantedAuthority> getUserAuthorities(User user) {
        List<Role> roles = roleRepository.findAll();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase())));

        return authorities;
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // save the new password
        repository.save(user);
    }
}
