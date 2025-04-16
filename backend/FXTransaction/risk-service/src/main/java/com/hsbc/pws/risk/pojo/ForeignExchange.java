package com.hsbc.pws.risk.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Title 外汇交易BO
 * @Description ForeignExchange
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForeignExchange implements Serializable {
	private static final long serialVersionUID = 5972561046771055196L;
	
	/**
	 * 客户编号
	 */
	@NotBlank(message = "客户编号不能为空！")
	private String userId;
	/**
	 * 账户编号
	 */
	@NotBlank(message = "账户编号不能为空！")
	private String accountId;
	/**
	 * 交易类型 0结汇 1购汇
	 */
	@NotNull(message = "交易类型不能为空！")
	private Integer fxType;
	/**
	 * 交易金额
	 */
	@NotNull(message = "交易金额不能为空！")
	private BigDecimal amount;
}
