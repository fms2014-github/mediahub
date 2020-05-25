package com.ssafy.d103.auth.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder // builder를 사용할수 있게 합니다.
@Entity // jpa entity임을 알립니다.
@Getter // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor // 인자없는 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
@Table(name = "member") // 'user' 테이블과 매핑됨을 명시
public class MemberEntity{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
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

    @Column(length = 1)
    private Integer firstLogin;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<RoleType> roles = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "auth_id")
    private List<AuthEntity> auth;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "label_id")
    private List<LabelEntity> labelList;


}