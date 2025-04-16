package com.hsbc.pws.risk.controller;

import com.hsbc.pws.common.response.Result;
import com.hsbc.pws.risk.entity.AssessmentInfo;
import com.hsbc.pws.risk.pojo.AssessmentInfoCheck;
import com.hsbc.pws.risk.service.AssessmentInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.annotation.Resource;

/**
 * @Title 风险评估表单管理
 * @Description AssessmentInfoController
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@RestController
@RequestMapping("/risks/info")
@ResponseBody
@Slf4j
public class AssessmentInfoController {
	@Resource
	private AssessmentInfoService assessmentInfoService;

	// 查询列表
	@PostMapping("/list")
	public Result<Page<AssessmentInfo>> list(@RequestBody AssessmentInfo request) throws Exception {
		return Result.success(this.assessmentInfoService.list(request));
	}
	@PostMapping("/info/listAll")
	public Result<List<AssessmentInfo>> listAll(@RequestBody AssessmentInfo request){
		return Result.success(this.assessmentInfoService.findAll());
	}
	
	// 保存
	@PostMapping(value = { "/save" })
	public Result<?> save(@Valid @RequestBody AssessmentInfo request)  throws Exception {
		if (request.getClientId().length() > 18) {
			log.warn("客户编号长度错误! ClientId:{}", request.getClientId());
			Result.error("客户编号长度错误!");
		}

		this.assessmentInfoService.save(request);

		return Result.success();
	}

	// 审核
	@PostMapping("/check")
	public Result<?> check(@Valid @RequestBody AssessmentInfoCheck assessmentInfoCheck) throws Exception {
		this.assessmentInfoService.check(assessmentInfoCheck);

		return Result.success();
	}

	// 查看
	@PostMapping("/viewById/{aiId}")
	public Result<AssessmentInfo> viewById(@PathVariable Integer aiId) throws Exception {
		AssessmentInfo assessmentInfo = this.assessmentInfoService.findById(aiId);

		if (assessmentInfo == null) {
			log.warn("评估表单数据不存在! aiId:{}", aiId);
			Result.error("评估表单数据不存在! aiId:" + aiId);
		}

		return Result.success(assessmentInfo);
	}

	// 查看
	@PostMapping("/viewByClientId/{clientId}")
	public Result<List<AssessmentInfo>> viewByClientId(@PathVariable String clientId) throws Exception {
		return Result.success(this.assessmentInfoService.findByClientId(clientId));
	}
}
