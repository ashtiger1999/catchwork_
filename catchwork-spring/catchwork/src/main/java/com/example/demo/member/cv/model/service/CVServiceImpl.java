package com.example.demo.member.cv.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.corp.recruit.model.dto.RecruitCV;
import com.example.demo.member.cv.model.dto.CV;
import com.example.demo.member.cv.model.dto.CVAward;
import com.example.demo.member.cv.model.dto.CVEducation;
import com.example.demo.member.cv.model.dto.CVExperience;
import com.example.demo.member.cv.model.dto.CVLanguage;
import com.example.demo.member.cv.model.dto.CVMilitary;
import com.example.demo.member.cv.model.dto.CVOuter;
import com.example.demo.member.cv.model.dto.CVPortfolio;
import com.example.demo.member.cv.model.dto.CVQualify;
import com.example.demo.member.cv.model.dto.CVTraining;
import com.example.demo.member.cv.model.mapper.CVMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(value = "myBatisTransactionManager", rollbackFor = Exception.class)
public class CVServiceImpl implements CVService {

	@Autowired
	private CVMapper mapper;

	// pdf 저장경로 생성
	@Value("${file.upload.cv-pdf-path}")
	private String uploadDir;

	@Value("${file.upload.cv-img-path}")
	private String uploadPath;

	// 이력서 pdf 업로드
	@Override
	public RecruitCV uploadCVPdf(MultipartFile file, int recruitCVEdu, int recruitCVCareer, String recruitCVPdfTitle,
			String memNo, int recruitNo) throws Exception {

		// 1. 저장경로 설정
		File dir = new File(uploadDir);
		if (!dir.exists())
			dir.mkdirs();

		// 2. 실제 저장 파일 경로
		String filePath = uploadDir + File.separator + recruitCVPdfTitle;

		// 3. 업로드된 파일 → 서버에 저장
		file.transferTo(new File(filePath));

		// 4. DTO 생성
		RecruitCV recruitCV = RecruitCV.builder()
				.recruitCVEdu(recruitCVEdu)
				.recruitCVCareer(recruitCVCareer)
				.recruitCVPdfTitle(recruitCVPdfTitle)
				.recruitCVPdfPath(filePath)
				.memNo(String.valueOf(memNo))
				.recruitNo(recruitNo)
				.build();

		// 5. Mapper 호출 → DB INSERT
		mapper.uploadCVPdf(recruitCV);

		return recruitCV;
	}
	
	// 이력서 주인 확인
	@Override
	public boolean isOwner(int cvNo, String memNo) {
		return mapper.checkCVOwner(cvNo, memNo) > 0;
	}

	// 이력서 리스트 조회
	@Override
	public List<CV> selectCVList(String memNo) {
		return mapper.selectCVList(memNo);
	}

	// 이력서 조회
	@Override
	public CV selectCV(int cvNo) {

		CV cv = mapper.selectCV(cvNo);

		if (cv != null) {
			cv.setMilitary(mapper.selectMilitary(cvNo));
			cv.setEducation(mapper.selectEducation(cvNo));
			cv.setExperience(mapper.selectExperience(cvNo));
			cv.setAward(mapper.selectAward(cvNo));
			cv.setQualify(mapper.selectQualify(cvNo));
			cv.setLanguage(mapper.selectLanguage(cvNo));
			cv.setOuter(mapper.selectOuter(cvNo));
			cv.setTraining(mapper.selectTraining(cvNo));
			cv.setPortfolio(mapper.selectPortfolio(cvNo));
		}

		return cv;
	}

	// 이력서 추가
	@Override
	@Transactional
	public void addCV(CV cv) throws Exception {

		// 1. CV add
		mapper.addCV(cv);
		int cvNo = cv.getCvNo();

		// 2. Military
		CVMilitary military = cv.getMilitary();
		if (military != null) {
			military.setCvNo(cvNo);
			mapper.addMilitary(military);
		}

		// 3. Education
		CVEducation edu = cv.getEducation();
		if (edu != null) {
			edu.setCvNo(cvNo);
			mapper.addEducation(edu);
		}

		// 4. Experience
		if (cv.getExperience() != null) {
			for (CVExperience exp : cv.getExperience()) {
				exp.setCvNo(cvNo);
				mapper.addExperience(exp);
			}
		}

		// 5. Award
		if (cv.getAward() != null) {
			for (CVAward award : cv.getAward()) {
				award.setCvNo(cvNo);
				mapper.addAward(award);
			}
		}

		// 6. Qualify
		if (cv.getQualify() != null) {
			for (CVQualify qualify : cv.getQualify()) {
				qualify.setCvNo(cvNo);
				mapper.addQualify(qualify);
			}
		}

		// 7. Language
		if (cv.getLanguage() != null) {
			for (CVLanguage lang : cv.getLanguage()) {
				lang.setCvNo(cvNo);
				mapper.addLanguage(lang);
			}
		}

		// 8. Outer
		if (cv.getOuter() != null) {
			for (CVOuter outer : cv.getOuter()) {
				outer.setCvNo(cvNo);
				mapper.addOuter(outer);
			}
		}

		// 9. Training
		if (cv.getTraining() != null) {
			for (CVTraining train : cv.getTraining()) {
				train.setCvNo(cvNo);
				mapper.addTraining(train);
			}
		}

		// 10. Portfolio
		if (cv.getPortfolio() != null) {
			for (CVPortfolio port : cv.getPortfolio()) {
				port.setCvNo(cvNo);
				mapper.addPortfolio(port);
			}
		}
	}

	// 이력서 수정
	@Override
	@Transactional
	public void updateCV(CV cv) throws Exception {
	    mapper.updateCV(cv);
	    int cvNo = cv.getCvNo();

	    // 단일 항목
	    if (cv.getMilitary() != null) {
	        cv.getMilitary().setCvNo(cvNo);
	        mapper.updateMilitary(cv.getMilitary());
	    }
	    if (cv.getEducation() != null) {
	        cv.getEducation().setCvNo(cvNo);
	        mapper.updateEducation(cv.getEducation());
	    }

	    // 다건 항목 전부 delete 후 insert
	    mapper.deleteExperience(cvNo);
	    mapper.deleteAward(cvNo);
	    mapper.deleteQualify(cvNo);
	    mapper.deleteLanguage(cvNo);
	    mapper.deleteOuter(cvNo);
	    mapper.deleteTraining(cvNo);
	    mapper.deletePortfolio(cvNo);

	    if (cv.getExperience() != null) {
	        for (CVExperience exp : cv.getExperience()) {
	            exp.setCvNo(cvNo);
	            mapper.addExperience(exp);
	        }
	    }
	    if (cv.getAward() != null) {
	        for (CVAward award : cv.getAward()) {
	            award.setCvNo(cvNo);
	            mapper.addAward(award);
	        }
	    }
	    if (cv.getQualify() != null) {
	        for (CVQualify qualify : cv.getQualify()) {
	            qualify.setCvNo(cvNo);
	            mapper.addQualify(qualify);
	        }
	    }
	    if (cv.getLanguage() != null) {
	        for (CVLanguage lang : cv.getLanguage()) {
	            lang.setCvNo(cvNo);
	            mapper.addLanguage(lang);
	        }
	    }
	    if (cv.getOuter() != null) {
	        for (CVOuter outer : cv.getOuter()) {
	            outer.setCvNo(cvNo);
	            mapper.addOuter(outer);
	        }
	    }
	    if (cv.getTraining() != null) {
	        for (CVTraining train : cv.getTraining()) {
	            train.setCvNo(cvNo);
	            mapper.addTraining(train);
	        }
	    }
	    if (cv.getPortfolio() != null) {
	        for (CVPortfolio port : cv.getPortfolio()) {
	            port.setCvNo(cvNo);
	            mapper.addPortfolio(port);
	        }
	    }
	}

	// 이력서 삭제
	@Override
	@Transactional
	public void deleteCV(int cvNo) throws Exception {
		// 자식 테이블 삭제 (외래키 제약 있으니까 순서 중요)
		mapper.deleteExperience(cvNo);
		mapper.deleteAward(cvNo);
		mapper.deleteQualify(cvNo);
		mapper.deleteLanguage(cvNo);
		mapper.deleteOuter(cvNo);
		mapper.deleteTraining(cvNo);
		mapper.deletePortfolio(cvNo);
		mapper.deleteMilitary(cvNo);
		mapper.deleteEducation(cvNo);

		// 부모 테이블 CV 삭제
		mapper.deleteCV(cvNo);
	}


	// 이미지 처리(스케줄러)
	@Override
	public int deleteUnusedImage() {
						// 파일시스템의 이미지 목록 조회
						File dir = new File(uploadPath);
						File[] files = dir.listFiles((d, name) -> name.endsWith(".jpg") || name.endsWith(".png"));
		
						if (files == null) return 0;
		
						List<String> fileSystemImageList = Arrays.stream(files)
								.map(File::getName)
								.collect(Collectors.toList());
		
		
						// DB에서 사용 중인 이미지 목록 조회
						List<String> usedImageList = mapper.selectUsedImage();
		
						// 비교하여 사용되지 않는 이미지 식별
						List<String> unusedImageList = new ArrayList<>();
						for (String image : fileSystemImageList) {
								if (!usedImageList.contains(image)) {
										unusedImageList.add(image);
								}
						}
		
						// 파일 시스템에서 해당 이미지 삭제
						int deleteCount = 0;
						for (String image : unusedImageList) {
								File file = new File(uploadPath, image);
								if (file.exists()) {
										file.delete();
										deleteCount++;
								}
						}
		
						return deleteCount;
	}
}
