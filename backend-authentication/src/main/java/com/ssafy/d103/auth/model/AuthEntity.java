package com.ssafy.d103.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth")
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(length = 100)
    private String auth_provider;

    @Column(length = 200)
    private String access_token;

    @Column(length = 200)
    private String refresh_token;

    @Column(length = 50)
    private String token_type;

    @Override
    public String toString() {
        return "AuthEntity{" +
                "id=" + id +
                ", auth_provider='" + auth_provider + '\'' +
                ", access_token='" + access_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", token_type='" + token_type + '\'' +
                '}';
    }
}