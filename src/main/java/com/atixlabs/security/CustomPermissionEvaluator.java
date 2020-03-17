package com.atixlabs.security;

import com.atixlabs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("permission")
public class CustomPermissionEvaluator implements CustomPermissionEvaluatorInterface {

    @Autowired
    private UserRepository userRepository;

    public boolean canUpdate(UserPrincipal userMakingRequest, Long id, String rol) {
        boolean state;
        if(userMakingRequest.getId() != id){
            state = false;
        }else{
            state = true;
        }
        if(hasRole(userMakingRequest, rol)){state = true;}

        return state;
    }

    public boolean hasRole(UserPrincipal principal, String rol) {
        for (GrantedAuthority grantedAuth : principal.getAuthorities()) {
            if (grantedAuth.getAuthority().contains(rol)) {
                return true;
            }
        }
        return false;
    }

}