package cz.upce.inpia.f1app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    private Long driver_id;

    private String name;
    private String surename;
    private String code;
    private String nationalilty;
    private int year;

}
