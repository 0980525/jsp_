package repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.BoardController;
import domain.BoardVO;
import orm.DatabaseBuilder;

public class BoardDAOImpl implements BoardDAO {

	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
//DB연결 
	private SqlSession sql;
	
	public BoardDAOImpl() {
		new DatabaseBuilder();
		sql = DatabaseBuilder.getFactory().openSession();
	}
	
//메서드 구현
	@Override
	public int insert(BoardVO bvo) {
		log.info(">>insert check 3 ");
		int isOk = sql.insert("BoardMapper.add",bvo);
		if(isOk>0) sql.commit();
		return isOk;
	}

	@Override
	public List<BoardVO> selectList() {
		log.info(">>list check 3 ");
		return sql.selectList("BoardMapper.list");
	}

	@Override
	public BoardVO selectOne(int bno) {
		log.info(">>detail check 3 ");
		return sql.selectOne("BoardMapper.detail",bno);
	}

	@Override
	public int update(BoardVO bvo) {
		int isOk = sql.update("BoardMapper.up",bvo);
		if(isOk>0)sql.commit();
		return isOk;
	}

	@Override
	public int delete(int bno) {
		int isOk = sql.delete("BoardMapper.del",bno);
		if(isOk>0) sql.commit();
		return isOk;
	}

	@Override
	public List<BoardVO> selectList(String id) {
		
		return sql.selectList("BoardMapper.mylist",id);
	}

	

	

	

	

	
}
