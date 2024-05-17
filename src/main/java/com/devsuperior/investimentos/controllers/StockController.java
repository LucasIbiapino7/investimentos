package com.devsuperior.investimentos.controllers;


import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
import com.devsuperior.investimentos.services.BrapiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
