package com.intellectus.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserToken {
    private String username;
    private String token;
    private int connections;

    public UserToken(String username, String token){
        this.username = username;
        this.token = token;
        connections = 1;
    }

    public void connectUser(){
        connections ++;
    }

    public void disconnectUser(){
        if (connections == 0) throw new RuntimeException("No connections where found when trying to disconnect a user");
        connections--;
    }

}
