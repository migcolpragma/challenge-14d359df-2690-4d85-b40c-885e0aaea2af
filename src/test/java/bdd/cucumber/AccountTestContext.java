// TODO(reto): archivo stubbeado; implementa los cuerpos.
package bdd.cucumber;

import domain.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountTestContext {

    private Account account;
    private boolean isValid;
    private boolean preRegistered;
    private String lastErrorMessage;

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setValidationResult(boolean valid) {
        this.isValid = valid;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setPreRegistered(boolean preRegistered) {
        this.preRegistered = preRegistered;
    }

    public boolean isPreRegistered() {
        return preRegistered;
    }

    public void setErrorMessage(String message) {
        this.lastErrorMessage = message;
    }

    public String getErrorMessage() {
        return lastErrorMessage;
    }

    public void clear() {
        this.account = null;
        this.isValid = false;
        this.preRegistered = false;
        this.lastErrorMessage = null;
    }
}