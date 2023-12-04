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
import service.MemberService;
import service.MemberServiceImpl;

@WebServlet("/memb/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// 로그객체 (import주의)
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	// rdp
	private RequestDispatcher rdp;
	// destPage(주소설정 )
	private String destPage;
	// isOk
	private int isOk;
	// service(실제 일하는 객체)
	private MemberService msv;// service->인터페이스로 생성

	public MemberController() {
		msv = new MemberServiceImpl();// service->class로 생성
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 모든 서비스 처리 경로
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/") + 1);

		log.info(">> path >>" + path);

		switch (path) {
		case "join":
			// index의 /member/join경로
			destPage = "/member/join.jsp";
			break;
		case "register":
			try {
				// jsp에서 보낸 파라미터 값 받기
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				String emailString = request.getParameter("email");
				int age = Integer.parseInt(request.getParameter("age"));

				MemberVO mvo = new MemberVO(id, pwd, emailString, age);
				log.info(">>join check 1 " + mvo);
				isOk = msv.register(mvo);
				log.info("join >>", (isOk > 0) ? "ok" : "fail");
				destPage = "/index.jsp";

			} catch (Exception e) {
				log.info("join error");
				e.printStackTrace();
			}
			break;

		case "login":
			try {
				// id,pwd 를 jsp에서 전송 받음
				// id,pwd객체화하여 DB로 전송
				// 같은 이름의 id/pwd가 있다면 로그인 (세션객체에 값을 저장)
				// session : 모든 jsp에서 공유되는 객체
				// id가 없으면 , alert창을 이용하여 로그인 정보가 없습니다. 메시지 띄우기.
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");

				MemberVO mvo = new MemberVO(id, pwd);

				log.info("login check 1");
				// id,pwd가 일치하는 데이터의 객체를 리턴받을것
				MemberVO loginMvo = msv.login(mvo);
				log.info("login mvo >>" + loginMvo);

				// 로그인 객체가 있음을 의미-만약 로그인 객체가 없다면 loginMvo = null
				// 가져온 로그인 객체를 세션에 저장
				if (loginMvo != null) {
					// 세션 가져오기
					// 연결된 세션 객체가 있다면 기존 객체 가져오기, 없으면 생성
					HttpSession ses = request.getSession();
					ses.setAttribute("ses", loginMvo);// 세션에 담기
					ses.setMaxInactiveInterval(10 * 60);// 로그인 유지시간 (초단위설정)
				} else {
					// 로그인 객체가 없다면
					request.setAttribute("msg_login", -1);
				}
				destPage = "/index.jsp";

			} catch (Exception e) {
				e.printStackTrace();
				log.info("login error");

			}

			break;
		case "logout":
			try {
				// 세션에 값이 있다면 해당 세션 연결 끊기
				HttpSession ses = request.getSession(); // 로그인한 정보가져옴
				// 로그인 끊기 전에 lastlogin 정보 업데이트해줘야함 (로그인할때 해도 상관없음)
				// ses에서 mvo객체로 가져오기
				MemberVO mvo = (MemberVO) ses.getAttribute("ses"); // 형변환! object로 던져진 값을 형변환해서 쓰기
				log.info("ses에서 추출한 mvo >>{}", mvo);
				// lastlogin update
				isOk = msv.lastLogin(mvo.getId());
				log.info("lastlogin >>", isOk > 0 ? "ok" : "fail");
				// 세션 무효화
				ses.invalidate();
				destPage = "/index.jsp";

			} catch (Exception e) {
				e.printStackTrace();
				log.info("logout error");
			}
			break;
		case "list":
			try {
				List<MemberVO> list = msv.getList();
				//리스트가 오면 리스트 리턴
				request.setAttribute("list", list);
				destPage="/member/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("list error");
			}
			break;
		case "detailMove" : 
			destPage = "/member/detail.jsp";
			break;
		case "detail" : 
			try {
//				현재 사용자의 정보 띄우기
				
//				String id=request.getParameter("id");
//				String pwd=request.getParameter("pwd");
//				String email=request.getParameter("email");
//				int age= Integer.parseInt(request.getParameter("age")) ;
//				
//				MemberVO mvo = new MemberVO(id,pwd,email,age);
//				
//				isOk = msv.getDetail(mvo);
//				log.info("detail >>", isOk > 0 ? "ok" : "fail");
//				수정 누르면 db에 정보 변경되야함 <못했음>
				destPage = "/member/detail.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("detail error");
			}
			break;
		case "modify":
			try {
				HttpSession ses = request.getSession();
				String id=request.getParameter("id");
				String pwd=request.getParameter("pwd");
				String email=request.getParameter("email");
				int age= Integer.parseInt(request.getParameter("age")) ;
				
				MemberVO mvo = new MemberVO(id,pwd,email,age); //id-where의 조건/나머지 변경될값
				isOk = msv.modify(mvo);
				log.info("modify >>", isOk > 0 ? "ok" : "fail");
				
				if(isOk > 0) {
					request.setAttribute("msg_modify", -1);
				}
				ses.invalidate();
				destPage="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("modify error");
			}
			
			break;
		case "remove" : 
			try {
				//id해당 유저를 탈퇴(삭제후 세션 끊기)
				HttpSession ses = request.getSession(); //현재 로그인된 정보
				//jsp에서 쿼리스트링으로 값을 달고옴
				String id=request.getParameter("id");
				log.info("remove check1");
				isOk = msv.remove(id);
				ses.invalidate();
				destPage = "/index.jsp";
				
				
				//값 가져옴 object로 가져와서 membervo로 형변환
//				MemberVO mvo = (MemberVO)ses.getAttribute("ses");
//				String id=mvo.getId();
//				isOk = msv.remove(id);
				
				//세션 끊고 index로 이동
//				ses.invalidate();
//				if(isOk>0) {
//					request.setAttribute("msg_remove", );
//				}->alert  회원 탈퇴 메시지 띄움
//				destPage = "/index.jsp";
			} catch (Exception e) {
				log.info("remove error");
				e.printStackTrace();
				
			}
			break;
			

		}
		// 목적지 주소 값 설정
		rdp = request.getRequestDispatcher(destPage);
		// 목적지 주소로 전송(정보 싣어서 보내기)
		rdp.forward(request, response);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		service(request, response);
	}

}
