package com.hsbc.pws.risk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hsbc.pws.risk.entity.RiskAssessment;

/**
 * @Title 风险评估管理
 * @Description RiskAssessmentDao
 * @Author 张馨心
 * @Version 1.0
 * @Copyright Copyright (c) 2025
 * @Company www.hsbc.com
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public interface RiskAssessmentDao extends JpaRepository<RiskAssessment, String>, JpaSpecificationExecutor<RiskAssessment> {
	public List<RiskAssessment> findByClientId(String clientId);

	@Query(value = "SELECT * FROM risk_assessment WHERE created_at >= CURDATE() - INTERVAL 6 MONTH", nativeQuery = true)
	public List<RiskAssessment> findAllByHalfYear();
}