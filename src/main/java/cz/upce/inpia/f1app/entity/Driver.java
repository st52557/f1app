package cz.upce.inpia.f1app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "id")
    private Set<Result> driverResults;

    private String name;
    private String surename;
    private String code;
    private String nationalilty;
    private int year;

}
