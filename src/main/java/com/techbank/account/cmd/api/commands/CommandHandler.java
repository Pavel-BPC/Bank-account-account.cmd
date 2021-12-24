package com.techbank.account.cmd.api.commands;

public interface CommandHandler {
    void handler(OpenAccountCommand command);

    void handler(DepositFundsCommand command);

    void handler(WithdrawFundsCommand command);

    void handler(CloseAccountCommand command);
}
