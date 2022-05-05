package ch6.domain;

import ch6.exception.WrongIdPasswordsException;

import java.time.LocalDateTime;

public class Member {

    private Long id;
    private String email;
    private String password;
    private String name;
    private LocalDateTime registerLocalDateTime;

    public Member(String email, String password, String name, LocalDateTime registerLocalDateTime) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.registerLocalDateTime = registerLocalDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getRegisterLocalDateTime() {
        return registerLocalDateTime;
    }

    public void setRegisterLocalDateTime(LocalDateTime registerLocalDateTime) {
        this.registerLocalDateTime = registerLocalDateTime;
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!password.equals(oldPassword)) {
            throw new WrongIdPasswordsException();
        }
        this.password = newPassword;
    }
}
