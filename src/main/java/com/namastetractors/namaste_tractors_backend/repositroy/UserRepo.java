package com.namastetractors.namaste_tractors_backend.repositroy;

import com.namastetractors.namaste_tractors_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailVerificationToken(String emailVerificationToken);
}
