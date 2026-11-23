package application.usecase;

import domain.model.Account;
import domain.port.AccountRepository;
import domain.service.AccountValidationService;
import java.math.BigDecimal;

public class AccountUseCase {

    private final AccountRepository accountRepository;
    private final AccountValidationService validationService;

    public AccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.validationService = new AccountValidationService(accountRepository);
    }

    public Account openAccount(String accountNumber, BigDecimal initialBalance, String accountType, String ownerId) {
        Account account = new Account(accountNumber, initialBalance, accountType, ownerId, "ACTIVE");
        validationService.validateAccountCreation(account);
        return accountRepository.save(account);
    }

    public Account deposit(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        validationService.validateAccountExists(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
        return accountRepository.save(account.withBalance(account.balance().add(amount)));
    }

    public Account withdraw(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        validationService.validateAccountExists(accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber).get();
        if (account.balance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        return accountRepository.save(account.withBalance(account.balance().subtract(amount)));
    }

    public BigDecimal getBalance(String accountNumber) {
        validationService.validateAccountExists(accountNumber);
        return accountRepository.findByAccountNumber(accountNumber).get().balance();
    }
}
