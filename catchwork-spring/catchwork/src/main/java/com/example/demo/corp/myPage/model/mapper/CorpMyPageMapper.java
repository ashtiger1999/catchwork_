package com.example.demo.corp.myPage.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // @Param 어노테이션을 위해 추가

import com.example.demo.corp.myPage.model.dto.CorpMyPage;

@Mapper
public interface CorpMyPageMapper {

    // 기업 회원 정보 조회
    CorpMyPage getCorpMyPage(String memNo);

    // 회원(MEMBER) 테이블의 기본 정보를 수정
    void updateMemberCoreInfo(CorpMyPage corpMyPage);

    // 기업 회원(CORPORATE_MEMBER) 테이블의 부서명을 수정
    void updateCorporateMemberDepartment(CorpMyPage corpMyPage);

    // 멤버 비밀번호 조회	
    String selectMemberPassword(String memNo);

    // 회원 탈퇴 처리: 단일 회원 (MEM_STATUS를 1로 변경)
    void withdraw(String memNo);

    // memNo로 CORP_NO 조회
    String getCorpNoByMemNo(String memNo);

    /*
    // corpNo로 같은 기업 회원 memNo 리스트 조회
    List<String> getMemNosByCorpNo(String corpNo);
    */

    // 기업 상태 업데이트 (CORP_STATUS를 1로 변경)
    void updateCorpInfoStatus(String corpNo);

    // 대표 여부 확인 (CORP_MEM_ROLE_CHECK 조회)
    String getRoleCheckByMemNo(String memNo);

    // 비밀번호 변경 (추가된 메서드, 필요 시 사용)
    void changePw(@Param("memNo") String memNo, @Param("memPw") String memPw);
}
