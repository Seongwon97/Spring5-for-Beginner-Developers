package ch5.printer;

import ch5.dao.MemberDao;
import ch5.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;

public class MemberListPrinter {

    private MemberDao memberDao;
    private MemberPrinter printer;

    public MemberListPrinter() {}

    public MemberListPrinter(MemberDao memberDao, MemberPrinter printer) {
        this.memberDao = memberDao;
        this.printer = printer;
    }

    public void printAll() {
        Collection<Member> members = memberDao.selectAll();
        members.forEach(m -> printer.print(m));
    }

    @Autowired
    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Autowired
    @Qualifier("summaryPrinter")
    public void setPrinter(MemberPrinter printer) {
        this.printer = printer;
    }
    // 만약 인수 타입을 MemberPrinter를 상속받은 MemberSummaryPrinter로 할 경우,
    // 해당 위치에는 MemberSummaryPrinter타입의 빈만 올 수 있어서 @Qualifier를 안해도 된다.
}
