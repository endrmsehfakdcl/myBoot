package com.keduit.controller;

import com.keduit.dto.ResponseDTO;
import com.keduit.dto.UserDTO;
import com.keduit.model.UserEntity;
import com.keduit.security.TokenProvider;
import com.keduit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO){

        try{
            if(userDTO == null || userDTO.getPassword() == null)  {
                throw new RuntimeException("사용자 혹은 비밀번호가 없습니다.");
            }

            UserEntity userEntity = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();

            UserEntity registeredUser = userService.create(userEntity);
            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .password(passwordEncoder.encode(registeredUser.getPassword()))
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = userService.getByCredentials(userDTO.getUsername(), userDTO.getPassword(), passwordEncoder);

        if(userEntity != null) {
            final String token = tokenProvider.create(userEntity);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(userEntity.getUsername())
                    .id(userEntity.getId())
                    .password(userEntity.getPassword())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login error")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
