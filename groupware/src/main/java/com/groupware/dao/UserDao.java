package com.groupware.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.groupware.vo.UserVo;

/*
 * 각 DAO에는 이름과 역할에 관련된 쿼리문만 넣자
 * 4.UserDao->회원정보 관련 쿼리문
 * 단일PK셀렉트->int,다중PK셀렉트->String
*/

@Mapper
@SuppressWarnings("rawtypes")
public interface UserDao {
	
	//Login Process
	public int userIdCheck(String userid);
	public int userPwCheck(String userpw);
	public void insertUser(UserVo vo);
	public int selectUserCount(UserVo userVo);
	
	//Other Service Process===========================================================
	
	//1.User
	public int selectUserPK(String userid);
	public String[] selectUsers();
	public String[] selectUsersPK();
	public List<HashMap> selectUserListbyUserName(String user_name);
	public List<HashMap> selectUserListbyDepPK(int dep_ai);
	
	//2.Department
	public int selectDepPKbyUserPK(int user_ai);
	public int selectDepPKbyUserID(String userid);
	public int selectDepPKbyDepName(String dep_name);
	public String selectDepNamebyDepPK(int dep_ai);

	//3.Rank
	public int selectRankPKbyUserPK(int user_ai);
	public int selectRankPKbyUserID(String userid);
	public int selectRankPKByRankName(String rank_name);
	public String selectRankNamebyRankPK(int rank_ai);
	
}
