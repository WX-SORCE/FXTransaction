package com.alxy.accountservice.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeVo {
    String userId;
    String baseCurrency;
    BigDecimal baseBalance;
    String targetCurrency;
    BigDecimal targetBalance;
}
