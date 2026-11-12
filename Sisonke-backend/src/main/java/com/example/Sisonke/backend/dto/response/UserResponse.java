
package com.example.Sisonke.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String fullName;
    private String email;
    private String token;

    public UserResponse(UUID id, String fullName, String email, String token) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.token = token;
    }
}
