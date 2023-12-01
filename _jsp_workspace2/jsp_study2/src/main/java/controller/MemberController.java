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

import domain.MemberVO;
import service.MemberService;
import service.MemberServiceImpl;


@WebServlet("/memb/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private RequestDispatcher rdp;
    private String destPage;
    private int isOk;
    private MemberService msv;
    
    public MemberController() {
    	msv = new MemberServiceImpl();
    }

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/")+1);
		
		log.info("> path : ",path);
		
		switch(path) {
		case "join":
			destPage = "/member/join.jsp";
			break;
		case "register" : 
			try {
				//jsp에서 보낸 파라미터 값 받기
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				String email = request.getParameter("email");
				int age = Integer.parseInt(request.getParameter("age"));
				
				//mvo객체 생성 service로 토스 (register)
				MemberVO mvo = new MemberVO(id,pwd,email,age);
				log.info("join check 1"+mvo);
				isOk = msv.register(mvo);
				log.info("join >>",(isOk>0)?"ok":"fail");
				
				//index 페이지로 이동
				destPage = "/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("register error");
			}
			break;
		case "login":
			try {
				String id = request.getParameter("id");
				String pwd = request.getParameter("pwd");
				
				MemberVO mvo = new MemberVO(id,pwd);
				
				MemberVO loginMvo = msv.login(mvo);
				log.info("login mvo : ",loginMvo);
				
				if(loginMvo != null) {
					HttpSession ses = request.getSession();
					ses.setAttribute("ses",loginMvo);
					ses.setMaxInactiveInterval(10*60);
				}else {
					request.setAttribute("msg_login", -1);
				}
				destPage="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("login error");
			}
			break;
		case "logout":
			
			try {
				HttpSession ses = request.getSession();
				MemberVO mvo = (MemberVO)ses.getAttribute("ses");
				isOk = msv.lastLogin(mvo.getId());
				ses.invalidate();
				destPage="/index.jsp";
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "list": 
			try {
				List<MemberVO>list = msv.getList();
				request.setAttribute("list", list);
				destPage = "/member/list.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "detail":
			try {
				destPage="/member/detail.jsp";				
			} catch (Exception e) {
				e.printStackTrace();
				log.info("detail error");
			}
			break;
		case "modify": 
			try {
				HttpSession ses=request.getSession();
				String id=request.getParameter("id");
				String pwd=request.getParameter("pwd");
				String email = request.getParameter("email");
				int age =Integer.parseInt(request.getParameter("age"));
				
				MemberVO mvo = new MemberVO(id,pwd,email,age);
				isOk=msv.modify(mvo);
				
				if(isOk>0) {
					request.setAttribute("msg_modify", -1);
				}
				ses.invalidate();
				destPage="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
				log.info("modify error");
			}
			break;
		case "remove": 
			try {
				HttpSession ses = request.getSession();
				String id=request.getParameter("id");
				isOk = msv.remove(id);
				ses.invalidate();
				destPage="/index.jsp";
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default: break;
		}
		
		rdp=request.getRequestDispatcher(destPage);
		rdp.forward(request, response);
	}
	

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		service(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		service(request, response);
	}

}
