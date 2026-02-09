package studyhub.studyhub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import studyhub.studyhub.domain.user.User;
import studyhub.studyhub.domain.user.UserRepository;

@Configuration
public class InitDataConfig {

    @Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            userRepository.save(
                    User.create("user1@test.com", "1234", "유저1")
            );

            userRepository.save(
                    User.create("user2@test.com", "1234", "유저2")
            );
        };
    }
}
