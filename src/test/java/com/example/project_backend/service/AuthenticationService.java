package com.example.project_backend.service;

import com.example.project_backend.dto.auth.AuthRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AuthenticationService {

    @Value("${test.auth.username}")
    private String username;

    @Value("${test.auth.password}")
    private String password;

    public String getToken() {
        AuthRequest request = new AuthRequest();
        request.setUsername(username);
        request.setPassword(password);

        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

    }

}

