package com.hsbc.pws.risk.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.pws.risk.pojo.ForeignExchange;
import com.hsbc.pws.risk.service.FxMonitoringService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title 外汇交易监控
 * @Description FxMonitoringController
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@RestController
@RequestMapping("/fx")
@ResponseBody
@Slf4j
public class FxMonitoringController {
	@Resource
	private FxMonitoringService fxMonitoringService;

	// 监控
	@PostMapping("/monitoring")
	public int monitoring(@Valid @RequestBody ForeignExchange request) throws Exception {
		return this.fxMonitoringService.fireRules(request);
	}
}
