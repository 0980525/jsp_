console.log("board_detail.js in");
console.log(bnoVal);

//컨트롤러에서 리스트 요청 비동기 통신 
async function getCommentListFromServer(bno){
    try {
        const resp = await fetch("/cmt/list"+bno);
        const result = await resp.json();
        return result;
    } catch (error) {
        console.log(error);
    }
}

//리스트뿌리기 리스너
function spreadCommentList(result){ //result => 댓글 리스트
    console.log(result);
    let div = document.getElementById("commentline");
    
    console.log(div);
    div.innerHTML="";//원래 만들어 뒀던 구조 지우기
    //값이 여러개 들어올 예정
    let writer = document.getElementById('cmtWriter').value;
   
    for(let i=0;i<result.length;i++){
        
        let html = `<div>`;
        html += `<div> ${result[i].cno}, ${result[i].bno}, ${result[i].writer}, ${result[i].regdate} </div>`;
        html += `<div>`;
        if(writer == result[i].writer){
        html += `<button type="button" data-cno=${result[i].cno} class="cmtModBtn"> 수정 </button>`; // 수정하기 위해서 cno 데이터값이 필요
        html += `<button type="button" data-cno=${result[i].cno} class="cmtDelBtn"> 삭제 </button> <br>`;
		}
        html += `<input type="text" value="${result[i].content}" class="cmtText">`;
        html += `</div> </div><br><hr>`;

        div.innerHTML += html; //각 댓글 객체를 누적해서 담기

       
    }  
    

}



function printCommentList(bno){
    getCommentListFromServer(bno).then(result=>{
        console.log(result);
            spreadCommentList(result);
        if(result.length > 0){
        }else{
            let div = document.getElementById("commentline");
            div.innerHTML = `<div>comment가 없습니다.</div>`
        }
    })
}

document.getElementById('cmtAddButtom').addEventListener('click',()=>{
    const cmtText=document.getElementById('cmtText').value;
    if(cmtText ==null || cmtText == ''){
        alert('댓글을 입력해주세요.');
        return false;
    }else{
        //댓글 등록
        let cmtDate = {
            bno:bnoVal,
            writer:document.getElementById('cmtWriter').value,
            content:cmtText
        };
        // 댓글 등록 비동기 통신 호출
        postCommentToServer(cmtDate).then(result=>{
            console.log(result);
            if(result > 0){
                alert('댓글 등록 성공 ~');
                document.getElementById('cmtText').value=""; //댓글등록후 입력된 input에 남은 값 지우기
            }

            printCommentList(bnoVal);
            
        })
    }
})

async function postCommentToServer(cmtData){
    try {
        const url="/cmt/post";
        const config={
            //method() , headers(바디 설정정보) , body(실제객체내용)
            method:'post',
            headers:{
                'Content-Type':'application/json; charset=utf-8'
            },
            body:JSON.stringify(cmtData)

        };
        const resp = await fetch(url, config);
        const result=await resp.text(); //isOk 값을 리턴
        return result;
    } catch (error) {
        console.log(error);
    }
}

//수정 : cno, content => result : isOk
async function updateCommentFromServer(cnoVal, cmtText){
    try {
        const url = "/cmt/modify";
        const config = {
            method:'post',
            headers:{
                'Content-Type':'application/json; charset=utf-8'
            },
            body:JSON.stringify({cno:cnoVal, content:cmtText})
        };
        const resp = await fetch(url, config);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}

//삭제 : cno => result : isOk
async function removeCommentFromServer(cnoVal){
    try {
        const url= '/cmt/remove?cnoVal='+cnoVal;
        const resp= await fetch(url);
        const result = await resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}

document.addEventListener('click',(e)=>{ //전체를 타겟으로 활성화
    console.log(e.target);
    //삭제//수정 버튼이 클릭되면 
    if(e.target.classList.contains('cmtDelBtn')){ //타겟에서 클래스리스트에 '삭제'버튼이 존재한다면
        let cnoVal = e.target.dataset.cno; //data-cno의 값을 추출
        console.log(cnoVal);
        removeCommentFromServer(cnoVal).then(result=>{
            //result = isOk
            if(result > 0){
                alert('댓글 삭제 성공~');
                printCommentList(bnoVal);
            }
        })
    }
    if(e.target.classList.contains('cmtModBtn')){
        let cnoVal = e.target.dataset.cno;
        console.log(cnoVal);
        //html에 있는 cmtText 가져오기
        let div = e.target.closest('div'); //타겟(cmtModBtn)을 기준으로 가장 가까운 div찾기
        let cmtText = div.querySelector('.cmtText').value; //해당 div의 querySelect로 .cmtText클래스를 가지고있는 value 값
        console.log("cmtText >>>"+cmtText);
        updateCommentFromServer(cnoVal,cmtText).then(result=>{ //result = isOk
            console.log(result);
            if(result > 0){
                alert('댓글 수정 성공~');
                printCommentList(bnoVal);
            }
        })
    }

})