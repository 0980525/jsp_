package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import domain.CommentVO;
import service.CommentService;
import service.CommentServiceImpl;

//비동기식 서블릿 목적지 없음 (requestDispacther ,destPage) -왔던 위치 그대로 데이터 보냄
@WebServlet("/cmt/*")
public class CommemtController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(CommemtController.class);   
	private CommentService csv;
	private int isOk;

    public CommemtController() {
        csv = new CommentServiceImpl();
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//contentType은 jsp화면으로 갈때 설정 => 비동기식에서는 설정 안함 
		
		String uri = request.getRequestURI(); // 현재 경로 표시 cmt/post -> cmt/list/370
		log.info("경로 >>"+uri);
		String pathUri = uri.substring("/cmt/".length()); //post, list/370
		String path = pathUri;
		String pathVar = "";
		if(pathUri.contains("/")) {
			path=pathUri.substring(0,pathUri.lastIndexOf("/"));
			pathVar=pathUri.substring(pathUri.lastIndexOf("/")+1);
		}
		log.info("path >> {}"+path);
		log.info("pathVar >> {}"+pathVar);
	
		switch(path) {
		case "post":
			try {
				//js에서 보낸 데이터를 읽어들이는 작업 (js->controller) String 으로 보내서 StringBuffer로 받아야함
				StringBuffer sb = new StringBuffer();
				String line = "";
//				buffer읽어오는 reader(json읽음)/ 보낼 writer있어야함
				BufferedReader br= request.getReader(); //댓글 객체
				
				while((line = br.readLine()) != null) { //한줄씩 읽음
					sb.append(line); //line에 한줄씩 읽은 거 추가
				}
				log.info(">>>>>sb<<<<<"+sb.toString());
			// commentVO객체로 생성
				JSONParser parser = new JSONParser(); //sb.toString 값을 parsing 해서 (text)->json 변경 
				JSONObject jsonObj = (JSONObject)parser.parse(sb.toString()); //json object형태로 변환 //key:value 형태
				
				//key 를 이용햐여 value 를 추출
				int bno = Integer.parseInt(jsonObj.get("bno").toString()); //get이 obj형태라 toString 으로 문자로 변환 =>숫자로 변환해서 담음
				String writer =jsonObj.get("writer").toString();
				String content = jsonObj.get("content").toString();
				
				CommentVO cvo = new CommentVO(bno, writer, content);
				log.info("commentVO >>> {} "+ cvo); //들어온거 확인
				
			//csv한테 등록하라고 보내기
				isOk=csv.post(cvo);
				log.info("isOk >> "+((isOk>0)? "ok":"fail"));
				
			//결과데이터 전송 => 화면에 출력 (response 객체의 body에 기록)
				PrintWriter out = response.getWriter();
				out.print(isOk);
			
			} catch (Exception e) {
				log.info("comment post error");
				e.printStackTrace();
			}
			break; 
		case "list": 
			try {
				int bno = Integer.parseInt(pathVar);
				List<CommentVO> list = csv.getList(bno);
				log.info("list >> {}"+list);
				
				//list => json 객체로 변환하여 화면에 뿌리기
				//jsonObj형태로 만들어 key:value 
				JSONObject[] jsonObjArr = new JSONObject[list.size()]; //전체리스트의 사이즈만큼 jsonOdj배열만들기
				//{…}, {…}, {…}배열로 만듦
				JSONArray jsonObjList = new JSONArray();
				
				for(int i=0;i<list.size();i++) {
					jsonObjArr[i]=new JSONObject();
//					jsonObjArr[i].put(key, value); //key 만들어야함 데이터만 가져왔음
					jsonObjArr[i].put("cno", list.get(i).getCno());
					jsonObjArr[i].put("bno", list.get(i).getBno());
					jsonObjArr[i].put("writer", list.get(i).getWriter());
					jsonObjArr[i].put("content", list.get(i).getContent());
					jsonObjArr[i].put("regdate", list.get(i).getRegdate());
					
					jsonObjList.add(jsonObjArr[i]);
				}
				//'[{…}, {…}, {…}]' String '' 로 덮어씌움 
				String jsonData = jsonObjList.toJSONString();
				
					PrintWriter out = response.getWriter();
					out.print(jsonData);
					
				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("list error");
			}
			break; 
		case "remove":
			try {
				int cno = Integer.parseInt(request.getParameter("cnoVal")) ;
				isOk = csv.remove(cno);
				log.info("comment remove >> {} "+(isOk>0?"ok":"fail"));
				PrintWriter out = response.getWriter();
				out.print(isOk);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("remove error");
			}
			
			
			break; 
		case "modify": 
			try {
				//js 에서 json 으로 데이터 받는거 
						//js에서 보낸 데이터를 읽어들이는 작업 (js->controller) String 으로 보내서 StringBuffer로 받아야함
							StringBuffer sb = new StringBuffer();
							String line = null;
							//buffer읽어오는 reader(json읽음)/ 보낼 writer있어야함
							BufferedReader br= request.getReader(); //댓글 객체
							
							while((line = br.readLine()) != null) { //한줄씩 읽음
								sb.append(line); //line에 한줄씩 읽은 거 추가
							}
							log.info(">>>>>String Buffer<<<<<"+sb.toString());
						// commentVO객체로 생성
							JSONParser parser = new JSONParser(); //sb.toString 값을 parsing 해서 (text)->json 변경 
							JSONObject jsonObj = (JSONObject)parser.parse(sb.toString()); //json object형태로 변환 //key:value 형태
				
				//key 를 이용햐여 value 를 추출
				int cno = Integer.parseInt(jsonObj.get("cno").toString());
				String content = jsonObj.get("content").toString();
				CommentVO cvo = new CommentVO(cno, content);
				log.info("commentVO >>> {} "+ cvo); //들어온거 확인
				
			//csv한테 등록하라고 보내기
				isOk=csv.modify(cvo);
				log.info("isOk >> "+((isOk>0)? "ok":"fail"));
				
			//결과데이터 전송 => 화면에 출력 (response 객체의 body에 기록)
				PrintWriter out = response.getWriter();
				out.print(isOk);
				
//				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("modify error");
			}
			break; 
//		case "": break; 
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

}
