package com.thang.spotify.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thang.spotify.common.enums.Gender;
import com.thang.spotify.dto.validator.GenderSubset;
import com.thang.spotify.dto.validator.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Schema(
            description = "Email address of the user",
            example = "abc@gmail.com"
    )
    private String email;

    @NotBlank(message = "Username is required")
    @Schema(
            description = "The username of the user",
            example = "giao.lang"
    )
    private String displayName;

    @NotBlank(message = "Password is required")
    @Schema(
            description = "The password of the user",
            example = "abc12345678!@#"
    )
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{10,}$",
            message =
                    "Password must be at least 10 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @GenderSubset(anyOf = {Gender.FEMALE, Gender.MALE, Gender.OTHER}, message = "Invalid gender value")
    private Gender gender;

    @Schema(
            description = "The date of birth of the user",
            example = "1990-01-01"
    )
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

}
