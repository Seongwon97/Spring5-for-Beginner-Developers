package ch3.printer;

import ch3.dao.MemberDao;
import ch3.domain.Member;

public class MemberInfoPrinter {

    private MemberDao memberDao;
    private MemberPrinter printer;

    public void printMemberInfo(String email) {
        Member member = memberDao.selectByEmail(email);
        if (member == null) {
            System.out.println("데이터 없음\n");
            return;
        }
        printer.print(member);
        System.out.println();
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void setPrinter(MemberPrinter printer) {
        this.printer = printer;
    }
}
