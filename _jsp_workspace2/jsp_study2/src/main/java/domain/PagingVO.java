package domain;

public class PagingVO {

	private int pageNo;
	private int qty;
	private String type;
	private String keyword;
	
	public PagingVO() {}
	
	//페이지네이션을 클릭하면 설정되는 값
	public PagingVO(int pageNo, int qty, String type, String keyword) {
		this.pageNo=pageNo;
		this.qty = qty;
		this.type = type;
		this.keyword=keyword;
	}
	
	public int getPageStart() {
		return (pageNo-1)*qty;
	}
	
	public String[] getTypeToArray() {
		return this.type ==null ?
				new String[] {} : this.type.split("");
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "PagingVO [pageNo=" + pageNo + ", qty=" + qty + ", type=" + type + ", keyword=" + keyword + "]";
	}
	
	
}
