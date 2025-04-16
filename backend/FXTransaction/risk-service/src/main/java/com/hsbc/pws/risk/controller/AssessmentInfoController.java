package com.hsbc.pws.risk.controller;

import com.hsbc.pws.common.response.Result;
import com.hsbc.pws.risk.entity.AssessmentInfo;
import com.hsbc.pws.risk.filter.FlaskFeign;
import com.hsbc.pws.risk.filter.ThreadLocalUtil;
import com.hsbc.pws.risk.pojo.AssessmentInfoCheck;
import com.hsbc.pws.risk.service.AssessmentInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
	@Resource
	private FlaskFeign flaskFeign;
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

	// 获取交易token
	@PostMapping("getFaceToken")
	Result<?> getTokenByFace(@RequestParam("image") MultipartFile file) {
		return flaskFeign.getTokenByFace(file);
	}

	// token验证 -- KYC
	@PostMapping("/kyc")
	public Result<?> kyc(@RequestParam AssessmentInfo request, @RequestParam String faceToken) {
		Map<String, Object> claims = ThreadLocalUtil.get();
		String username = claims.get("username").toString();
		String userId = claims.get("userId").toString();
		try {
			// 调用人脸 token 验证
			boolean tokenResult = flaskFeign.validateFaceToken(username, faceToken);
			if (!tokenResult) {
				Result.error(401, "人脸Token验证失败：");
			}
			// 验证通过，保存/修改表单信息
			if (request.getClientId().length() > 18) {
				log.warn("客户编号长度错误! ClientId:{}", request.getClientId());
				Result.error("客户编号长度错误!");
			}
			this.assessmentInfoService.save(request);
		} catch (Exception e) {
			Result.error(500, "充值异常：" + e.getMessage());
		}
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

