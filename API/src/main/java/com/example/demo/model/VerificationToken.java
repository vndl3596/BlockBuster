package com.example.demo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @EqualsAndHashCode.Exclude
    private Account accountInToken = new Account();

    @Column(name = "create_time")
    private Instant createdTime = Instant.now();

    @Column(name = "token_content")
    @NotBlank(message = "Token content cannot be empty")
    private String tokenContent;


}
