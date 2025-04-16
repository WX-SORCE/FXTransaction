package com.hsbc.pws.risk.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @Title 通用配置
 * @Description Config
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@Configuration
@RefreshScope
@Data
public class GlobalConfig {
	@Value("${rousing.page.size:10}")
	private int pageSize;

}
