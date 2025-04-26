package com.example.mybank;

import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service("customerRequestService")
public class CustomerRequestServiceImpl implements CustomerRequestService {

    @Override
    public Calendar submitRequest(String type, String description, Calendar accountSinceDate) {
        return accountSinceDate;
    }
}
