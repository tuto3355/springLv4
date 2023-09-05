package com.nakta.springlv1.domain.user.controller;

import com.nakta.springlv1.domain.user.service.UserService;
import com.nakta.springlv1.domain.user.dto.LoginRequestDto;
import com.nakta.springlv1.domain.user.dto.SignupRequestDto;
import com.nakta.springlv1.domain.user.dto.StringResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/user/signup")
    public ResponseEntity<StringResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(userService.signup(requestDto));
    }

//    @ResponseBody
//    @PostMapping("/user/login")
//    public ResponseEntity<StringResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
//        return ResponseEntity.ok(userService.login(requestDto, res));
//    }
}
