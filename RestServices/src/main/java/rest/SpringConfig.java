package rest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.database.ZborDatabase;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class SpringConfig {


    @Bean
    Properties getProps() {
        Properties props = new Properties();
        try {
            System.out.println("Searching bd.config in directory " + ((new File(".")).getAbsolutePath()));
            props.load(new FileReader("server.properties"));
        } catch (IOException e) {
            System.err.println("Configuration file bd.config not found" + e);
        }
        return props;
    }


    @Bean
    ZborDatabase zborDatabase(){ return new ZborDatabase(getProps());}

}