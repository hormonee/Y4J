package com.y4j.final_project.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.y4j.final_project.admin.service.AdminService;
import com.y4j.final_project.authority.service.AuthorityService;
import com.y4j.final_project.command.AdminVO;
import com.y4j.final_project.command.AuthorityVO;
import com.y4j.final_project.command.MessageVO;
import com.y4j.final_project.command.UserVO;
import com.y4j.final_project.message.service.MessageService;
import com.y4j.final_project.user.service.UserService;
import com.y4j.final_project.util.Criteria;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MessageService messageService;
	
	
	@GetMapping("/admin_login")
	public String admin_login() {

		return "admin/admin_login";
	}
	
	@GetMapping("/admin_join")
	public String admin_join() {
		
		return "admin/admin_join";
	}
	
	@GetMapping("/admin_home")
	public String admin_home() {
		
		return "admin/admin_home";
	}
	
	@GetMapping("/admin_hold")
	public String admin_hold() {
		
		return "admin/admin_hold";
	}
	
	@PostMapping("/adminJoinForm")
	public String adminJoinForm(@Valid AdminVO vo1, Errors errors, Model model, Criteria cri,
								RedirectAttributes ra) {
		
		//유효성 검사
		if(errors.hasErrors()) { //에러가 있다면 true, 없다면 false
			List<FieldError> list = errors.getFieldErrors(); 
			for(FieldError err : list) {
				if(err.isBindingFailure()) { //유효성 검사의 실패가 아니라, 자바 내부의 에러라면 true 반환
					model.addAttribute("valid_" + err.getField(), "형식이 올바르지 않습니다");
				} else { //유효성 검사에 실패한 목록
					model.addAttribute("valid_" + err.getField(), err.getDefaultMessage());
				}
			}
			model.addAttribute("vo1", vo1);
			
			return "redirect:/admin/admin_join";
		}
		
		//비밀번호 암호화
		vo1.setAdmin_pw(passwordEncoder.encode(vo1.getAdmin_pw()));

		//관리자 회원가입
		String adminType = vo1.getAdmin_type();
		vo1.setAdmin_type("none");
		int result1 = adminService.registAdmin(vo1);
		
		//권한 신청 vo 빌드
		AuthorityVO vo2 = AuthorityVO.builder()
						  .authority_mng_no(authorityService.getAuthorityApplyTotal(cri) + 1)
						  .authority_mng_admin_no(adminService.getAdminInfo2(vo1.getAdmin_id()).getAdmin_no())
						  .authority_mng_admin_id(vo1.getAdmin_id())
						  .authority_mng_admin_name(vo1.getAdmin_name())
						  .authority_mng_admin_apply_type(adminType)
						  .ent_name(vo1.getEnt_name())
						  .build();
		//권한 신청
		int result2 = authorityService.applyAuthority(vo2);
		
		String msg = (result1 == 1 && result2 == 1) ? "정상적으로 회원가입 되었습니다.\n관리자 권한 승인을 기다려주세요." : "회원가입에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/admin/admin_login";
	}
	
	@PostMapping("/adminLoginForm")
	public String adminLoginForm(@RequestParam("admin_id") String admin_id, @RequestParam("admin_pw") String admin_pw, 
			HttpServletRequest request, RedirectAttributes ra) {

		int count = adminService.adminIdCheck(admin_id); //db에 저장되어있는 아이디
		String saved_pw = adminService.login(admin_id); //db에 저장되어있는 비밀번호

		//세션이 있으면 세션 반환, 없으면 새로 생성해서 반환
		//getSession()은 기본값이 true인데 false로 설정할 경우, 세션이 없을 때 새로 생성하지 않고 null 반환
		HttpSession session = request.getSession();
		
		if(count == 1 && passwordEncoder.matches(admin_pw, saved_pw)) {

			//세션에 회원정보 저장 - 아이디로 가져와서
			AdminVO adminVO = adminService.getAdminInfo2(admin_id);
			session.removeAttribute("user_id");
			session.setAttribute("admin_id", admin_id);
			session.setAttribute("admin_type", adminVO.getAdmin_type());
			session.setAttribute("admin_name", adminVO.getAdmin_name());
			
			return "admin/admin_home";
			
		} else {
			ra.addFlashAttribute("msg", "로그인에 실패했습니다.");
			
			return "redirect:/admin/admin_login";
		}
	}
	
	@GetMapping("/adminLogoutForm")
	public String adminLogoutForm(HttpSession session, RedirectAttributes ra) {
		
		session.invalidate();
		ra.addFlashAttribute("msg", "정상적으로 로그아웃 되었습니다.");
		
		return "redirect:/admin/admin_login";
	}
	
	// 아이디 찾기
	@GetMapping("/admin_find_id")
	public String admin_find_id() {

		return "admin/admin_find_id";
	}

	@PostMapping("/adminFindIdForm")
	public String adminFindIdForm(AdminVO vo, Model model) {
		AdminVO result = adminService.getAdminInfo2(vo.getAdmin_id()); // db에 담겨있는 vo값들
		// System.out.println(result);

		model.addAttribute("vo", result); // model에 담아서 화면에 뿌리기
		return "admin/admin_find_id_result"; // 결과페이지로
	}

	// 아이디찾기 결과
	@GetMapping("/user_find_id_result")
	public String user_find_id_result() {

		return "user/user_find_id_result";
	}

	// 비밀번호 찾기
	@GetMapping("/user_find_pw")
	public String user_find_pw() {

		return "user/user_find_pw";
	}
	
	@GetMapping("/admin_msg")
	public String admin_msg(HttpSession session, Model model) {

		if(session.getAttribute("user_id") != null) {
			model.addAttribute("uncheckedMsgNum", messageService.getUncheckedMsg(session.getAttribute("user_id")));
		
		} else if(session.getAttribute("admin_id") != null) {
			model.addAttribute("uncheckedMsgNum", messageService.getUncheckedMsg(session.getAttribute("admin_id")));
		}
			
		return "admin/admin_msg";
	}
	
	@PostMapping("/sendMsgForm")
	public String sendMsgForm(MessageVO vo, RedirectAttributes ra) {
		
		MessageVO msgVO = null;
		
		if(vo.getMsg_writer_type().equals("user") && vo.getMsg_receiver_type().equals("user")) {
			UserVO writerVO = userService.getUserInfo2(vo.getMsg_writer_id());
			UserVO receiverVO = userService.getUserInfo2(vo.getMsg_receiver_id());
			
			try {
				msgVO = MessageVO.builder()
						  .msg_writer_no(writerVO.getUser_no())
						  .msg_writer_id(writerVO.getUser_id())
						  .msg_writer_type(vo.getMsg_writer_type())
						  .msg_writer_name(writerVO.getUser_name())
						  .msg_receiver_no(receiverVO.getUser_no())
						  .msg_receiver_id(receiverVO.getUser_id())
						  .msg_receiver_type(vo.getMsg_receiver_type())
						  .msg_receiver_name(receiverVO.getUser_name())
						  .msg_title(vo.getMsg_title())
						  .msg_content(vo.getMsg_content())
						  .build();
				
			} catch (Exception e) {
				ra.addFlashAttribute("msg", "쪽지 발송에 실패했습니다.");
				return "redirect:/admin/admin_msg";
			}
			
		} else if(vo.getMsg_writer_type().equals("user") && vo.getMsg_receiver_type().equals("admin")) {
			UserVO writerVO = userService.getUserInfo2(vo.getMsg_writer_id());
			AdminVO receiverVO = adminService.getAdminInfo2(vo.getMsg_receiver_id());
			
			try {
				msgVO = MessageVO.builder()
						  .msg_writer_no(writerVO.getUser_no())
						  .msg_writer_id(writerVO.getUser_id())
						  .msg_writer_type(vo.getMsg_writer_type())
						  .msg_writer_name(writerVO.getUser_name())
						  .msg_receiver_no(receiverVO.getAdmin_no())
						  .msg_receiver_id(receiverVO.getAdmin_id())
						  .msg_receiver_type(vo.getMsg_receiver_type())
						  .msg_receiver_name(receiverVO.getAdmin_name())
						  .msg_title(vo.getMsg_title())
						  .msg_content(vo.getMsg_content())
						  .build();
				
			} catch (Exception e) {
				ra.addFlashAttribute("msg", "쪽지 발송에 실패했습니다.");
				return "redirect:/admin/admin_msg";
			}
			
		} else if(vo.getMsg_writer_type().equals("admin") && vo.getMsg_receiver_type().equals("user")) {
			AdminVO writerVO = adminService.getAdminInfo2(vo.getMsg_writer_id());
			UserVO receiverVO = userService.getUserInfo2(vo.getMsg_receiver_id());
			
			try {
				msgVO = MessageVO.builder()
						  .msg_writer_no(writerVO.getAdmin_no())
						  .msg_writer_id(writerVO.getAdmin_id())
						  .msg_writer_type(vo.getMsg_writer_type())
						  .msg_writer_name(writerVO.getAdmin_name())
						  .msg_receiver_no(receiverVO.getUser_no())
						  .msg_receiver_id(receiverVO.getUser_id())
						  .msg_receiver_type(vo.getMsg_receiver_type())
						  .msg_receiver_name(receiverVO.getUser_name())
						  .msg_title(vo.getMsg_title())
						  .msg_content(vo.getMsg_content())
						  .build();
				
			} catch (Exception e) {
				ra.addFlashAttribute("msg", "쪽지 발송에 실패했습니다.");
				return "redirect:/admin/admin_msg";
			}
			
		} else if(vo.getMsg_writer_type().equals("admin") && vo.getMsg_receiver_type().equals("admin")) {
			AdminVO writerVO = adminService.getAdminInfo2(vo.getMsg_writer_id());
			AdminVO receiverVO = adminService.getAdminInfo2(vo.getMsg_receiver_id());
			
			try {
				msgVO = MessageVO.builder()
						  .msg_writer_no(writerVO.getAdmin_no())
						  .msg_writer_id(writerVO.getAdmin_id())
						  .msg_writer_type(vo.getMsg_writer_type())
						  .msg_writer_name(writerVO.getAdmin_name())
						  .msg_receiver_no(receiverVO.getAdmin_no())
						  .msg_receiver_id(receiverVO.getAdmin_id())
						  .msg_receiver_type(vo.getMsg_receiver_type())
						  .msg_receiver_name(receiverVO.getAdmin_name())
						  .msg_title(vo.getMsg_title())
						  .msg_content(vo.getMsg_content())
						  .build();
				
			} catch (Exception e) {
				ra.addFlashAttribute("msg", "쪽지 발송에 실패했습니다.");
				return "redirect:/admin/admin_msg";
			}
		}
		
		int result = messageService.sendMsg(msgVO);
		String msg = (result == 1) ? "정상적으로 발송 처리되었습니다." : "쪽지 발송에 실패했습니다.";
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/admin/admin_msg";
	}
	
}
