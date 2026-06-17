package io.echo.silentcabin.user.service;

import io.echo.silentcabin.user.domain.User;
import io.echo.silentcabin.user.dto.SignUpRequestDto;
import io.echo.silentcabin.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    User user;
    Long userId = 1L;
    String email = "test@example.com";
    String nickname = "test";
    String password = "test1234!";
    String encodedPassword = "test1234!";

    @BeforeEach
    void setUp() {
        user = new User(email, nickname, encodedPassword);
        ReflectionTestUtils.setField(user, "id", userId);
    }

    @Nested
    @DisplayName("request 메서드는")
    public class Request {
        SignUpRequestDto request;
        @BeforeEach
        void setUp() {
            request = new SignUpRequestDto(email, nickname, password);
        }
        @Nested
        @DisplayName("유효한 입력이 주어지면")
        class Context_with_valid_request {
            @Test
            @DisplayName("회원 정보를 저장한다")
            void it_save_user() {
                //given
                given(userRepository.existsByEmail(request.email())).willReturn(false);
                given(passwordEncoder.encode(request.password())).willReturn(encodedPassword);

                //when
                userService.signUp(request);

                //then
                ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
                verify(userRepository).save(captor.capture());

                User savedUser = captor.getValue();
                assertThat(savedUser.getEmail()).isEqualTo(email);
                assertThat(savedUser.getNickname()).isEqualTo(nickname);
                assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
            }
        }

    }

}