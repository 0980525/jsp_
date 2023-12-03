package repository;

import java.util.List;

import domain.BoardVO;
import domain.PagingVO;

public interface BoardDAO {

	int insert(BoardVO bvo);

	List<BoardVO> selectList(PagingVO pgvo);

	BoardVO getDetail(int bno);

	int readcountUpdate(int bno);

	int Update(BoardVO bvo);

	int delete(int bno);

//	int selectOne(int bno);

	int getTotal();

}
