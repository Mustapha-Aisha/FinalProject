package com.FinalProject.NextGenFinalProject.Repository;

import com.FinalProject.NextGenFinalProject.Entity.Order;
import com.FinalProject.NextGenFinalProject.Entity.OrderStatus;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(  User user);

    List<Order> findByUserAndOrderStatus(User user, OrderStatus orderStatus);

//    boolean existsByUserAndProductAndOrderStatus(User user, Product product, OrderStatus orderStatus);

}
