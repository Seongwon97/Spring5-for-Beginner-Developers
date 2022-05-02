package ch2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppContext.class);
        Greeter greeter = ctx.getBean("greeter", Greeter.class);
        String message = greeter.greet("스프링");
        System.out.println(message);
        ctx.close();
    }
}
