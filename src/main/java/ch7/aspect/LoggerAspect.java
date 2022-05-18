package ch7.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

@Aspect
@Order(3)
public class LoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Pointcut("execution(public * ch7.calculator.*.*(..))")
    public void publicTarget() {
    }

    @Before("publicTarget()")
    public void before(JoinPoint joinPoint) {
        System.out.println(">>> request >>>");
        Object[] arguments = joinPoint.getArgs();

        //매개변수 배열의 종류와 값을 출력
        for (Object object : arguments) {
            logger.debug(object.getClass().getSimpleName() + ": " + object);
        }
    }
}
