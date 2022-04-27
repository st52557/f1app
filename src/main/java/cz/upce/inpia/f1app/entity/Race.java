package cz.upce.inpia.f1app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Table(name="race", schema = "public")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "id",cascade = CascadeType.ALL)
    private Set<Result> raceResults;

    private Integer year;
    private Integer round;
    private String circuit;
}
