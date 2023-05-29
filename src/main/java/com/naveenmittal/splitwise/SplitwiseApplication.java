package com.naveenmittal.splitwise;

import com.naveenmittal.splitwise.commands.CommandExecutor;
import com.naveenmittal.splitwise.commands.RegisterUserCommand;
import com.naveenmittal.splitwise.controllers.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
@EnableJpaAuditing
public class SplitwiseApplication implements CommandLineRunner {

    @Autowired
    CommandExecutor commandExecutor;
    @Autowired
    UserController userController;
    public static void main(String[] args) {
        SpringApplication.run(SplitwiseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(userController);
        commandExecutor.registerCommand(registerUserCommand);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("Enter command:");
            String input = reader.readLine();
            commandExecutor.execute(input);
        }
    }
}
