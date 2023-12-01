package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.MemberVO;
import service.BoardService;
import service.BoardServiceImpl;

@WebServlet("/brd/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
//로그 기록 객체 생성
	private static final Logger log = LoggerFactory.getLogger(BoardController.class);
//멤버변수 선언
	//jsp에서 받은 요청을 처리/처리결과(데이터)를 다른 jsp에 보내는 역할을 하는 객체
	private RequestDispatcher rdp;
	//목적지 주소 저장하는 변수
	private String destPage;
	//DB구문 체크 값 저장 변수
	private int isOk;
	
	//controller랑 service 연결 변수 (interface로 생성)
	private BoardService bsv;
  
//생성자
    public BoardController() {
    	//bsv를 구현 할 객체(class로 생성)
       bsv = new BoardServiceImpl();
    }
//실제 처리 메서드 service
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//매개변수의 객체 인코딩 설정 
		//요청
		request.setCharacterEncoding("utf-8");
		//응답
		response.setCharacterEncoding("utf-8");
		//content type 설정
		response.setContentType("text/html;charset=UTF-8");
	//jsp에서 오는 요청 주소를 받음
		String uri = request.getRequestURI();
		log.info("uri >>",uri);
		///brd/register에서 register만 추출
		String path = uri.substring(uri.lastIndexOf("/")+1);
		log.info("path >>" ,path);
		switch(path) {
		case "register":
			destPage="/board/register.jsp";
			break;
		case "insert":
			//.jsp에서 제목,작성자,내용 요청해서 받아옴, 
			//받은자료로 BoardVO bvo생성, 
			//service한테 bvo매개변수로 토스-> DB에 저장됨, 
			//실행 후 페이지이동 
			try {
				String title = request.getParameter("title");
				String writer = request.getParameter("writer");
				String content = request.getParameter("content");
				log.info("insert check 1 ");
				
				BoardVO bvo = new BoardVO(title,writer,content);
				
				isOk = bsv.register(bvo);
				log.info("board register >> ",isOk>0? "ok":"fail");
				
				destPage="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("insert error");
			}
			break;
		case "list":
			try {
				//리스트 생성후 서비스한테 토스(서비스가 할 일->DB에서 전체 리스트 조회 -> 전체리스트 ->.jsp에 뿌려주기)
				//list를 요청
				//list.jsp로 페이지 이동 
				List<BoardVO> list = bsv.getlist();
				request.setAttribute("list", list);
				destPage="/board/list.jsp";
			} catch (Exception e) {
				log.info("list error");
				e.printStackTrace();
			}
			
			break;
		case "detail":
			try {
				//jsp에서 보낸 bno를 받아서 
				//bsv한테 요청 해당 번호의 전체 값을 조회
				//detail.jsp에 뿌리기
				//페이지 이동 jsp로 
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetail(bno);
				request.setAttribute("bvo", bvo);
				destPage="/board/detail.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("detail error");
			
			}
			break;
		case "modify":
			try {
				//수정할 데이터의 bno를 받아 
				//bsv한테 토스해서 얻어온 요청값을 bvo형태로 저장 (setAttribute) 
				//modify.jsp로 보내기
				//페이지 이동 .jsp로
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetail(bno);
				request.setAttribute("bvo", bvo);
				destPage="/board/modify.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("modify error");
			}
			break;
		case "edit":
			try {
				//파라미터로 받은 bno, title,content데이터를 DB에 수정하여 넣고 list로 이동 
				//수정한 값은 새로 bvo에 넣어서 bsv한테 토스->DB에 저장
				int bno = Integer.parseInt(request.getParameter("bno"));
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				
				BoardVO bvo = new BoardVO(bno,title,content);
				isOk = bsv.modify(bvo);
				destPage="list";
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("edit error");
			}
			
			break;
		case "remove":
			try {
				//bno 받아와서 bsv한테 remove로 패스
				//이동페이지는 list
				int bno = Integer.parseInt(request.getParameter("bno"));
				isOk = bsv.remove(bno);
				destPage="list";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("remove error");
			}
			break;
		case "mylist" : 
			try {
				HttpSession ses = request.getSession();
				MemberVO mvo = (MemberVO)ses.getAttribute("ses");
				String id = mvo.getId();
				List<BoardVO> bvo = bsv.chList(id);
				request.setAttribute("mylist", bvo);
				destPage="/board/myList.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("myList error");
			}
			break;
			default: break;
		}
	//case 끝난 후 목적지 주소로 데이터를 전달 
		rdp = request.getRequestDispatcher(destPage);
		//요청에 필요한 객체를 가지고 destPage경로로 전송
		rdp.forward(request, response);
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

}
