package com.buynow.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buynow.config.JwtProvider;
import com.buynow.domain.USER_ROLE;
import com.buynow.exception.SellerException;
import com.buynow.exception.UserException;
import com.buynow.model.Cart;
import com.buynow.model.User;
import com.buynow.model.VerificationCode;
import com.buynow.repository.CartRepository;
import com.buynow.repository.UserRepository;
import com.buynow.repository.VerificationCodeRepository;
import com.buynow.request.LoginRequest;
import com.buynow.request.SignupRequest;
import com.buynow.response.AuthResponse;
import com.buynow.service.AuthService;
import com.buynow.service.EmailService;
import com.buynow.service.UserService;
import com.buynow.utils.OtpUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final CustomeUserServiceImplementation customUserDetails;
    private final CartRepository cartRepository;

    @Override
    public void sentLoginOtp(String email) throws UserException, MessagingException {

        String SIGNING_PREFIX = "signing_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
            userService.findUserByEmail(email);
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtils.generateOTP();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "Buy Now Login/Signup Otp";
        String text = "your login otp is - ";
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
        
    }

    @Override
    public String createUser(SignupRequest req) throws SellerException {

        String email = req.getEmail();
        String fullName = req.getFullName();
        String otp = req.getOtp();

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }
        User user = userRepository.findByEmail(email);

        if (user == null) {

            User createdUser = new User();
            createdUser.setEmail(email);
            createdUser.setFullName(fullName);
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setPassword(passwordEncoder.encode(otp));

            System.out.println(createdUser);

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(
                USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signin(LoginRequest req) throws SellerException, UserException {

        String username = req.getEmail();
        String otp = req.getOtp();

        //System.out.println(username + " ----- " + otp);
        VerificationCode isExist = verificationCodeRepository.findByEmail(username);
        if(!isExist.getOtp().equals(otp)) {
        	throw new UserException("wrong otp...");
        }
        Authentication authentication = authenticate(username, otp); // 1 manual way
        SecurityContextHolder.getContext().setAuthentication(authentication); // 2 manual way // its a holder to hold the authenticate user
//        Authentication authentication =                            // correct way to using the authenticationManager
//                authenticationManager.authenticate(
//                        new OtpAuthenticationToken(
//                                req.getEmail(),
//                                req.getOtp()
//                        )
//                );

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); 
        // it provides implementations of morespecific subinterfaces like Set and List.
        
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;

    }

    private Authentication authenticate(String username, String otp) throws SellerException {
        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null ");
            throw new BadCredentialsException("Invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //This constructor should only be used by AuthenticationManager or AuthenticationProvider
        //implementations that are satisfied withproducing a trusted (i.e. isAuthenticated() = true)authentication token.
    }
} 
// correct way
//STEP 1 — Create OTPAuthenticationToken
//
//Instead of using UsernamePasswordAuthenticationToken directly,
//we create custom token.
//
//public class OtpAuthenticationToken extends UsernamePasswordAuthenticationToken {
//
//    public OtpAuthenticationToken(Object principal, Object credentials) {
//        super(principal, credentials);
//    }
//
//    public OtpAuthenticationToken(Object principal, Object credentials,
//                                  Collection<? extends GrantedAuthority> authorities) {
//        super(principal, credentials, authorities);
//    }
//}
//STEP 2 — Create Custom AuthenticationProvider
//
//This is where OTP validation happens.
//
//@Component
//public class OtpAuthenticationProvider implements AuthenticationProvider {
//
//    private final CustomeUserServiceImplementation userDetailsService;
//    private final VerificationCodeRepository verificationCodeRepository;
//
//    public OtpAuthenticationProvider(
//            CustomeUserServiceImplementation userDetailsService,
//            VerificationCodeRepository verificationCodeRepository) {
//        this.userDetailsService = userDetailsService;
//        this.verificationCodeRepository = verificationCodeRepository;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication)
//            throws AuthenticationException {
//
//        String email = authentication.getName();
//        String otp = authentication.getCredentials().toString();
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//
//        if (userDetails == null) {
//            throw new BadCredentialsException("User not found");
//        }
//
//        VerificationCode verificationCode =
//                verificationCodeRepository.findByEmail(email);
//
//        if (verificationCode == null ||
//                !verificationCode.getOtp().equals(otp)) {
//            throw new BadCredentialsException("Invalid OTP");
//        }
//
//        return new OtpAuthenticationToken(
//                userDetails,
//                null,
//                userDetails.getAuthorities()
//        );
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
//Now OTP validation is inside AuthenticationProvider
//🔥 This is real Spring Security way
//
//✅ STEP 3 — Configure AuthenticationManager
//
//In SecurityConfig:
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    private final OtpAuthenticationProvider otpAuthenticationProvider;
//
//    public SecurityConfig(OtpAuthenticationProvider otpAuthenticationProvider) {
//        this.otpAuthenticationProvider = otpAuthenticationProvider;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//            .csrf().disable()
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/signin", "/signup", "/sent/**").permitAll()
//                    .anyRequest().authenticated()
//            )
//            .authenticationProvider(otpAuthenticationProvider);
//
//        return http.build();
//    }
//}
//
//Now Spring knows about your custom provider.
//STEP 4 — Update signin() Method (NO BYPASS)
//
//Now change your signin() method.
//
//Before (manual bypass):
//
//Authentication authentication = authenticate(username, otp);
//SecurityContextHolder.getContext().setAuthentication(authentication);
//
//Now correct way:
//
//@Override
//public AuthResponse signin(LoginRequest req) {
//
//    Authentication authentication =
//            authenticationManager.authenticate(
//                    new OtpAuthenticationToken(
//                            req.getEmail(),
//                            req.getOtp()
//                    )
//            );
//
//    String token = jwtProvider.generateToken(authentication);
//
//    AuthResponse authResponse = new AuthResponse();
//    authResponse.setJwt(token);
//    authResponse.setMessage("Login Success");
//
//    return authResponse;
//}
//
//🔥 No manual SecurityContextHolder setting
//🔥 No manual authenticate method
//🔥 Spring handles everything
