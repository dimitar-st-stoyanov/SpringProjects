package com.social.dss_social_media.repositories;

import com.social.dss_social_media.models.SocialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialProfileRepository extends JpaRepository<SocialProfile,Long> {
}
