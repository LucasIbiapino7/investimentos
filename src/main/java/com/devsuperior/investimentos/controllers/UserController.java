package com.devsuperior.investimentos.controllers;

import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody UserDTO dto){
        service.insert(dto);
        return ResponseEntity.noContent().build();
    }

}
