package com.hsbc.pws.risk.service;

import java.io.IOException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title 通用配置
 * @Description DroolsConfig
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@Configuration
@Slf4j
public class DroolsConfig {
	private static final String RULES_PATH = "rules/";

	private final KieServices kieServices = KieServices.Factory.get();

	@Bean
	public KieContainer kieContainer() throws IOException {
		KieFileSystem kieFileSystem = this.kieServices.newKieFileSystem();

		// 加载rules目录下的所有规则文件
		for (Resource file : getRuleFiles()) {
			kieFileSystem.write(ResourceFactory.newClassPathResource(this.RULES_PATH + file.getFilename(), "UTF-8"));
		}

		KieBuilder kieBuilder = this.kieServices.newKieBuilder(kieFileSystem);
		kieBuilder.buildAll();

		Results results = kieBuilder.getResults();
		if (results.hasMessages(Message.Level.ERROR)) {
			throw new IllegalStateException("规则文件编译错误:" + results.getMessages());
		}

		return this.kieServices.newKieContainer(this.kieServices.getRepository().getDefaultReleaseId());
	}

	@Bean
	public KieSession kieSession() throws IOException {
		return kieContainer().newKieSession();
	}

	private Resource[] getRuleFiles() throws IOException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		//return resourcePatternResolver.getResources("classpath*:" + this.RULES_PATH + "**/*.drl");
		return resourcePatternResolver.getResources("classpath*:" + this.RULES_PATH + "**/*.*");
	}
	
	/**
	 * private static final KieServices kieServices = KieServices.Factory.get(); //
	 * 制定规则文件的路径 private static final String RULES_CUSTOMER_RULES_DRL =
	 * "rules/ForeignExchange.drl";
	 * 
	 * @Bean public KieContainer kieContainer() { // 获得Kie容器对象 KieFileSystem
	 *       kieFileSystem = kieServices.newKieFileSystem();
	 *       kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_CUSTOMER_RULES_DRL));
	 * 
	 *       KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
	 *       kieBuilder.buildAll();
	 * 
	 *       KieModule kieModule = kieBuilder.getKieModule(); KieContainer
	 *       kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
	 * 
	 *       return kieContainer; }
	 * 
	 * @Bean public KieSession kieSession() { return kieContainer().newKieSession();
	 *       }
	 **/
}
