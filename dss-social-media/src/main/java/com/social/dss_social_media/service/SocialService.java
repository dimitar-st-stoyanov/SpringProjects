package com.social.dss_social_media.service;

import com.social.dss_social_media.models.SocialUser;
import com.social.dss_social_media.repositories.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialService {

    @Autowired
    private SocialUserRepository userRepository;

    public List<SocialUser> getAllUsers() {
              return userRepository.findAll();
    }

    public SocialUser saveUser(SocialUser socialUser) {
        return userRepository.save(socialUser);
    }

    public SocialUser deleteUser(Long userId) {
        SocialUser socialUser = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User Not Found"));
        userRepository.delete(socialUser);
        return socialUser;
    }
}
