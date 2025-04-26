package com.example.mybank;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.Calendar;

@Validated
public interface CustomerRequestService {

    @Future
    Calendar submitRequest(@NotBlank String type,
                           @Size(min = 20, max = 100) String description,
                           @Past Calendar accountSinceDate);
}
