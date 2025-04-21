package com.social.dss_social_media.config;

import com.social.dss_social_media.models.SocialGroups;
import com.social.dss_social_media.models.SocialPost;
import com.social.dss_social_media.models.SocialProfile;
import com.social.dss_social_media.models.SocialUser;
import com.social.dss_social_media.repositories.SocialGroupsRepository;
import com.social.dss_social_media.repositories.SocialPostRepository;
import com.social.dss_social_media.repositories.SocialProfileRepository;
import com.social.dss_social_media.repositories.SocialUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    private final SocialUserRepository userRepository;
    private final SocialGroupsRepository groupsRepository;
    private final SocialProfileRepository socialProfileRepository;
    private final SocialPostRepository postRepository;

    public DataInitializer(SocialUserRepository userRepository, SocialGroupsRepository groupsRepository, SocialProfileRepository socialProfileRepository, SocialPostRepository postRepository) {
        this.userRepository = userRepository;
        this.groupsRepository = groupsRepository;
        this.socialProfileRepository = socialProfileRepository;
        this.postRepository = postRepository;
    }

    @Bean
    public CommandLineRunner initializeData(){
        return args -> {
            //Create Users
            SocialUser user1 = new SocialUser();
            SocialUser user2 = new SocialUser();
            SocialUser user3 = new SocialUser();

            //Save Users to DB
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            //Create Groups
            SocialGroups group1 = new SocialGroups();
            SocialGroups group2 = new SocialGroups();

            //Add users to groups
            group1.getSocialUsers().add(user1);
            group1.getSocialUsers().add(user2);
            group2.getSocialUsers().add(user2);
            group2.getSocialUsers().add(user3);

            //Save groups to the DB
            groupsRepository.save(group1);
            groupsRepository.save(group2);

            //Associate users with groups
            user1.getGroups().add(group1);
            user2.getGroups().add(group1);
            user2.getGroups().add(group2);
            user3.getGroups().add(group2);

            //Sae user back to the DB to update associations
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            //Create posts
            SocialPost post1 = new SocialPost();
            SocialPost post2 = new SocialPost();
            SocialPost post3 = new SocialPost();

            //Assoc post with users;
            post1.setUser(user1);
            post2.setUser(user2);
            post3.setUser(user3);

            //Save posts to DB
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            //Create profiles
            SocialProfile profile1 = new SocialProfile();
            SocialProfile profile2 = new SocialProfile();
            SocialProfile profile3 = new SocialProfile();

            //Assoc profiles with users
            profile1.setSocialUser(user1);
            profile2.setSocialUser(user2);
            profile3.setSocialUser(user3);

            //Save profile to DB
            socialProfileRepository.save(profile1);
            socialProfileRepository.save(profile2);
            socialProfileRepository.save(profile3);

        };
    }
}
