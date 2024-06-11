package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.dto.UserDeleteDTO;
import com.devsuperior.investimentos.dto.UserInsertDTO;
import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Role;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.projection.UserDetailsProjection;
import com.devsuperior.investimentos.repositories.RoleRepository;
import com.devsuperior.investimentos.repositories.UserRepository;
import com.devsuperior.investimentos.services.exceptions.DatabaseException;
import com.devsuperior.investimentos.services.exceptions.DateException;
import com.devsuperior.investimentos.services.exceptions.PasswordException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import com.devsuperior.investimentos.util.CustomUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserUtil customUserUtil;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.isEmpty()){
            throw new UsernameNotFoundException("Usuário Não Encontrado");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    @Transactional
    public void insert(UserInsertDTO dto) {
        User user = new User();
        copyDtoToUser(dto, user);
        Role role = roleRepository.findByAuthority("ROLE_CLIENT");
        user.getRoles().clear();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.addRole(role);
        user = repository.save(user);
    }

    @Transactional
    public UserDTO updateData(UserDTO dto) {
        User user = authenticated();
        copyDtoToUser(dto, user);
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(UserDeleteDTO dto) {
        User user = authenticated();
        if (!authPassword(dto.getPassword(), user)){
            throw new PasswordException("Senha Incorreta");
        };
        try {
            repository.delete(user);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    protected User authenticated(){
        try {
            String username = customUserUtil.getLoggedUsername();
            return repository.findByEmail(username).get();
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe(){
        User user = authenticated();
        return new UserDTO(user);
    }

    protected Account getAccount(){
        User user = authenticated();
        Account account =  user.getAccount();
        if (account == null){
            throw new ResourceNotFoundException("Nenhuma Conta vinculada a esse usuário!");
        }
        return user.getAccount();
    }

    protected Account authUser(String password){
        User user = authenticated();
        Account account =  user.getAccount();
        if (account == null){
            throw new ResourceNotFoundException("Nenhuma Conta vinculada a esse usuário!");
        }
        if (!authPassword(password, user)){
            throw new PasswordException("Senha Incorreta!");
        }
        return user.getAccount();
    }

    protected boolean authPassword(String password, User user){
        return passwordEncoder.matches(password, user.getPassword());
    }

    private void copyDtoToUser(UserDTO dto, User user) {
        try {
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setBirthDate(LocalDate.parse(dto.getBirthDate()));
            user.setEmail(dto.getEmail());
        }catch (DateTimeParseException e){
            throw new DateException("Data Formatada Incorretamente. Formato certo: yyyy-MM-dd");
        }
    }

}
