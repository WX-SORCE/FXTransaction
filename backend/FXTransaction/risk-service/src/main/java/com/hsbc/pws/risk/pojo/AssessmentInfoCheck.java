package com.hsbc.pws.risk.pojo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Title 评估表单审核Pojo
 * @Description AssessmentInfoCheck
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
public class AssessmentInfoCheck implements Serializable {
	private static final long serialVersionUID = 2656768871784471039L;
	
	/**
	 * 评估表单编号
	 */
	@NotNull(message = "评估表单编号不能为空！")
	private Integer aiId;
	/**
	 * 客户编号
	 */
	@NotBlank(message = "客户编号不能为空！")
	private String clientId;
	/**
	 * 风控人员编号
	 */
	@NotBlank(message = "风控人员编号不能为空！")
	private String evaluatorId;
	/**
	 * 附注说明
	 */
	private String remarks;
}
