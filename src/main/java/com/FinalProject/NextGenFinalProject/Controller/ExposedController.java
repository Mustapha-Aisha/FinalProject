package com.FinalProject.NextGenFinalProject.Controller;

import com.FinalProject.NextGenFinalProject.Dto.AppResponse;
import com.FinalProject.NextGenFinalProject.Dto.ProductRequest;
import com.FinalProject.NextGenFinalProject.Entity.Product;
import com.FinalProject.NextGenFinalProject.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ExposedController {

    private final UserService userService;
    @GetMapping("/getProducts")
    public AppResponse<Map<String, Object>> getAllProd(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "4") int size
    ){
        return userService.getAllProducts(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public AppResponse<Product> getProdById(@PathVariable Long id){
        return userService.getProductById(id);
    }



}
