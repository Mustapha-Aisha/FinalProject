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



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-products")
    public AppResponse<String> createProd(@Valid @RequestBody ProductRequest proRequest){
        return userService.createProducts(proRequest);

}

//    @PostMapping("/postReview")
//    public AppResponse<String>  submitReview(@Valid @RequestBody ReviewRequest reviewRequest){
//        return orderService.submitReview(reviewRequest);
//
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public AppResponse<Product> updateProd(@PathVariable long id, @Valid @RequestBody ProductRequest productRequest ){
        return userService.updateP(id, productRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/products/{id}")
    public AppResponse<String> deleteProduct(@PathVariable long id) {
        return userService.deleteProduct(id);
    }
}
