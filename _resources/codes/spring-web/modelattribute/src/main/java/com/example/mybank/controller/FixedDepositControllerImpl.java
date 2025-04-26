package com.example.mybank.controller;

import com.example.mybank.domain.FixedDepositDetails;
import com.example.mybank.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/fixedDeposit")
public class FixedDepositControllerImpl implements FixedDepositController {

    @Autowired
    private FixedDepositService fixedDepositService;

    @GetMapping(params = "fdAction=createFDForm")
    public String showOpenFixedDepositForm(Model model) {
        // 在这里的时候，getFixedDepositDetails() 已经被调用了
        // 此时 model 中已经存在 fixedDepositDetails 对象了
        System.out.println(model.getAttribute("fixedDepositDetails"));
        return "access-denied";
    }

    @ModelAttribute("fixedDepositDetails")
    public FixedDepositDetails getFixedDepositDetails() {
        FixedDepositDetails fixedDepositDetails = new FixedDepositDetails();
        fixedDepositDetails.setId(1);
        fixedDepositDetails.setBankAccountId(1);
        fixedDepositDetails.setCreationDate(new Date());
        fixedDepositDetails.setMaturityDate(new Date());
        System.out.println("getFixedDepositDetails() called, return a new instance of FixedDepositDetails");
        return fixedDepositDetails;
    }

    // 视图名称: fixedDeposit/list.html
    @GetMapping(path = "/list")
    @ModelAttribute("fdList")
    public List<FixedDepositDetails> listFixedDeposits() {
        return fixedDepositService.getFixedDeposits();
    }
}
