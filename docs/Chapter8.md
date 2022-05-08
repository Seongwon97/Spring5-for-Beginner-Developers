# DB 연동
## 커넥션 풀
자바에서 DBMS로 커넥션을 생성하는 시간은 비교적 길기 때문에 DB 커넥션을 생성하는 시간이 전체 성능에 영향을 줄 수 있다.
그리고 동시에 접속하는 사용자가 많으면 사용자마다 DB 커넥션을 생성해야하기에 DBMS에 부하를 줄 수 있다.

이러한 부하를 줄일 수 있는 방법이 커넥션 풀이다. 커넥션 풀은 일정 개수의 DB커넥션을 미리 만들어두고 필요에 따라 커넥션을 가져와 사용을하고 반납을 하는 방법으로 운영된다.
이 방법은 커넥션을 생성하는 부하가 적기 떄문에 많은 동시 접속자를 처리할 수 있고 일정 개수의 커넥션을 유지하여 DBMS에 주는 부하를 일정 수준으로 유지할 수 있다.

DB 커넥션 풀 기능을 제공하는 모듈로는 Tomcat JDBC, HikariCP, DBCP, c3p0등이 존재한다.
(지속적인 개발, 성능을 고려하면 Tomcat JDBC, HikariCP을 사용하는 것이 좋다.)

커넥션 풀이란? - p.183

- 생성한 커넥션들은 영원히 유지되지 않고 DBMS의 설정에 따라 일정 시간 내에 쿼리를 실행하지 않으면 연결이 끊기기도 한다.
  - 연결이 끊기어도 해당 커넥션은 여전히 커넥션 풀 안에 남아있는데, 이 상태에서 해당 커넥션을 사용하려고 한다면 exception이 발생하게 된다.
    - 이러한 문제를 방지하려면 커넥션 풀의 커넥션이 유효한지 주기적으로 검사해야한다. (관련 속성: minEvictableIdleTimeMills, timeBetweenEvictionRunsMillis, testWhileIdle)



## DataSource 
JDBC API는 DriverManager가 아닌 DataSource 이용해 DB 연결을 할 수 있습니다.

스프링은 DataSource를 스프링 빈으로 등록하고 해당 빈을 사용하여 DB Connection을 구하는 방법을 제공합니다.

> DataSource는 TomCat JDBC 모듈에서 제공을 합니다.
> - `implementation 'org.apache.tomcat:tomcat-jdbc:10.0.20'`를 의존성에 추가

Tomcat JDBC DataSource 클래스의 주요 프로퍼티 - p.187

# JdbcTemplate을 통한 쿼리 실행
스프링을 사용하면 DataSource, Connection, Statement, ResultSet을 직접 사용하지 않고 JdbcTemplate을 이용해 쿼리를 편리하게 이용할 수 있다.

### select
jdbcTemplate를 통해 데이터를 읽고 변환해주는 방법은 `query()`와 `queryForObject()`가 있다.
- `query()`는 반환 타입이 List로 반환을 하지만 `queryForObject()`는 Reference타입 또는 RowMapper로 반환해주는 한가지 객체를 반환한다.
- `queryForObject()`의 쿼리문 실행 결과는 반드시 한개의 행이어야 한다. 만약 결과 행이 없거나 두개 이상이면 예외가 발생한다.
  - 행이 두개 이상이면 `IncorrectResultSizeDataAccessException`이 발생한다.
  - 행이 0개이면 `EmptyResultDataAccessException`이 발생한다.
  - 즉, 쿼리문의 결과 값이 정확히 1개가 아니라면 `query()`를 사용해야 한다.

### insert, update, delete
다음 쿼리문들을 실행하기 위해서는 jdbcTemplate의 `update()`메서드를 사용해야 한다.
- `update()`메서드는 쿼리 실행 결과로 변경된 행의 개수를 반환한다.
- update는 `PrepareStatement`의 set메서드를 사용해서 직접 인덱스 파라미터(쿼리문의 ?)의 값을 설정해야 할 떄도 있다.
  - `PrepareStatementCreator`를 인자로 받는 메서드를 이영해 직접 `PrepareStatement`를 생성하고 설정해야 한다.
  - p.199
```java
// PrepareStatementCreator를 사용한 예시
    public void update(Member member) {
        String SQL = "update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(SQL);
                pstmt.setString(1, member.getName());
                pstmt.setString(2, member.getPassword());
                pstmt.setString(3, member.getEmail());
                pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegisterLocalDateTime()));
                return pstmt;
            }
        });
    }
```

- DDL에서 테이블을 생성할 때 id와 같은 컬럼의 값을 `auto_increment` 설정을 해뒀을 떄 `keyHolder`를 사용하면 자동 생성된 키 값을 구할 수 있다.
  - jdbcTemplate.update(new PreparedStatementCreator() {...}, keyHolder);와 같은 순서로 사용해야한다.
```java
// KeyHolder 사용 예시
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
```

## Spring JDBC의 익셉션
> DB연동 과정에서 발생 가능한 익셉션 종류는 p.207을 참조할 것

jdbcTemplate의 `update()`, `query()`와 같은 JDBC API를 사용하는 과정에서 SQLException이 발생한다면 스프링은 해당 예외를 알맞은 `DataAccessException`으로 변환하여 예외를 던진다.

예를 들면 MySQL용 JDBC 드라이버는 SQL문법이 잘못된 경우 `SQLException`을 상속받은 `MySQLSyntaxErrorException`을 발생시고, JdbcTemplate은 해당 예외를 `DataAccessException`을 상속받은 `BadSqlGrammerException`으로 변환하여 던진다.

> DataAccessException은 데이터 연결에 문제가 있을때 스프링이 모듈이 발생시키는 스프링 제공 익셉션이다.
> 해당 익셉션은 runtimeException이며, 스프링은 DataAccessException을 상속받은 다양한 익셉션을 제공한다.
> - Spring을 사용하지 않고 JDBC를 직접 사용하면 checkedException인 `SQLException`을 처리해줘야해서 try/catch로 처리를 해줘야한다. 하지만 스프링은 UncheckedException인 `DataAccessException`을 던져주어 예외를 원할때만 처리해줘도 된다.

스프링이 Jdbc에서 발생시키는 예외를 DataAccessException으로 변환하는 이유는 사용하는 연동 기술에 종속적인 개발을 막기 위해서이다.
각각의 연동 기술별로 발생시키는 예외가 다를텐데, 해당 예외를 스프링이 제공하는 DataAccessException으로 변환함으로써 구현 기술에 상관없이 동일한 코드로 예외 처리를 할 수 있다.

