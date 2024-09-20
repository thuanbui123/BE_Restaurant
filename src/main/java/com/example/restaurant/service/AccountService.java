package com.example.restaurant.service;

import com.example.restaurant.request.AccountRequest;
import com.example.restaurant.request.AuthRequest;
import com.example.restaurant.request.EditAccountRequest;
import com.example.restaurant.request.RegisterRequest;
import com.example.restaurant.entity.AccountInfo;
import com.example.restaurant.mapper.AccountMapper;
import com.example.restaurant.repository.AccountInfoRepository;
import com.example.restaurant.response.AccountInfoResponse;
import com.example.restaurant.utils.PaginateUtil;
import com.example.restaurant.utils.Slugify;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountInfoRepository repository;

    @Autowired
    private EmployeeService service;

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
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng có tên đăng nhập là: " + username));
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
            AccountInfo info = findBySlug(accountDetails.getUsername());
            response.put("token", jwt);
            response.put("username", accountDetails.getUsername());
            response.put("img", info.getImg());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Tên đăng nhập hoặc mật khẩu không đúng!", HttpStatus.UNAUTHORIZED);
        }
    }

    public AccountInfo findOneById (Integer id) {
        return repository.findOneById(id) != null ? repository.findOneById(id) : null;
    }

    public ResponseEntity<?> register (RegisterRequest registerRequest) {
        try {
            if(repository.findByUsername(registerRequest.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
            }
            AccountInfo accountInfo = AccountMapper.mapToAccountInfo(registerRequest);
            repository.save(accountInfo);
            final AccountDetails accountDetails = loadUserByUsername(registerRequest.getUsername());
            final String jwt = jwtService.generateToken(accountDetails.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", accountDetails.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi đăng ký tài khoản: " + e.getMessage());
        }
    }

    public ResponseEntity<?> addAccount (AccountRequest accountRequest) {
        try {
            if(repository.findByUsername(accountRequest.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại!");
            }
            AccountInfo accountInfo = AccountMapper.mapToAccountInfo(accountRequest);
            repository.save(accountInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm tài khoản thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã có lỗi xảy ra khi thêm tài khoản mới: " + e.getMessage());
        }
    }

    public ResponseEntity<?> findAll(Pageable pageable) {
        return PaginateUtil.paginate(repository::findAll, pageable, AccountMapper::maToAccountResponse);
    }

    public ResponseEntity<?> findAll () {
        List<Integer> accountIdList = service.getAccountIdList();
        List<AccountInfoResponse> responses = repository.findAll().stream()
                .filter(accountInfo -> !accountIdList.contains(accountInfo.getId()) && accountInfo.getRole().equals("ROLE_EMPLOYEE"))
                .map(accountInfo -> new AccountInfoResponse(
                        accountInfo.getId(),
                        accountInfo.getUsername()
                ))
                .toList();
        return ResponseEntity.ok().body(responses);
    }

    public ResponseEntity<?> findByRole(String role, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findByRole(role, pg),
                pageable,
                AccountMapper::maToAccountResponse
        );
    }

    public ResponseEntity<?> findBySlug (String query, Pageable pageable) {
        final String slug = Slugify.toSlug(query);
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCaseAndRole(slug, "ROLE_EMPLOYEE", pg),
                pageable,
                AccountMapper::maToAccountResponse
        );
    }

    public AccountInfo findBySlug (String query) {
        final String slug = Slugify.toSlug(query);
        return repository.findOneBySlug(slug);
    }

    public ResponseEntity<?> findData (Integer page, Integer size, String prefix, String query) {
        if ("find-all".equals(prefix) && query == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByRole("ROLE_EMPLOYEE", pageable);
        } else if ("get-accounts".equals(prefix) && query == null) {
            return findAll();
        } else if ("search".equals(prefix) && query != null) {
            Pageable pageable = PageRequest.of(page, size);
            return findBySlug(query, pageable);
        }
        return new ResponseEntity<>("API không tồn tại!", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> updateData (String query, EditAccountRequest request) {
        try {
            String slug = Slugify.toSlug(query);
            AccountInfo existsAccount = repository.findOneBySlug(slug);
            if (existsAccount == null) {
                return new ResponseEntity<>("Tài khoản không tồn tại!" , HttpStatus.NOT_FOUND);
            }
            existsAccount.setImg(request.getImg());
            return new ResponseEntity<>(AccountMapper.maToAccountResponse(repository.save(existsAccount)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra trong khi cập nhật tài khoản!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteData (String slug) {
        try {
            AccountInfo accountInfo = repository.findOneBySlug(slug);
            if (accountInfo == null) {
                return new ResponseEntity<>("Tài khoản không tồn tại!", HttpStatus.NOT_FOUND);
            }
            repository.deleteById(accountInfo.getId());
            return new ResponseEntity<>("Xóa tài khoản thành công.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra trong khi xóa tài khoản!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
