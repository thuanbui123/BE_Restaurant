package com.example.restaurant.service;

import com.example.restaurant.DTO.AuthRequest;
import com.example.restaurant.DTO.RegisterRequest;
import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.mapper.AccountMapper;
import com.example.restaurant.repository.AccountInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AccountInfo> userDetail = repository.findByUsername(username);
        // Converting UserInfo to UserDetails
        return userDetail.map(AccountDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(AccountInfo accountInfo) {
        // Encode password before saving the user
        accountInfo.setPassword(encoder.encode(accountInfo.getPassword()));
        repository.save(accountInfo);
        return "User Added Successfully";
    }

    public AccountDetails loadUserByUsernameAndPassword (AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = encoder.encode(authRequest.getPassword());
        Optional<AccountInfo> accountInfo = repository.findByUsernameAndPassword(username, password);
        return accountInfo.map(AccountDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public ResponseEntity<?> login (AuthRequest authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            // If authentication is successful, generate and return JWT
            AccountDetails accountDetails = loadUserByUsername(authRequest.getUsername());
            String jwt = jwtService.generateToken(accountDetails.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", accountDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> register (RegisterRequest registerRequest) {
        if(repository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        AccountInfo accountInfo = AccountMapper.mapToAccountInfo(registerRequest);
        repository.save(accountInfo);
        final AccountDetails accountDetails = loadUserByUsername(registerRequest.getUsername());
        final String jwt = jwtService.generateToken(accountDetails.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("username", accountDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
