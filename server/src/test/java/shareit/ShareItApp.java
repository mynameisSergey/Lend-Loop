package shareit;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ShareItApp {
    @Getter
    static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);

    }

    public static void stop() {
        context.close();
    }

}