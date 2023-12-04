package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import handler.PagingHandler;
import service.BoardService;
import service.BoardServiceImpl;

@WebServlet("/brd/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      //로그 기록 객체 생성
	private static final Logger log=
			LoggerFactory.getLogger(BoardController.class);
	//멤버변수 선언
	private RequestDispatcher rdp; //jsp에서 받은 요청을 처리, 처리결과(데이터)를 다른 jsp로 보내는 역할을 하는 객체
	private String destPage;//목적지 주소 저장하는 변수 
	private int isOk;//DB구문 체크 값 저장변수 
	
	//controller <-> service
	private BoardService bsv; //interface로 생성
	
	public BoardController() {
		//생성자
		bsv=new BoardServiceImpl(); //class로 생성 bsv를 구현할 객체
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 실제 처리메서드 doGet,doPost로 오는 요청 모두 처리(매니저역할)
		log.info("필요한 로그 띄우기 가능");
		
		//매개변수의 객체의 인코딩 설정
		request.setCharacterEncoding("utf-8"); //요청
		response.setCharacterEncoding("utf-8"); //응답
		
		response.setContentType("text/html;charset=UTF-8");//content type 설정 
		//비동기 나가는 contenttype을 각각 설정해야함
		
		String uri=request.getRequestURI();//jsp에서 오는 요청 주소를 받는 설정
		System.out.println("sysout을 통한 로그>"+uri);
		log.info("log객체를 통한 로그 >"+uri); ///brd/register->register만 필요
		String path = uri.substring(uri.lastIndexOf("/")+1); //register
		log.info("실 요청 경로 >>"+path);
		
		switch(path) {
		case "register" : 
			destPage = "/board/register.jsp";
			break;
		case "insert" : 
			//jsp에서 가져온 제목,작성자,내용을 꺼내오기(경로확인,파라미터확인=>try-catch)
			try {
				//jsp 에서 데이터를 입력후 =>컨트롤러로 전송 
				//DB에 등록한 후 =>index.jsp로 이동
				//제목꺼내오기
				String title = request.getParameter("title");
				//작성자
				String writer = request.getParameter("writer");
				//내용
				String content = request.getParameter("content");
				log.info(">>>insert check 1 ");
				
				BoardVO bvo = new BoardVO(title, writer, content);
				log.info("insert bvo >> {} "+bvo);
				
				//만들어진 bvo를 DB에 저장 잘 되면 1/아니면 0리턴
				isOk=bsv.register(bvo);
				log.info("board register >>{}",isOk>0? "ok":"fail");
				
				//목적지 주소
				destPage="/index.jsp";
				
 			} catch (Exception e) {
				log.info("insert Error");
				e.printStackTrace();
			}
			break;
		case "list" :
			try {
				//index에서 list버튼을 클릭하면 컨트롤러에서 DB로 전체 리스트 요청
				//전체리스트를 가지고 list.jsp에 뿌리기
				log.info("list check 1");
				PagingVO pgvo = new PagingVO();//1페이지/10개/0번지
				if(request.getParameter("pageNo") != null) {
					int pageNo = Integer.parseInt(request.getParameter("pageNo"));
					int qty = Integer.parseInt(request.getParameter("qty"));
					String type=request.getParameter("type");
					String keyword=request.getParameter("keyword");
					log.info(">>pageNo/qty/type/keyword" + "/"+pageNo +"/"+ qty+"/"+type+"/"+keyword);
					 pgvo = new PagingVO(pageNo, qty,type,keyword);
					 //생성자를 바꿔서 만들었지만 set으로도 가능!
				}
				//db에서 전체게시글 수 가져오기
//				int bno = Integer.parseInt(request.getParameter("bno"));
//				int totalCount = bsv.getCount(bno); 
				List<BoardVO> list = bsv.getList(pgvo);
				int totalCount = bsv.getTotal(pgvo); 
				log.info("totalCount >>>{}" + totalCount);
				PagingHandler ph = new PagingHandler(pgvo,totalCount);
				//request.setAttribute("ph-객체(변수)명", ph-실제 객체);
				
				log.info("list>>{}"+list);
				/* list를 jsp로 전송 
				 * 검색어를 반영한 리스트
				 * */
				request.setAttribute("list", list);
				request.setAttribute("ph", ph);
				
				destPage = "/board/list.jsp";
			} catch (Exception e) {
				log.info("list error");
				e.printStackTrace();
			}
			break;
		case "detail" : 
			try {
				//jsp에서 보낸 bno를 받아서 해당 번호의 전체 값을 조회하여 detail.jsp에 뿌리기
				int bno = Integer.parseInt(request.getParameter("bno"));
				log.info("detail check 1");
				
				BoardVO bvo = bsv.getDetail(bno); //boardService 한테 값 달라고 요청
				log.info("detail bvo >>{}"+bvo);
				request.setAttribute("bvo", bvo);
				destPage = "/board/detail.jsp";
				
			} catch (Exception e) {
				log.info("detail error");
				e.printStackTrace();
			}
			break;
		case "modify" :
			try {
				//수정할 데이터의 bno를 받아서 수정페이지로 보내->modify.jsp를 띄우는 역할
				int bno = Integer.parseInt(request.getParameter("bno"));
				BoardVO bvo = bsv.getDetail(bno);
				request.setAttribute("bvo", bvo);
				destPage = "/board/modify.jsp";
				
			} catch (Exception e) {
				log.info("modify error");
				e.printStackTrace();
			}
			break;
		case "edit" : 
			try {
				//파라미터로 받은 bno,title,content데이터를 
				//DB에 수정하여 넣고 list로 이동
				int bno = Integer.parseInt(request.getParameter("bno"));
				String title = request.getParameter("title");
				String content = request.getParameter("content");
				
				BoardVO bvo = new BoardVO(bno, title, content);
				log.info("edit check 1");
				log.info("edit >>{}"+bvo);
				
				isOk = bsv.modify(bvo);
				log.info("edit>>{}",isOk >0? "ok":"fail");
				destPage="list";//내부의 list 케이스로 돌림 
				
				
			} catch (Exception e) {
				log.info("edit error");
				e.printStackTrace();
			}
			
			break;
		case  "remove" : 
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				log.info("remove check1");
				isOk = bsv.remove(bno);
				log.info("remove >>{}",isOk>0? "ok" : "fail");
				
				destPage = "list";
			} catch (Exception e) {
				log.info("remove error");
				e.printStackTrace();
			}
			
			
			break;
		
		}
		//case끝난 후 목적지 주소로 데이터를 전달 (requestDispatcher)
		rdp = request.getRequestDispatcher(destPage);//
		rdp.forward(request, response);//요청에 필요한 객체를 가지고 destpage 경로로 전송하는 역할
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get으로 오는 요청 처리response.getWriter().append("Served at: ").append(request.getContextPath());
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// post로 오는 요청 처리
		service(request, response);
	}

}
