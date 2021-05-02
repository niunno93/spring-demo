package it.ovi.demo.controllers.v1.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CreateUserDetailDto {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    public CreateUserDetailDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    protected CreateUserDetailDto() {
        // json
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
