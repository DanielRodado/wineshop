package com.wineshop.ecommerce.controllers;

import com.wineshop.ecommerce.dto.AccessoryDTO;
import com.wineshop.ecommerce.models.Accessory;
import com.wineshop.ecommerce.models.Wine;
import com.wineshop.ecommerce.services.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccessoryController {

    @Autowired
    private AccessoryService accessoryService;

    @GetMapping("/accessories")
    public Set<AccessoryDTO> getAllAccessoriesDTO() {
        return accessoryService.getAllAccessoriesDTO();
    }

    @PostMapping("/accessories/create")
    public ResponseEntity<String> createWines(@RequestBody List<Accessory> accessories) {

        for (Accessory accessory: accessories) {
            accessoryService.saveAccessory(accessory);
        }

        return new ResponseEntity<>("Accessories created successfully!", HttpStatus.CREATED);
    }

}
