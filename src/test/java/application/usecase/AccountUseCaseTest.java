package application.usecase;

import domain.model.Account;
import domain.port.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountUseCaseTest {

    private AccountRepository accountRepository;
    private AccountUseCase accountUseCase;

    private static final String ACCOUNT_NUMBER = "ACC-001";
    private static final String OWNER_ID = "owner-1";

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountUseCase = new AccountUseCase(accountRepository);
    }

    // --- openAccount ---

    @Test
    void openAccount_validData_savesAndReturnsAccount() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = accountUseCase.openAccount(ACCOUNT_NUMBER, BigDecimal.valueOf(500), "SAVINGS", OWNER_ID);

        assertEquals(ACCOUNT_NUMBER, result.accountNumber());
        assertEquals(BigDecimal.valueOf(500), result.balance());
        assertEquals("ACTIVE", result.status());
        verify(accountRepository).save(any());
    }

    @Test
    void openAccount_withZeroBalance_isAllowed() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = accountUseCase.openAccount(ACCOUNT_NUMBER, BigDecimal.ZERO, "CHECKING", OWNER_ID);

        assertEquals(BigDecimal.ZERO, result.balance());
    }

    @Test
    void openAccount_duplicateAccountNumber_throwsException() {
        Account existing = new Account(ACCOUNT_NUMBER, BigDecimal.TEN, "SAVINGS", OWNER_ID, "ACTIVE");
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(existing));
        BigDecimal balance = BigDecimal.valueOf(100);

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.openAccount(ACCOUNT_NUMBER, balance, "SAVINGS", OWNER_ID));
    }

    @Test
    void openAccount_invalidAccountType_throwsException() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());
        BigDecimal balance = BigDecimal.valueOf(100);

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.openAccount(ACCOUNT_NUMBER, balance, "INVALID", OWNER_ID));
    }

    @Test
    void openAccount_negativeBalance_throwsException() {
        BigDecimal negativeBalance = BigDecimal.valueOf(-1);

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.openAccount(ACCOUNT_NUMBER, negativeBalance, "SAVINGS", OWNER_ID));
    }

    // --- deposit ---

    @Test
    void deposit_validAmount_updatesBalance() {
        Account account = new Account(ACCOUNT_NUMBER, BigDecimal.valueOf(100), "SAVINGS", OWNER_ID, "ACTIVE");
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = accountUseCase.deposit(ACCOUNT_NUMBER, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), result.balance());
    }

    @Test
    void deposit_zeroAmount_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.deposit(ACCOUNT_NUMBER, BigDecimal.ZERO));
    }

    @Test
    void deposit_nonExistentAccount_throwsException() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());
        BigDecimal amount = BigDecimal.valueOf(100);

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.deposit(ACCOUNT_NUMBER, amount));
    }

    // --- withdraw ---

    @Test
    void withdraw_validAmount_updatesBalance() {
        Account account = new Account(ACCOUNT_NUMBER, BigDecimal.valueOf(200), "SAVINGS", OWNER_ID, "ACTIVE");
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = accountUseCase.withdraw(ACCOUNT_NUMBER, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), result.balance());
    }

    @Test
    void withdraw_exactBalance_leavesZero() {
        Account account = new Account(ACCOUNT_NUMBER, BigDecimal.valueOf(100), "SAVINGS", OWNER_ID, "ACTIVE");
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account result = accountUseCase.withdraw(ACCOUNT_NUMBER, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.ZERO, result.balance());
    }

    @Test
    void withdraw_insufficientFunds_throwsException() {
        Account account = new Account(ACCOUNT_NUMBER, BigDecimal.valueOf(50), "SAVINGS", OWNER_ID, "ACTIVE");
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));
        BigDecimal amount = BigDecimal.valueOf(100);

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.withdraw(ACCOUNT_NUMBER, amount));
    }

    @Test
    void withdraw_nonExistentAccount_throwsException() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());
        BigDecimal amount = BigDecimal.valueOf(50);

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.withdraw(ACCOUNT_NUMBER, amount));
    }

    // --- getBalance ---

    @Test
    void getBalance_existingAccount_returnsBalance() {
        Account account = new Account(ACCOUNT_NUMBER, BigDecimal.valueOf(300), "SAVINGS", OWNER_ID, "ACTIVE");
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.of(account));

        BigDecimal balance = accountUseCase.getBalance(ACCOUNT_NUMBER);

        assertEquals(BigDecimal.valueOf(300), balance);
    }

    @Test
    void getBalance_nonExistentAccount_throwsException() {
        when(accountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                accountUseCase.getBalance(ACCOUNT_NUMBER));
    }
}
