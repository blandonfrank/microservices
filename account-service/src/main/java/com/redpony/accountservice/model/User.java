package com.redpony.accountservice.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
@Getter @Setter
public class User {
    @NotNull
    @Length(min = 3, max = 20)
    private String username;

    @NotNull
    @Length(min = 6, max = 40)
    private String password;

}
