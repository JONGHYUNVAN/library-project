package com.demo.library.user.entity;

import com.demo.library.loanNban.entity.Ban;
import com.demo.library.loanNban.entity.Loan;
import javax.persistence.*;
import lombok.*;

import java.util.List;


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
    @Column(nullable = false,length = 20)
    private String name;
    @Column(nullable = false,length = 20)
    private String nickName;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Getter
    public enum Gender{MALE,FEMALE}

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Ban> bans;

    @Column(nullable = false, name = "user_status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Getter
    public enum Status{ACTIVE,DORMANT}

}

