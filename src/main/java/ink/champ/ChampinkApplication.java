package ink.champ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс запуска программы. Запускает {@link SpringApplication}
 * @author Maxim
 */
@SpringBootApplication
public class ChampinkApplication {

    /**
     * Начальный метод запуска программы
     * @param args Аргументы для запуска программы
     */
    public static void main(String[] args) {
        SpringApplication.run(ChampinkApplication.class, args);
        System.out.println("Champink is started... http://localhost:20202/");
    }
}
