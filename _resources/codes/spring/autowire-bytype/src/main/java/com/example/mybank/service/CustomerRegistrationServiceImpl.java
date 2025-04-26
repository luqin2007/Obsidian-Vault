package com.example.mybank.service;

import com.example.mybank.dao.CustomerRegistrationDao;
import com.example.mybank.domain.CustomerRegistrationDetails;

public class CustomerRegistrationServiceImpl {

    private CustomerRegistrationDetails customerRegistrationDetails;
    private CustomerRegistrationDao customerRegistrationDao;

    public void setCustomerRegistrationDetails(CustomerRegistrationDetails customerRegistrationDetails) {
        this.customerRegistrationDetails = customerRegistrationDetails;
    }

    public void setCustomerRegistrationDao(CustomerRegistrationDao customerRegistrationDao) {
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
