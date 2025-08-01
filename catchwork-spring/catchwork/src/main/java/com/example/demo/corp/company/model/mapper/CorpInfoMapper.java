package com.example.demo.corp.company.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.corp.company.model.dto.CorpInfo;

@Mapper
public interface CorpInfoMapper {

	/**
	 * memNo 기반 기업회원이 본인의 기업 상세를 보는
	 * @author JIN
	 * @param memNo
	 * @return
	 */
	CorpInfo selectCorpByMemNo(@Param("memNo") String memNo);

	/**
	 * 기업 정보 수정
	 * @author JIN
	 * @param corpInfo
	 * @return
	 */
	int updateCorpInfo(CorpInfo corpInfo);

	/**
	 * 공고 작성페이지에서 기업정보 가져오기
	 * @author BAEBAE
	 * @param memNo
	 * @return
	 */
	CorpInfo selectCorpInfoByMember(String memNo);
	
	/**
	 * 사용중인 이미지 조회(스케줄러)
	 * 
	 * @author JAEHO
	 * @return
	 */
	List<String> selectUsedImage();

	/**
	 * 탈퇴 기업 처리(스케줄러)
	 * 탈퇴 기업 대상 조회
	 * 
	 * @author JAEHO
	 * @param deleteTargetPeriod
	 * @return
	 */
	List<Integer> selectTargetCorp(int deleteTargetPeriod);

	/**
	 * 탈퇴 기업 처리(스케줄러)
	 * 기업 인사 탈퇴
	 * 
	 * @author JAEHO
	 * @param corpNo
	 */
	void withdrawCorpMember(int corpNo);

	/**
	 * 탈퇴 기업 처리(스케줄러)
	 * 
	 * @author JAEHO
	 * @param deleteTargetPeriod
	 * @return
	 */
	int removeTargetCorp(int deleteTargetPeriod);
}
