package com.ubt.gymms.services.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ubt.gymms.entities.User;
import com.ubt.gymms.entities.administration.CredentialsDTO;
import com.ubt.gymms.entities.administration.UserDTO;
import com.ubt.gymms.services.feignClients.IdentityService;


@Service("userDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private IdentityService identityService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CredentialsDTO credentialsDTO = CredentialsDTO.builder().email(email).build();
        ResponseEntity<UserDTO> responseEntity = identityService.loadUserByUsername(credentialsDTO);
        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            User user = prepareUser(responseEntity.getBody());
            user.setAuthorities(getGrantedAuthorities(getPrivileges(user.getId())));
            return user;
        }
        else {
            throw new UsernameNotFoundException(email);
        }
    }

    public UserDetails loadUserByEmailAndPassword(String email, String password) throws UsernameNotFoundException {
        CredentialsDTO credentialsDTO = CredentialsDTO.builder().email(email).password(password).build();
        ResponseEntity<UserDTO> responseEntity = identityService.loadUserByEmailAndPassword(credentialsDTO);
        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            User user = prepareUser(responseEntity.getBody());
            user.setAuthorities(getGrantedAuthorities(getPrivileges(user.getId())));
            return user;
        }
        else {
            throw new UsernameNotFoundException(email);
        }
    }

    public boolean updatePassword(String email, String password) {
        CredentialsDTO credentialsDTO = CredentialsDTO.builder().email(email).password(password).build();
        ResponseEntity responseEntity = identityService.updatePassword(credentialsDTO);
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }

    public boolean activateUser(String email) {
        CredentialsDTO credentialsDTO = CredentialsDTO.builder().email(email).build();
        ResponseEntity responseEntity = identityService.activateUser(credentialsDTO);
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }

    private User prepareUser(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .image(userDTO.getImage())
                .accountNonExpired(userDTO.isAccountNonExpired())
                .accountNonLocked(userDTO.isAccountNonLocked())
                .credentialsNonExpired(userDTO.isCredentialsNonExpired())
                .tokenExpired(userDTO.isTokenExpired())
                .enabled(userDTO.isEnabled())
                .build();
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    private List<String> getPrivileges(Long id) {
        ResponseEntity<List<String>> responseEntity = identityService.getPrivilegesByUser(id);
        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        return null;
    }
}