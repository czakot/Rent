package com.rent.utility;

import com.rent.domain.Role;
import com.rent.entity.UserProfile;
import com.rent.service.UserDetailsImpl;
import org.springframework.security.core.Authentication;

public class Users {

    private Users() {}

    public static UserDetailsImpl getUserDetails(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    public static UserProfile getUserProfile(Authentication authentication) {
        return ((UserDetailsImpl) authentication.getPrincipal()).getUserProfile();
    }

    public static Role getPreferredInitialRole(Authentication authentication) {
        UserDetailsImpl userDetails = Users.getUserDetails(authentication);
        UserProfile userProfile = userDetails.getUserProfile();
        // todo later can be simplified (when userProfile never can be null)
        Role role = userProfile==null ? null : userProfile.getPreferredInitialRole();
        return role != null ? role : Role.getMostWeightedRoleFromSet(userDetails.getRoles());
    }
}
