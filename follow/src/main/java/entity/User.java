package entity;

import dto.FollowDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;
    private String email;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    private List<Follow> followings;

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    private List<Follow> followers;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public FollowDTO toFollow(String status) {
        return new FollowDTO(this.name, this.email, status);
    }
}
