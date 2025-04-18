package com.hsbc.pws.risk.controller;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.pws.common.response.Result;
import com.hsbc.pws.risk.entity.RiskAssessment;
import com.hsbc.pws.risk.service.RiskAssessmentService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title 风险评估管理
 * @Description RiskAssessmentController
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@RestController
@RequestMapping("/risks")
@ResponseBody
@Slf4j
public class RiskAssessmentController {
	@Resource
	private RiskAssessmentService riskAssessmentService;

	// 查询列表
	@PostMapping("/list")
	public Result<Page<RiskAssessment>> list(@RequestBody RiskAssessment request) throws Exception {
		return Result.success(this.riskAssessmentService.list(request));
	}
	@PostMapping("/listAll")
	public Result<?> listAll(@RequestBody RiskAssessment request) throws Exception {
		return Result.success(this.riskAssessmentService.listAll());
	}
	
	// 查看
	@PostMapping("/viewById/{riskId}")
	public Result<RiskAssessment> viewById(@PathVariable String riskId) throws Exception {
		RiskAssessment riskAssessment = this.riskAssessmentService.findById(riskId);
		if (riskAssessment == null) {
			log.warn("风险评估数据不存在! riskId:{}", riskId);
			Result.error("风险评估数据不存在! riskId:" + riskId);
		}

		return Result.success(riskAssessment);
	}

	// 查看
	@PostMapping("/viewByClientId/{clientId}")
	public Result<RiskAssessment> viewByClientId(@PathVariable String clientId) throws Exception {
		RiskAssessment riskAssessment = this.riskAssessmentService.findByClientId(clientId);
		if (riskAssessment == null) {
			log.warn("风险评估数据不存在! clientId:{}", clientId);
			Result.error("风险评估数据不存在! clientId:" + clientId);
		}

		return Result.success(riskAssessment);
	}
}
