package com.social.dss_social_media.repositories;

import com.social.dss_social_media.models.SocialPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialPostRepository extends JpaRepository<SocialPost,Long> {
}
