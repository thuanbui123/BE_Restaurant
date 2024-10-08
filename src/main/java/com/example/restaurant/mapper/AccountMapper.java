package com.example.restaurant.mapper;

import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.request.AccountRequest;
import com.example.restaurant.request.RegisterRequest;
import com.example.restaurant.response.AccountResponse;
import com.example.restaurant.utils.Slugify;
import com.example.restaurant.utils.TimeConvertUtil;
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
        accountInfo.setUsername(registerRequest.getUsername());
        accountInfo.setSlug(Slugify.toSlug(registerRequest.getUsername()));
        accountInfo.setPassword(encoder.encode(registerRequest.getPassword()));
        accountInfo.setImg("http://localhost:8080/api-restaurant/uploads/73508276-6443-4464-8119-820c9c7d971f_avatarDefault.jpg");
        accountInfo.setRole("ROLE_USER");
        return accountInfo;
    }

    public static AccountInfo mapToAccountInfo (AccountRequest accountRequest){
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setUsername(accountRequest.getUsername());
        accountInfo.setSlug(Slugify.toSlug(accountRequest.getUsername()));
        accountInfo.setPassword(encoder.encode(accountRequest.getPassword()));
        accountInfo.setImg(accountRequest.getImg());
        accountInfo.setRole("ROLE_EMPLOYEE");
        return accountInfo;
    }

    public static AccountResponse maToAccountResponse (AccountInfo accountInfo) {
        AccountResponse response = new AccountResponse();
        response.setId(accountInfo.getId());
        response.setUsername(accountInfo.getUsername());
        response.setRole(accountInfo.getRole());
        response.setImg(accountInfo.getImg());
        response.setCreatedAt(TimeConvertUtil.convertTimestampToDate(accountInfo.getCreatedAt()));
        response.setUpdatedAt(TimeConvertUtil.convertTimestampToDate(accountInfo.getUpdatedAt()));
        return response;
    }
}
