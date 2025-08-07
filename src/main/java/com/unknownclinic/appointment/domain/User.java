package com.unknownclinic.appointment.domain;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class User {
	
    private Long id;

    @NotNull
    private String cardNumber;

    @NotNull
    private LocalDate birthday;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;
}