package com.hsbc.pws.risk.service;

import java.time.Duration;
import java.util.List;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hsbc.pws.common.utils.LocalCacheUtils;
import com.hsbc.pws.risk.constant.GlobalConstant;
import com.hsbc.pws.risk.entity.RiskAssessment;
import com.hsbc.pws.risk.pojo.AnnualCumulativeTotal;
import com.hsbc.pws.risk.pojo.ForeignExchange;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title 外汇交易监控
 * @Description FxMonitoringService
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@Service
@Slf4j
public class FxMonitoringService {
	@Resource
	private KieSession kieSession;

	@Resource
	private RedisTemplate<String, String> redisTemplate;

	@Value("${fx.local.cache.expire:5}")
	private int cacheExpireTime;
	
	protected final Cache<String, List<RiskAssessment>> cache = Caffeine.newBuilder()
			.expireAfterWrite(Duration.ofMinutes(this.cacheExpireTime))
			.maximumSize(20000)
			.build();

	public int fireRules(ForeignExchange foreignExchange) throws Exception {
		//AnnualCumulativeTotal annualCumulativeTotal = (AnnualCumulativeTotal) LocalCacheUtils.get(GlobalConstant.REDIS_FX_PREFIX + foreignExchange.getUserId());
		AnnualCumulativeTotal annualCumulativeTotal = JSON.parseObject((String) LocalCacheUtils.get(GlobalConstant.REDIS_FX_PREFIX + foreignExchange.getUserId()),
				AnnualCumulativeTotal.class, JSONReader.Feature.UseBigDecimalForFloats);
		if (annualCumulativeTotal == null) {
			annualCumulativeTotal = new AnnualCumulativeTotal();
		}
		
		annualCumulativeTotal.setAmount(annualCumulativeTotal.getAmount().add(foreignExchange.getAmount()));
		annualCumulativeTotal.setTimes(annualCumulativeTotal.getTimes() + 1);
		
		this.kieSession.insert(annualCumulativeTotal);
		this.kieSession.fireAllRules();
		
		Integer state = annualCumulativeTotal.getState();
		if (state.intValue() == 0) {
			this.redisTemplate.opsForValue().set(GlobalConstant.REDIS_FX_PREFIX + foreignExchange.getUserId(), JSON.toJSONString(annualCumulativeTotal));
		}

		return state;
	}

	@Scheduled(cron = "${risk.scheduled.cron.fx.monitoring:0 0 0 1 1 *}")
	public void changeVoiceHistory() {
		// 每年1月1日0点0分0秒
		// 清空所有外汇交易监控累计金额和累计次数
		this.redisTemplate.delete(this.redisTemplate.keys(GlobalConstant.REDIS_FX_PREFIX + "*"));
	}
}
