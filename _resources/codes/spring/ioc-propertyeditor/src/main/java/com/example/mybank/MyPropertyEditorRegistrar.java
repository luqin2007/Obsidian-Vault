package com.example.mybank;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {

    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), false);
        registry.registerCustomEditor(Date.class, dateEditor);
    }
}
