package com.example.mybank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class BankstatementControllerImpl implements BankstatementController {

    @RequestMapping(path = "/user/{userId}/bankstatement/{statementId}")
    public String getBankstatementDirectly(@PathVariable("userId") int userId, @PathVariable("statementId") int statementId) {
        System.out.println("userId=" + userId + ", statementId=" + statementId);
        return "access-denied";
    }

    @RequestMapping(path = "/user/{userId}/bankstatement2/{statementId}")
    public String getBankstatementByMap(@PathVariable Map<String, String> allVariables) {
        String userId = allVariables.get("userId");
        String statementId = allVariables.get("statementId");
        System.out.println("userId=" + userId + ", statementId=" + statementId);
        return "access-denied";
    }

    @RequestMapping(path = "/bankstatement/{statementId:[\\d\\d\\d]}.{responseType:[a-z]}")
    public ModelAndView getBankstatementByRegex(@PathVariable("statementId") int statementId, String responseType) {
        System.out.println("statementId=" + statementId + ", responseType=" + responseType);
        return new ModelAndView("access-denied");
    }

    @RequestMapping(path = "/bankstatement/{statementId}")
    public ModelAndView getBankstatementFromMatrix(@PathVariable("statementId") int statementId,
                                 @MatrixVariable("responseType") String responseType) {
        System.out.println("statementId=" + statementId + ", responseType=" + responseType);
        return new ModelAndView("access-denied");
    }

    @RequestMapping(path = "/bankstatement/{statementId}/user/{userName}")
    public ModelAndView getBankstatementFromSameMatrixName(@PathVariable("statementId") int statementId,
                                 @PathVariable("userName") String userName,
                                 @MatrixVariable(name = "statementId", pathVar = "id") int someId,
                                 @MatrixVariable(name = "userName", pathVar = "id") int anotherId) {
        System.out.println("statementId=" + statementId + ", username=" + userName);
        System.out.println("someId=" + someId + ", anotherId=" + anotherId);
        return new ModelAndView("access-denied");
    }
}
