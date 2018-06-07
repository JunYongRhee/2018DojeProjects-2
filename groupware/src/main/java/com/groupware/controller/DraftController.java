package com.groupware.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.groupware.service.CommonService;
import com.groupware.service.DraftService;
import com.groupware.vo.DraftVo;

@Controller
public class DraftController {
	@Autowired DraftService draftservice;
	@Autowired CommonService commonservice;
	
	@RequestMapping("/draftdoc")
	public ModelAndView showdraftdoc(HttpServletRequest req) {
		return commonservice.selectdoc(req);
	}
	
	@RequestMapping("/draftmanager")
	public String showdraftmanager(HttpServletRequest req) {
		return "/draftmanager";
	}
	
	@RequestMapping(value="/getdraft.ajax")
	@ResponseBody
	public List<DraftVo> getDraft(HttpServletRequest req) {
		return draftservice.getDraft();
	}
	
	@RequestMapping(value="/loadpreview.ajax",produces = "application/text; charset=utf8")
	@ResponseBody
	public String loadpreview(HttpServletRequest req) {
		return draftservice.loadpreview(req);
	}

	@RequestMapping("gochangedoc")
	public ModelAndView linkchangedoc(HttpServletRequest req,DraftVo draftvo) {
		return draftservice.linkchangedoc(req,draftvo,"/changedoc");
	}

	@RequestMapping("/writedoc")
	public ModelAndView linkwritedoc(String viewName) {
		return draftservice.linkwritedoc("/writedoc");
	}
	
	@RequestMapping("/insertdoc")
	public ModelAndView insertdoc(DraftVo draftvo) {
		return draftservice.insertdoc(draftvo,"/index");
	}
	
	@RequestMapping("/updatedoc")
	public String updatedoc(DraftVo draftvo) {
		return draftservice.updatedoc(draftvo,"/index");
	}
	
	@RequestMapping(value="/godeldoc.ajax",produces = "application/text; charset=utf8")
	@ResponseBody
	public String deleteDoc(HttpServletRequest req) {
		return draftservice.deleteDoc(req);
	}
	
}
