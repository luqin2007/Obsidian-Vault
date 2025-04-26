package com.example.mybank.util;

import org.springframework.validation.Errors;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static final Utils INSTANCE = new Utils();

    public String formatErrors(Errors errors, String name) {
        return "error: " + name;
    }

    public String formatDate(Date date) {
        return SimpleDateFormat.getDateInstance().format(date);
    }

    public String formatMoney(long money, String currency) {
        return String.format("%s %d", currency.toUpperCase(Locale.ROOT), money);
    }
}
