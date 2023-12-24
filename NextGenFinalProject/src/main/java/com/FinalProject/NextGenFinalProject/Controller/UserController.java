package com.FinalProject.NextGenFinalProject.Controller;

import com.FinalProject.NextGenFinalProject.Dto.*;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Entity.User;
import com.FinalProject.NextGenFinalProject.Service.CartService;
import com.FinalProject.NextGenFinalProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    public final CartService cartService;
    @GetMapping("/getProducts")
    public AppResponse<Map<String, Object>> getAllProd(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "page", required = false, defaultValue = "3") int size
    ){
        return userService.getAllProducts(PageRequest.of(page, size));
    }

   @PostMapping("/addToCart")
    public AppResponse<String> addToCart(
            @RequestParam Long productId,
            @RequestParam Long customerId,
            @RequestParam int quantity
    ) {
        return cartService.addToCart(productId, customerId, quantity);
    }

    @DeleteMapping("/removeFromCart")
    public AppResponse<String> removeFromCart(
            @RequestParam Long productId,
            @RequestParam Long customerId
    ) {
        return cartService.removeFromCart(productId, customerId);

    }

    @GetMapping("/view/{customerId}")
    public ResponseEntity<AppResponse<CartResponseFromDb>> viewCart(@PathVariable Long customerId) {
        AppResponse<CartResponseFromDb> response = cartService.viewCart(customerId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create-products")
    public AppResponse<String> createProd(@Valid @RequestBody ProductRequest proRequest){
        return userService.createProducts(proRequest);

    }

    @GetMapping("/{id}")
    public AppResponse<ProductResponseFromDb> getProdById(@PathVariable Long id){
        return userService.getProductById(id);
    }

    @PatchMapping("/{id}")
    public AppResponse<Product> updateProd(@PathVariable long id, @Valid @RequestBody ProductRequest productRequest ){
        return userService.updateP(id, productRequest);
    }

    @DeleteMapping("/products/{id}")
    public AppResponse<String> deleteProduct(@PathVariable long id) {
        return userService.deleteProduct(id);
    }
}
