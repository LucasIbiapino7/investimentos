package com.devsuperior.investimentos.controllers;

import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.services.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService service;

    @PostMapping
    public ResponseEntity<PortfolioDTO> insert(@RequestBody @Valid PortfolioDTO dto){
        dto = service.insert(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> findAll(){
        List<PortfolioDTO> result = service.findAll();
        return ResponseEntity.ok(result);
    }

}
