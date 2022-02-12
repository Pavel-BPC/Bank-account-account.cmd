package com.techbank.account.cmd;

import com.techbank.account.cmd.api.commands.*;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommandApplication {

    @Autowired
    private CommandHandler commandHandler;

    @Autowired
    private CommandDispatcher commandDispatcher;

    public static void main(String[] args) {
        SpringApplication.run(CommandApplication.class, args);
    }

    @PostConstruct
    public void registerHandler() {
        commandDispatcher.registerHandle(OpenAccountCommand.class, commandHandler::handler);
        commandDispatcher.registerHandle(DepositFundsCommand.class, commandHandler::handler);
        commandDispatcher.registerHandle(WithdrawFundsCommand.class, commandHandler::handler);
        commandDispatcher.registerHandle(CloseAccountCommand.class, commandHandler::handler);
        commandDispatcher.registerHandle(RestoreReadDbCommand.class, commandHandler::handler);
    }
}
