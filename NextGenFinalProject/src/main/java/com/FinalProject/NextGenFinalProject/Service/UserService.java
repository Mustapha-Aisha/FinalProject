package com.FinalProject.NextGenFinalProject.Service;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Dto.User;
import com.FinalProject.NextGenFinalProject.Entity.*;
import com.FinalProject.NextGenFinalProject.Exception.ApiException;
import com.FinalProject.NextGenFinalProject.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final ProductRepository prodRepo;
    private final CartRepository cartRepo;
    private final ReviewRepository reviewRepository;
    private final UserRepo userRepo;
    private final OrderRepository orderRepository;


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

    public AppResponse<Review> submitReview(ReviewRequest reviewRequest) {
        Product product = getProductById(reviewRequest.getProductId()).getData();
        User customer = getCustomerById(reviewRequest.getCustomerId()).getData();

        if (product == null || customer == null) {
            return new AppResponse<>("Product or customer not found", null);
        }

        // Check if the customer has a completed order for the product
        boolean hasPurchased = hasCustomerPurchasedProduct(product, customer);

        if (hasPurchased) {
            // Proceed with review creation
            return new AppResponse<>("Review posted Successfully", createReview(product, customer, reviewRequest.getRating(), reviewRequest.getComment()));
        } else {
            return new AppResponse<>("Customer has not purchased the product", null);
        }
    }

    public Review createReview(Product product, User customer, int rating, String comment) {
        Review review = new Review();
        review.setProduct(product);
        review.setUser(customer);
        review.setRating(rating);
        review.setComment(comment);


        return reviewRepository.save(review);
    }
    public boolean hasCustomerPurchasedProduct(Product product, User customer) {
        List<Order> customerOrders = orderRepository.findByUser(customer);

        for (Order order : customerOrders) {
            for (Product orderedProduct : order.getProducts()) {
                if (orderedProduct.getId().equals(product.getId()) &&
                        order.getStatus().equals(OrderStatus.DELIVERED)) {
                    return true; // Customer has purchased the product
                }
            }
        }

        return false; // Customer has not purchased the product
    }

    public Order checkoutCart(com.FinalProject.NextGenFinalProject.Entity.User user, Cart cart, String shippingAddress, String paymentInformation) {
            Order order = new Order();
            order.setUser(user);
            order.setProducts(cart.getCartItems().stream().map(CartItem::getProduct).collect(Collectors.toList()));
            order.setTotalPrice(cart.getTotalPrice());
            order.setShippingAddress(shippingAddress);
            order.setOrderDate(LocalDateTime.now().toString());
            order.setStatus(OrderStatus.PENDING); // Set the initial status
            order.setPaymentInformation(paymentInformation);

            order = orderRepository.save(order);

            cart.getCartItems().clear();
            cart.setTotalPrice(0);
            cartRepo.save(cart);

            return order;
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

    public AppResponse<Product> getProductById(long id) {
        Product prod = prodRepo.findById(id).orElseThrow(() -> new ApiException("id not found"));
//        ProductResponseFromDb productResponseFromDb = new ProductResponseFromDb(prod);
        return new AppResponse<>(0, "successful", prod);
    }

    public AppResponse<User> getCustomerById(long id) {
        com.FinalProject.NextGenFinalProject.Entity.User user = userRepo.findById(id).orElseThrow(() -> new ApiException("id not found"));
        User cusResponseFromDb = new User(user);
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
