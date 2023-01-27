package com.example.SmartFridge.DTO;

public class AggregationTransportDTO {
    String field1;
    String field2;
    Double field3;

    public AggregationTransportDTO(String field1, String field2, Double field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public String getField1() {
        return field1;
    }

    public String getField2() {
        return field2;
    }

    public Double getField3() {
        return field3;
    }
}
