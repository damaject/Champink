package ink.champ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChampinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChampinkApplication.class, args);
        System.out.println("Champink is started... http://localhost:20202/");
    }

}
