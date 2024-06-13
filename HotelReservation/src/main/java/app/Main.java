package app;

import app.utils.DataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //DataLoader dataLoader = new DataLoader();
        //dataLoader.loadData();

        SpringApplication.run(Main.class, args);
    }
}