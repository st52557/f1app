package cz.upce.inpia.f1app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Table(name="user", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String password;
    private String email;
    private String name;
    private String recoveryCode = "";
    private Date recoveryCodeExp;
    private Integer admin = 0;
}
