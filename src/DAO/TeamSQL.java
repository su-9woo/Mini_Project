package DAO;

import java.util.Scanner;

import DTO.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamSQL {


    Connection con;
    PreparedStatement pstmt;
    ResultSet rs;


    public void connect() {
        con = DBC.DBConnect();
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void foodRes(int menu) {
        connect();
        String sql = "SELECT * FROM STORES WHERE CNUM = ?";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, menu);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.print(rs.getInt(1) + "." + rs.getString(2));
                System.out.println(" 주소 : " + rs.getString(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    // 회원가입 메소드
    public void registermembers(MEMBERS members) {
        connect();
        String sql = "INSERT INTO MEMBERS (MID, MPW, MNAME, PHONE, ADDR) VALUES (?, ?, ?, ?, ?)";
        String sql1 = "INSERT INTO ORDERS (ORDERID, MID, TOTALPRICE, FNUM) VALUES (?, ?, ?, ?)";

        try {
            // 전역 변수 pstmt 사용
            pstmt = con.prepareStatement(sql);

            // PreparedStatement에 각 파라미터 설정
            pstmt.setString(1, members.getMID());
            pstmt.setString(2, members.getMPW());
            pstmt.setString(3, members.getMNAME());
            pstmt.setString(4, members.getPHONE());
            pstmt.setString(5, members.getADDR());

            // SQL 실행
            int result = pstmt.executeUpdate();

            // 결과
            if (result > 0) {
                System.out.println("\n회원가입이 성공적으로 완료되었습니다.");

                // 회원가입 후 ORDERS 테이블에 주문 추가
                // ORDERID 생성
                String orderId = generateOrderId(); // 주문 번호 생성 메소드 호출
                pstmt = con.prepareStatement(sql1);
                pstmt.setString(1, orderId);
                pstmt.setString(2, members.getMID()); // MID 설정
                pstmt.setDouble(3, 0); // 기본값으로 TOTALPRICE 설정 (0)
                pstmt.setNull(4, java.sql.Types.INTEGER); // FNUM은 null로 설정 (선택사항)

                int orderResult = pstmt.executeUpdate();

                if (orderResult > 0) {

                } else {
                    System.out.println("장바구니 생성에 실패하였습니다.");
                }
            } else {
                System.out.println("회원가입에 실패하였습니다. 다시 시도해주세요");
            }

        } catch (SQLException e) {
            System.out.println("데이터베이스 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    // 로그인 메소드
    public MEMBERS loginMembers(MEMBERS members) {
        connect();
        String sql = "SELECT * FROM MEMBERS WHERE MID = ?";   // 아이디로만 검색
        MEMBERS loggedInMembers = null;  // 로그인 된 사용자 정보를 저장할 변수 loggedInMembers

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, members.getMID());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 아이디로 사용자를 찾았을 때 비밀번호 비교
                String storedPassword = rs.getString("MPW");
                if (storedPassword.equals(members.getMPW())) {
                    // 로그인 성공 시 MEMBERS 객체에 데이터 설정
                    loggedInMembers = new MEMBERS();
                    loggedInMembers.setMID(rs.getString("MID"));
                    loggedInMembers.setMPW(storedPassword);
                    loggedInMembers.setMNAME(rs.getString("MNAME"));
                    loggedInMembers.setPHONE(rs.getString("PHONE"));
                    loggedInMembers.setADDR(rs.getString("ADDR"));
                    System.out.println("로그인에 성공했습니다");
                } else {
                    System.out.println("비밀번호를 확인해주세요");
                }
            } else {
                System.out.println("아이디가 존재하지 않습니다");
            }
        } catch (SQLException e) {
            System.out.println("로그인 오류 발생" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            close();
        }
        return loggedInMembers;
    }   // end loginMembers

    // 입금에 관한 메소드
    public void deposit(String MID, int amount) {
        // SQL 쿼리: 입금액을 계좌의 현재 잔액에 더하는 업데이트 쿼리
        String sqlSelect = "SELECT BALANCE FROM MEMBERS WHERE MID = ?";
        String sqlUpdate = "UPDATE MEMBERS SET BALANCE = BALANCE + ? WHERE MID = ?";

        try {
            connect(); // 데이터베이스 연결

            // 현재 잔액 조회
            pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, MID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int currentBalance = rs.getInt("BALANCE"); // 현재 잔액 가져오기
                System.out.println("현재 잔액: " + currentBalance);

                // 잔액 업데이트
                pstmt = con.prepareStatement(sqlUpdate);


                pstmt.setInt(1, amount); // 입금할 금액 설정
                pstmt.setString(2, MID); // 아이디 불러오기

                int result = pstmt.executeUpdate(); // 업데이트 실행

                if (result > 0) {
                    System.out.println("충전이 성공적으로 완료되었습니다.");
                    System.out.println("충전 후 잔액: " + (currentBalance + amount)); // 새 잔액 출력
                } else {
                    System.out.println("충전에 실패했습니다.");
                }
            } else {
                System.out.println("아이디를 찾을수 없습니다.");
            }

        } catch (SQLException e) {
            // SQL 예외 처리
            System.out.println("충전 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
    }


    // 출금 메소드
    public void pay(String MID, int TOTALPRICE) {
        connect(); // 데이터베이스 연결
        Scanner sc = new Scanner(System.in);

        String sqlSelect = "SELECT BALANCE FROM MEMBERS WHERE MID = ?";
        String sqlUpdate = "UPDATE MEMBERS SET BALANCE = BALANCE - ? WHERE MID = ?";

        try {

            // 현재 잔액 조회
            pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, MID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int currentBalance = rs.getInt("BALANCE"); // 현재 잔액 가져오기
                System.out.println("현재 잔액: " + currentBalance);

                // 출금액이 현재 잔액보다 큰 경우
                if (TOTALPRICE > currentBalance) {
                    System.out.println("잔액이 부족하여 출금할 수 없습니다.");
                    return;
                }

                // 잔액 업데이트
                pstmt = con.prepareStatement(sqlUpdate);
                pstmt.setInt(1, TOTALPRICE); // 출금할 금액 설정
                pstmt.setString(2, MID); // 계좌 번호 설정

                int result = pstmt.executeUpdate(); // 업데이트 실행

                if (result > 0) {
                    System.out.println("결제가 성공적으로 완료되었습니다.");
                    System.out.println("결제 후 잔액: " + (currentBalance - TOTALPRICE)); // 잔액 출력
                } else {
                    System.out.println("결제에 실패했습니다.");
                }
            } else {
                System.out.println("아이디를 찾을수 없습니다.");
            }

        } catch (SQLException e) {
            // SQL 예외 처리
            System.out.println("결제 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
    }

    // 데이터베이스에서 회원 정보를 아이디로 조회하는 메서드
    public MEMBERS findMemberById(String MID) {
        connect();
        MEMBERS members = new MEMBERS();
        String sql = "SELECT * FROM MEMBERS WHERE MID = ?";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, MID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("MID");            // 아이디
                String NAME = rs.getString("MNAME");        // 이름
                String PHONE = rs.getString("PHONE");     // 전화번호
                String ADDR = rs.getString("ADDR");      // 주소
                int BALANCE = rs.getInt("BALANCE");     // 잔액
                System.out.println( "┌───────────────────────[[ 회원정보 ]]─────────────────────┐");
                System.out.println("  회원 정보");
                System.out.println("  아이디 : " + ID);
                System.out.println("  이름 : " + NAME);
                System.out.println("  연락처 : " + PHONE);
                System.out.println("  주소 : " + ADDR);
                System.out.println("  잔액 : " + BALANCE);
                System.out.println( "└─────────────────────────────────────────────────────────┘");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
        return members;
    }

    public List<String> getKoreanRestaurants() {    // 한식
        connect();
        String sql = "SELECT * FROM STORES WHERE CNUM = 1";
        List<String> restaurants = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String restaurantInfo = rs.getString(2) + " 주소: " + rs.getString(3);
                restaurants.add(restaurantInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
        return restaurants;

    }

    public List<String> getChinaRestaurants() {    // 중식
        connect();
        String sql = "SELECT * FROM STORES WHERE CNUM = 2";
        List<String> restaurants = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String restaurantInfo = rs.getString(2) + " 주소: " + rs.getString(3);
                restaurants.add(restaurantInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
        return restaurants;
    }

    public List<String> getWestRestaurants() {    // 양식
        connect();
        String sql = "SELECT * FROM STORES WHERE CNUM = 3";
        List<String> restaurants = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String restaurantInfo = rs.getString(2) + " 주소: " + rs.getString(3);
                restaurants.add(restaurantInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
        return restaurants;
    }

    public FOOD getFood(int SNUM) {    // 이건 그 메뉴 가져온거임
        connect();
        FOOD FOOD = new FOOD();

        String sql = "SELECT * FROM Food WHERE SNUM = ?";


        try {

            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, SNUM);
            rs = pstmt.executeQuery();
            System.out.println( "\n┌────────────────────────[[ 주문 ]]──────────────────────┐");

            while (rs.next()) {
                String restaurantInfo = "  "+rs.getString(2) + " 가격: " + rs.getString(3);
                System.out.println(restaurantInfo);
            }
            System.out.println( "└────────────────────────────────────────────────────────┘");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
        return FOOD;
    }


    public FOOD getFoodPrice(int koreaChoice, String FOODNAME) {
        connect();
        FOOD FOOD = new FOOD();


        String sql = "SELECT * FROM Food WHERE SNUM = ? and FOODNAME = ?";


        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, koreaChoice);
            pstmt.setString(2, FOODNAME);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("음식 번호 :" + rs.getInt(1));
                System.out.println("음식 이름 :" + rs.getString(2));
                System.out.println("음식 가격 :" + rs.getInt(3));
                System.out.println("가게 번호 :" + rs.getInt(4));


            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return FOOD;
    }

    public int getTOTALPRICE(String MID) {
        connect();
        String sql = "SELECT TOTALPRICE FROM ORDERS WHERE MID = ? and fnum is null";
        int totalPrice = 0; // 총금액을 저장할 변수

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, MID);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalPrice = rs.getInt("TOTALPRICE"); // 컬럼 이름을 사용하여 총금액 가져오기
                System.out.println(totalPrice);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 리소스 해제
            close();
        }

        return totalPrice; // 총금액 반환
    }

    public ORDERS getPrice(int koreaChoice, String FOODNAME, String MID) {
        connect();
        String sqltotal = "SELECT * FROM ORDERS WHERE MID = ? and fnum is null";
        String sql = "SELECT * FROM Food WHERE SNUM = ? and FOODNAME = ?";
        String sqlup = "UPDATE ORDERS SET TOTALPRICE = TOTALPRICE + ? WHERE MID = ? and fnum is null";

        try {
            // 장바구니에서 현재 총금액 조회
            pstmt = con.prepareStatement(sqltotal);
            pstmt.setString(1, MID);
            rs = pstmt.executeQuery();

            if (rs.next()) { // 결과가 있는지 확인
                int TOTALPRICE = rs.getInt(2); // 총금액 가져오기

                // 음식 정보 조회
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, koreaChoice);
                pstmt.setString(2, FOODNAME);
                rs = pstmt.executeQuery();

                if (rs.next()) { // 음식 가격이 있는지 확인
                    int FOODPRICE = rs.getInt(3); // 음식 가격


                    // 장바구니 금액 업데이트
                    pstmt = con.prepareStatement(sqlup);
                    pstmt.setInt(1, FOODPRICE); // NUMBER 타입에 맞게 설정
                    pstmt.setString(2, MID);
                    int result = pstmt.executeUpdate(); // 업데이트 실행

                    if (result > 0) {
                        ORDERS ORDERS = new ORDERS();
                        int FINALTOTALPRICE = TOTALPRICE + FOODPRICE;
                        ORDERS.setTOTALPRICE(FINALTOTALPRICE);
                        System.out.println("장바구니의 총금액: " + (FINALTOTALPRICE)); // 새 장바구니 총금액 출력

                    } else {
                        System.out.println("장바구니에 들어간 메뉴가 없습니다."); // 여기서 문제가 발생
                        // 추가 디버깅 정보
                        System.out.println("현재 MID: " + MID); // MID 값 확인
                    }
                } else {
                    System.out.println("해당 음식이 존재하지 않습니다."); // 음식이 없는 경우
                }
            } else {
                System.out.println("장바구니에 해당 MID가 없습니다."); // MID가 없는 경우
            }

        } catch (SQLException e) {
            System.out.println("장바구니 업데이트 중 오류 발생");
            e.printStackTrace(); // 오류의 상세한 정보 출력
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }

        return null;
    }

    // 토핑
    // CNUM에 해당하는 토핑 가격을 특정 MID의 ORDERS 테이블의 TOTALPRICE에 추가하는 메소드
    public void getToping(int CNUM, String MID) {


        connect();

        // SQL 쿼리문
        String selectSql1 = "SELECT * FROM TOPING WHERE CNUM = ?";
        String selectSql2 = "SELECT PRICE FROM TOPING WHERE CNUM = ? AND TNAME = ?";
        String updateSql = "UPDATE ORDERS SET TOTALPRICE = TOTALPRICE + ? WHERE MID = ? and FNUM is  null"; // MID로 특정 주문 업데이트

        int totalTopingPrice = 0;

        try {
            // CNUM에 해당하는 모든 토핑 조회
            pstmt = con.prepareStatement(selectSql1);
            pstmt.setInt(1, CNUM);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String topingName = rs.getString("TNAME");
                int topingPrice = rs.getInt("PRICE");
                System.out.println(topingName + " 가격: " + topingPrice);
            }
            Scanner sc = new Scanner(System.in);

            System.out.print("추가하실 사이드 이름을 입력해주세요: ");
            String TNAME = sc.next(); // TNAME 입력받기


            // 토핑 가격 조회 (TNAME을 사용하여)
            pstmt = con.prepareStatement(selectSql2);
            pstmt.setInt(1, CNUM);
            pstmt.setString(2, TNAME); // TNAME 설정
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int topingPrice = rs.getInt("PRICE");
                System.out.println(TNAME + " 가격: " + topingPrice);
                totalTopingPrice = topingPrice; // 가격 저장
            } else {

                return; // 더 이상 진행하지 않음
            }

            // ORDERS 테이블의 TOTALPRICE 업데이트
            if (totalTopingPrice > 0) {
                pstmt = con.prepareStatement(updateSql);
                pstmt.setInt(1, totalTopingPrice);
                pstmt.setString(2, MID); // MID 설정
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("회원 ID " + MID + "의 총 금액에 사이드 가격 : " +totalTopingPrice + "원 입니다.  성공적으로 추가되었습니다.");
                } else {
                    System.out.println("업데이트할 주문이 없습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
    }


    public void reset(String MID) {
        connect();
        String sqlUpdate = "UPDATE ORDERS SET TOTALPRICE = 0 WHERE MID = ?";

        try {
            // 장바구니 금액 초기화
            pstmt = con.prepareStatement(sqlUpdate);
            pstmt.setString(1, MID); // MID 설정
            pstmt.executeUpdate(); // 업데이트 실행
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            close();
        }
    }

    // 주문번호 생성 메소드
    private String generateOrderId() {
        // 여기서는 랜덤한 1부터 시작되는 정수형 주문 번호를 생성합니다.
        // 실제로는 데이터베이스에서 마지막 주문 번호를 조회하여 +1 해주는 방식이 더 안전합니다.
        return String.valueOf(System.currentTimeMillis()); // 예시로 현재 시간 밀리초를 사용
    }

    public int getFP(int koreaChoice, String foodname) {
        connect();
        int result = 0;

        String sql = "SELECT foodprice FROM FOOD WHERE SNUM = ? AND FOODNAME LIKE ?";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, koreaChoice);
            pstmt.setString(2, "%" + foodname + "%");

            rs = pstmt.executeQuery();

            if(rs.next()){
                result = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int getfNum(int koreaChoice, String foodname) {
        connect();
        int result = 0;

        String sql = "SELECT fnum FROM FOOD WHERE SNUM = ? AND FOODNAME LIKE ?";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, koreaChoice);
            pstmt.setString(2, "%" + foodname + "%");

            rs = pstmt.executeQuery();

            if(rs.next()){
                result = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public void insertJang(int fnum, int price, String mid) {
        String sql = "insert into orders values (?,?,?,?)";

        try {
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, generateOrderId());
            pstmt.setInt(2, price);
            pstmt.setString(3, mid);
            pstmt.setInt(4, fnum);
            
            int result = pstmt.executeUpdate();
            
            if(result > 0){
                System.out.println("장바구니 등록완료");
            } else {
                System.out.println("장바구니 등록 실패");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void Jang(String MID) {
        connect();
        String sql = "SELECT * FROM ORDERS where mid = ? and fnum is not null";

        ArrayList<ORDERS> ors = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,MID);
            rs = pstmt.executeQuery();

            while (rs.next()){
                ORDERS or = new ORDERS();

                or.setORDERID(rs.getString(1));
                or.setTOTALPRICE(rs.getInt(2));
                or.setMID(rs.getString(3));
                or.setFNUM(rs.getInt(4));
                ors.add(or);
            }

            for(ORDERS or : ors){
                System.out.println(or);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetJang(String MID) {
        connect();
        String sql = "DELETE FROM ORDERS WHERE mid = ? AND fnum IS NOT NULL";

        ArrayList<ORDERS> ors = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,MID);
            rs = pstmt.executeQuery();

            while (rs.next()){
                ORDERS or = new ORDERS();

                or.setORDERID(rs.getString(1));
                or.setTOTALPRICE(rs.getInt(2));
                or.setMID(rs.getString(3));
                or.setFNUM(rs.getInt(4));
                ors.add(or);
            }

            for(ORDERS or : ors){
                System.out.println(or);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



