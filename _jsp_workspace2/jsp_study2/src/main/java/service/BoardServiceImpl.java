package service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.BoardController;
import domain.BoardVO;
import repository.BoardDAO;
import repository.BoardDAOImpl;

public class BoardServiceImpl implements BoardService {
	
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);

	private BoardDAO bdao;
	
	public BoardServiceImpl() {
		bdao = new BoardDAOImpl();
	}
	@Override
	public int register(BoardVO bvo) {
		log.info("insert check 2 ");
		return bdao.insert(bvo);
	}
	@Override
	public List<BoardVO> getlist() {
		
		return bdao.selectList();
	}
	@Override
	public BoardVO getDetail(int bno) {
		
		return bdao.selectOne(bno);
	}
	@Override
	public int modify(BoardVO bvo) {
		
		return bdao.update(bvo);
	}
	@Override
	public int remove(int bno) {
		
		return bdao.delete(bno);
	}
	@Override
	public List<BoardVO> chList(String id) {
		// TODO Auto-generated method stub
		return bdao.selectList(id);
	}
	


}
