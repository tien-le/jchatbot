package com.learn4you.jchatbot.dto.request;

public record ExpenseInfo(
    String category,
    String itemName,
    Double amount,
    String unit
) {}