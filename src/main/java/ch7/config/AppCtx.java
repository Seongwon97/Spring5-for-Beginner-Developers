package ch7.config;

import ch7.calculator.Calculator;
import ch7.aspect.ExeTimeAspect;
import ch7.calculator.RecCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/*
@Aspect이 붙은 클래스를 공통 기능으로 적용하려면 @EnableAspectJAutoProxy 어노테이셔을 설정 클래스에 붙여야한다.
@EnableAspectJAutoProxy은 스프링은 @Aspect이 붙은 빈 객체를 찾아서 빈 객체의 @Pointcut과 @Around설정을 사용환다.
 */

@Configuration
@EnableAspectJAutoProxy
public class AppCtx {

    @Bean
    public ExeTimeAspect exeTimeAspect() {
        return new ExeTimeAspect();
    }

    @Bean
    public Calculator calculator() {
        return new RecCalculator();
    }
}
