package com.FinalProject.NextGenFinalProject.Repository;

import com.FinalProject.NextGenFinalProject.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByCustomerId(Long id);
}
