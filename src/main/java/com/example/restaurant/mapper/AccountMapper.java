package com.example.restaurant.mapper;

import com.example.restaurant.DTO.RegisterRequest;
import com.example.restaurant.entity.AccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private static PasswordEncoder encoder;

    public AccountMapper(PasswordEncoder encoder) {
        AccountMapper.encoder = encoder;
    }

    public static AccountInfo mapToAccountInfo (RegisterRequest registerRequest) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setEmail(registerRequest.getEmail());
        accountInfo.setUsername(registerRequest.getUsername());
        accountInfo.setPassword(encoder.encode(registerRequest.getPassword()));
        accountInfo.setRole("ROLE_USER");
        return accountInfo;
    }
}
