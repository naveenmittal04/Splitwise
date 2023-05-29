package com.naveenmittal.splitwise.commands;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CommandExecutor {
    private List<Command> commands;

    public CommandExecutor() {
        this.commands = new ArrayList<>();
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void removeCommand(Command command) {
        commands.remove(command);
    }

    public void execute(String input) {
        for (Command command : commands) {
            if (command.match(input)) {
                command.execute(input);
                return;
            }
        }
        System.out.println("Command Not Found");
    }
}
