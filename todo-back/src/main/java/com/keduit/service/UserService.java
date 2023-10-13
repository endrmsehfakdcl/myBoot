package com.keduit.service;

import com.keduit.model.UserEntity;
import com.keduit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("invalid arguments");
        }

        final String username = userEntity.getUsername();
        if(userRepository.existsByUsername(username)){
            log.warn("이미 등록된 사용자가 있습니다.");
            throw new RuntimeException("이미 등록된 사용자가 있습니다.");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String username,
                                       final String password,
                                       final PasswordEncoder passwordEncoder){
        final UserEntity oriUser = userRepository.findByUsername(username);

        if (oriUser != null && passwordEncoder.matches(password, oriUser.getPassword()) ) {
            return oriUser;
        }

        return null;
    }


}
