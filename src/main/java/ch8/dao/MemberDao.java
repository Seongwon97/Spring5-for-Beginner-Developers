package ch8.dao;

import ch8.domain.Member;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;

public class MemberDao {

    private JdbcTemplate jdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Member selectByEmail(String email) {
        String SQL = "select * from MEMBER where EMAIL = ?";
        List<Member> results = jdbcTemplate.query(SQL, new MemberRowMapper(), email);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Member> selectAll() {
        String SQL = "select * from MEMBER";
        return jdbcTemplate.query(SQL, rowMapper());
    }

    private RowMapper<Member> rowMapper() {
        return (ResultSet rs, int rowNum) -> {
            Member member = new Member(
                    rs.getString("EMAIL"),
                    rs.getString("PASSWORD"),
                    rs.getString("NAME"),
                    rs.getTimestamp("REGDATE").toLocalDateTime());
            member.setId(rs.getLong("ID"));
            return member;
        };
    }

    public int count() {
        String SQL = "select count(*) from MEMBER";
        return jdbcTemplate.queryForObject(SQL, Integer.class);
    }


    public void update(Member member) {
        String SQL = "update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?";
        jdbcTemplate.update(SQL, member.getName(), member.getPassword(), member.getEmail());
    }
//    public void update(Member member) {
//        String SQL = "update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?";
//        jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                PreparedStatement pstmt = con.prepareStatement(SQL);
//                pstmt.setString(1, member.getName());
//                pstmt.setString(2, member.getPassword());
//                pstmt.setString(3, member.getEmail());
//                pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterLocalDateTime()));
//                return pstmt;
//            }
//        });
//    }

    public void insert(Member member) {
        String SQL = "insert into MEMBER(EMAIL, PASSWORD, NAME, REGDATE) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(SQL, new String[] {"ID"}); // {ID}는 자동 생성되는 키 컬럼의 목록을 지정할 떄 사용한다.
                pstmt.setString(1, member.getName());
                pstmt.setString(2, member.getPassword());
                pstmt.setString(3, member.getEmail());
                pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterLocalDateTime()));
                return pstmt;
            }
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        member.setId(keyValue.longValue());
    }
}
