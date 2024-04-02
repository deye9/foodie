package com.foodie.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodie.FoodieBaseRepository;
import com.foodie.FoodieBaseResponse;
import com.foodie.FoodieBaseService;
import com.foodie.user.contracts.ChangePasswordRequest;
import com.foodie.user.model.User;
import com.foodie.user.repositories.UserRepository;
import com.foodie.user.utils.FoodieUserDetails;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements FoodieBaseService<User, UUID> {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public FoodieBaseRepository<User, UUID> getRepository() {
        return repository;
    }

    public User validateAndGetUser(Object connectedUser) {

        User retrievedUser;
        if (connectedUser instanceof Principal) {
            retrievedUser = ((FoodieUserDetails) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getUser();
        } else if (connectedUser instanceof UUID) {
            retrievedUser =  findByIdAndDeletedAtIsNull((UUID) connectedUser).orElse(null);
        } else {
            throw new IllegalArgumentException("Invalid connectedUser type");
        }

        if (retrievedUser == null || !retrievedUser.getIsActive()) {
            throw new RuntimeException("Requested user not found or not active");
        }

        return retrievedUser;
    }

    public void deactivateUser(Object connectedUser) {

        User user = validateAndGetUser(connectedUser);
        user.setIsActive(false);
        repository.save(user);
    }

    public Optional<User> findByIdAndDeletedAtIsNull(UUID id) {
        return repository.findByIdAndDeletedAtIsNull(id);
    }

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        User user = ((FoodieUserDetails) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal()).getUser();

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
