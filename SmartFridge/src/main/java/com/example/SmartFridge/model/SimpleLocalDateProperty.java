package com.example.SmartFridge.model;

import java.time.LocalDate;

public class SimpleLocalDateProperty {
    private LocalDate data;
    public SimpleLocalDateProperty(LocalDate d) {
        data = d;
    }
    public void set(LocalDate d){
        data = d;
    }
    public LocalDate get(){
        return data;
    }
}