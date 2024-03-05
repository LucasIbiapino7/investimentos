package com.devsuperior.investimentos.controllers;

import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.dto.UserDeleteDTO;
import com.devsuperior.investimentos.dto.UserInsertDTO;
import com.devsuperior.investimentos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody @Valid UserInsertDTO dto){//MUDAR PRA 201
        service.insert(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @PutMapping
    public ResponseEntity<UserDTO> updateData(@RequestBody @Valid UserDTO dto){
        dto = service.updateData(dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody UserDeleteDTO dto){
        service.delete(dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @GetMapping
    public ResponseEntity<UserDTO> getMe(){
        UserDTO dto = service.getMe();
        return ResponseEntity.ok(dto);
    }

}
