package com.groupware.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.groupware.dao.DraftDao;
import com.groupware.dao.UserDao;
import com.groupware.vo.DraftVo;

@Service
@SuppressWarnings("rawtypes")
public class DraftService {
	@Autowired DraftDao draftdao;
	@Autowired UserDao userdao;
	@Autowired CommonService commonservice;
	
	//1.미리보기 보여주기
	public String loadpreview(HttpServletRequest req) {
		String originalpk = req.getParameter("ai");
		int draft_ai = Integer.parseInt(originalpk);
		String content = draftdao.selectDocContentbyDraftPk(draft_ai);
		return content;
	}
	
	//2.문서DB에 삽입
	public ModelAndView insertdoc(DraftVo draftvo, String viewName) {
		ModelAndView model = new ModelAndView();
		model.setViewName(viewName);
		model.addObject("vo",draftvo);
		
		draftdao.insertDoc(draftvo);
		return model;
	}
	
	//3.문서업데이트
	public String updatedoc(DraftVo draftvo, String viewName) {
		commonservice.deleteOldImage(draftvo);
		draftdao.updateDoc(draftvo);
		return viewName;
	}
	
	//4.문서작성페이지이동(작성자정보를 추가)
	public ModelAndView linkwritedoc(String viewName) {
		String loginedid = commonservice.getSession("id");
		int userai = userdao.selectUserPK(loginedid);
		int depai = userdao.selectDepPKbyUserID(loginedid);
		int rankai = userdao.selectRankPKbyUserID(loginedid);
		
		ModelAndView view = new ModelAndView();
		view.setViewName(viewName);
		view.addObject("userai",userai);
		view.addObject("depai",depai);
		view.addObject("rankai",rankai);
		
		return view;
	}
	
	//5.문서편집페이지이동
	public ModelAndView linkchangedoc(HttpServletRequest req,DraftVo vo,String viewName) {
		int pk = vo.getDraft_ai();
		ModelAndView model = new ModelAndView();
		DraftVo resultvo = draftdao.selectDraftInfosbyDraftPK(pk);
		
		model.setViewName(viewName);
		model.addObject("vo",resultvo);
		
		return model;
	}
	
	//6.문서정보List제작
	public List<DraftVo> getDraft() {
		List<HashMap> draftlist = draftdao.selectDraftList();
		List<DraftVo> draftvos = new ArrayList<DraftVo>();
		
		draftvos = commonservice.makeDraftVoList(draftlist);
		return draftvos;
	}
	
	//7.문서삭제
	public String deleteDoc(HttpServletRequest req) {
		String resultStr = "삭제되었습니다";
		int draft_pk = Integer.parseInt(req.getParameter("ai"));
		
		draftdao.deleteDoc(draft_pk);
		return resultStr;
	}
}
