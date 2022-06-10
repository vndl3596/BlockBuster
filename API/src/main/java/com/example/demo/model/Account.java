package com.example.demo.model;

import com.example.demo.model.address.Town;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_acc_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "acc_name", unique = true)
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Column(name = "acc_password")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @Column(name = "email", unique = true)
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[a-z0-9](.?[a-z0-9]){0,}@g(oogle)?mail.com$", message = "Invalid email")
    private String email;

    @Column(name = "avatar")
    private String avatar = "https://www.shareicon.net/data/512x512/2017/01/06/868320_people_512x512.png";

    @Column(name = "firstname")
    @NotBlank(message = "Firstname cannot be empty")
    private String firstname;

    @Column(name = "lastname")
    @NotBlank(message = "Lastname cannot be empty")
    private String lastname;

    @Column(name = "birthday")
    @NotNull(message = "Birthday cannot be empty")
    private Date birthday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_commune_ward_town")
    @EqualsAndHashCode.Exclude
    private Town idTown;

    @Column(name = "address_detail")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    @NotNull(message = "Gender cannot be empty")
    private boolean gender;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<RoleForAccount> accountRoles = new ArrayList<>();

    @OneToMany(mappedBy = "accountInToken", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<VerificationToken> verificationTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<UserHistory> userHistories = new ArrayList<>();

    @OneToMany(mappedBy = "accountDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private List<MovieEvaluate> movieEvaluates = new ArrayList<>();

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthday=" + birthday +
                ", idTown=" + idTown +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", accountRoles=" + accountRoles +
                ", verificationTokens=" + verificationTokens +
                ", userHistories=" + userHistories +
                ", movieEvaluates=" + movieEvaluates +
                '}';
    }
}
