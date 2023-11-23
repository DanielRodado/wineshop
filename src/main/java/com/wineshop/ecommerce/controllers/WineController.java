package com.wineshop.ecommerce.controllers;

import com.wineshop.ecommerce.dto.WineDTO;
import com.wineshop.ecommerce.dto.WineValuationDTO;
import com.wineshop.ecommerce.models.Variety;
import com.wineshop.ecommerce.models.Wine;
import com.wineshop.ecommerce.services.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class WineController {

    @Autowired
    private WineService wineService;

    @GetMapping("/wines")
    public Set<WineDTO> getAllWinesDTO() {
        return wineService.getAllWinesDTO();
    }

    @PatchMapping("/wines/ranking")
    public ResponseEntity<String> valuateWine(@RequestParam Long wineId, @RequestParam Byte valuation) {

        if (!wineService.existsWineById(wineId)) {
            return new ResponseEntity<>("The wine you are trying to value does not exist", HttpStatus.FORBIDDEN);
        }

        if (valuation < 1 || valuation > 10) {
            return new ResponseEntity<>("Valuation score has to be between 1 and 10", HttpStatus.FORBIDDEN);
        }

        Wine wine = wineService.findWineById(wineId);
        wine.addValuation(valuation);

        return new ResponseEntity<>("Valuation received. Thanks!" ,HttpStatus.OK);
    }

    @GetMapping("/wines/varieties")
    public ResponseEntity<Object> getWineVarieties() {

        return new ResponseEntity<>(Variety.values(), HttpStatus.OK);
    }

    @GetMapping("/wines/ranking")
    public ResponseEntity<Object> getWineValuation(@RequestParam Long wineId) {

        if (!wineService.existsWineById(wineId)) {
            return new ResponseEntity<>("The wine you are requesting to value does not exist", HttpStatus.FORBIDDEN);
        }

        Wine wine = wineService.findWineById(wineId);

        if (wine.getValuations().isEmpty()) {
            return new ResponseEntity<>("The wine you are requesting to value does not exist", HttpStatus.FORBIDDEN);
        }

        int sumValuation = 0;
        for (Byte valuation: wine.getValuations()) {
            sumValuation += valuation;
        }

        WineValuationDTO wineValuation = new WineValuationDTO(wine.getName(),
                (double) sumValuation/wine.getValuations().size());


        return new ResponseEntity<>(wineValuation, HttpStatus.OK);
    }

    @PostMapping("/wines/create")
    public ResponseEntity<String> createWines(@RequestBody List<Wine> wines) {

        for (Wine wine: wines) {
            wineService.saveWine(wine);
        }

        return new ResponseEntity<>("Wines created successfully!", HttpStatus.CREATED);
    }

}
