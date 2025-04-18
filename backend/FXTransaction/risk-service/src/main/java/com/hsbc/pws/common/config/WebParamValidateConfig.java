package com.hsbc.pws.common.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 * @Title 通用配置
 * @Description WebParamValidateConfig
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@Configuration
public class WebParamValidateConfig {
	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
				.configure()
				// failFast的意思只要出现校验失败的情况，就立即结束校验，不再进行后续的校验�?
				.failFast(false)
				//.failFast(true)
				.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
		methodValidationPostProcessor.setValidator(validator());
		return methodValidationPostProcessor;
	}
}