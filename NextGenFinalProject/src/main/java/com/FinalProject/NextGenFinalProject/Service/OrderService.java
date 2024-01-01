package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Config.JwtAuthenticationFilter;
import com.FinalProject.NextGenFinalProject.Dto.AppResponse;
import com.FinalProject.NextGenFinalProject.Dto.CartRequest;
import com.FinalProject.NextGenFinalProject.Dto.OrderRequest;
import com.FinalProject.NextGenFinalProject.Entity.Order;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.OrderRepository;
import com.FinalProject.NextGenFinalProject.Repository.ProductRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepo userRepo;
    private final ProductRepository prodRepo;
    private final OrderRepository orderRepository;
    private static final String ORDER_PLACED = "Placed";

    public AppResponse<String> placeOrder(OrderRequest request){

        List<CartRequest.ItemRequest> itemRequest = request.getItemRequests();
        for(CartRequest.ItemRequest o : itemRequest){
            String userEmail = JwtAuthenticationFilter.CURRENT_USER;
            User user = userRepo.findByEmail(userEmail).orElse(null);

            Optional<Product> product1 = prodRepo.findById(o.getProductId());
            if (product1.isEmpty()){
                return new AppResponse<>("Product not found");
            }
            Product product = product1.get();
            Order order = new Order();
            order.setFirstName(request.getFirstName());
            order.setLastName(request.getLastName());
            order.setAddress(request.getAddress());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setPostalCode(request.getPostalCode());
            order.setOrderAmount(product.getPrice() * o.getQuantity());
            order.setOrderStatus(ORDER_PLACED);
            order.setUser(user);
            order.setProduct(product);


            orderRepository.save(order);
        }
        return new AppResponse<>(0, "Your Order has been successfully Placed");
    }
}
