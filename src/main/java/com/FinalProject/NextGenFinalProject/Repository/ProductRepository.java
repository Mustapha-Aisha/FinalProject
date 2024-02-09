package com.FinalProject.NextGenFinalProject.Repository;

import com.FinalProject.NextGenFinalProject.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

        Product findByName(String name);

        Boolean existsByName (String name);
}
