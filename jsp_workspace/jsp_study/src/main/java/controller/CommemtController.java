package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.filters.SetCharacterEncodingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		String uri = request.getRequestURI();
		log.info("경로 >>"+uri);
		
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}

}
