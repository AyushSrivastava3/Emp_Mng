package com.dq.empportal.model;

import com.dq.empportal.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role; // Added role field

}
