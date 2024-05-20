package com.devsuperior.investimentos.controllers;


import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.services.BrapiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/stocks")
public class StockController {

    @Autowired
    private BrapiService brapiService;

    @GetMapping
    public ResponseEntity<BrapiResponseListDTO> getAll(
            @RequestParam(name = "search", defaultValue = "") String search
    ){
        BrapiResponseListDTO result = brapiService.getQuoteList(search);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StockBrapiFindByIdDTO> findById(@PathVariable(name = "id") String id){
        StockBrapiFindByIdDTO response = brapiService.findById(id);
        return ResponseEntity.ok(response);
    }

}
