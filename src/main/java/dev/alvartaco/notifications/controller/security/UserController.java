package dev.alvartaco.notifications.controller.security;

import dev.alvartaco.notifications.model.secure.User;
import dev.alvartaco.notifications.repository.secure.IUserRepository;
import dev.alvartaco.notifications.response.AuthResponse;
import dev.alvartaco.notifications.security.JwtProvider;
import dev.alvartaco.notifications.service.secure.IUserService;
import dev.alvartaco.notifications.service.secure.UserServiceImplementation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user, HttpServletResponse response) throws Exception {
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
        String jwt = JwtProvider.generateToken(authentication);

        // Create the jwtToken cookie and set it in the response
        Cookie cookie = new Cookie("jwtToken", jwt);
        cookie.setMaxAge(2 * 60); // 2 minutes
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        authResponse.setUser(iUserService.findUserByEmail(email));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        log.info("{}-------{}", email, password);

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        // Create the jwtToken cookie and set it in the response
        Cookie cookie = new Cookie("jwtToken", jwt);
        cookie.setMaxAge(2 * 60); // 2 minutes
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login success");
        authResponse.setJwt(jwt);
        authResponse.setStatus(true);
        authResponse.setUser(iUserService.findUserByEmail(email));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }


    private Authentication authenticate(String username, String password) {

        log.info("{}---++----{}", username, password);

        UserDetails userDetails = userServiceImplementation.loadUserByUsername(username);

        log.info("Sig in in user details{}", userDetails);

        if (userDetails == null) {
            log.info("Sign in details - null");

            throw new BadCredentialsException("Invalid username and password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.info("Sign in userDetails - password mismatch{}", userDetails);

            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }
}