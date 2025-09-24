package org.rhaen.otpverificationinstagram.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Bu yerda ham heshlangan parol saqlanadi

    @Column(name = "instagram_username", unique = true, nullable = false)
    private String instagramUsername;

    @Column(name = "instagram_psid", unique = true) // Instagram Page-Scoped ID
    private String instagramPsid;

    @Column(name = "full_name")
    private String fullName;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}