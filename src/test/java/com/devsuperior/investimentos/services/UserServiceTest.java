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
import com.devsuperior.investimentos.testes.Factory;
import com.devsuperior.investimentos.util.CustomUserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.security.auth.login.AccountException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserUtil customUserUtil;

    private String usernameExist;
    private String usernameNotExist;
    private String password;
    private UserDetailsProjection projection;
    private List<UserDetailsProjection> list;
    private List<UserDetailsProjection> listEmpty;
    private Role role;
    private UserInsertDTO userInsertDTO;
    private User user;
    private User userWithAccount;
    private UserDTO userDTO;
    private UserDeleteDTO userDeleteDTO;

    @BeforeEach
    void setUp() {

        password = "123456";
        usernameExist = "maria@gmail.com";
        usernameNotExist = "lucas@gmail.com";
        user = Factory.createUser();
        userWithAccount = Factory.createUserWithAccount();
        userDTO = Factory.createUserDTO();
        role = Factory.createRole();
        projection = Factory.createUserDetailsProjection();
        userInsertDTO = Factory.createUserInsertDTO();
        userDeleteDTO = Factory.createUserDeleteDTO();
        list = List.of(projection);
        listEmpty = new ArrayList<>();

        Mockito.when(repository.searchUserAndRolesByEmail(usernameExist)).thenReturn(list);
        Mockito.when(repository.searchUserAndRolesByEmail(usernameNotExist)).thenReturn(listEmpty);

        Mockito.when(roleRepository.findByAuthority(ArgumentMatchers.any())).thenReturn(role);

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.any())).thenReturn("encorder");

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(user);

        Mockito.when(repository.findByEmail(usernameExist)).thenReturn(Optional.of(user));
        Mockito.when(repository.findByEmail(usernameNotExist)).thenReturn(Optional.empty());

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists(){
        UserDetails response = service.loadUserByUsername(usernameExist);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getUsername(), usernameExist);
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameNotExists(){

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails response = service.loadUserByUsername(usernameNotExist);
        });

    }

    @Test
    public void insertShouldPersistAnfDoNothing(){

        Assertions.assertDoesNotThrow(() -> {
            service.insert(userInsertDTO);
        });

    }

    @Test
    public void updateDataShouldThrowUsernameNotFoundExceptionWhenUserNonLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(UsernameNotFoundException.class).when(serviceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDTO response = service.updateData(userDTO);
        });

    }

    @Test
    public void updateDataShouldPersistAndReturnUserDTOWhenUserLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();

        UserDTO response = serviceSpy.updateData(userDTO);

        Assertions.assertNotNull(response);

    }

    @Test
    public void deleteShouldDoNothingWhenUserLoggedPasswordCorrectAndIdIsNonDependent(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();
        Mockito.doReturn(true).when(serviceSpy).authPassword(userDeleteDTO.getPassword(), user);
        Mockito.doNothing().when(repository).delete(user);

        Assertions.assertDoesNotThrow(() -> {
            serviceSpy.delete(userDeleteDTO);
        });

    }

    @Test
    public void deleteShouldThrowUsernameNotFoundExceptionWhenUserNonLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(UsernameNotFoundException.class).when(serviceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            serviceSpy.delete(userDeleteDTO);
        });

    }

    @Test
    public void deleteShouldThrowPasswordExceptionWhenUserLoggedButPasswordIncorrect(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();

        Assertions.assertThrows(PasswordException.class, () -> {
            serviceSpy.delete(userDeleteDTO);
        });

    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenUserLoggedPasswordCorrectButIdIsDependent(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();
        Mockito.doReturn(true).when(serviceSpy).authPassword(userDeleteDTO.getPassword(), user);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).delete(user);

        Assertions.assertThrows(DatabaseException.class, () -> {
            serviceSpy.delete(userDeleteDTO);
        });

    }

    @Test
    public void getMeShouldReturnUserDTOWhenUserLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();

        UserDTO response = serviceSpy.getMe();

        Assertions.assertNotNull(response);

        Assertions.assertEquals(response.getId(), user.getId());
    }

    @Test
    public void getMeShouldThrowUsernameNotFoundExceptionWhenUserNonLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(UsernameNotFoundException.class).when(serviceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            UserDTO response = serviceSpy.getMe();
        });

    }


    @Test
    public void getAccountShouldReturnAccountWhenUserLoggedAndAccountExists(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(userWithAccount).when(serviceSpy).authenticated();

        Account response = serviceSpy.getAccount();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(), userWithAccount.getId());
    }

    @Test
    public void getAccountShouldThrowUsernameNotFoundExceptionWhenUserNonLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(UsernameNotFoundException.class).when(serviceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            Account response = serviceSpy.getAccount();
        });
    }

    @Test
    public void getAccountShouldThrowResourceNotFoundExceptionWhenUserLoggedButAccountNonExists(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Account response = serviceSpy.getAccount();
        });
    }

    @Test
    public void authUserShouldReturnAccountWhenUserLoggedAndAccountExistsAndPasswordCorrect(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(userWithAccount).when(serviceSpy).authenticated();
        Mockito.doReturn(true).when(serviceSpy).authPassword(userDeleteDTO.getPassword(), user);

        Account response = serviceSpy.authUser(password);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(), userWithAccount.getId());
    }

    @Test
    public void authUserShouldThrowUsernameNotFoundExceptionWhenUserNonLogged(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doThrow(UsernameNotFoundException.class).when(serviceSpy).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            Account response = serviceSpy.authUser(password);
        });
    }

    @Test
    public void authUserShouldThrowResourceNotFoundExceptionWhenUserLoggedButAccountNonExists(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(user).when(serviceSpy).authenticated();

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Account response = serviceSpy.authUser(password);
        });
    }

    @Test
    public void authUserShouldThrowPasswordExceptionWhenUserLoggedButAccountExistsButIncorrectPassword(){

        UserService serviceSpy = Mockito.spy(service);

        Mockito.doReturn(userWithAccount).when(serviceSpy).authenticated();
        Mockito.doReturn(false).when(serviceSpy).authPassword(userDeleteDTO.getPassword(), user);

        Assertions.assertThrows(PasswordException.class, () -> {
            Account response = serviceSpy.authUser(password);
        });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUsernameExists(){

        Mockito.when(customUserUtil.getLoggedUsername()).thenReturn(usernameExist);

        User result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(), user.getUsername());

    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserNotExist(){
        Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUsername();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            User result = service.authenticated();
        });
    }

    @Test
    public void copyDtoToUserThrowDateExceptionWhenBirthDateIsInvalid(){

        userInsertDTO.setBirthDate("111111");

        Assertions.assertThrows(DateException.class, () -> {
            service.insert(userInsertDTO);
        });

    }

}