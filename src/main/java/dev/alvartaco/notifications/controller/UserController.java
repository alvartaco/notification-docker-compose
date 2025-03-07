package dev.alvartaco.notifications.controller;

import dev.alvartaco.notifications.model.secure.User;
import dev.alvartaco.notifications.repository.secure.IUserRepository;
import dev.alvartaco.notifications.response.AuthResponse;
import dev.alvartaco.notifications.security.JwtProvider;
import dev.alvartaco.notifications.service.secure.IUserService;
import dev.alvartaco.notifications.service.secure.UserServiceImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final IUserRepository iUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImplementation userServiceImplementation;
    private final IUserService iUserService;

    public UserController(IUserRepository iUserRepository,
                          PasswordEncoder passwordEncoder,
                          UserServiceImplementation userServiceImplementation,
                          IUserService iUserService){
        this.iUserRepository = iUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServiceImplementation = userServiceImplementation;
        this.iUserService = iUserService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String mobile = user.getMobile();
        String role = user.getRole();

        User isEmailExist = iUserRepository.findByEmail(email);
        if (isEmailExist != null) {
            throw new Exception("Email Is Already Used With Another Account");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setMobile(mobile);
        createdUser.setRole(role);
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser = iUserRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        authResponse.setUser(iUserService.findUserByEmail(email));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(email + "-------" + password);

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);
        authResponse.setUser(iUserService.findUserByEmail(email));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    private Authentication authenticate(String username, String password) {

        System.out.println(username + "---++----" + password);

        UserDetails userDetails = userServiceImplementation.loadUserByUsername(username);

        System.out.println("Sig in in user details" + userDetails);

        if (userDetails == null) {
            System.out.println("Sign in details - null" );

            throw new BadCredentialsException("Invalid username and password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            System.out.println("Sign in userDetails - password mismatch" + userDetails);

            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }

    /*
    @GetMapping("/validate")
    public ResponseEntity<Object> validateAndRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (JwtProvider.getEmailFromJwtToken(token) != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create("http://localhost:8082/web"));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return null;
    }
    */

}