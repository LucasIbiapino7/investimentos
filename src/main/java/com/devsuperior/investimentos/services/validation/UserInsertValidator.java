package com.devsuperior.investimentos.services.validation;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import com.devsuperior.investimentos.controllers.handlers.FiledMessage;
import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.dto.UserInsertDTO;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.repositories.UserRepository;
import com.devsuperior.investimentos.services.exceptions.DateException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FiledMessage> list = new ArrayList<>();

        User user = repository.findByEmail(dto.getEmail());
        if (user != null){
            list.add(new FiledMessage("email", "email já existe"));
        }

        LocalDate birthDate;
        //Testa se a Data está formatada corretamente
        try {
            birthDate = LocalDate.parse(dto.getBirthDate());
        }catch (DateTimeParseException e){
            throw new DateException("Data No formato Inválido");
        }

        //Testa se é uma Data após o momento atual
        if (birthDate.isAfter(LocalDate.now())){
            list.add(new FiledMessage("birthDate", "Datas futuras não são aceitas"));
        }

        if (birthDate.until(LocalDate.now(), ChronoUnit.YEARS) < 18){
            list.add(new FiledMessage("birthDate", "Idade Menor que 18 anos"));
        }

        for (FiledMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}