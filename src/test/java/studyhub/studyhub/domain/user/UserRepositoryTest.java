package studyhub.studyhub.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // springframework의 transactional이 더 풍부한 기능이 많다.
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    public void user_save_test() {
        User user = User.create("test@test.com", "1234", "테스트");
        userRepository.save(user);

        Assertions.assertThat(user.getId()).isNotNull();
    }

}