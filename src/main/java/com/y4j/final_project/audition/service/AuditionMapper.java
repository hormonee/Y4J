package com.y4j.final_project.audition.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.y4j.final_project.command.AdminVO;
import com.y4j.final_project.command.AuditionFileVO;
import com.y4j.final_project.command.AuditionVO;
import com.y4j.final_project.util.Criteria;

@Mapper
public interface AuditionMapper {

	//오디션 지원서 저장 메서드
	public int registAud(AuditionVO vo);

	//오디션 지원서 내 파일 정보 저장	
	public int registAudFile(AuditionFileVO vo);
	
	//오디션 지원서 전체 조회 메서드
	public ArrayList<AuditionVO> getAudList(Criteria cri);
	
	//전체 오디션 지원서 수 반환 메서드
	public int getAudTotal(Criteria cri);
	
	//오디션 지원서 특정 1개 데이터 반환 메서드
	public AuditionVO getAudCv(int audition_cv_no);
	
	//이미지 데이터 조회
	public List<AuditionFileVO> getAudFile(AuditionVO vo);
	
	//오디션 1차 합격 처리
	public int passFirstStage(int audition_cv_no);

	//오디션 1차 불합격 처리
	public int failFirstStage(int audition_cv_no);
	
}
