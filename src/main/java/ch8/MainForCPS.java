package ch8;

import ch8.exception.MemberNotFoundException;
import ch8.exception.WrongIdPasswordsException;
import ch8.config.AppCtx;
import ch8.service.ChangePasswordService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainForCPS {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppCtx.class);

        ChangePasswordService cps = ctx.getBean("changePwdSvc", ChangePasswordService.class);
        try {
            cps.changePassword("madvirus@madvirus.net", "1234", "1111");
            System.out.println("암호를 변경했습니다.");
        } catch (MemberNotFoundException e) {
            System.out.println("회원 데이터가 존재하지 않습니다.");
        } catch (WrongIdPasswordsException e) {
            System.out.println("암호가 올바르지 않습니다.");
        }
        System.out.println(cps.getClass().getName());

        ctx.close();
    }
}
