package com.hsbc.pws.risk.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hsbc.pws.common.response.Result;

/**
 * @Title 权限服务auth接口
 * @Description AuthServiceClient
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@FeignClient(name = "auth-service", path = "/v1/auth")
public interface AuthServiceClient {
	@PutMapping("/updateLevel")
	public Result<?> updateLevel(@RequestParam String clientId, @RequestParam Integer identityLevel , @RequestParam String Status);
}
