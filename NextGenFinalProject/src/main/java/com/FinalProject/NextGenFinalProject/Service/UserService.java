package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.*;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.CartRepository;
import com.FinalProject.NextGenFinalProject.Repository.ProductRepository;
import com.FinalProject.NextGenFinalProject.Repository.UserRepo;
import com.FinalProject.NextGenFinalProject.Repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final ProductRepository prodRepo;
    private final CartRepository cartRepo;
    private final ReviewRepository reviewRepository;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


     public AppResponse<Map<String, Object>> getAllProducts(Pageable pageable){
        Page<ProductResponseFromDb> response = prodRepo.findAll(pageable).map(ProductResponseFromDb::new);

        Map<String, Object> page = Map.of(
                "page", response.getNumber(),
                "totalPages", response.getTotalPages(),
                "totalElements", response.getTotalElements(),
                "size", response.getSize(),
                "content", response.getContent()
        );

        return new AppResponse<>("success", page);


    }

    public AppResponse<Review> submitReview(Long productId, Long customerId, int rating, String comment) {
        ProductResponseFromDb product = getProductById(productId).getData();
        UserResponseFromDb customer = getCustomerById(customerId).getData();

        if (product == null || customer == null) {
            return new AppResponse<>("product/customer cant be found", null);
        }

        // Check if customer has a purchased order for the product
        List<Order> orders = orderService.getOrdersByProductAndCustomer(product, customer);
        boolean purchased = orders.stream()
                .anyMatch(order -> order.getStatus().equals(OrderStatus.SHIPPED) || order.getStatus().equals(OrderStatus.DELIVERED));

        // If purchased, proceed with review creation
        if (purchased) {
            return createReview(product, customer, rating, comment, false);
        }

        // Check if product exists in active cart
        Cart cart = customer.getCart();
        CartItem cartItem = cart.cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotPurchasedException());

        if (cartItem.getQuantity() <= 0) {
            throw new ProductNotPurchasedException();
        }

        // Create review, mark it as cart-based for transparency
        return createReview(product, customer, rating, comment, true);
    }

    private Review createReview(Product product, Customer customer, int rating, String comment, boolean fromCart) {
        Review review = new Review();
        review.setProduct(product);
        review.setCustomer(customer);
        review.setRating(rating);
        review.setComment(comment);
        review.setIsFromCart(fromCart); // Flag indicating cart-based review

        return reviewRepository.save(review);
    }


    public AppResponse<String> createProducts(ProductRequest request){
        Optional<Product> existingProduct = Optional.ofNullable(prodRepo.findByName(request.getName()));
        if (existingProduct.isPresent()) {
            return new AppResponse<>("Product already exists");
        }

        Product product = new Product();
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setImageURL(request.getImageURL());
        product.setIsAvailable(product.getQuantity() > 0 );

        prodRepo.save(product);
        return new AppResponse<>("Product created Successfully");
    }

    public AppResponse<ProductResponseFromDb> getProductById(long id) {
        Product prod = prodRepo.findById(id).orElseThrow(() -> new ApiException("id not found"));
        ProductResponseFromDb productResponseFromDb = new ProductResponseFromDb(prod);
        return new AppResponse<>(0, "successful", productResponseFromDb);
    }

    public AppResponse<UserResponseFromDb> getCustomerById(long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new ApiException("id not found"));
        UserResponseFromDb cusResponseFromDb = new UserResponseFromDb(user);
        return new AppResponse<>(0, "successful", cusResponseFromDb);
    }

    public AppResponse<Product> updateP(long id, ProductRequest productRequest){
        Product p = prodRepo.findById(id).orElseThrow(() -> new ApiException("Product doesn't exist"));
        p.setDescription(productRequest.getDescription());
        p.setBrand(productRequest.getBrand());
        p.setName(productRequest.getName());
        p.setCategory(productRequest.getCategory());
        p.setPrice(productRequest.getPrice());
        p.setQuantity(productRequest.getQuantity());
        p.setImageURL(productRequest.getImageURL());
        p.setIsAvailable(p.getQuantity() > 0 );

        prodRepo.save(p);
        return new AppResponse<>("Product updated Successfully", p);
    }

    public AppResponse<String> deleteProduct(long id) {
        try {
            Optional<Product> productOptional = prodRepo.findById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                prodRepo.delete(product);
                return new AppResponse<>("Product deleted successfully");
            } else {
                throw new ApiException("Product doesn't exist");
            }
        } catch (ApiException e) {
            return new AppResponse<>("Product doesn't exist");
        }
    }

}
