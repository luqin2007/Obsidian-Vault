package com.example.mybank.service;

import com.example.mybank.dao.CustomerRegistrationDao;
import com.example.mybank.domain.CustomerRegistrationDetails;

import java.beans.ConstructorProperties;

public class CustomerRegistrationServiceImpl {

    private CustomerRegistrationDetails customerRegistrationDetails;
    private CustomerRegistrationDao customerRegistrationDao;

    public CustomerRegistrationServiceImpl(CustomerRegistrationDetails customerRegistrationDetails, CustomerRegistrationDao customerRegistrationDao) {
        this.customerRegistrationDetails = customerRegistrationDetails;
        this.customerRegistrationDao = customerRegistrationDao;
    }

    @Override
    public String toString() {
        return "CustomerRegistrationServiceImpl{\n" +
                "    customerRegistrationDetails=" + customerRegistrationDetails +
                ",\n    customerRegistrationDao=" + customerRegistrationDao +
                "\n}";
    }
}
