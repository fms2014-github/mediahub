package com.ssafy.d103.auth.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "label")
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "LABEL_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(length = 50, nullable = false)
    private String labelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private LabelEntity parent;

    @OneToMany(mappedBy = "parent")
    private List<LabelEntity> children = new ArrayList<>();

    public void addLabel(final LabelEntity label){
        children.add(label);
    }

    public void removeLabel(final LabelEntity label){
        children.remove(label);
        label.setMember(null);
    }

    @OneToMany
    @JoinColumn(name = "CHANNEL_ID")
    private List<ChannelEntity> followChannelList;


    @Override
    public String toString() {
        return "LabelEntity{" +
                "id=" + id +
                ", labelName='" + labelName + '\'' +
                ", parent=" + parent +
                ", children=" + children +
                ", followChannelList=" + followChannelList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabelEntity that = (LabelEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(member, that.member) &&
                Objects.equals(labelName, that.labelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, labelName);
    }
}