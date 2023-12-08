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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.BoardVO;
import domain.PagingVO;
import handler.FileRemoveHandler;
import handler.PagingHandler;
import net.coobird.thumbnailator.Thumbnails;
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
	private String savePath; //파일저장경로를 저장하는 변수 
	
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
				//파일을 업로드할 물리적인 경로를 설정((파일을 바깥에 둔 경우 )D:-jsp_workspace-jsp_study-src-(main부터 시작 화면은 webapp/나머지 java)main-webapp-_fileUpload
//프로젝트 파일 안에 파일업로드 폴더를 둔 경우
//servlet컨테이너의 설정정보 가지고있음 = getServletcontext				
//savePath=getServletContext().getRealPath() = webapp까지의 경로	
				savePath=getServletContext().getRealPath("/_fileUpload");
				File fileDir = new File(savePath);
				log.info("저장 위치 :>> "+savePath);
				
				DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
				fileItemFactory.setRepository(fileDir); //가져온 파일을 저장할 위치를 file객체로 지정
				fileItemFactory.setSizeThreshold(1024*1024*3);//파일 저장을 위한 임시 메모리 설정 : byte단위
				
				//미리 객체 설정
				BoardVO bvo = new BoardVO();
				//서블릿 파일업로드 multipart/form-data 형식으로 
				ServletFileUpload fileUpload= new ServletFileUpload(fileItemFactory);
				
				List<FileItem> itemList = fileUpload.parseRequest(request);
				for(FileItem item : itemList) {
					switch(item.getFieldName()) {
					case "title" : bvo.setTitle(item.getString("utf-8"));
					break;
					case "writer" : bvo.setWriter(item.getString("utf-8"));
					break;
					case "content" : bvo.setContent(item.getString("utf-8"));
					break;
					case "image_file" : 
						//(필수가 아니어서 없을수 있음)이미지가 있는지 체크 (null로 하지않음 값의 존재유무 getsize로 체크)
						if(item.getSize() > 0) { //값이 0이면 없는것 / 데이터의 크기를 바이트 단위로 리턴/크기가 0보다 큰지 체크
							String fileName = item.getName()  //경로를 포함한 파일의 이름 ~~/파일이름.jpg
									.substring(item.getName().lastIndexOf("/")+1); //이름만 분리
							//.substring(item.getName().lastIndexOf(file.separator)+1); 
							//file.separator = 파일 경로 기호를 저장// mac-(\) window-(/) / 
							//파일 이름 같으면 먼저있던 파일 사라짐->이름 다르게 해줘야함(시스템시간을 이용) (난수_라이브러리를 이용한 방법도 있음)
							//시스템의 시간을 이용하여 파일을 구분 /시간_dog.jpg
							fileName = System.currentTimeMillis()+"_"+fileName; 
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							log.info(uploadFilePath.toString()); //D:\lhs\jsp_workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\jsp_study\_fileUpload
							
							//저장
							try {
								item.write(uploadFilePath); //자바 객체를 디스크에 쓰기
								bvo.setImageFile(fileName); //bvo에 저장할 값 설정
								
								//썸네일 작업 : 전체 리스트페이지에서 트래픽 과다사용 방지용 
								Thumbnails.of(uploadFilePath)
								.size(75, 75)  //픽셀기준
								.toFile(new File(fileDir+File.separator+"_th_"+fileName)); 
								
							} catch (Exception e) {
								log.info(">>> file writer on disk error");
								e.printStackTrace();
								}
						}
						
						break;
					} //switch
				} //for
				
				isOk = bsv.register(bvo);
				log.info("board register >>{}",isOk>0? "ok":"fail");
				
				//목적지 주소
				destPage="/index.jsp";
				
				
//				파일 업로드 없을때 사용 insert구문
//				//jsp 에서 데이터를 입력후 =>컨트롤러로 전송 
//				//DB에 등록한 후 =>index.jsp로 이동
//				//제목꺼내오기
//				String title = request.getParameter("title");
//				//작성자
//				String writer = request.getParameter("writer");
//				//내용
//				String content = request.getParameter("content");
//				log.info(">>>insert check 1 ");
//				
//				BoardVO bvo = new BoardVO(title, writer, content);
//				log.info("insert bvo >> {} "+bvo);
//				
//				//만들어진 bvo를 DB에 저장 잘 되면 1/아니면 0리턴
//				isOk=bsv.register(bvo);
//				log.info("board register >>{}",isOk>0? "ok":"fail");
//				
//				//목적지 주소
//				destPage="/index.jsp";
				
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
					case "title" : bvo.setTitle(item.getString("utf-8"));
						break;
					case "content" : bvo.setContent(item.getString("utf-8")); 
						break;
					case "image_file" : old_file = item.getString("utf-8"); //이전파일의 보관용
						break;
					case "new_file" : //새로운 파일은 등록이 될수도 있고, 안될 수도 있음
						if(item.getSize()>0) {
							//새로운 등록 파일이 있다면 ...
							if(old_file != null) { // 이미지파일에 값이 있음
								//old_file 삭제 작업
								//파일 삭제 핸들러를 통해서 파일 삭제 작업 진행 
								FileRemoveHandler fileHandler = new FileRemoveHandler(); //객체 생성
								isOk =fileHandler.deleteFile(old_file, savePath); //파일지워서 isDel을 isOk로 받기
							}
							//새로운 파일에 대한 객체 작업
							String fileName = item.getName()
									.substring(item.getName().lastIndexOf(File.separator)+1);
							log.info("new_file name >> {} " + fileName);
							
							fileName = System.currentTimeMillis()+"_"+fileName;
							File uploadFilePath = new File(fileDir+File.separator+fileName);
							
							try {
								item.write(uploadFilePath);
								bvo.setImageFile(fileName);
								//썸네일 작업
								Thumbnails.of(uploadFilePath)
								.size(75, 75)
								.toFile(new File(fileDir+File.separator+"_th_"+fileName));
								
							} catch (Exception e) {
								log.info("File Update Error");
								e.printStackTrace();
							}
						}else {
							//기존 이미지파일은 있지만 , 새로운 이미지파일이 없다면 
							//기존객체를 bvo에 담기
							bvo.setImageFile(old_file);
						}
						break;
					}//switch
				}//for
				
				isOk = bsv.modify(bvo);
				log.info("edit>>{}",isOk >0? "ok":"fail");
				destPage="list";//내부의 list 케이스로 돌림 
				
				
				
//				//파라미터로 받은 bno,title,content데이터를 
//				//DB에 수정하여 넣고 list로 이동
//				int bno = Integer.parseInt(request.getParameter("bno"));
//				String title = request.getParameter("title");
//				String content = request.getParameter("content");
//				
//				BoardVO bvo = new BoardVO(bno, title, content);
//				log.info("edit check 1");
//				log.info("edit >>{}"+bvo);
//				
//				isOk = bsv.modify(bvo);
//				log.info("edit>>{}",isOk >0? "ok":"fail");
//				destPage="list";//내부의 list 케이스로 돌림 
				
				
			} catch (Exception e) {
				log.info("edit error");
				e.printStackTrace();
			}
			
			break;
		case  "remove" : 
			try {
				int bno = Integer.parseInt(request.getParameter("bno"));
				
				  //댓글, 파일도 같이 삭제
	            BoardVO bvo = new BoardVO();
	            bvo = bsv.getDetail(bno); //특정 게시글의 상세정보를 가져옴
	            String fileName = bvo.getImageFile();
	            
	            savePath = getServletContext().getRealPath("/_fileUpload"); //연결된 파일 이름 가져오기
	            FileRemoveHandler fileRemoveHandler = new FileRemoveHandler();
	            isOk = fileRemoveHandler.deleteFile(fileName, savePath); //파일 삭제
	          
				
				log.info("저장 위치 :>> "+savePath);
				log.info("저장 위치 :>> "+fileName);
				
				FileRemoveHandler fileHandler = new FileRemoveHandler();
				isOk = fileHandler.deleteFile(fileName, savePath);
				
				
				
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
