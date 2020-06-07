package com.ssafy.d103.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 100)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column
    private String profileUrl;

    @Column
    private String providerId;

    @Column(length = 1, nullable = false)
    @ColumnDefault(value = "0")
    private Integer firstLogin;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false,length=20)
    private RoleType role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Auth> auth = new ArrayList<Auth>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<StreamChannel> streamChannel = new ArrayList<StreamChannel>();

    @Column
    private Long rootLabelId;

    public Member(String name, String email, AuthProvider provider, int firstLogin){
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.firstLogin = firstLogin;
    }


}