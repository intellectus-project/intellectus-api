package com.atixlabs.controllers;

import com.atixlabs.exceptions.InactiveUserException;
import com.atixlabs.controllers.model.AuthenticatedUserDto;
import com.atixlabs.payload.JwtAuthenticationResponse;
import com.atixlabs.payload.LoginRequest;
import com.atixlabs.repositories.UserRepository;
import com.atixlabs.security.JwtTokenProvider;
import com.atixlabs.security.UserPrincipal;
import com.atixlabs.services.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    public static final String URL_MAPPING = "/api/auth/login";
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    //TODO: cuando se genera un nuevo token invalidar los existentes
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Perform authentication Login");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername().trim(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            logger.info("Generated token [" + jwt + "] for user [" + loginRequest.getUsername() + "]");

            Optional<AuthenticatedUserDto> authenticatedUserDto = null;

            authenticatedUserDto = userService.findUserAuthenticated(loginRequest.getUsername(), loginRequest.getPassword());

            if (authenticatedUserDto.isPresent()) {
                authenticatedUserDto.get().setAccessToken(jwt);
                return ResponseEntity.ok(authenticatedUserDto);
            } else {
                return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
            }
        } catch (InactiveUserException ex) {
            logger.error("User ["+loginRequest.getUsername()+"] inactive");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unable to login. Please contact the system administratoren ");
        }
    }

    @GetMapping("/logout")
    public void logout(@AuthenticationPrincipal UserPrincipal usuarioActual) {
        logger.info("Perform authentication Logout " + usuarioActual.getUsername());
        tokenProvider.revoqueToken(usuarioActual.getUsername());
    }
}