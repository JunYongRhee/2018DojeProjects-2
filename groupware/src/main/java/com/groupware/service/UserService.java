package com.groupware.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.groupware.dao.LineDao;
import com.groupware.dao.UserDao;
import com.groupware.vo.UserVo;

@Service
public class UserService{
	@Autowired UserDao userdao;
	@Autowired LineDao linedao;
	@Autowired CommonService commonservice;
	@Autowired HttpSession session;
	
	//1.회원가입 서비스
	public ModelAndView userjoinlogic(HttpServletRequest req, UserVo userVo, String viewName) {
		ModelAndView model = new ModelAndView(viewName);
		model.addObject("vo",userVo);

		System.out.println(userVo.toString());
		System.out.println("success");

		//session.setAttribute(name, value);
		userdao.insertUser(userVo);
		return model;
	}

	//2.로그인 서비스
	public String loginCheck(HttpServletRequest req){
		String user_id = req.getParameter("user_id");
		String user_pw = req.getParameter("user_pw");
		String returnStr = "";
		
		UserVo userVo = new UserVo();
		userVo.setUser_id(user_id);
		userVo.setUser_pw(user_pw);
		int cnt = userdao.selectUserCount(userVo);
		
		if(cnt == 1){
			commonservice.addSession("id", user_id);
			returnStr = "T";
		}else{
			returnStr = "F";
		}
		return returnStr;
	}
	
	//3.로그아웃
	public ModelAndView userlogout(HttpServletRequest req, String viewName) {
		ModelAndView model = new ModelAndView(viewName);
		commonservice.removeSession("id");
		return model;
	}
	
	//4.결재생성에 쓰일 회원정보 미리보기
	public String loaduserpreview(HttpServletRequest req) {
		String originalpk = req.getParameter("ai");
		int user_ai = Integer.parseInt(originalpk);
		int dep_ai = userdao.selectDepPKbyUserPK(user_ai);
		int rank_ai = userdao.selectRankPKbyUserPK(user_ai);
		String dep = userdao.selectDepNamebyDepPK(dep_ai);
		String rank = userdao.selectRankNamebyRankPK(rank_ai);
		
		String userinfo = dep+","+rank;
		return userinfo;
	}
	
}

