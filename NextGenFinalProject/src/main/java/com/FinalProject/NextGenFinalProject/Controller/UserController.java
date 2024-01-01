package com.FinalProject.NextGenFinalProject.Controller;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Entity.Review;
import com.FinalProject.NextGenFinalProject.Service.CartService;
import com.FinalProject.NextGenFinalProject.Service.OrderService;
import com.FinalProject.NextGenFinalProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final CartService cartService;

    private final OrderService orderService;


    @GetMapping("/getProducts")
    public AppResponse<Map<String, Object>> getAllProd(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "page", required = false, defaultValue = "3") int size
    ){
        return userService.getAllProducts(PageRequest.of(page, size));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addToCart")
    public AppResponse<String> addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(request);
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/removeFromCart/{cartItemId}")
    public AppResponse<String> removeFromCart(@PathVariable long cartItemId) {
        return cartService.removeFromCart(cartItemId);

    }
//
//    @GetMapping("/viewCart/{customerId}")
//    public ResponseEntity<AppResponse<CartResponseFromDb>> viewCart(@PathVariable Long customerId) {
//        AppResponse<CartResponseFromDb> response = cartService.viewCart(customerId);
//        return ResponseEntity.ok(response);
//    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/placeOrder")
    public AppResponse<String> placeOrder(@RequestBody OrderRequest request){
        return orderService.placeOrder(request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getProdDetails/{isSingleProdCheckout}/{productId}")
    public List<Product> getProdDetails (@PathVariable boolean isSingleProdCheckout, @PathVariable Long productId){
        return userService.getProdDetails(isSingleProdCheckout, productId);
    }



    @PostMapping("/create-products")
    public AppResponse<String> createProd(@Valid @RequestBody ProductRequest proRequest){
        return userService.createProducts(proRequest);

    }

    @PostMapping("/postReview")
    public AppResponse<Review>  submitReview(@Valid @RequestBody ReviewRequest reviewRequest){
        return userService.submitReview(reviewRequest);

    }

    @GetMapping("/{id}")
    public AppResponse<Product> getProdById(@PathVariable Long id){
        return userService.getProductById(id);
    }

    @PatchMapping("/update/{id}")
    public AppResponse<Product> updateProd(@PathVariable long id, @Valid @RequestBody ProductRequest productRequest ){
        return userService.updateP(id, productRequest);
    }

    @DeleteMapping("/products/{id}")
    public AppResponse<String> deleteProduct(@PathVariable long id) {
        return userService.deleteProduct(id);
    }
}
