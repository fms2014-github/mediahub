package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "labels")
public class Label {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "label_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(length = 50, nullable = false)
    private String labelName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "super_label_id")
    private Label superLabel;

    @OneToMany(mappedBy = "superLabel", cascade = CascadeType.ALL)
    private Collection<Label> subLabels = new ArrayList<Label>();

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    private Collection<Channel> channels = new ArrayList<Channel>();

    public Label() {}
    public Label(String labelName){
        this.labelName = labelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label that = (Label) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(labelName, that.labelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, labelName);
    }
}