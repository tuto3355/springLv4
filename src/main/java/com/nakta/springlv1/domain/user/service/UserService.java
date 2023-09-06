package com.nakta.springlv1.domain.user.service;

import com.nakta.springlv1.domain.user.dto.SignupRequestDto;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import com.nakta.springlv1.domain.user.entity.User;
import com.nakta.springlv1.domain.user.entity.UserRoleEnum;
import com.nakta.springlv1.domain.user.repository.UserRepository;
import com.nakta.springlv1.global.exception.CustomException;
import com.nakta.springlv1.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN_TOKEN = "MEronGMERoNGaMEROngMErONG";

    public StringResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> tmpUser = userRepository.findByUsername(username);
        if (tmpUser.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_ID);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(ErrorCode.ADMINTOKEN_NOT_MATCH);
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username,password, role);
        userRepository.save(user);
        return new StringResponseDto( "새로운 아이디 저장 성공 ㅋㅋ");

    }
}
