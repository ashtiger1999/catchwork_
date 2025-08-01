package com.example.demo.admin.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.admin.model.dto.Admin;
import com.example.demo.admin.model.dto.AdminReport;
import com.example.demo.support.model.dto.Support;
import com.example.demo.admin.model.dto.ReportSummary;
import com.example.demo.report.model.dto.Report;
import com.example.demo.admin.model.dto.ReportSearchCriteria;
import com.example.demo.admin.model.dto.ReportList;
import com.example.demo.admin.model.dto.SupportList;
import com.example.demo.admin.model.entity.AdminEntity;

@Mapper
public interface AdminMapper {
	Admin findByAdminId(String adminId);


	Admin adminInfo(String adminId);
	
	/**
	 * 전체 문의 목록 조회 (관리자용)
	 * 
	 * @author BAEBAE
	 * @param params
	 * @return
	 */
	List<Support> getAllSupportList(Map<String, Object> params);

	/**
	 * 특정 문의 상세 조회 (관리자용)
	 * 
	 * @author BAEBAE
	 * @param supportNo
	 * @return
	 */
	Support getSupportDetail(int supportNo);

	/**
	 * 문의 답변 등록 (관리자용)
	 * 
	 * @author BAEBAE
	 * @param support
	 * @return
	 */
	int submitSupportAnswer(Support support);

	/**
	 * 최근 미처리 신고 목록 조회
	 * 
	 * @return
	 * @author 민장
	 */
	List<ReportList> selectRecentReportList(Map<String, Object> param);

	/**
	 * 최근 미처리 문의 목록 조회
	 * 
	 * @param param
	 * @return
	 * @author 민장
	 */
	List<SupportList> selectRecentSupportList(Map<String, Object> param);

	/**
	 * 최근 미처리 신고 개수 조회
	 * 
	 * @return
	 * @author 민장
	 */
	Map<String, Object> selectRecentReportCount();

	/**
	 * 최근 미처리 문의 개수 조회
	 * 
	 * @return
	 * @author 민장
	 */
	Map<String, Object> selectRecentSupportCount();

	/**
	 * 최근 7일 문의수 통계
	 * 
	 * @return
	 * @author 민장
	 */
	List<Map<String, Object>> selectRecentReportChart();

	/**
	 * 최근 7일 신고수 통계
	 * 
	 * @return
	 * @author 민장
	 */
	List<Map<String, Object>> selectRecentSupportChart();


}
