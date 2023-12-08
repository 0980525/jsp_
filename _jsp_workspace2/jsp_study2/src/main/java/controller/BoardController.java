package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.MemberVO;
import domain.PagingVO;
import handler.PagingHandler;
import net.coobird.thumbnailator.Thumbnails;
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
	
	//파일 저장 경로를 저장하는 변수 
	private String savePath;
	
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
			//jsp에서 가져온 제목,작성자,내용 꺼내오기(경로확인,파라미터확인 =>try catch)
			try {
				savePath = getServletContext().getRealPath("/_fileUpload");
				//임시저장 위치 fileDir
				File fileDir = new File(savePath);
				log.info("저장 위치 :>> "+savePath);
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				
				fileItemFactory.setRepository(fileDir);
				fileItemFactory.setSizeThreshold(1024*1024*3);
				
				BoardVO bvo = new BoardVO();
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				List<FileItem> itemList = fileUpload.parseRequest(request);
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "title": bvo.setTitle(item.getString("utf-8"));
					break;
					case "writer" : bvo.setWriter(item.getString("utf-8"));
					break;
					case "content" : bvo.setContent(item.getString("utf-8"));
					break;
					case "image_file" : 
						//이미지 있는지 체크
						if(item.getSize() > 0) {//값이 0이면 없음
							//파일 이름.file에서 이름만 분리
							String fileName = item.getName().substring(item.getName().lastIndexOf("/")+1);
							//파일이름이 같을 경우 구분을 위한 시간 삽입
							fileName = System.currentTimeMillis()+"_"+fileName;
							//uploadFilePath 생성 파일 경로+파일 경로구분separator+파일 이름
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							//toString 으로 경로 확인 로그찍기
							log.info("uploadFilePath >> "+uploadFilePath.toString());
							
							//저장
							try {
								//자바객체를 디스크에 씀 -디스크에 쓰는 이유 : 디스크가 아니라 메모리에만 저장하면 서버가 재시작될 때 파일이 손실될 수 있어서 
								item.write(uploadFilePath);
								//bvo에 저장할 값 설정
								bvo.setImageFile(fileName);
								
								//thumbnails 썸네일 작업 : 전체리스트 페이지에서 트래픽 과다사용 방지용
								Thumbnails.of(uploadFilePath)
									.size(75, 75)
									.toFile(new File(fileDir+File.separator+"th_"+fileName));
								
							} catch (Exception e) {
								log.info(">>file writer on disk error");
								e.printStackTrace();
							}
						}
						} //switch
					break;
				}//for
				
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
				PagingVO pgvo = new PagingVO();
				if(request.getParameter("pageNo")!= null) {
					int pageNo = Integer.parseInt(request.getParameter("pageNo"));
					int qty = Integer.parseInt(request.getParameter("qty"));
					String type = request.getParameter("type");
					String keyword = request.getParameter("keyword");
					log.info(">>pageNo/qty/type/keyword/"+pageNo+"/"+qty+"/"+type+"/"+keyword);
					pgvo = new PagingVO(pageNo,qty,type,keyword);
				}
				
				//리스트 생성후 서비스한테 토스(서비스가 할 일->DB에서 전체 리스트 조회 -> 전체리스트 ->.jsp에 뿌려주기)
				//list를 요청
				//list.jsp로 페이지 이동 
				List<BoardVO> list = bsv.getlist();
				int totalCount = bsv.getTotal(pgvo);
				log.info("totalCount >> {}"+totalCount);
				PagingHandler ph = new PagingHandler(pgvo,totalCount);
				log.info("list>>{}"+list);
				
				request.setAttribute("list", list);
				request.setAttribute("ph", ph);
				
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
				savePath = getServletContext().getRealPath("/_fileUpload");
				File fileDir = new File(savePath);
				
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir);
				fileItemFactory.setSizeThreshold(3*1024*1024);
				
				BoardVO bvo = new BoardVO();
				
				ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
				
				List<FileItem> itemList = fileUpload.parseRequest(request);
				String old_file = null;
				
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "bno" : bvo.setBno(Integer.parseInt(item.getString("utf-8")));
						break;
					case "title" : 
						break;
					case "content" : 
						break;
					case "image_file" : 
						break;
					case "new_file" : 
						break;
					}
				}
				
			} catch (Exception e) {
				log.info("edit error");
				e.printStackTrace();
			
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
