package ch7.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Aspect
@Order(2)
public class ExeTimeAspect {
/*
@Aspect를 적용한 클래스는 Advice와 PointCut을 함께 제공한다.
 */

    @Pointcut("execution(public * ch7.calculator..*(..))") // 공통 기능을 적용할 대상을 설정
    // ch7 패키지와 그 하위 패키지에 위치한 타입의 public메서드를 Pointcut으로 설정
    // 패키지 위치 설정할 때, 순환 참조를 조심해야한다.
    public void publicTarget() {
    }

    /*
    publicTarget()메서드에 정의한 Public에 공통 기능을 적용한다.
    즉, 위의 Pointcut에서 지정한 ch7 하위에 속한 빈 객체의 Public메서드에 @Around가 붙은 아래의 measure()메서드를 적용한다.
     */
    @Around("publicTarget()")
    public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
        // ProceedingJoinPoint는 프록시 대상 객체의 메서드를 호출할 때 사용한다.
        long start = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.nanoTime();
            Signature sig = joinPoint.getSignature(); // 호출한 객체의 시그니처 불러오기
            System.out.printf("%s.%s(%s) 실행 시간 : %d ns\n",
                    joinPoint.getTarget().getClass().getSimpleName(), // getTarget()은 호출 메서드의 대상 객체 불러오기
                    sig.getName(), Arrays.toString(joinPoint.getArgs()), // getArgs()는 호출 메서드의 인자 목록을 불러온다.
                    (finish - start));
        }
    }
}
