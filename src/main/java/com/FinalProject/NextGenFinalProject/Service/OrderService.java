package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Config.JwtAuthenticationFilter;
import com.FinalProject.NextGenFinalProject.Dto.AppResponse;
import com.FinalProject.NextGenFinalProject.Dto.CartRequest;
import com.FinalProject.NextGenFinalProject.Dto.OrderRequest;
import com.FinalProject.NextGenFinalProject.Dto.ReviewRequest;
import com.FinalProject.NextGenFinalProject.Entity.*;
import com.FinalProject.NextGenFinalProject.Repository.OrderRepository;
import com.FinalProject.NextGenFinalProject.Repository.ProductRepository;
import com.FinalProject.NextGenFinalProject.Repository.ReviewRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepo userRepo;
    private final ProductRepository prodRepo;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public AppResponse<String> placeOrder(OrderRequest request) {
        List<CartRequest.ItemRequest> itemRequests = request.getItemRequests();

        for (CartRequest.ItemRequest itemRequest : itemRequests) {
            String userEmail = JwtAuthenticationFilter.CURRENT_USER;
            User user = userRepo.findByEmail(userEmail).orElse(null);

            Optional<Product> productOptional = prodRepo.findById(itemRequest.getProductId());
            if (productOptional.isEmpty()) {
                return new AppResponse<>("Product not found");
            }

            Product product = productOptional.get();
            Order order = new Order();
            order.setFirstName(request.getFirstName());
            order.setLastName(request.getLastName());
            order.setAddress(request.getAddress());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setPostalCode(request.getPostalCode());
            order.setOrderAmount(product.getPrice() * itemRequest.getQuantity());
            order.setOrderStatus(OrderStatus.PLACED);
            order.setUser(user);
            order.setProduct(product);

            orderRepository.save(order);

        }

        return new AppResponse<>(0, "Your Order has been successfully Placed");
    }


}


