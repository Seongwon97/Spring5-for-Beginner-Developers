# Spring Bean
- 스프링이 생성하는 객체를 **빈(Bean)** 이라고 한다.
- 빈을 수동으로 등록하기 위해서는 아래와 같이 `@Configuration`이 붙은 클래스 내에서 개발자가 직접 빈으로 등록하고 싶은 객체를 반환하는 메서드에 `@Bean`을 붙여줘야 한다.

```java
@Configuration
public class AppContext {

    @Bean
    public Greeter greeter() {
        Greeter greeter = new Greeter();
        greeter.setFormat("%s, 안녕하세요!");
        return greeter;
    }
}
```

```java
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppContext.class);
        Greeter greeter = ctx.getBean(Greeter.class);
        String message = greeter.greet("스프링");
        System.out.println(message);
        ctx.close();
    }
}
```
- `AnnotationConfigApplicationContext`
  - 자바 설정에서 정보를 읽어와 빈 객체를 생성과 초기화를 수행한다.
  - `ApplicationContext`인터페이스를 구현한 클래스이다.
  - 해당 객체를 생성할 때 AppContext 클래스를 파라미터로 전달하면 `@Bean`을 통해 설정한 값들에 의해 Greeter가 초기화 및 생성된다.
- `getBean()`은 생성된 빈 객체를 검색하고 불러올 때 사용된다.
- 스프링은 Bean을 싱글턴으로 생성하고 관리한다. 
  - 하나의 `@Bean` 어노테이션마다 하나의 빈을 생성한다.
  - 어떻게 가능할까?
    - 스프링은 설정 클래스를 그대로 사용하지 않고 설정 클래스를 상속한 샐운 설정 클래스를 만들어 사용한다. 설정 클래스에 있는 객체를 반환하는 메서드들은 항상 새로운 객체를 생성하지 않고, 한번 생성한 객체를 보관했다가 이후에는 동일한 객체를 반환한다.

## ApplicationContext (Spring Container)
- `ApplicationContext` 또는 `BeanFactory`는 Bean 객체의 생성, 초기화, 보관, 제거 등을 관리하고 있어서 **컨테이너** 라고도 불린다.
- 스프링 컨테이너는 내부적으로 빈 객체와 빈 이름을 연결하는 정보를 갖는다. 
- 이름과 실제 객체의 관계뿐만 아니라 실제 객체의 생성, 초기화, 의존 주입 등 스프링 컨테이너는 객체 관리를 위한 다양한 기능을 제공한다.


