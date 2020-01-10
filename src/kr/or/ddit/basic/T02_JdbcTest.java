package kr.or.ddit.basic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class T02_JdbcTest {
/*
	문제1) 사용자로부터 lprod_id 값을 입력받아 입력한 값보다
		 lprod_id가 큰 자료들을 출력하시오.
		 
	문제2) lprod_id값을 2개 입력받아 두 값 중 작은 값부터 큰값 사이의
		  자료를 출력하시오.
 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		int id;
		int id2;
		
//		System.out.print("lprod_id 몇 초과로 조회하실지 \n입력 > ");
//		id = Integer.parseInt(sc.nextLine());
		System.out.print("lprod_id 몇부터 몇까지 조회하실지 \n 입력 >");
		id = Integer.parseInt(sc.nextLine());
		System.out.print(" 입력 > ");
		id2 = Integer.parseInt(sc.nextLine());
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521/xe";
			String userId = "PC02";
			String password = "java";
			
			conn = DriverManager.getConnection(url, userId, password);
			
			stat = conn.createStatement();
			
//			String sql = "select * from lprod WHERE lprod_id > " + id;
			String sql2 = "select * from lprod where lprod_id between " + id + " and " + id2;
			
			res = stat.executeQuery(sql2);
			
			System.out.println("실행한 쿼리문 : " + sql2);
			System.out.println("=== 쿼리문 실행결과 ===");
			
			
//			int max, min;
//			if(num1 > num2) {
//				max = num1;
//				min = num2;
//			}else {
//				max = num2;
//				min = num1;
//			}
//			max = Math.max(num1, num2);
//			min = Math.min(num1, num2);
			
			while(res.next()) {
				System.out.println("lprod_id : " + res.getInt("lprod_id"));
				System.out.println("lprod_gu : " + res.getString("lprod_gu"));
				System.out.println("lprod_nm : " + res.getString("lprod_nm"));
				System.out.println("-------------------------------------");
			}
			System.out.println("출력 끝...");
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(res != null) try {res.close();} 
					catch(SQLException e2) {}
			if(stat != null) try {stat.close();} 
					catch(SQLException e2) {}
			if(conn != null) try {conn.close();} 
					catch(SQLException e2) {}
		}
		
		
		
		
	}
}
