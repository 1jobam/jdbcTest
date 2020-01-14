package tutorial;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import kr.or.ddit.util.DBUtil2;
import kr.or.ddit.util.DButil;

public class Exam07 {
	Scanner sc = new Scanner(System.in);
	private Connection conn;
	private Statement stat;
	private PreparedStatement psta;
	private ResultSet re;
	
	
	public static void main(String[] args) {
		new Exam07().start();
	}
	
	public void start() {
		String menu;
		
		do {
			System.out.println("======================================");
			System.out.println("게시판 관리 시스템에 오신것을 환영합니다.");
			System.out.println("--------------------------------------");
			System.out.println("1). 게시글 작성");
			System.out.println("2). 게시글 수정");
			System.out.println("3). 게시글 삭제");
			System.out.println("4). 게시글 검색");
			System.out.println("5). 게시글 전체 조회");
			System.out.println("6). 시스템 종료");
			System.out.println("======================================");
			System.out.print("메뉴를 선택하여 주세요 > ");
			menu = sc.nextLine();
			
			switch(menu) {
				case "1" :
					boardInsert();
					break;
				case "2" :
					boardUpdate();
					break;
				case "3" :
					boardDelete();
					break;
				case "4" :
					boardcheck();
					break;
				case "5" :
					boardAllSelect();
					break;
				case "6" :
					System.exit(0);
					break;
				default :
					System.out.println("잘못된 입력입니다.");
					break;
			}
			
		}while(!menu.equals("6"));
	}

	private void boardcheck() {
		System.out.println("======================================");
		System.out.println("게시글 검색을 진행하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("검색하시고 싶으신 게시글의 번호를 입력하여주세요 > ");
		int num = Integer.parseInt(sc.next());
		
		try {
			
			conn = DBUtil2.getConnection();
			
			String sql = " select * from jdbc_board where board_no = ?";
			
			psta = conn.prepareStatement(sql);
			
			psta.setInt(1, num);
			
			re = psta.executeQuery();
			
			sc.nextLine(); //버퍼 비우기
			
			while(re.next()) {
				System.out.println("게시글 번호 : " + re.getInt("board_no"));
				System.out.println("게시글 제목 : " + re.getString("board_title"));
				System.out.println("게시글 작성자 : " + re.getString("board_writer"));
				System.out.println("게시글 작성날짜 : " + re.getDate("board_date"));
				System.out.println("게시글 내용 : " + re.getString("board_content"));
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
		
	}

	private void boardUpdate() {
		System.out.println("======================================");
		System.out.println("게시글 수정을 진행하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("수정하실 게시글의 번호를 입력하여주세요 > ");
		int board_no = Integer.parseInt(sc.next());
		
		System.out.println("입력하신 게시글의 번호 : " + board_no + "입니다.");
		System.out.print("수정하실 게시글의 제목을 입력하여주세요 > ");
		String boardTitle = sc.next();
		System.out.print("수정하실 게시글의 작성자 이름을 입력하여주세요 > ");
		String boardwriter = sc.next();
		System.out.print("수정하실 게시글의 내용을 입력하여주세요 > ");
		String boardcontent = sc.next();
		
		try {
			conn = DBUtil2.getConnection();
			
			String sql = " update jdbc_board set board_title = ?, board_writer = ?, board_content = ? where board_no = ? ";
			
			psta = conn.prepareStatement(sql);
			
			psta.setString(1, boardTitle);
			psta.setString(2, boardwriter);
			psta.setString(3, boardcontent);
			psta.setInt(4, board_no);
			
			if(psta.executeUpdate() > 0) {
				sc.nextLine(); // 버퍼 비우기
				System.out.println("게시글 수정에 성공하셨습니다...");
			}else {
				sc.nextLine(); // 버퍼 비우기
				System.out.println("게시글 수정에 실패하셨습니다!!!");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
	}

	private void boardDelete() {
		System.out.println("======================================");
		System.out.println("게시글 삭제를 진행 하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("삭제하실 게시글 번호를 입력하여주세요 > ");
		int board_no = Integer.parseInt(sc.next());
		
		try {
			
			conn = DBUtil2.getConnection();
			
			String sql = " delete from jdbc_board where board_no = ? ";
			
			psta = conn.prepareStatement(sql);
			
			psta.setInt(1, board_no);
			
			if(psta.executeUpdate() > 0) {
				sc.nextLine();
				System.out.println("삭제가 성공적으로 진행되었습니다...");
			}else {
				sc.nextLine();
				System.out.println("삭제가 진행되지 않았습니다!!!");
				return;
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
	}
	
	private void boardcheck(int board_no) {
		
		int chk;
		
		try {
			conn = DBUtil2.getConnection();
			
			String sql = " select * from jdbc_board where board_no = ? ";
			
			psta = conn.prepareStatement(sql);
			
			re = psta.executeQuery();
			
			int boardno = board_no;
			
			psta.setInt(1, boardno);
			
			while(re.next()) {
				int boardNo = re.getInt("board_no");
				if(boardno == boardNo) {
					chk = 1;
				}else {
					chk = 0;
				}
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
	}
	

	private void boardInsert() {
		System.out.println("======================================");
		System.out.println("게시글 등록을 진행 하겠습니다.");
		System.out.println("--------------------------------------");
		System.out.print("등록하실 게시글 제목을 입력하여주세요 > ");
		String boardtitle = sc.next();
		
		System.out.print("등록하실 게시글의 작성자를 입력하여주세요 > ");
		String boardwriter = sc.next();
		
		System.out.print("등록하실 게시글의 내용을 입력하여주세요 > ");
		String boardcontent = sc.next();
		
		try {
			conn = DBUtil2.getConnection();
			
			String sql = " insert into jdbc_board (board_no, board_title, board_writer, board_date, board_content) values (board_seq.nextVal, ?, ?, sysdate, ?) ";
			
			psta = conn.prepareStatement(sql);
			
			psta.setString(1, boardtitle);
			psta.setString(2, boardwriter);
			psta.setString(3, boardcontent);
			
			int cnt = psta.executeUpdate();
			
			if(cnt > 0) {
				System.out.println("--------------------------------------");
				System.out.println("게시판 생성에 성공하셨습니다...");
				sc.nextLine();
			}else {
				System.out.println("--------------------------------------");
				System.out.println("게시판 생성을 실패하셨습니다!!!");
				sc.nextLine();
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
	}

	private void boardAllSelect() {
		System.out.println();
		System.out.println("======================================");
		System.out.println("등록된 게시글을 전체 조회 하겠습니다.");
		
		try {
		conn = DBUtil2.getConnection();
		
		stat = conn.createStatement();
		
		String sql = " select * from jdbc_board ";
		
		re = stat.executeQuery(sql);
		
		while(re.next()) {
			System.out.println("--------------------------------------");
			System.out.println("게시글 번호 : " + re.getInt("board_no"));
			System.out.println("게시글 제목 : " + re.getString("board_title"));
			System.out.println("게시글 작성자 : " + re.getString("board_writer"));
			System.out.println("게시글 작성날짜 : " + re.getDate("board_date"));
			System.out.println("게시글 내용 : " + re.getString("board_content"));
		}
		System.out.println("======================================");
		System.out.println("등록된 게시글 출력 완료...");
		
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
		
	}

	private void disconnect(){
		//자원 반납용
		if(re != null) try {re.close();} catch(SQLException e2) {}
		if(psta != null) try {psta.close();} catch(SQLException e2) {}
		if(stat != null) try {stat.close();} catch(SQLException e2) {}
		if(conn != null) try {conn.close();} catch(SQLException e2) {}

	}
}
