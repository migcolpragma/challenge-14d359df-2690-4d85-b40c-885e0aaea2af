package bdd.karate;

import com.intuit.karate.junit5.Karate;

public class AccountApiTest {
    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:bdd/karate/features/account-api.feature");
    }
}
