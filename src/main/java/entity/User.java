package entity;

import com.example.springmvc.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummary.class)
    private Long id;

    @JsonView(Views.UserSummary.class)
    private String name;

    @JsonView(Views.UserSummary.class)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonView(Views.UserDetails.class)
    private List<Order> orders;
}
