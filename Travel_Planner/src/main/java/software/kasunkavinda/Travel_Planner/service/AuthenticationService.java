package software.kasunkavinda.Travel_Planner.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.kasunkavinda.Travel_Planner.dto.reqAndresp.response.JwtAuthResponse;
import software.kasunkavinda.Travel_Planner.dto.reqAndresp.secure.SignIn;
import software.kasunkavinda.Travel_Planner.dto.reqAndresp.secure.SignUp;
import software.kasunkavinda.Travel_Planner.entity.UserEntity;
import software.kasunkavinda.Travel_Planner.repository.UserRepo;
import software.kasunkavinda.Travel_Planner.util.Mapping;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepo userRepo;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Mapping mapper;


    public JwtAuthResponse signIn(SignIn signIn) {
        logger.info("Attempting to authenticate user: {}", signIn.getEmail());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signIn.getEmail(), signIn.getPassword()));
        UserEntity userByEmail = userRepo.findByEmail(signIn.getEmail())
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", signIn.getEmail());
                    return new UsernameNotFoundException("User not found");
                });
        var generatedToken = jwtService.generateToken((UserDetails) userByEmail);
        logger.info("Token generated for user: {}", signIn.getEmail());



        logger.info("User signed in: {}", signIn.getEmail());

        // Return response with token and branch name
        return JwtAuthResponse.builder()
                .token(generatedToken)
                .build();
    }

    public JwtAuthResponse signUp(SignUp signUp) {
        logger.info("Attempting to sign up user: {}", signUp.getEmail());
        boolean opt = userRepo.existsByEmail(signUp.getEmail());

        if (opt) {
            logger.warn("User already exists: {}", signUp.getEmail());
            return JwtAuthResponse.builder().token("User already exists").build();
        } else {
            var buildUser = UserEntity.builder()
                    .userId(signUp.getUsername())
                    .country(signUp.getCountry())
                    .email(signUp.getEmail())
                    .password(passwordEncoder.encode(signUp.getPassword()))
                    .build();
            var savedUser = userRepo.save(buildUser);


            var genToken = jwtService.generateToken(savedUser);



            logger.info("User signed up: {},", signUp.getEmail());

            // Return response with token and branch name
            return JwtAuthResponse.builder()
                    .token(genToken)
                    .build();
        }
    }

    public JwtAuthResponse refreshToken(String accessToken) {
        logger.info("Attempting to refresh token for access token: {}", accessToken);
        var userName = jwtService.extractUsername(accessToken);
        var userEntity = userRepo.findByEmail(userName).orElseThrow(() -> {
            logger.warn("User not found: {}", userName);
            return new UsernameNotFoundException("User not found");
        });
        var refreshToken = jwtService.generateToken( userEntity);
        logger.info("Token refreshed for user: {}", userName);

        logger.info("Token refreshed for user: {},", userName);

        // Return response with token and branch name
        return JwtAuthResponse.builder()
                .token(refreshToken)
                .build();
    }
}
