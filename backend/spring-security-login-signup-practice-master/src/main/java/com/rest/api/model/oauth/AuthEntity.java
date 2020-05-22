e;
package com.rest.api.model.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth")
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    @Column(length = 100)
    private String auth_provider;

    @Column(length = 200)
    private String access_token;

    @Column(length = 200)
    private String refresh_token;

    @Column(length = 50)
    private String token_typ
}
