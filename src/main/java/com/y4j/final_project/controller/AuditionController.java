package com.y4j.final_project.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.y4j.final_project.audition.service.AuditionService;
import com.y4j.final_project.command.AdminVO;
import com.y4j.final_project.command.AuditionVO;
import com.y4j.final_project.command.MessageVO;
import com.y4j.final_project.command.UserVO;
import com.y4j.final_project.message.service.MessageService;
import com.y4j.final_project.user.service.UserService;
import com.y4j.final_project.util.Criteria;
import com.y4j.final_project.util.PageVO;

@Controller
@RequestMapping("/audition")
public class AuditionController {

	@Autowired
	AuditionService auditionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MessageService messageService;
	
	
	//오디션 공고 페이지
	@GetMapping("/audition_notice")
	public String audition_notice() {
	
		return "audition/audition_notice";
	}
	
	//오디션 지원자 목록
	@GetMapping("/admin_audition_list")
	public String admin_audition_list(Model model, Criteria cri) {
		
		ArrayList<AuditionVO> list = auditionService.getAudList(cri);
		model.addAttribute("list", list);
		
		int total = auditionService.getAudTotal(cri);
		PageVO pageVO = new PageVO(cri, total);
		model.addAttribute("pageVO", pageVO);
		
		return "audition/admin_audition_list";
	}
	
	@GetMapping("/admin_audition_register")
	public String admin_audition_register() {
		
		return "audition/admin_audition_register";
	}

	//오디션 접수 처리
	@PostMapping("/audApplyForm")
	public String audApplyForm(AuditionVO vo, RedirectAttributes ra,
			@RequestParam("file") List<MultipartFile> list ) {
		
		//선택되지 않은 파일 태그는 제거
		list = list.stream()
				   .filter((v) -> v.isEmpty() == false)
				   .collect(Collectors.toList());
				
		for(MultipartFile multipartFile : list) {
			if(multipartFile.getContentType().contains("jpeg") == false
				&& multipartFile.getContentType().contains("jpg") == false
				&& multipartFile.getContentType().contains("mp4") == false) {
				
				ra.addFlashAttribute("msg", "jpg, jpeg, mp4 파일만 등록 가능합니다.");
				
				return "redirect:/audition/audition_notice";
			}
		}
		
		//오디션 접수(ServiceImpl)
		int result = auditionService.registAud(vo, list);
		
		String msg = (result == 1) ? "정상적으로 접수 처리되었습니다." : "오디션 접수에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/audition/audition_notice";
	}	
	
	//오디션 1차 합격 처리
	@PostMapping("/passFirstStageForm")
	public String passFirstStageForm(AuditionVO vo2,
							  RedirectAttributes ra) {

		AuditionVO vo = auditionService.getAudCv(vo2.getAudition_cv_no());
		UserVO userVO = userService.getUserInfo2(vo.getAudition_cv_user_id());
		
		int result = auditionService.passFirstStage(vo.getAudition_cv_no());
		
		String msg = (result == 1) ? "정상적으로 " + vo.getAudition_cv_name()
			+ "님이 1차 합격 처리되었습니다.\n2차 오디션 정보가 쪽지로 발송되었습니다." : "합격 처리에 실패했습니다.";
		
		ra.addFlashAttribute("msg", msg);
		
		//권한 신청 승인 시, 권한 변경 내역 쪽지 발송
		MessageVO msgVO = MessageVO.builder()
						  .msg_writer_no(6)
						  .msg_writer_id("Administrator")
						  .msg_writer_type("admin")
						  .msg_writer_name("Administrator")
						  .msg_receiver_no(userVO.getUser_no())
						  .msg_receiver_id(userVO.getUser_id())
						  .msg_receiver_type("user")
						  .msg_receiver_name(userVO.getUser_name())
						  .msg_title("축하드립니다! 1차 오디션에 합격하셨습니다.")
						  .msg_content(userVO.getUser_name() + "(" + userVO.getUser_id()
						  	+ ")님은 2차 오디션 대상자 입니다.\n" +  "2차 오디션 일자 : 2023년 3월 31일\n"
							+ "2차 오디션 장소 : 서울시 강남구 테헤란로 7길 7, 7층\n"
						  	+ "2차 오디션 평가 사항 : 면접 및 자유 주제 연기 또는 춤, 노래") 
						  .build();
		messageService.sendMsg(msgVO);

		return "redirect:/audition/admin_audition_list";
	}
	
	//오디션 1차 불합격 처리
	@PostMapping("/failFirstStageForm")
	public String failFirstStageForm(AuditionVO vo2,
			  				 RedirectAttributes ra) {
		
		AuditionVO vo = auditionService.getAudCv(vo2.getAudition_cv_no());
		UserVO userVO = userService.getUserInfo2(vo.getAudition_cv_user_id());
		
		int result = auditionService.failFirstStage(vo.getAudition_cv_no());
		
		String msg = (result == 1) ? "정상적으로 " + vo.getAudition_cv_name()
			+ "님이 1차 불합격 처리되었습니다." : "불합격 처리에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);
		
		//권한 신청 승인 시, 권한 변경 내역 쪽지 발송
		MessageVO msgVO = MessageVO.builder()
						  .msg_writer_no(6)
						  .msg_writer_id("Administrator")
						  .msg_writer_type("admin")
						  .msg_writer_name("Administrator")
						  .msg_receiver_no(userVO.getUser_no())
						  .msg_receiver_id(userVO.getUser_id())
						  .msg_receiver_type("user")
						  .msg_receiver_name(userVO.getUser_name())
						  .msg_title("1차 오디션에 불합격하셨습니다.")
						  .msg_content(userVO.getUser_name() + "(" + userVO.getUser_id()
						  	+ ")님은 1차 오디션에 불합격 하셨습니다.\n" +  "지원해주셔서 감사합니다.") 
						  .build();
		messageService.sendMsg(msgVO);
		
		return "redirect:/audition/admin_audition_list";
	}

}
