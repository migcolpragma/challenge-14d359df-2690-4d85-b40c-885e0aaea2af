package infrastructure.controller;

import application.usecase.AccountUseCase;
import domain.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountUseCase accountUseCase;

    public AccountController(AccountUseCase accountUseCase) {
        this.accountUseCase = accountUseCase;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Map<String, Object> body) {
        try {
            String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8);
            BigDecimal balance = new BigDecimal(body.get("balance").toString());
            String type = (String) body.get("type");
            String owner = (String) body.get("owner");
            Account created = accountUseCase.openAccount(accountNumber, balance, type, owner);
            return ResponseEntity.status(HttpStatus.CREATED).body(toMap(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber, @RequestBody Map<String, Object> body) {
        try {
            BigDecimal amount = new BigDecimal(body.get("amount").toString());
            Account updated = accountUseCase.deposit(accountNumber, amount);
            return ResponseEntity.ok(toMap(updated));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Account not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String accountNumber, @RequestBody Map<String, Object> body) {
        try {
            BigDecimal amount = new BigDecimal(body.get("amount").toString());
            Account updated = accountUseCase.withdraw(accountNumber, amount);
            return ResponseEntity.ok(toMap(updated));
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Account not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<?> getBalance(@PathVariable String accountNumber) {
        try {
            BigDecimal balance = accountUseCase.getBalance(accountNumber);
            return ResponseEntity.ok(Map.of("balance", balance));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    private Map<String, Object> toMap(Account account) {
        return Map.of(
            "accountNumber", account.accountNumber(),
            "balance", account.balance(),
            "accountType", account.accountType(),
            "ownerId", account.ownerId(),
            "status", account.status()
        );
    }
}
