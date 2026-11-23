package domain.service;

import domain.model.Account;
import domain.port.AccountRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class AccountValidationService {
    private final AccountRepository accountRepository;
    private final Validator validator;
    private final Set<String> validAccountTypes = Set.of("SAVINGS", "CHECKING", "INVESTMENT");

    public AccountValidationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void validateAccountCreation(Account account) {
        validateAccountType(account.accountType());
        validateUniqueAccountNumber(account.accountNumber());
        validateBeanConstraints(account);
    }

    private void validateAccountType(String accountType) {
        if (!validAccountTypes.contains(accountType)) {
            throw new IllegalArgumentException("Invalid account type. Allowed types: " + validAccountTypes);
        }
    }

    private void validateUniqueAccountNumber(String accountNumber) {
        accountRepository.findByAccountNumber(accountNumber)
            .ifPresent(a -> { throw new IllegalArgumentException("Account number already exists"); });
    }

    private void validateBeanConstraints(Account account) {
        var violations = validator.validate(account);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            violations.forEach(v -> sb.append(v.getMessage()).append("; "));
            throw new IllegalArgumentException("Validation errors: " + sb);
        }
    }

    public void validateAccountExists(String accountNumber) {
        accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }
}