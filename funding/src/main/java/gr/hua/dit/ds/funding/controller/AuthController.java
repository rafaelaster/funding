package gr.hua.dit.ds.funding.controller;

import gr.hua.dit.ds.funding.config.JwtUtils;
import gr.hua.dit.ds.funding.entity.Role;
import gr.hua.dit.ds.funding.entity.User;
import gr.hua.dit.ds.funding.payload.request.LoginRequest;
import gr.hua.dit.ds.funding.payload.request.SignupRequest;
import gr.hua.dit.ds.funding.payload.response.JwtResponse;
import gr.hua.dit.ds.funding.payload.response.MessageResponse;
import gr.hua.dit.ds.funding.repository.RoleRepository;
import gr.hua.dit.ds.funding.repository.UserRepository;
import gr.hua.dit.ds.funding.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("authentication");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.println("authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("post authentication");
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("jw: " + jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("ROLE_CREATOR")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
            Role userRole2 = roleRepository.findByName("ROLE_SUPPORTER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole2);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role CreatorRole = roleRepository.findByName("ROLE_CREATOR")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(CreatorRole);
                        Role SupporterRole = roleRepository.findByName("ROLE_SUPPORTER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(SupporterRole);

                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
