package com.groupware.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.groupware.dao.DraftDao;
import com.groupware.dao.LineDao;
import com.groupware.dao.UserDao;
import com.groupware.vo.LineVo;
import com.groupware.vo.UserVo;

@Service
@SuppressWarnings("rawtypes")
public class LineService {
	@Autowired LineDao linedao;
	@Autowired DraftDao draftdao;
	@Autowired UserDao userdao;
	@Autowired CommonService commonservice;
	
	public ModelAndView goapprovalline(HttpServletRequest req) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/approvalline");
		return view;
	}
	
	//1.결재라인 미리보기
	public String loadlinepreview(HttpServletRequest req) {
		String originalpk = req.getParameter("ai");
		int apl_ai = Integer.parseInt(originalpk);
		String content = linedao.selectLineContentbyPK(apl_ai);
		return content;
	}
	
	//2.결재라인에 필요한 유저 검색
	public List<UserVo> linesearch(HttpServletRequest req) {
		String[] reqarray = req.getParameterValues("searchinfos");
		String[] searchinfos = reqarray[0].split(",");
		String searchoption = searchinfos[0];
		String keyword = searchinfos[1];
		
		List<UserVo> uservos = new ArrayList<UserVo>();
		
		if(searchoption.equals("emp")) {
			List<HashMap> userlist = userdao.selectUserListbyUserName(keyword);
			uservos = commonservice.makeUserVoList(userlist);
		}else if(searchoption.equals("dep")) {
			int dep_ai = userdao.selectDepPKbyDepName(keyword);
			List<HashMap> userlist = userdao.selectUserListbyDepPK(dep_ai);
			uservos = commonservice.makeUserVoList(userlist);
		}else {
			System.err.println("unavaliable options!!!");
			uservos = null;
		}
		return uservos;
	}
	
	//3.결재라인 DB삽입 
	public String insertline(LineVo linevo,String viewName) {
		linedao.insertApl(linevo);
		return viewName;
	}
}
