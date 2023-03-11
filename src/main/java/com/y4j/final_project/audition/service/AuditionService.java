package com.y4j.final_project.audition.service;

import java.util.ArrayList;

import com.y4j.final_project.command.AuditionFileVO;
import com.y4j.final_project.command.AuditionVO;
import com.y4j.final_project.util.Criteria;

public interface AuditionService {

	//오디션 지원폼 등록 메서드
	public int registAud(AuditionVO vo);

	//오디션 지원폼 전체 조회 메서드	
	public ArrayList<AuditionVO> getAudList(Criteria cri);
	
	//전체 오디션 지원 수 반환 메서드
	public int getAudTotal(Criteria cri);
	
}
