# 의존 자동 주입
```java
@Configuration
@Import(AppCtx2.class)
public class AppCtx1 {

    @Bean
    public MemberDao memberDao() {
        return new MemberDao();
    }

    @Bean
    public MemberPrinter memberPrinter() {
        return new MemberPrinter();
    }
}
```
앞의 챕터에서 배운바와 같이 이전까지는 DI를 `@Configuration`이 붙은 설정 클래스에서 직접 생성자나 setter를 통해 주입을 해주었다.
하지만 위와 같이 직접 의존 빈을 주입하지 않고 스프링이 자동으로 주입해주는 **자동 주입 기능** 도 존재한다. 

- 자동 주입 기능은 스프링 3,4 버전에서는 호불호가 갈렸으나 스프링 부트가 나오면서 의존 자동 주입을 사용하는 추세로 바뀌었다. (거의 기본으로 사용한다.)
