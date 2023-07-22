package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.models.Users;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.demo.service.UserService;



@Controller
public class MainController {

	@Autowired
	BCryptPasswordEncoder passcode;
	
	@Autowired
	Environment env;
	
	@Autowired
	OtpService otpserv;
	
	@Autowired
	EmailService emailserv;
	
	@Autowired
	UserService userserv;
	
	@GetMapping("/")
	public String home(HttpSession sess,HttpServletRequest request)
	{
		String base_url =	ServletUriComponentsBuilder
				.fromRequestUri(request)
				.replacePath(null)
				.build()
				.toUriString();
		
		sess.setAttribute("base_url", base_url);
		sess.setAttribute("appname", env.getProperty("spring.application.name"));
		return "Home";
	}
	
	@GetMapping("login")
	public String  loginPage()
	{
		return "Login";
	}
	
	@GetMapping("/forgotpass")
	public String forgotPassword(){
		return "ForgotPassword";
	}
	
	@RequestMapping("/forgotpassword")
	public String forGotPassword(@ModelAttribute("Users") Users users,HttpSession sess ,RedirectAttributes attr)
	{
		Users user = userserv.getUserByEmailId(users.getEmail());
		if(user!=null)
		{
			otpserv.generateotp(users.getEmail());
			int otp = otpserv.getOtp(users.getEmail());
			
			sess.setAttribute("vemail", users.getEmail());
			sess.setAttribute("otp", otp);
			sess.setAttribute("userid", user.getUser_id());
			emailserv.sendSimpleEmail(users.getEmail(), "Respected Sir/Ma'am, \n\t Your OTP for change password is "+otp, "OTP for confirmation");
			attr.addFlashAttribute("response","OTP sent to your email "+users.getEmail());
			return "redirect:/confotppass";
		}
		else{
			attr.addFlashAttribute("reserr", "User Not found for Given Email");
			return "redirect:/forgotpass";
		}
	}
	
	@GetMapping("/confotppass")
	public String confOTPForgotPassword(@ModelAttribute("Users") Users users,Model model,HttpSession sess)
	{
		model.addAttribute("vemail", sess.getAttribute("vemail"));
		return "ConfirmOtpForgotPass";
	}
	
	@PostMapping("/confotppassword")
	public String confirmOtpPassword(@ModelAttribute("Users") Users users, HttpSession sess,RedirectAttributes attr)
	{
		//Users user = userserv.getUserByEmailId((String)sess.getAttribute("vemail"));
		Integer n_otp = Integer.parseInt(users.getCnf_otp());
		int  new_otp = n_otp;
		Integer o_otp = (Integer) sess.getAttribute("otp");;
		int  old_otp = o_otp;
		
		if(new_otp==old_otp){
			Users user = userserv.getUserByEmailId((String)sess.getAttribute("vemail"));
			otpserv.clearOtp((String)sess.getAttribute("vemail"));
			sess.setAttribute("userid", user.getUser_id());
			return "redirect:/changepass";
		}
		else{
			attr.addFlashAttribute("reserr", "OTP did not matched");
			return "redirect:/confotppass";
		}
	}
	
	@GetMapping("changepass")
	public String changePassword(){
		return "ChangePassword";
	}
	
	@PostMapping("changepassword")
	public String updatePassword(@ModelAttribute("Users")Users users,HttpSession sess,RedirectAttributes attr){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String enpass = passcode.encode(users.getCnf_pass());
		Long uid = (Long) sess.getAttribute("userid");
		
		int res = userserv.updateUsersPassword(enpass, uid);
		
		if(res>0)
		{
			if(auth.getName().equals("admin")) {
				attr.addFlashAttribute("response", "Password updated successfully");
				return "redirect:/";
			}
			else {
				attr.addFlashAttribute("response", "Password updated successfully");
				return "redirect:/login";
			}
		}
		else{
			if(auth.getName().equals("admin")) {
				attr.addFlashAttribute("reserr", "Password is not updated ");
				return "redirect:/changepass";
			}
			else {
				attr.addFlashAttribute("reserr", "Password is not updated ");
				return "redirect:/login";
			}
		}
	}
}
