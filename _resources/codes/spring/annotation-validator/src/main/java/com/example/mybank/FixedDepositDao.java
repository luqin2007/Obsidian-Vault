package com.example.mybank;

import org.springframework.validation.BeanPropertyBindingResult;

public class FixedDepositDao {

    public boolean createFixedDeposit(FixedDepositDetails fdd) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(fdd, "fixedDepositDetails");
        FixedDepositValidator validator = new FixedDepositValidator();
        validator.validate(fdd, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println("Validation failed for FixedDepositDetails");
            return false;
        }

        // 校验通过
        // do something ...

        return true;
    }
}
