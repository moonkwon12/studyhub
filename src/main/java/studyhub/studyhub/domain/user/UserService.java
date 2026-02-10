package studyhub.studyhub.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studyhub.studyhub.domain.user.dto.UserCreateRequest;
import studyhub.studyhub.domain.user.dto.UserResponse;
import studyhub.studyhub.global.exception.EmailAlreadyExistsException;
import studyhub.studyhub.global.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public Long createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User(
                request.getEmail(),
                request.getPassword(), // TODO: BCrypt 적용
                request.getName()
        );

        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserResponse findUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return new UserResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }
}