package ch3.service;

import ch3.dao.MemberDao;
import ch3.domain.Member;
import ch3.exception.MemberNotFoundException;

public class ChangePasswordService {

    private MemberDao memberDao;

    public void changePassword(String email, String oldPassword, String newPassword) {
        Member member = memberDao.selectByEmail(email);
        if (member == null) {
            throw new MemberNotFoundException();
        }
        member.changePassword(oldPassword, newPassword);

        memberDao.update(member);
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
}
