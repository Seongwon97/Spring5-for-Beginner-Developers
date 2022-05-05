package ch6.main;

import ch6.config.AppCtx;
import ch6.domain.Client;
import ch6.domain.Client2;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;

public class MainForSpring {

    public static void main(String[] args) throws IOException {

        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppCtx.class);

        Client client = context.getBean(Client.class);
        client.send();

        Client2 client2 = context.getBean(Client2.class);
        client2.send();

        Client clientP = context.getBean(Client.class);
        context.close();

        System.out.println("Prototype Bean Scope Test: " + (client == clientP));
    }
}
