package repository;

import java.util.List;

import domain.MemberVO;

public interface MemberDAO {

	int insert(MemberVO mvo);

	MemberVO login(MemberVO mvo);

	int lastLogin(String id);

	List<MemberVO> selectList();

//	int getDetail(MemberVO mvo);

	int update(MemberVO mvo);

	int delete(String id);

	

}
