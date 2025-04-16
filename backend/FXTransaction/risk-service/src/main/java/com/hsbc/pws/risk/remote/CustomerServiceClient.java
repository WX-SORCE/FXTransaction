package com.hsbc.pws.risk.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hsbc.pws.common.response.Result;
import com.hsbc.pws.risk.pojo.CustomerRisk;

/**
 * @Title 客户服务Client接口
 * @Description CustomerServiceClient
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@FeignClient(name = "client-service", path = "/v1/client")
public interface CustomerServiceClient {
	@PutMapping("/updateRiskInfo")
	public Result<?> saveRisk(@RequestBody CustomerRisk customerRisk);
}
