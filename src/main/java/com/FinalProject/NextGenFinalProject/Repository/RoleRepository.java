package com.FinalProject.NextGenFinalProject.Repository;

import com.FinalProject.NextGenFinalProject.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);

}
