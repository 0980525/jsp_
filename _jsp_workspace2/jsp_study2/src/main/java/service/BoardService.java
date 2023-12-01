package service;

import java.util.List;

import domain.BoardVO;

public interface BoardService {

	
	int register(BoardVO bvo);

	List<BoardVO> getlist();

	BoardVO getDetail(int bno);

	int modify(BoardVO bvo);

	int remove(int bno);

	List<BoardVO> chList(String id);

	

}
