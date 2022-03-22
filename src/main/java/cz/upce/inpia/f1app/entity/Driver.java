package cz.upce.inpia.f1app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Max;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "id")
    private Set<Result> driverResults;

    private String name;
    private String surename;
    @Size(min = 3,max = 3)
    private String code;
    private String ref_name;
    private String nationalilty;
    @Min(1000)
    @Max(3000)
    private int born;

}
