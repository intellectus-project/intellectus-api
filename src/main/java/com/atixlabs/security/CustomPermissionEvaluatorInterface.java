package com.atixlabs.security;

public interface CustomPermissionEvaluatorInterface {

    boolean hasRole(UserPrincipal principal, String rol);

}
