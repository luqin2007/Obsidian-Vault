package com.example.mybank;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class FixedDepositDetails {
    @Min(0)
    private long id;
    @Min(1000)
    private float depositAmount;
    private int tenure;
    @NotBlank
    private String email;
}
