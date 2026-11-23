Feature: Create Account Setup

  Scenario: Crear cuenta base
    Given url 'http://localhost:8080'
    And path '/api/accounts'
    * def ownerId = java.util.UUID.randomUUID().toString()
    And request { owner: '#(ownerId)', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201
    * def accountNumber = response.accountNumber
