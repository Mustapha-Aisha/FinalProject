package com.FinalProject.NextGenFinalProject.Repository;

import com.FinalProject.NextGenFinalProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);

    Boolean existsByEmail(String email);
}
