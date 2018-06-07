package com.groupware.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.groupware.JavaUtils;
import com.groupware.dao.AprovDao;
import com.groupware.dao.DraftDao;
import com.groupware.dao.UserDao;
import com.groupware.vo.ApprovalVo;
import com.groupware.vo.DraftVo;
import com.groupware.vo.UserVo;


@Service
@SuppressWarnings("rawtypes")
public class CommonService{
	@Autowired HttpSession session;
	@Autowired DraftDao draftdao;
	@Autowired UserDao userdao;
	@Autowired AprovDao aprovdao;
	JavaUtils jutils = new JavaUtils();
	
	//1.세션추가
	public void addSession(String name,Object value) {
		session.setAttribute(name, value);
	}
	
	//2.세션제거
	public void removeSession(String name) {
		session.removeAttribute(name);
	}
	
	//3.세션의정보(로그인정보)얻기
	public String getSession(String name) {
		return (String) session.getAttribute(name);
	}
	
	//4.DB에 문서를 셀렉트하여 필요한 정보를 view에 Add
	public ModelAndView selectdoc(HttpServletRequest req) {
		String headurl = "http://localhost:8088/";
		String url = req.getRequestURL().toString(); //작업요청을 보내오는 페이지의 주소값
		int doccount = draftdao.selectDocCount(); //문서개수
		String[] docspk = draftdao.selectDocsPK(); //문서PK
		String[] docs = draftdao.selectDocNames(); //문서명
		HashMap<String, Integer> map = new HashMap<String, Integer>(); //문서PK+문서명
		ModelAndView view = new ModelAndView();
		
		if(url.equals(headurl+"draftdoc")) {
			for(int i=0;i<docspk.length;i++) {
				map.put(docs[i], Integer.parseInt(docspk[i]));
			}
			view.setViewName("/draftdoc");
			view.addObject("doccount",doccount);
			view.addObject("docmap", map);
		}else if(url.equals(headurl+"draftmanager")) {
			view.setViewName("/draftmanager");
			view.addObject("doccount",doccount);
			view.addObject("pkarrays",docspk);
		}else if(url.equals(headurl+"approvalline")) {
			for(int i=0;i<docspk.length;i++) {
				map.put(docs[i], Integer.parseInt(docspk[i]));
			}
			view.setViewName("/approvalline");
			view.addObject("doccount",doccount);
				view.addObject("docmap", map);
			}
		return view;
	}
	
	//5.사용하지 않는 문서의 이미지 삭제
	public void deleteOldImage(DraftVo draftvo) {
		String imgRegex = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
		String updatedcontent = draftvo.getDraft_content();
		String prevcontent = draftdao.selectDocContentbyDraftPk(draftvo.getDraft_ai());
		List<String> updateimgs = new ArrayList<String>();
		List<String> previmgs = new ArrayList<String>();
		List<String> delimgs = new ArrayList<String>();
		
		updateimgs = jutils.makeMatcherList(imgRegex, updatedcontent);
		previmgs = jutils.makeMatcherList(imgRegex, prevcontent);
		
		delimgs = findDeletedImgs(previmgs, updateimgs);
		dropDeletedImgs(delimgs);
	}
	
	//5-1.사용하지 않는 문서의 이미지 찾기
	public List<String> findDeletedImgs(List<String>previmg,List<String>updateimg) {
		if(!previmg.isEmpty()) {
			if(!updateimg.isEmpty()) {
			for(int k=0;k<previmg.size();k++) {
				for(int i=0;i<updateimg.size();i++) {
					if(previmg.get(k).equals(updateimg.get(i))) {
						previmg.remove(k);
					}
				}
			}
			}else{
				System.out.println("업데이트 과정에서 모든 이미지들이 삭제됨");
			}
		}
		return previmg;
	}
	
	//5-2.이미지 파일 삭제
	public void dropDeletedImgs(List<String>delimgs) {
		for(int i=0;i<delimgs.size();i++) {
			String delimg = delimgs.get(i).substring(10,34);
			String headurl = 
					"C:/Users/JunYongLee/Desktop/"
					+ "2018DojeProjects-master/"
					+ "groupware/src/main/webapp";
			File delimgfile = new File(headurl+delimg);
			delimgfile.delete();
		}
	}
	
	//6.UserVo객체 리스트 생성
	public List<UserVo> makeUserVoList(List<HashMap> userlist){
		List<UserVo> uservos = new ArrayList<UserVo>();
		
		if(userlist.size()!=0) {
			for(int i=0;i<userlist.size();i++) {
				UserVo vo = new UserVo(); // arrayList든 Vector든 아이템을 추가하려면 다른 레퍼런스(주소)를 가지는 새로운 객체를 넣어주어야함. 그래서 new 연산자를 사용해야한다
				
				vo.setUser_ai((int)userlist.get(i).get("user_ai"));
				vo.setDep_ai((int)userlist.get(i).get("dep_ai"));
				vo.setRank_ai((int)userlist.get(i).get("rank_ai"));
				vo.setUser_name((String)userlist.get(i).get("user_name"));
				vo.setDep_name(userdao.selectDepNamebyDepPK
						((int) userlist.get(i).get("dep_ai")));
				vo.setRank_name(userdao.selectRankNamebyRankPK
						((int) userlist.get(i).get("rank_ai")));
				
				uservos.add(vo);
			}
			}else {
				System.err.println("warning no results!!!");
				uservos = null;
			}
		return uservos;
	}
	
	//7.DraftVo객체 리스트 생성
	public List<DraftVo> makeDraftVoList(List<HashMap> draftlist){
		List<DraftVo> draftvos = new ArrayList<DraftVo>();
		
		if(draftlist.size()!=0) {
			for(int i=0;i<draftlist.size();i++) {
				// arrayList든 Vector든 아이템을 추가할때 다른 레퍼런스를 가지는 새로운 객체를 넣어주어야함. 
				//그래서 new 연산자를 사용해야한다
				DraftVo vo = new DraftVo(); 
				
				vo.setDraft_ai((int)draftlist.get(i).get("draft_ai"));
				vo.setDraft_name((String)draftlist.get(i).get("draft_name"));
				vo.setDraft_type((String)draftlist.get(i).get("draft_type"));
				vo.setDraft_temp((int)draftlist.get(i).get("draft_temp"));
				vo.setDraft_important((int)draftlist.get(i).get("draft_important"));
				
				draftvos.add(vo);
			}
			}else {
				System.err.println("warning no results!!!");
				draftvos = null;
			}
		return draftvos;
	}
	
	//8.AprovVo객체 리스트 생성
	public List<ApprovalVo> makeAprovVoList(List<HashMap> aprovlist){
		List<ApprovalVo> aprovvos = new ArrayList<ApprovalVo>();
		
		if(aprovlist.size()!=0) {
			for(int i=0;i<aprovlist.size();i++) {
				ApprovalVo vo = new ApprovalVo(); // arrayList든 Vector든 아이템을 추가하려면 다른 레퍼런스(주소)를 가지는 새로운 객체를 넣어주어야함. 그래서 new 연산자를 사용해야한다
				String sLatestLoginDate = "";
				
				try {
					  java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					  sLatestLoginDate = formatter.format(aprovlist.get(i).get("aprov_reg"));
					} catch (Exception ex) {
					  ex.printStackTrace();
					  sLatestLoginDate = "";
					}
				
				vo.setAprov_ai((int)aprovlist.get(i).get("aprov_ai"));
				vo.setAprov_title((String)aprovlist.get(i).get("aprov_title"));
				vo.setAprov_reg(sLatestLoginDate);
				vo.setAprov_status((int)aprovlist.get(i).get("aprov_status"));
				aprovvos.add(vo);
			}
			}else {
				System.err.println("warning no results!!!");
				aprovvos = null;
			}
		return aprovvos;
	}
}
