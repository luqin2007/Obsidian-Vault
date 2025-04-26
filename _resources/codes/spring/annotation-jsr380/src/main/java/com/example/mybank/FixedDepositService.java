package com.example.mybank;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FixedDepositService {

    @Autowired
    private Validator validator;

    @Autowired
    private ValidatorFactory validatorFactory;

    public boolean createFixedDeposit(FixedDepositDetails fdd) {
        // 这个 Validator 是 JSR380 的 jakarta.validation.Validator
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<FixedDepositDetails>> validations = validator.validate(fdd);
        if (!validations.isEmpty()) {
            System.out.println("Validation failed for FixedDepositDetails");
            return false;
        }

        // 校验成功
        // do something

        return true;
    }

    public boolean createFixedDeposit2(FixedDepositDetails fdd) {
        // 这个 Validator 是 JSR380 的 jakarta.validation.Validator
        Set<ConstraintViolation<FixedDepositDetails>> validations = validator.validate(fdd);
        if (!validations.isEmpty()) {
            System.out.println("Validation failed for FixedDepositDetails");
            return false;
        }

        // 校验成功
        // do something

        return true;
    }
}
