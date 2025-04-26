package com.example.mybank.controller;

import com.example.mybank.ResultContext;
import com.example.mybank.domain.FixedDepositDetails;
import com.example.mybank.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

@Controller
@RequestMapping("/fixedDeposit")
public class FixedDepositControllerImpl implements FixedDepositController {

    @Autowired
    FixedDepositService fixedDepositService;

    private static final String LIST_METHOD = "getFixedDepositList";
    private final Queue<ResultContext<?>> deferredResultQueue = new ConcurrentLinkedQueue<>();

    @RequestMapping(params = "fdAction=create", method = RequestMethod.POST)
    public ModelAndView showOpenFixedDepositForm() {
        FixedDepositDetails fdd = new FixedDepositDetails();
        fdd.setId(1);
        fdd.setBankAccountId(1);
        fdd.setCreationDate(new Date());
        fdd.setMaturityDate(new Date());
        fdd.setEmail("You must enter a valid email");
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("newFixedDepositDetails", fdd);
        return new ModelAndView("createFixedDepositForm", modelMap);
    }

    @RequestMapping(params = "fdAction=close", method = RequestMethod.GET)
    public String closeFixedDeposit(@RequestParam("fixedDepositId") int id) {
        fixedDepositService.closeFixedDeposit(id);
        return "redirect:/fixedDeposit/fdList";
    }

    @GetMapping("fdList")
    public ModelAndView createFixedDeposit() {
        FixedDepositDetails fdd = new FixedDepositDetails();
        fdd.setId(1);
        fdd.setBankAccountId(1);
        fdd.setCreationDate(new Date());
        fdd.setMaturityDate(new Date());
        Map<String, Object> model = Map.of("newFixedDepositDetails", fdd);
        return new ModelAndView("createFixedDepositForm", model);
    }

    @RequestMapping(params = "fdAction=open", method = RequestMethod.GET)
    public ModelAndView openFixedDeposit(@RequestParam Map<String, String> params) {
        String depositAmount = params.get("depositAmount");
        String tenure = params.get("tenure");
        System.out.println(params);
        return new ModelAndView("access-denied");
    }

    @GetMapping(path = "/list_async")
    public Callable<ModelAndView> listFixedDepositsAsync() {
        return () -> {
            Thread.sleep(5000);
            Map<String, Object> modelData = new HashMap<>();
            modelData.put("fdList", fixedDepositService.getFixedDeposits());
            return new ModelAndView("fixedDeposit/list", modelData);
        };
    }

    @GetMapping(path = "/list_async2")
    public DeferredResult<ResponseEntity<List<FixedDepositDetails>>> listFixedDeposits2() {
        ResultContext<ResponseEntity<List<FixedDepositDetails>>> context = new ResultContext<>(LIST_METHOD);
        deferredResultQueue.add(context);
        return context.getResult();
    }

    // 设置任务执行计划
    @Scheduled(fixedRate = 1000)
    public void processResults() {
        while (!deferredResultQueue.isEmpty()) {
            ResultContext<?> context = deferredResultQueue.poll();
            switch (context.getMethodToInvoke()) {
                case LIST_METHOD:
                    var result = (DeferredResult<ResponseEntity<List<FixedDepositDetails>>>) context.getResult();
                    List<FixedDepositDetails> fixedDeposits = fixedDepositService.getFixedDeposits();
                    var response = new ResponseEntity<>(fixedDeposits, HttpStatus.OK);
                    result.setResult(response);
                    break;
                // ...
            }
        }
    }
}
