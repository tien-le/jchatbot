package com.learn4you.jchatbot.dto.request;

public record BillItem(String product,
                       Integer quantity,
                       Double price,
                       String unit) {
}