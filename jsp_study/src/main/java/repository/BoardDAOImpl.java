package repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import orm.DatabaseBuilder;


public class BoardDAOImpl implements BoardDAO {

	private static final Logger log=
			LoggerFactory.getLogger(BoardDAOImpl.class);
	
	//DB연결
	private SqlSession sql;
	
	public BoardDAOImpl() {
		new DatabaseBuilder();
		sql = DatabaseBuilder.getFactory().openSession();
	
	}
	//메서드 구현

	@Override
	public int insert(BoardVO bvo) {
		log.info(">>insert check 3");
		//실제 DB에 저장=>mybatis - mapper가 실행해줌
		//sql.insert (mapperNamepace.id로 인식)
		int isOk = sql.insert("BoardMapper.add", bvo);//BoardMapper에 add에 bvo를 저장
		//insert,update,delete시 DB가 변경되는 구문 시 commit();으로 완결
		if(isOk > 0) {
			sql.commit();
		}
		return isOk;
	}

	@Override
	public List<BoardVO> selectList(PagingVO pgvo) {
		log.info(">>list check3");
		return sql.selectList("BoardMapper.list",pgvo);
	}

	@Override
	public BoardVO getDetail(int bno) {
		log.info(">>detail check3");
		return sql.selectOne("BoardMapper.detail", bno);
	}

	@Override
	public int readcountUpdate(int bno) {
		int isOk = sql.update("BoardMapper.read",bno);
		if(isOk>0) {
			sql.commit();
		}
		return isOk;
	}

	@Override
	public int Update(BoardVO bvo) {
		int isOk = sql.update("BoardMapper.up",bvo);
		if(isOk>0) {
			sql.commit();
		}
		return isOk;
		
	}

	@Override
	public int delete(int bno) {
		log.info("3");
		int isOk = sql.delete("BoardMapper.del",bno);
		if(isOk>0) {
			sql.commit();
		}
		return isOk;
	}
	@Override
	public int getTotal() {
		// TODO Auto-generated method stub
		return sql.selectOne("BoardMapper.tot");
	}

//	@Override
//	public int selectOne(int bno) {
//
//		return sql.selectOne("BoardMapper.count");
//	}
	
}

