package com.personal.logseparator;

import com.personal.logseparator.core.SeparatorService;
import com.personal.logseparator.model.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.util.Scanner;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@SuppressWarnings("squid:S106")
public class LogSeparatorApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogSeparatorApplication.class);
    private static final String YES = "Y";
    private static final String NO = "N";

    private final SeparatorService separatorService;

    @Autowired
    public LogSeparatorApplication(SeparatorService separatorService) {
        this.separatorService = separatorService;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(LogSeparatorApplication.class);
        LogSeparatorApplication app = ctx.getBean(LogSeparatorApplication.class);
        app.run();
    }

    private void run() {
        LOGGER.info("------LOG SEPARATOR APPLICATION STARTED------");
        try (Scanner scanner = new Scanner(System.in)) {
            boolean next = true;
            Log log;
            while (next) {
                log = new Log();
                System.out.println("Please enter file path input: ");
                log.setPathInput(scanner.nextLine());
                System.out.println("Please enter search key: ");
                log.setSearchKey(scanner.nextLine());
                System.out.println("Please enter path output: ");
                log.setPathOutput(scanner.nextLine());
                System.out.println("Please enter desired file name: ");
                log.setFileName(scanner.nextLine());
                System.out.println("Proceed with this current setup? Y/N ");
                if (YES.equalsIgnoreCase(scanner.nextLine())) {
                    System.out.println("Please wait while your query been exported. ");
                    separatorService.processLog(log);
                    next = this.nextValidation(scanner);
                } else {
                    next = this.nextValidation(scanner);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Log Separator Exception: ", e);
        }
        LOGGER.info("------LOG SEPARATOR APPLICATION ENDED------");
    }

    private boolean nextValidation(Scanner scanner) {
        System.out.println("Do you want to search again? Y/N ");
        String answer = scanner.nextLine();
        return this.answerValidation(scanner, answer);
    }

    private boolean answerValidation(Scanner scanner, String answer) {
        if (YES.equalsIgnoreCase(answer)) {
            return true;
        } else if (NO.equalsIgnoreCase(answer)) {
            return false;
        } else {
            System.out.println("Input either with Y or N: ");
            String answerValid = scanner.nextLine();
            return this.answerValidation(scanner, answerValid);
        }
    }
}
