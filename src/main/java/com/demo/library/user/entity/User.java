package com.demo.library.user.entity;

import com.demo.library.loanNban.entity.Ban;
import com.demo.library.loanNban.entity.Loan;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private  String password;
    @Column(nullable = false,length = 20)
    private String name;
    @Column(nullable = false,length = 20)
    private String nickName;
    @Builder.Default
    @ElementCollection(fetch = LAZY)
    private List<String> roles = new ArrayList<>();

    @Column(nullable = false,length = 40)
    private String email;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Getter
    public enum Gender{MALE,FEMALE,OAUTH}

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Ban> bans;
    @Column
    private String provider;
    @Column
    private Long providerId;
    @Column(nullable = false, name = "user_status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Getter
    public enum Status{ACTIVE,DORMANT}

}

