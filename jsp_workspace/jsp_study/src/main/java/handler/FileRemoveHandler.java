package handler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileRemoveHandler {

	private static final Logger log = LoggerFactory.getLogger(FileRemoveHandler.class);
	
	//파일이름과 경로를 받아 파일을 삭제하는 메서드
	//리턴타입 int => 삭제된다면 1, 아니면 0 
	
	public int deleteFile(String imageFileName, String savePath) {
		
		boolean isDel = false; //삭제가 잘 되었는지 체크하는 변수 
		log.info(">>>>deleteFile method 접근 완료"+imageFileName);

		File fileDir = new File(savePath);
		File removeFile = new File(fileDir+File.separator+imageFileName);
		File removeThumbFile = new File(fileDir+File.separator+"_th_"+imageFileName);
		
		//파일이 존재 해야 삭제
		if(removeFile.exists() || removeThumbFile.exists()) {
			isDel = removeFile.delete(); //delete() : 파일을 삭제하는 메서드, boolean으로 리턴
			log.info(">> fileRemove :"+(isDel ? "ok" : "fail"));
			if(isDel) {
				isDel = removeThumbFile.delete();
				log.info(">> fileThumbRemove :"+(isDel ? "ok" : "fail"));
			}
		}
		log.info("remove Ok");
		
		return isDel? 1 :0;
	}
	
	
	
	
	
}
