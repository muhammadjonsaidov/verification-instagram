package org.rhaen.otpverificationinstagram.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Instagram username is required")
    @Size(min = 3, max = 30, message = "Instagram username must be between 3 and 30 characters")

    @Pattern(regexp = "^(?!.*\\.\\.)(?!.*\\.$)[a-z0-9._]+$", message = "Invalid Instagram username format")
    private String instagramUsername;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 254, message = "Email address cannot exceed 254 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_#\\-.,:;{}|()<>/~`^+=]).{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;
}
