package bdd.cucumber;

import application.usecase.AccountUseCase;
import domain.model.Account;
import infrastructure.repository.AccountJpaRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountStepDefinitions {

    private final AccountTestContext testContext;
    private final AccountUseCase accountUseCase;
    private final AccountJpaRepository accountJpaRepository;
    private Exception thrownException;

    private static final String DEFAULT_OWNER_ID = "owner-1";

    public AccountStepDefinitions(AccountTestContext testContext, AccountUseCase accountUseCase,
                                   AccountJpaRepository accountJpaRepository) {
        this.testContext = testContext;
        this.accountUseCase = accountUseCase;
        this.accountJpaRepository = accountJpaRepository;
    }

    @Before
    public void setUp() {
        accountJpaRepository.deleteAll();
        testContext.clear();
        thrownException = null;
    }

    @Given("Una cuenta con saldo inicial de {int} y tipo {string} asociada a un cliente existente")
    public void givenAnExistingAccount(int balance, String accountType) {
        String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8);
        Account account = new Account(accountNumber, BigDecimal.valueOf(balance), accountType, DEFAULT_OWNER_ID, "ACTIVE");
        testContext.setAccount(account);
        testContext.setPreRegistered(true);
    }

    @Given("Una cuenta con saldo inicial de {int} y tipo {string}")
    public void givenAnAccountWithNegativeBalance(int balance, String accountType) {
        String accountNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 8);
        BigDecimal amount = BigDecimal.valueOf(Math.abs(balance));
        testContext.setAccount(new Account(accountNumber, amount, accountType, DEFAULT_OWNER_ID, "ACTIVE"));
        if (balance < 0) {
            testContext.setErrorMessage("Balance cannot be null or negative");
        }
    }

    @When("Se crea la cuenta")
    public void whenTheAccountIsCreated() {
        try {
            Account account = testContext.getAccount();
            Account created = accountUseCase.openAccount(
                account.accountNumber(), account.balance(), account.accountType(), account.ownerId());
            testContext.setAccount(created);
            testContext.setValidationResult(true);
        } catch (Exception e) {
            thrownException = e;
            testContext.setValidationResult(false);
            testContext.setErrorMessage(e.getMessage());
        }
    }

    @When("Se intenta crear la cuenta")
    public void whenAttemptingToCreateAccount() {
        try {
            Account account = testContext.getAccount();
            if (testContext.isPreRegistered()) {
                accountUseCase.openAccount(account.accountNumber(), account.balance(), account.accountType(), account.ownerId());
            }
            BigDecimal balance = testContext.getErrorMessage() != null ? BigDecimal.valueOf(-1) : account.balance();
            accountUseCase.openAccount(account.accountNumber(), balance, account.accountType(), account.ownerId());
            testContext.setValidationResult(true);
        } catch (Exception e) {
            thrownException = e;
            testContext.setValidationResult(false);
            testContext.setErrorMessage(e.getMessage());
        }
    }

    @Then("La cuenta es válida y se registra correctamente")
    public void thenTheAccountShouldBeValid() {
        assertTrue(testContext.isValid());
        assertNotNull(testContext.getAccount());
        assertEquals("ACTIVE", testContext.getAccount().status());
    }

    @Then("La creación falla y se lanza una excepción de negocio")
    public void thenTheCreationFailsWithBusinessException() {
        assertFalse(testContext.isValid());
        assertNotNull(thrownException);
        assertInstanceOf(IllegalArgumentException.class, thrownException);
    }
}
