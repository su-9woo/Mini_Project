package SERVICE;

import DAO.TeamSQL;
import DTO.MEMBERS;

import DTO.FCATEGORY;
import DTO.FOOD;
import DTO.ORDERS;
import DTO.STORES;
import DTO.TOPING;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MembersService {


    // 정규식 패턴 정의
    private static final String MID_Pattern = "^[a-zA-Z0-9]{8,15}$";    // 아이디는 영문, 숫자 포함 8~15자리
    private static final String MPW_Pattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[&!@*])[a-zA-Z\\d&!@*]{8,15}$"; // 비밀번호는 영문, 숫자, 특수문자(&,!,@,*) 포함 8~15자리
    private static final String MNAME_Pattern = "^[가-힣]{2,5}$";     // 이름은 한글로만 최대 5자리까지
    private static final String PHONE_Pattern = "^\\d{10,11}$";      // 연락처는 숫자만 가능(ex. 01012345678, 0212345678)
    private static final String ADDR_Pattern = "^[가-힣0-9\\s]+$";    // 주소는 한글과 숫자만 가능


    private final Scanner sc = new Scanner(System.in);
    private final TeamSQL teamSQL = new TeamSQL();
    private final MEMBERS members = new MEMBERS();
    private final FOOD FOOD = new FOOD();
    private final FCATEGORY FCATEGORY = new FCATEGORY();
    private final ORDERS ORDERS = new ORDERS();
    private final STORES STORES = new STORES();
    private final TOPING TOPING = new TOPING();


    public void register() {

        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.println("회원정보를 입력하세요");

                // 아이디 입력받고 유효성 검사
                System.out.println("아이디를 입력하세요");
                System.out.println("조건 : 영문자와 숫자로 구성된 8~15자로 입력");
                System.out.print("아이디 >> ");
                String MID = sc.nextLine();
                if (!Pattern.matches(MID_Pattern, MID)) {
                    throw new IllegalArgumentException("아이디는 영문자와 숫자로 구성된 8~15자로 입력해주세요.");
                }
                members.setMID(MID);

                // 비밀번호 입력받고 유효성 검사
                System.out.println("비밀번호를 입력하세요");
                System.out.println("조건 : 영문, 숫자, 특수문자(&,!,@,*) 포함 8~15자리");
                System.out.print("비밀번호 >> ");
                String MPW = sc.nextLine();
                if (!Pattern.matches(MPW_Pattern, MPW)) {
                    throw new IllegalArgumentException("비밀번호는 영문, 숫자, 특수문자(&,!,@,*)를 포함한 8~15자리로 입력해주세요");
                }
                members.setMPW(MPW);

                // 이름 입력받고 유효성 검사
                System.out.println("이름을 입력하세요");
                System.out.println("조건 : 한글로만 최대 5자리까지");
                System.out.print("이름 >> ");
                String MNAME = sc.nextLine();
                if (!Pattern.matches(MNAME_Pattern, MNAME)) {
                    throw new IllegalArgumentException("이름은 한글로 최대 5글자까지 입력해주세요");
                }
                members.setMNAME(MNAME);

                // 연락처 입력받고 유효성 검사
                System.out.println("연락처를 입력하세요");
                System.out.println("조건 : 숫자만 가능(ex. 01012345678, 0212345678)");
                System.out.print("연락처 >> ");
                String PHONE = sc.nextLine();
                if (!Pattern.matches(PHONE_Pattern, PHONE)) {
                    throw new IllegalArgumentException("연락처는 숫자로만 10 ~ 11자로 입력해주세요");
                }
                members.setPHONE(PHONE);

                // 주소 입력받고 유효성 검사
                System.out.println("주소를 입력하세요");
                System.out.println("조건 : 한글과 숫자만 가능");
                System.out.print("주소 >> ");
                String ADDR = sc.nextLine();
                if (!Pattern.matches(ADDR_Pattern, ADDR)) {
                    throw new IllegalArgumentException("주소는 한글, 숫자로만 입력해주세요");
                }
                members.setADDR(ADDR);

                // 모든 입력이 유효한 경우 반복 종료
                validInput = true;

                // 회원가입 처리
                TeamSQL teamSQL = new TeamSQL();
                teamSQL.registermembers(members);

            } catch (IllegalArgumentException e) {
                // 유효성 검사 실패 시 메시지 출력 후 재입력 유도
                System.out.println(e.getMessage());
                System.out.println("다시 시도해주세요.");
            } catch (Exception e) {
                // 기타 예외 처리
                System.out.println("오류가 발생했습니다: " + e.getMessage());
                System.out.println("다시 시도해주세요.");
            }
        } // end while(!validInput)
    }

    // 로그인 메소드
    public MEMBERS membersLogin() {

        // 로그인을 위해 아이디 입력받기
        System.out.print("아이디 입력 >> ");
        members.setMID(sc.next());

        // 로그인을 위해 비밀번호 입력받기
        System.out.print("비밀번호 입력 >> ");
        members.setMPW(sc.next());

        // 로그인 시도
        MEMBERS loggedInMembers = teamSQL.loginMembers(members);

        if (loggedInMembers != null) {
            System.out.println(loggedInMembers.getMNAME() + "님 환영합니다.");
            return loggedInMembers;
        } else {
            System.out.println("로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요");
            return null;
        }

    }

    // 충전 메서드
    public void deposit() {

        String MID = members.getMID();
        System.out.print("충전할 금액을 입력하세요. >> ");
        try {
            int amount = sc.nextInt();

            // 입금액이 0보다 큰지 확인
            if (amount <= 0) {
                System.out.println("충전액은 0보다 커야 합니다.");
                return;
            } else if (amount > 0) {
                System.out.println("입금처리 진행중..");
            } else {
                System.out.println("충전액은 숫자만 입력가능합니다.");
                return;
            }

            // 입금 처리

            teamSQL.deposit(MID, amount);

        } catch (InputMismatchException e) {
            System.out.println("잘못된 금액 입력입니다. 숫자를 입력해주세요.");
            sc.nextLine(); // 입력 버퍼 비우기
        } catch (RuntimeException e) {
            System.out.println("입금 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


    // 회원정보를 확인하는 메서드
    public MEMBERS viewMembersInfo() {

        // TeamSQL 클래스의 인스턴스를 생성하여 데이터베이스에 접근
        TeamSQL teamSQL = new TeamSQL();
        String MID = members.getMID();

        // 회원 정보를 데이터베이스에서 조회
        teamSQL.findMemberById(MID);

        if (MID != null) {
            // 회원 정보가 있을 때 정보를 출력

        } else {
            // 해당 회원 정보가 없을 때
            System.out.println("해당 아이디로 회원 정보를 찾을 수 없습니다.");
        }
        return members;
    }


    // 결제 메서드
// 결제 메서드
    public void pay() {


        boolean run1 = true;


        while (run1) {
            String MID = members.getMID();


            System.out.println("[1] 사이드 추가  [2] 결제  [3] 뒤로가기 ");
            boolean test1 = false;
            int userMenu = 0;
            while (!test1) {
                try {

                    System.out.print("선택  >> ");
                    userMenu = sc.nextInt(); // 사용자 메뉴 선택 입력
                    sc.nextLine(); // 버퍼 비우기
                    test1 = true;
                } catch (Exception e) {
                    System.out.println("잘못된입력입니다.");
                    System.out.println("[1] 사이드 추가  [2] 결제  [3] 뒤로가기 ");
                    System.out.print("다시 입력 하기 >>");
                    sc.nextLine();
                }
            }

            switch (userMenu) {
                case 1:

                    System.out.println("[1] 한식 [2] 중식  [3] 양식 ");
                    boolean test2 = false;
                    int CNUM = 0;
                    while (!test2) {
                        try {

                            System.out.print("선택  >> ");
                            CNUM = sc.nextInt();
                            sc.nextLine(); // 버퍼 비우기
                            test2 = true;

                        } catch (Exception e) {
                            System.out.println("잘못된입력입니다.");

                            System.out.println("[1] 한식 [2] 중식  [3] 양식 ");
                            System.out.print("다시 입력 하기 >>");
                            sc.nextLine();

                        }

                    }

                    teamSQL.getToping(CNUM, MID);
                    System.out.print("장바구니의 가격 :");
                    int TOTALPRICE = teamSQL.getTOTALPRICE(members.getMID());

                    try {


                        // 결제할 금액이 있는지 확인
                        if (TOTALPRICE <= 0) {
                            System.out.println("음식을 장바구니에 넣어주세요~");
                            break;
                        } else {
                            boolean pay = true;


                            while (pay) {


                                System.out.println("1.결제 : 2.뒤로가기");
                                boolean test3 = false;
                                int menu1 = 0;
                                while (!test3) {
                                    try {
                                        System.out.print("선택  >> ");
                                        menu1 = sc.nextInt(); // 사용자 메뉴 선택 입력
                                        sc.nextLine(); // 버퍼 비우기
                                        test3 = true;
                                    } catch (Exception e) {
                                        System.out.println("잘못된입력입니다.");
                                        System.out.println("1.결제 : 2.뒤로가기");
                                        System.out.print("다시 입력 하기 >>");
                                        sc.nextLine();
                                    }
                                }
                                switch (menu1) {
                                    case 1:
                                        teamSQL.pay(MID, TOTALPRICE);
                                        teamSQL.reset(MID);
                                        this.resetjang();
                                        pay = false;
                                        run1 = false;
                                        continue;
                                    case 2:
                                        pay = false;
                                        continue;


                                    default:
                                        System.out.println("잘못 입력하셨습니다. 다시입력해주세요.");

                                        break;


                                }
                            }
                        }


                    } catch (InputMismatchException e) {
                        System.out.println("잘못된 금액 입력입니다. 숫자를 입력해주세요.");
                        sc.nextLine(); // 입력 버퍼 비우기
                    } catch (RuntimeException e) {
                    }


                    break;
                case 2:                 // 결제
                    System.out.print("장바구니의 가격 :");
                    TOTALPRICE = teamSQL.getTOTALPRICE(members.getMID());

                    try {


                        // 결제할 금액이 있는지 확인
                        if (TOTALPRICE <= 0) {
                            System.out.println("음식을 장바구니에 넣어주세요~");
                            break;
                        } else {
                            boolean pay = true;


                            while (pay) {
                                System.out.println("1.결제 : 2.뒤로가기");
                                boolean test3 = false;
                                int menu1 = 0;
                                while (!test3) {
                                    try {
                                        System.out.print("선택  >> ");
                                        menu1 = sc.nextInt(); // 사용자 메뉴 선택 입력
                                        sc.nextLine(); // 버퍼 비우기
                                        test3 = true;
                                    } catch (Exception e) {
                                        System.out.println("잘못된입력입니다.");
                                        System.out.println("1.결제 : 2.뒤로가기");
                                        System.out.print("다시 입력 하기 >>");
                                        sc.nextLine();
                                    }
                                }
                                switch (menu1) {
                                    case 1:
                                        teamSQL.pay(MID, TOTALPRICE);
                                        teamSQL.reset(MID);
                                        this.resetjang();
                                        pay = false;
                                        run1 = false;
                                        continue;
                                    case 2:
                                        pay = false;
                                        continue;


                                    default:
                                        System.out.println("잘못 입력하셨습니다. 다시입력해주세요.");

                                        break;


                                }
                            }
                        }


                    } catch (InputMismatchException e) {
                        System.out.println("잘못된 금액 입력입니다. 숫자를 입력해주세요.");
                        sc.nextLine(); // 입력 버퍼 비우기
                    } catch (RuntimeException e) {

                    }
                    break;
                case 3:
                    run1 = false;
                    continue;
                default:
                    System.out.println("잘못 입력하셨습니다. 다시입력해주세요.");

                    break;


            }


        }


    }

    public void koreaFOOD() {
        System.out.println("  \n┌─────────────────────[[ 가게목록 ]]───────────────────────┐");
        System.out.println("  한식 가게 목록:");
        List<String> getKoreanRestaurants = teamSQL.getKoreanRestaurants(); // 가게 목록 가져오기
        for (int i = 0; i < getKoreanRestaurants.size(); i++) {

            System.out.println("  [" + (i + 1) + "] " + getKoreanRestaurants.get(i)); // 번호 매기기
        }
        // 가게 번호를 입력하면 가게의 메뉴를 출력하는 메소드 추가
        // 메뉴의 이름을 입력하면 가게의 메뉴의 가격을 SQL 쿼리문을 통해 가져오는 메소드 추가

        String MID = members.getMID();
        boolean storeMenu = true;
        while (storeMenu) {
            boolean test5 = false;
            int koreaChoice = 0;
            System.out.println("└─────────────────────────────────────────────────────────┘");


            while (!test5) {
                try {
                    if (koreaChoice >= 6) {
                        System.out.println("표기 된 가게번호만 입력해주세요");
                        System.out.print("선택  >> ");
                        koreaChoice = sc.nextInt();

                        sc.nextLine();


                    } else {
                        System.out.println("표기 된 가게 번호만 입력해주세요.");
                        System.out.print("선택  >> ");
                        koreaChoice = sc.nextInt();
                        if (0 <koreaChoice && 5 >=koreaChoice){
                            test5 = true;}

                    }


                } catch (Exception e) {
                    System.out.println("잘못된입력입니다.");

                    sc.nextLine();

                }
            }


            teamSQL.getFood(koreaChoice);
            System.out.print("메뉴 이름을 입력하세요 >> ");
            String FOODNAME = sc.next();
            teamSQL.getFoodPrice(koreaChoice, FOODNAME);
            int fnum = teamSQL.getfNum(koreaChoice, FOODNAME); // 1

            int price = teamSQL.getFP(koreaChoice, FOODNAME); // 가격

            teamSQL.insertJang(fnum, price, MID); // 1,2,3

            teamSQL.getPrice(koreaChoice, FOODNAME, MID);


            storeMenu = false;


        }


    }

    public void chinaFOOD() {
        System.out.println("  \n┌─────────────────────[[ 가게목록 ]]───────────────────────┐");
        System.out.println("  중식 가게 목록:");
        List<String> getchinaRestaurants = teamSQL.getChinaRestaurants(); // 가게 목록 가져오기
        for (int i = 0; i < getchinaRestaurants.size(); i++) {

            System.out.println("  [" + (i + 1) + "] " + getchinaRestaurants.get(i)); // 번호 매기기
        }
        // 가게 번호를 입력하면 가게의 메뉴를 출력하는 메소드 추가
        // 메뉴의 이름을 입력하면 가게의 메뉴의 가격을 SQL 쿼리문을 통해 가져오는 메소드 추가

        String MID = members.getMID();
        boolean storeMenu = true;
        while (storeMenu) {
            boolean test5 = false;
            int chinaChoice = 0;
            System.out.println("└─────────────────────────────────────────────────────────┘");


            while (!test5) {
                try {
                    if (chinaChoice >= 6) {
                        System.out.println("표기 된 가게번호만 입력해주세요");
                        System.out.print("선택  >> ");
                        chinaChoice = sc.nextInt();

                        sc.nextLine();


                    } else {
                        System.out.println("표기 된 가게 번호만 입력해주세요.");
                        System.out.print("선택  >> ");
                        chinaChoice = sc.nextInt();
                        if (0 <chinaChoice && 5 >=chinaChoice){
                            test5 = true;}

                    }


                } catch (Exception e) {
                    System.out.println("잘못된입력입니다.");

                    sc.nextLine();

                }
            }


            teamSQL.getFood(chinaChoice + 5);
            System.out.print("메뉴 이름을 입력하세요 >> ");
            String FOODNAME = sc.next();
            teamSQL.getFoodPrice(chinaChoice + 5, FOODNAME);
            int fnum = teamSQL.getfNum(chinaChoice + 5, FOODNAME); // 1

            int price = teamSQL.getFP(chinaChoice + 5, FOODNAME); // 가격

            teamSQL.insertJang(fnum, price, MID); // 1,2,3

            teamSQL.getPrice(chinaChoice + 5, FOODNAME, MID);


            storeMenu = false;


        }


    }

    public void westFOOD() {
        System.out.println("  \n┌─────────────────────[[ 가게목록 ]]───────────────────────┐");
        System.out.println("  양식 가게 목록:");
        List<String> getwestRestaurants = teamSQL.getWestRestaurants(); // 가게 목록 가져오기
        for (int i = 0; i < getwestRestaurants.size(); i++) {

            System.out.println("  [" + (i + 1) + "] " + getwestRestaurants.get(i)); // 번호 매기기
        }
        // 가게 번호를 입력하면 가게의 메뉴를 출력하는 메소드 추가
        // 메뉴의 이름을 입력하면 가게의 메뉴의 가격을 SQL 쿼리문을 통해 가져오는 메소드 추가

        String MID = members.getMID();
        boolean storeMenu = true;
        while (storeMenu) {
            boolean test5 = false;
            int westChoice = 0;
            System.out.println("└─────────────────────────────────────────────────────────┘");

            while (!test5) {
                try {
                    if (westChoice >= 6) {
                        System.out.println("표기 된 가게번호만 입력해주세요");
                        System.out.print("선택  >> ");
                        westChoice = sc.nextInt();

                        sc.nextLine();


                    } else {
                        System.out.println("표기 된 가게 번호만 입력해주세요.");
                        System.out.print("선택  >> ");
                        westChoice = sc.nextInt();
                        if (0 <westChoice && 5 >=westChoice){
                            test5 = true;}

                    }


                } catch (Exception e) {
                    System.out.println("잘못된입력입니다.");

                    sc.nextLine();

                }
            }


            teamSQL.getFood(westChoice + 10);
            System.out.print("메뉴 이름을 입력하세요 >> ");
            String FOODNAME = sc.next();
            teamSQL.getFoodPrice(westChoice + 10, FOODNAME);

            int fnum = teamSQL.getfNum(westChoice + 10, FOODNAME); // 1

            int price = teamSQL.getFP(westChoice + 10, FOODNAME); // 가격

            teamSQL.insertJang(fnum, price, MID); // 1,2,3
            teamSQL.getPrice(westChoice + 10, FOODNAME, MID);


            storeMenu = false;


        }


    }


    public void jang() {

        String MID = members.getMID();
        teamSQL.Jang(MID);
    }

    public void resetjang() {

        String MID = members.getMID();
        teamSQL.resetJang(MID);
    }
}