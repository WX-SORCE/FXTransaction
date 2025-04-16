package com.hsbc.pws.risk.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Title 年度累计总和
 * @Description AnnualCumulativeTotal
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
public class AnnualCumulativeTotal implements Serializable {
	private static final long serialVersionUID = -819374395168636611L;
	
	/**
	 * 客户编号
	 */
	private String clientId;
	/**
	 * 累计金额
	 */
	@Builder.Default
	private BigDecimal amount = new BigDecimal(0);
	/**
	 * 累计次数
	 */
	@Builder.Default
	private Integer times = 0;
	/**
	 * 监控状态 0允许交易、1年度累计交易金额超限、2年度累计交易次数超限
	 */
	@Builder.Default
	private Integer state = 0;
}
