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
            response.put("id", info.getId());
            response.put("img", info.getImg());
            response.put("role", info.getRole());
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
            response.put("id", accountInfo.getId());
            response.put("img", accountInfo.getImg());
            response.put("role", accountInfo.getRole());
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

    public ResponseEntity<?> findByRole(String role) {
        return ResponseEntity.ok().body(repository.findByRole(role));
    }

    public ResponseEntity<?> findByRoleNot(String role, Pageable pageable) {
        return PaginateUtil.paginate(
                (pg) -> repository.findByRoleNot(role, pageable),
                pageable,
                AccountMapper::maToAccountResponse
        );
    }

    public ResponseEntity<?> findBySlug (String query, Pageable pageable) {
        final String slug = Slugify.toSlug(query);
        return PaginateUtil.paginate(
                (pg) -> repository.findBySlugContainingIgnoreCase(slug, pg),
                pageable,
                AccountMapper::maToAccountResponse
        );
    }

    public AccountInfo findBySlug (String query) {
        final String slug = Slugify.toSlug(query);
        return repository.findOneBySlug(slug);
    }

    public ResponseEntity<?> findData (Integer page, Integer size, String prefix, String query) {
        if ("get-account-employee".equals(prefix) && query == null) {
            return findByRole("ROLE_EMPLOYEE");
        } else if ("get-accounts".equals(prefix) && query == null) {
            Pageable pageable = PageRequest.of(page, size);
            return findByRoleNot("ROLE_EMPLOYEE_ADMIN", pageable);
        } else if ("search".equals(prefix) && query != null) {
            query = Slugify.toSlug(query);
            Pageable pageable = PageRequest.of(page, size);
            return findBySlug(query, pageable);
        } else if ("get-accounts-user".equals(prefix) && query == null) {
            return findByRole("ROLE_USER");
        } else if ("find-account-by-id".equals(prefix) && query != null) {
            Integer id = Integer.parseInt(query);
            return ResponseEntity.ok().body(findOneById(id));
        }
        return new ResponseEntity<>("API không tồn tại!", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> updateData (Integer query, EditAccountRequest request) {
        try {
            AccountInfo existsAccount = repository.findOneById(query);
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
    public ResponseEntity<?> deleteData (Integer slug) {
        try {
            AccountInfo accountInfo = repository.findOneById(slug);
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
