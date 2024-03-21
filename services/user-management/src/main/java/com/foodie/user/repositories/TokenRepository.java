package com.foodie.user.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.foodie.user.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

  @Query(value = """
          select t from Token t inner join User u
          on t.user.id = u.id
          where u.id = :id and (t.expired = false or t.revoked = false)""")
  List<Token> findAllValidTokenByUser(UUID id);

  Optional<Token> findByTokenString(String tokenString);
}
