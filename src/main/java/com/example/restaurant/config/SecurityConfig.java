package com.example.restaurant.config;

import com.example.restaurant.filter.JwtAuthFilter;
import com.example.restaurant.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String apiAuthPrefix = ApiConfig.API_AUTH_PREFIX;

    private final String apiFoodCategoryPrefix = ApiConfig.API_FOOD_CATEGORY_PREFIX;

    private final String apiFoodPrefix = ApiConfig.API_FOOD_PREFIX;

    private final String apiRecommendationPrefix = ApiConfig.API_RECOMMENDATION;

    private final String apiEmployeePrefix = ApiConfig.API_EMPLOYEE_PREFIX;

    private final String apiCustomerPrefix = ApiConfig.API_CUSTOMER_PREFIX;

    private final String apiSupplierPrefix = ApiConfig.API_SUPPLIER_PREFIX;

    private final String apiBlogPostPrefix = ApiConfig.API_BLOG_POST_PREFIX;

    private final String apiTablesPrefix = ApiConfig.API_TABLES_PREFIX;

    private final String apiIngredientCategoriesPrefix = ApiConfig.API_INGREDIENT_CATEGORIES_PREFIX;

    private final String apiCombosPrefix = ApiConfig.API_COMBOS_PREFIX;

    private final String apiIngredientsPrefix = ApiConfig.API_INGREDIENTS_PREFIX;

    private final String apiImportInvoicePrefix = ApiConfig.API_IMPORT_INVOICE_PREFIX;

    private final String apiInvoiceDetailPrefix = ApiConfig.API_INVOICE_DETAIL_PREFIX;

    private final String apiBillPrefix = ApiConfig.API_BILL_PREFIX;

    final List<Pair<String, String>> bypassTokens = Arrays.asList(
            Pair.of(String.format("%s/authenticate", apiAuthPrefix), "POST"),
            Pair.of(String.format("%s/register", apiAuthPrefix), "POST"),
            Pair.of(String.format("%s/{prefix}", apiFoodCategoryPrefix), "GET"),
            Pair.of(String.format("%s/{prefix}", apiFoodPrefix), "GET"),
            Pair.of(String.format("%s/customer/{customerId}", apiRecommendationPrefix), "GET"),
            Pair.of(String.format("%s/{prefix}", apiTablesPrefix), "GET"),
            Pair.of(String.format("%s/{prefix}", apiIngredientCategoriesPrefix), "GET"),
            Pair.of(String.format("%s/{prefix}", apiCombosPrefix), "GET")
    );

    final List<Pair<String, String>> noBypassTokens = Arrays.asList(
            Pair.of(String.format("%s/add", apiFoodCategoryPrefix), "POST"),
            Pair.of(String.format("%s/update/{prefix}", apiFoodCategoryPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{prefix}", apiFoodCategoryPrefix), "DELETE"),
            Pair.of(String.format("%s/add", apiFoodPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiFoodPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiFoodPrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiEmployeePrefix), "GET"),
            Pair.of(String.format("%s/add", apiEmployeePrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiEmployeePrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiEmployeePrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiCustomerPrefix), "GET"),
            Pair.of(String.format("%s/add", apiCustomerPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiCustomerPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiCustomerPrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiSupplierPrefix), "GET"),
            Pair.of(String.format("%s/add", apiSupplierPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiSupplierPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiSupplierPrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiBlogPostPrefix), "GET"),
            Pair.of(String.format("%s/add", apiBlogPostPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiBlogPostPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiBlogPostPrefix), "DELETE"),
            Pair.of(String.format("%s/add", apiTablesPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiTablesPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiTablesPrefix), "DELETE"),
            Pair.of(String.format("%s/add", apiIngredientCategoriesPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiIngredientCategoriesPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiIngredientCategoriesPrefix), "DELETE"),
            Pair.of(String.format("%s/add", apiCombosPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiCombosPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiCombosPrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiIngredientsPrefix), "GET"),
            Pair.of(String.format("%s/add", apiIngredientsPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiIngredientsPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiIngredientsPrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiImportInvoicePrefix), "GET"),
            Pair.of(String.format("%s/add", apiImportInvoicePrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiImportInvoicePrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiImportInvoicePrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiInvoiceDetailPrefix), "GET"),
            Pair.of(String.format("%s/add", apiInvoiceDetailPrefix), "POST"),
            Pair.of(String.format("%s/{code}/add-ingredients", apiInvoiceDetailPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiInvoiceDetailPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiInvoiceDetailPrefix), "DELETE"),
            Pair.of(String.format("%s/{code}/delete-ingredient/{ingredientId}", apiInvoiceDetailPrefix), "DELETE"),
            Pair.of(String.format("%s/{prefix}", apiBillPrefix), "GET"),
            Pair.of(String.format("%s/add", apiBillPrefix), "POST"),
            Pair.of(String.format("%s/update/{code}", apiBillPrefix), "PUT"),
            Pair.of(String.format("%s/cancel/{code}", apiBillPrefix), "PUT"),
            Pair.of(String.format("%s/delete/{code}", apiBillPrefix), "DELETE")
    );

    final List<Pair<String, String>> noBypassTokensUsers = Arrays.asList(
            Pair.of(String.format("%s/{prefix}", apiBillPrefix), "GET"),
            Pair.of(String.format("%s/cancel/{code}", apiBillPrefix), "PUT")
    );

    @Autowired
    @Lazy
    private JwtAuthFilter authFilter;

    @Bean
    @Lazy
    public AccountService accountService() {
        return new AccountService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> {
                    // Duyệt qua danh sách bypassTokens và cho phép tất cả các yêu cầu với các đường dẫn này
                    for (Pair<String, String> bypassToken : bypassTokens) {
                        auth.requestMatchers(bypassToken.getSecond(), bypassToken.getFirst()).permitAll();
                    }

                    for (Pair<String, String> nobypassToken : noBypassTokens) {
                        auth.requestMatchers(nobypassToken.getSecond(), nobypassToken.getFirst()).hasAuthority("ROLE_EMPLOYEE_ADMIN");
                    }

                    for (Pair<String, String> nobyPassTokenUser : noBypassTokensUsers) {
                        auth.requestMatchers(nobyPassTokenUser.getSecond(), nobyPassTokenUser.getFirst()).hasAuthority("ROLE_USER");
                    }

                    auth
                            .requestMatchers("/uploads/**").permitAll()
                            .requestMatchers("/account/**").hasAuthority("ROLE_EMPLOYEE_ADMIN")
                            .requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
                            .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")
                            .requestMatchers("/auth/add-account/**").hasAuthority("ROLE_EMPLOYEE_ADMIN")
                            .anyRequest().authenticated(); // Bảo vệ tất cả các endpoint khác
                })
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .authenticationProvider(authenticationProvider()) // Custom authentication provider
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .cors(cors -> cors
                    .configurationSource(request -> {
                        var corsConfig = new CorsConfiguration();
                        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                        corsConfig.setAllowedHeaders(Arrays.asList("*"));
                        corsConfig.setAllowCredentials(true);
                        return corsConfig;
                    })
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(accountService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}