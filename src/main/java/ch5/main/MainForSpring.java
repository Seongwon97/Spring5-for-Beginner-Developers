package ch5.main;

import ch5.RegisterRequest;
import ch5.config.AppCtx;
import ch5.exception.DuplicateMemberException;
import ch5.exception.MemberNotFoundException;
import ch5.exception.WrongIdPasswordsException;
import ch5.printer.MemberInfoPrinter;
import ch5.printer.MemberListPrinter;
import ch5.printer.VersionPrinter;
import ch5.service.ChangePasswordService;
import ch5.service.MemberRegisterService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainForSpring {

    private static ApplicationContext ctx = null;

    public static void main(String[] args) throws IOException {
        ctx = new AnnotationConfigApplicationContext(AppCtx.class);
        // AnnotationConfigApplicationContext를 통한 스프링 컨테이너 생성

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("명령어를 입력하세요: ");
            String command = reader.readLine();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("종료합니다.");
                break;
            }
            if (command.startsWith("new ")) {
                processNewCommand(command.split(" "));
                continue;
            } else if (command.startsWith("change ")) {
                processChangeCommand(command.split(" "));
                continue;
            } else if (command.equals("list")) {
                processListCommand(command);
                continue;
            } else if (command.startsWith("info ")) {
                processInfoCommand(command.split(" "));
                continue;
            } else if (command.equals("version")) {
                processVersionCommand();
                continue;
            }
            printHelp(); // 도움말 출력
        }
    }

    private static void processNewCommand(String[] args) {
        if (args.length != 5) {
            printHelp();
            return;
        }
        MemberRegisterService service = ctx.getBean("memberRegSvc", MemberRegisterService.class);
        RegisterRequest request = new RegisterRequest();
        request.setEmail(args[1]);
        request.setName(args[2]);
        request.setPassword(args[3]);
        request.setConfirmPassword(args[4]);

        if (!request.isPasswordEqualToConfirmPassword()) {
            System.out.println("암호와 확인이 일치하지 않습니다.\n");
            return;
        }
        try {
            service.register(request);
            System.out.println("등록했습니다.\n");
        } catch (DuplicateMemberException e) {
            System.out.println("이미 존재하는 이메일입니다.\n");
        }
    }

    private static void processChangeCommand(String[] args) {
        if (args.length != 4) {
            printHelp();
            return;
        }
        ChangePasswordService service = ctx.getBean("changePwdSvc", ChangePasswordService.class);
        try {
            service.changePassword(args[1], args[2], args[3]);
            System.out.println("암호를 변경했습니다. \n");
        } catch (MemberNotFoundException e) {
            System.out.println("존재하지 않는 이메일입니다.\n");
        } catch (WrongIdPasswordsException e) {
            System.out.println("이메일과 암호가 일치하지 않습니다.\n");
        }
    }

    private static void processListCommand(String command) {
        MemberListPrinter listPrinter =
                ctx.getBean("listPrinter", MemberListPrinter.class);
        listPrinter.printAll();
    }

    private static void processInfoCommand(String[] args) {
        if (args.length != 2) {
            printHelp();
            return;
        }
        MemberInfoPrinter infoPrinter = ctx.getBean("infoPrinter", MemberInfoPrinter.class);
        infoPrinter.printMemberInfo(args[1]);
    }

    private static void processVersionCommand() {
        VersionPrinter versionPrinter = ctx.getBean("versionPrinter", VersionPrinter.class);
        versionPrinter.print();
    }

    private static void printHelp() {
        System.out.println();
        System.out.println("잘못된 명령입니다. 아래의 명령어 사용법을 확인하세요.");
        System.out.println("명령어 사용법: ");
        System.out.println("new 이메일 이름 암호 암호확인");
        System.out.println("change 이메일 현재비번 변경비번");
        System.out.println();
    }
}
