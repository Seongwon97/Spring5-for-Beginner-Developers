package ch4.config;

import ch4.dao.MemberDao;
import ch4.printer.MemberPrinter;
import ch4.printer.MemberSummaryPrinter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AppCtx2.class)
public class AppCtx1 {

    @Bean
    public MemberDao memberDao() {
        return new MemberDao();
    }

}
