package PROJECTMAIN;

import DAO.TeamSQL;
import DTO.MEMBERS;
import DTO.ORDERS;
import SERVICE.MembersService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainApplication {
    MEMBERS members = new MEMBERS();
    boolean isLoggedIn = false;


    public void startApplication() {
        try (Scanner sc = new Scanner(System.in)) { // Scanner를 사용하여 입력을 받음
            boolean run = true; // 프로그램 실행 상태 관리
            TeamSQL teamSQL = new TeamSQL();
            MEMBERS loggedInMembers = null;
            MembersService membersService = new MembersService();

            teamSQL.connect();
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("\n" +"\u001B[34m"+
                    "███████╗ █████╗ ██╗   ██╗ ██████╗██╗  ██╗ █████╗ ███╗   ███╗\n" +
                    "██╔════╝██╔══██╗╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗████╗ ████║\n" +
                    "███████╗███████║ ╚████╔╝ ██║     ███████║███████║██╔████╔██║\n" +
                    "╚════██║██╔══██║  ╚██╔╝  ██║     ██╔══██║██╔══██║██║╚██╔╝██║\n" +
                    "███████║██║  ██║   ██║   ╚██████╗██║  ██║██║  ██║██║ ╚═╝ ██║\n" +
                    "╚══════╝╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝\n" );

            while (run) {
                try {
                    // 메인 메뉴 출력
                    System.out.println( "┌───────────────────────[[ 메인메뉴 ]]─────────────────────┐");
                    System.out.println("  [1] 회원가입          \n  [2] 로그인         \n  [3] 종료       ");
                    System.out.println( "└─────────────────────────────────────────────────────────┘");
                    System.out.print("선택 >> ");

                    int menu = sc.nextInt(); // 사용자 메뉴 선택 입력
                    sc.nextLine(); // 버퍼 비우기

                    switch (menu) {
                        case 1:
                            // 회원가입 로직
                            membersService.register();
                            break;

                        case 2:
                            // 로그인 로직
                            loggedInMembers = membersService.membersLogin(); // 로그인 성공 시 사용자 정보 반환
                            if (loggedInMembers == null) {
                                System.out.println("다시 시도해주세요.");
                            } else {
                                isLoggedIn = true; // 로그인 상태 관리
                            }


                            while (isLoggedIn) {
                                try {
                                    // 로그인 후 사용자 메뉴 출력
                                    System.out.println("\n┌───────────────────────[[ 메뉴 ]]─────────────────────────┐");
                                    System.out.println("  [1] 회원 정보\n  [2] 충전\n  [3] 결제\n  [4] 음식카테고리 보기\n  [5] 장바구니 조회\n  [6] 로그아웃      ");
                                    System.out.println(  "└─────────────────────────────────────────────────────────┘");
                                    System.out.print("선택 >> ");

                                    int userMenu = sc.nextInt(); // 사용자 메뉴 선택 입력
                                    sc.nextLine(); // 버퍼 비우기

                                    switch (userMenu) {
                                        case 1:
                                            // 회원 정보 로직
                                            membersService.viewMembersInfo();
                                            break;

                                        case 2:
                                            // 충전 로직
                                            membersService.deposit();
                                            break;

                                        case 3: // 결제 로직
                                            membersService.pay();
                                            break;
                                        case 4: // 음식카테고리 보기 로직
                                            boolean categoryMenu = true; // 카테고리 메뉴 상태 관리


                                            while (categoryMenu) {
                                                try {
                                                    // 음식 카테고리 메뉴 출력
                                                    System.out.println("\n┌─────────────────────[[ 카테고리 ]]────────────────────────┐");
                                                    System.out.println("  [1] 한식                           ");
                                                    System.out.println("  [2] 중식                           ");
                                                    System.out.println("  [3] 양식                           ");
                                                    System.out.println("  [4] 뒤로가기                       ");
                                                    System.out.println(  "└─────────────────────────────────────────────────────────┘");
                                                    System.out.print("선택 >> ");

                                                    int categoryChoice = sc.nextInt(); // 카테고리 선택 입력


                                                    switch (categoryChoice) {
                                                        case 1: // 한식 선택
                                                            membersService.koreaFOOD();
                                                            break;

                                                        case 2: // 중식 선택
                                                            membersService.chinaFOOD();

                                                            break;

                                                        case 3: // 양식 선택
                                                            membersService.westFOOD();
                                                            break;

                                                        case 4: // 뒤로가기
                                                            categoryMenu = false; // 카테고리 메뉴 종료
                                                            break;

                                                        default:
                                                            System.out.println("잘못된 입력입니다. 다시 입력하세요.");
                                                            break;
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("잘못된 입력입니다. 다시 입력하세요.");
                                                    sc.nextLine();
                                                }
                                            }
                                            break;
                                        case 5: // 장바구니 출력 로직
                                            membersService.jang();


                                            break;
                                        case 6: // 로그아웃
                                            isLoggedIn = false; // 로그인 상태 종료
                                            System.out.println("로그아웃 되었습니다.");
                                            break;

                                        default:
                                            System.out.println("다시 입력하세요");
                                            break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("다시 입력해주세요.");
                                    sc.nextLine();
                                }
                            }
                            break;

                        case 3: // 프로그램 종료
                            System.out.println("프로그램을 종료합니다.");
                            run = false; // 프로그램 실행 상태 종료
                            break;

                        default:
                            System.out.println("잘못된 입력입니다. 다시 입력하세요.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                    sc.nextLine(); // 잘못된 입력 버퍼 비우기
                } catch (Exception e) {
                    System.out.println("예상치 못한 오류가 발생했습니다: "+"\u001B[0m");
                    e.printStackTrace();
                }


            }
        }
    }
}