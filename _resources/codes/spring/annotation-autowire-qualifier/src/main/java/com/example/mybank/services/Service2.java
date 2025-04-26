package com.example.mybank.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("myservice")
public class Service2 implements MyService {
}
