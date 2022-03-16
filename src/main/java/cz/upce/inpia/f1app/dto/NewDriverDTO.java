package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class NewDriverDTO {

    private int id;
    private String name;
    private String surename;
    private String code;
    private String nationalilty;
    private int born;

}
