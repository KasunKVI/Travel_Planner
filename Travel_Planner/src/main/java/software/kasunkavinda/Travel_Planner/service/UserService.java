package software.kasunkavinda.Travel_Planner.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import software.kasunkavinda.Travel_Planner.dto.reqAndresp.secure.SignUp;
import software.kasunkavinda.Travel_Planner.entity.UserEntity;
import software.kasunkavinda.Travel_Planner.repository.UserRepo;
import software.kasunkavinda.Travel_Planner.util.Mapping;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final Mapping map;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDetailsService userDetailsService() {

        return username -> {
            logger.info("Attempting to load user by username: {}", username);
            return (UserDetails) userRepo.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.warn("User not found with username: {}", username);
                        return new UsernameNotFoundException("User not found");
                    });
        };
    }

    public void save(SignUp user) {
        logger.info("Attempting to save user with email: {}", user.getEmail());
        userRepo.save(map.convertToEntity(user, UserEntity.class));
        logger.info("User saved successfully with email: {}", user.getEmail());
    }
}
