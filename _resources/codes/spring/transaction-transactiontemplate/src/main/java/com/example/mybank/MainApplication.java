package com.example.mybank;

import com.example.mybank.dao.CustomerRegistrationDao;
import com.example.mybank.domain.CustomerRegistrationDetails;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "com.example.mybank")
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
        CustomerRegistrationDao dao = (CustomerRegistrationDao) context.getBean("customerRegistrationDao");
        CustomerRegistrationDetails details = new CustomerRegistrationDetails();
        details.setAddress("到二仙桥，走成华大道");
        details.setAccountNumber("1");
        details.setCardNumber("1");
        dao.registerCustomer(details);
    }
}
