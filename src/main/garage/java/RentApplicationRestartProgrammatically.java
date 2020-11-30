package garage;

//import com.rent.*;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.context.ConfigurableApplicationContext;

//@SpringBootApplication
public class RentApplicationRestartProgrammatically {

    //private static ConfigurableApplicationContext context;

//    public static void main(String[] args) {

//        SpringApplication.run(RentApplicationRestart.class, args);
//        context = SpringApplication.run(RentApplication.class, args);
//    }

//    public static void restart() {
//        ApplicationArguments args = context.getBean(ApplicationArguments.class);
//
//        Thread thread = new Thread(() -> {
//            context.close();
//            context = SpringApplication.run(RentApplication.class, args.getSourceArgs());
//        });
//
//        thread.setDaemon(false);
//        thread.start();
//    }
}
