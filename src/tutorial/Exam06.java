package tutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Exam06 {
	
	Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("********************");
		System.out.println("호텔 문을 열었습니다.");
		System.out.println("********************");
		new Exam06().start();
	}
	
	public void start() {
		int menu;
		
		do {
			System.out.println("\n**********************************");
			System.out.println("어떤 업무를 하시겠습니까?");
			System.out.println("1.체크인\t2.체크아웃\t3.객실상태\t4.업무종료");
			System.out.println("**********************************");
			System.out.print("메뉴선택 => ");
			menu = Integer.parseInt(sc.nextLine());
			
			switch(menu) {
			case 1 :
				checkin();
				break;
			case 2 :
				checkout();
				break;
			case 3 :
				roomlist();
				break;
			case 4 :
				System.out.println("********************");
				System.out.println("호텔 문을 닫았습니다.");
				System.out.println("********************");
				System.exit(0);
				break;
			}
		}while(menu != 0);
	}
	
	
	
	public void checkin() {
		int number = 0;
		String name = null;
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pre = null;
		ResultSet res = null;
		
		
		System.out.println("어느방에 체크인 하시겠습니다.");
		System.out.print("방번호 입력 => ");
		number = Integer.parseInt(sc.nextLine());
		System.out.println("누구를 체크인 하시겠습니까?");
		System.out.print("이름 입력 => ");
		name = sc.nextLine();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC02";
			String password = "java";
		
		con = DriverManager.getConnection(url, userId, password);
		
		String sql = " insert into hotel_mng " + " (room_num, guest_name) " + " values " + " (?, ?) ";
		String sql2 = " select * from hotel_mng where room_num = " + number;
		
		stmt = con.createStatement();
		pre = con.prepareStatement(sql);
		res = stmt.executeQuery(sql2);
		
		while(res.next()) {
			if(res.getInt("room_num") == number) {
				System.out.println(number + "방에는 이미 사람이 있습니다.");
				return;
			
			}
		}
		
		pre.setInt(1, number);
		pre.setString(2, name);
		
		int cnt = pre.executeUpdate();
		if(cnt == 1) {
			System.out.println(name + "님 체크인 되었습니다.");	
		}
		
		}catch(ClassNotFoundException e){
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(res != null) try {res.close();}catch(SQLException e2) {}
			if(stmt != null) try {stmt.close();}catch(SQLException e2) {}
			if(pre != null) try {pre.close();}catch(SQLException e2) {}
			if(con != null) try {con.close();}catch(SQLException e2) {}
		}
	}

	public void checkout() {
		int number;
		Connection con = null;
		PreparedStatement pre = null;
		
		System.out.println("어느방을 체크아웃 하시겠습니까?");
		System.out.print("방번호 입력 =>");
		number = Integer.parseInt(sc.nextLine());
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC02";
			String password = "java";
		
		con = DriverManager.getConnection(url, userId, password);
		
		//지우기
		String sql = " delete from hotel_mng where room_num = " + number;
		
		pre = con.prepareStatement(sql);
		
		if(pre.executeUpdate() == 0) {
			System.out.println(number + "방에는 체크인한 사람이 없습니다.");
			return;
		}else {
			System.out.println("체크아웃 되었습니다.");
		}
		
		}catch(ClassNotFoundException e){
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(pre != null) try {pre.close();}catch(SQLException e2) {}
			if(con != null) try {con.close();}catch(SQLException e2) {}
		}
	}	
	
	public void roomlist() {
		Connection con = null;
		Statement sta = null;
		ResultSet res = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC02";
			String password = "java";
			
			con = DriverManager.getConnection(url, userId, password);
			
			sta = con.createStatement();
			
			String sql = "select * from hotel_mng";
		
			res = sta.executeQuery(sql);
			
			
			while(res.next()) {
				System.out.println("방번호 : " + res.getInt("room_num") + ", 투숙객 : " + res.getString("guest_name"));
			}
		
		}catch(ClassNotFoundException e){
			System.out.println("드라이버 로딩 실패!!!");
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(res != null) try {res.close();}catch(SQLException e2) {}
			if(sta != null) try {sta.close();}catch(SQLException e2) {}
			if(con != null) try {con.close();}catch(SQLException e2) {}
		}
	}
	
}
		
