package entity;

import com.example.springmvc.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserDetails.class)
    private Long id;

    @JsonView(Views.UserDetails.class)
    private String product;

    @JsonView(Views.UserDetails.class)
    private Double amount;

    @JsonView(Views.UserDetails.class)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

