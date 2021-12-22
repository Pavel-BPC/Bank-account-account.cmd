package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.AccountsClosedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {

    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .accountType(command.getAccountType())
                .createDate(new Date())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.balance = event.getOpeningBalance();
        this.active = true;
    }

    public void depositFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Funds connot be deposite into a closed account!");
        }
        if (amount <= 0) {
            throw new IllegalStateException("The deposite amount must be greater than 0");
        }

        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();

    }

    public void withdrawFunds(double amount) {
        if (!this.active) {
            throw new IllegalStateException("Funds connot be withdraw from a closed account!");
        }
        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();

    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalStateException("Bank account has already been closed!");
        }
        raiseEvent(AccountsClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountsClosedEvent event) {
        this.id = event.getId();
        this.active = false;

    }


}
