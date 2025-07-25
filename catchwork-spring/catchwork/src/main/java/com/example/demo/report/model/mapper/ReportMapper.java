package com.example.demo.report.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.report.model.dto.Report;

@Mapper
public interface ReportMapper {
	
	// 신고
    int insertReport(Report report);
    
    // 신고 중복
    int checkDuplicateReport(String reportTargetNo, String reportTargetType, String memNo);
}
