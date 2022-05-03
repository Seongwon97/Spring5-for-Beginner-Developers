package ch5.service;

import ch5.RegisterRequest;
import ch5.dao.MemberDao;
import ch5.domain.Member;
import ch5.exception.DuplicateMemberException;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class MemberRegisterService {

    @Autowired
    private MemberDao memberDao;

    public MemberRegisterService() {}

    public MemberRegisterService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long register(RegisterRequest req) {
        Member member = memberDao.selectByEmail(req.getEmail());
        if (member != null) {
            throw new DuplicateMemberException("dup email " + req.getEmail());
        }
        Member newMember = new Member(
                req.getEmail(), req.getPassword(), req.getName(), LocalDateTime.now());
        memberDao.insert(newMember);
        return newMember.getId();
    }
}
