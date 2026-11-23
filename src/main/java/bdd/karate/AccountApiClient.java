// TODO(reto): archivo stubbeado; implementa los cuerpos.
package bdd.karate;

import domain.model.Account;
import java.math.BigDecimal;
import java.util.Map;

// NO ES NECESARIO YA QUE SE ESTA USANDO LA LLAMADA HTTP DIRECTAMENTE DESDE EL FEATURE DE KARATE

public class AccountApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ACCOUNTS_ENDPOINT = "/api/accounts";

    public AccountApiClient() {
    }

    public Account createAccount(Account account) {
        throw new UnsupportedOperationException("TODO(reto): createAccount");
    }

    public Account getAccount(String accountNumber) {
        throw new UnsupportedOperationException("TODO(reto): getAccount");
    }

    private String buildRequestBody(Account account) {
        throw new UnsupportedOperationException("TODO(reto): buildRequestBody");
    }

    private Account mapToAccount(Map<String, Object> map) {
        throw new UnsupportedOperationException("TODO(reto): mapToAccount");
    }

    public static class AccountApiException extends RuntimeException {
        private final int statusCode;

        public AccountApiException(String message, int statusCode) {
            super(message);
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}