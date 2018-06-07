package com.groupware.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.groupware.service.AprovService;
import com.groupware.vo.ApprovalVo;

@Controller
public class AprovController {
	@Autowired AprovService aprovservice;
	
	@RequestMapping("/makeapproval")
	public ModelAndView showmakeapproval(HttpServletRequest req){
		return aprovservice.showmakeapproval(req);
	}
	
	@RequestMapping("/sendaprov")
	public String sendaprov(ApprovalVo aprovvo,String view){
		return aprovservice.insertaprov(aprovvo,"/index");
	}
	
	@RequestMapping("/getaprovlist.ajax")
	@ResponseBody
	public List<ApprovalVo> getaprovlist(HttpServletRequest req) {
		return aprovservice.getaprovlist(req);
	}
	
	@RequestMapping("/completeaprov.ajax")
	@ResponseBody
	public ModelAndView completeaprov(HttpServletRequest req,String viewName) {
		return aprovservice.completeaprov(req,"/index");
	}
	
	@RequestMapping(value="/aprovsearch.ajax")
	@ResponseBody
	public List<ApprovalVo> aprovsearch(HttpServletRequest req) {
		return aprovservice.aprovsearch(req);
	}

}
