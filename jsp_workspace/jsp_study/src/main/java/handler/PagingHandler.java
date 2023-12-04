package handler;

import domain.PagingVO;

public class PagingHandler {

	//list하단에 나오는 페이지 네이션 핸들링 클래스
	
	private int startPage; //현재 화면에서 보여줄 시작 페이지네이션 번호 => 1페이지에서 1번/2페이지에서 11번...
	private int endPage; //현재 화면에서 보여줄 끝 페이지네이션 번호 =>1페이지에서 10번/2페이지에서 20번...
	private int realEndPage;// 실제 전체리스트의 끝페이지 번호
	private boolean prev,next; //이전,다음페이지의 존재 여부 true면 보이고 false면 안보임(1페이지에선 이전페이지가 안보임)
	
	//파라미터로 받기
	private int totalCount; //전체 게시글 수 (db조회 필요)
	private PagingVO pgvo; //pageNo / qty =>현재사용자가 클릭한 번호와 한 화면에 표시되는 게시글 갯수
	
	public PagingHandler(PagingVO pgvo, int totalCount) {//비어있는 생성자 만들지 않음-오류남
		this.pgvo=pgvo; //기본 값 : 1페이지 게시글 10개
		//컨트롤러에서 DB 조회 후 파라미터로 전송된 게시글 전체의 갯수
		this.totalCount = totalCount; 
		
		//한 페이지에 1-10까지의 게시글이 들어가게
		// 페이지 번호 1-10 을 클릭해도 시작은 1, 끝은 10
		//내가 선택한 번호 :01 =>끝 번호 :10 / 내가 선택한 번호 :02 =>끝 번호 :10 /내가 선택한 번호 :11 =>끝 번호 :20
		//(페이지 번호 / 한 화면의 페이지네이션 수)*한 화면의 페이지네이션 수 => 1/10=0.1 =>(1로 올림 변환)*10 =>10  
		// 2/10 =0.2 =>(1로 올림)*10 =>10
		// 11/10 =1.1 =>(2로 올림)*10 =>20
		// 앞자리 숫자 기준 0->끝번호 10 
		//(정수/정수)=>정수 >>소수점 나오게 double로 형변환해주기 + int로 마지막에 리턴
		this.endPage = (int)Math.ceil(pgvo.getPageNo() / (double)pgvo.getQty())*pgvo.getQty() ;
		this.startPage = this.endPage -9; //끝페이지에서 -9 => 1
		
		//전체게시글 수 / 한 화면에 게시되는 게시글 수 
		//101개 / 10 =>10.1페이지가 필요  올림해서 11페이지가 나와야함
		//나머지 게시글이 하나라도 있다면 1페이지가 더 생겨야함
		//소수점 처리!! 형변환 맞추기 + int로 마지막에 리턴
		this.realEndPage=(int)Math.ceil(totalCount / (double)pgvo.getQty());
		
		//realEndPage = 11 / endPage =20 일치하지 않음 (만약 실제 페이지가 끝페이지보다 작으면 게시글의 끝페이지는 realEndPage로 바꿔줘)
		//진짜 끝 페이지가 endpage와 같지 않을 경우 처리
		if(this.realEndPage<this.endPage) {
			this.endPage = this.realEndPage;
		}
		
		//이전, 다음 유무
		//이전 버튼 : startPate 1,11,21... 시작페이지가 1일때 안나와야함(시작페이지가 1이상이면 무조건 나옴)
		this.prev = this.startPage > 1;
		//다음 버튼 
		this.next = this.endPage < this.realEndPage;
			
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getRealEndPage() {
		return realEndPage;
	}

	public void setRealEndPage(int realEndPage) {
		this.realEndPage = realEndPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public PagingVO getPgvo() {
		return pgvo;
	}

	public void setPgvo(PagingVO pgvo) {
		this.pgvo = pgvo;
	}

	@Override
	public String toString() {
		return "PagingHandler [startPage=" + startPage + ", endPage=" + endPage + ", realEndPage=" + realEndPage
				+ ", prev=" + prev + ", next=" + next + ", totalCount=" + totalCount + ", pgvo=" + pgvo + "]";
	}
	
	
}
