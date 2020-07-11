package com.intellectus.security;

public interface CustomPermissionEvaluatorInterface {

    boolean hasRole(UserPrincipal principal, String rol);

}
