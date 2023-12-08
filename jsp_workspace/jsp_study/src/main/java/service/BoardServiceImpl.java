package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import repository.BoardDAO;
import repository.BoardDAOImpl;
import repository.CommentDAO;
import repository.CommentDAOImpl;

public class BoardServiceImpl implements BoardService {

	private static final Logger log=
			LoggerFactory.getLogger(BoardServiceImpl.class);
	
	private BoardDAO bdao;//interface로 생성
	
	private CommentDAOImpl cdao = new CommentDAOImpl();
	public BoardServiceImpl() {
		bdao = new BoardDAOImpl();
		
	}

	@Override
	public int register(BoardVO bvo) {
		// 
		log.info(">>insert check2 ");
		
		return bdao.insert(bvo);
	}

	@Override
	public List<BoardVO> getList(PagingVO pgvo) {
		// 
		return bdao.selectList(pgvo);
	}

	@Override
	public BoardVO getDetail(int bno) {
		log.info(">>detail.check2");
		//detail체크시 readcount+1
		int isOk = bdao.readcountUpdate(bno);
		return bdao.getDetail(bno);
	}

	@Override
	public int modify(BoardVO bvo) {
		log.info(">>modify check2");
		return bdao.Update(bvo);
	}

	@Override
	public int remove(int bno) {
		log.info("2");
		
		cdao.deleteAll(bno);
		return bdao.delete(bno);
	}

	@Override
	public int getTotal(PagingVO pgvo) {
		// TODO Auto-generated method stub
		return bdao.getTotal(pgvo);
	}


}
