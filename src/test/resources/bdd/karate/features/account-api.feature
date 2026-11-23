Feature: Account Fintech API
  # Puedo llamar a otro feature con call read('create-account.feature') si quiero ejecutarlo antes

  Background:
    Given url 'http://localhost:8080'

  # Crear cuenta
  Scenario: Crear cuenta exitosamente
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201

  Scenario: Crear cuenta con balance negativo
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: -100 }
    When method POST
    Then status 400

  Scenario: Crear cuenta con owner vacío
    And path '/api/accounts'
    And request { owner: '', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 400

  # Depósito
  Scenario: Depositar exitosamente
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201
    * def accountNumber = response.accountNumber
    And path '/api/accounts/' + accountNumber + '/deposit'
    And request { amount: 500 }
    When method POST
    Then status 200
    And match response.balance == 1500

  Scenario: Depositar monto inválido
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201
    * def accountNumber = response.accountNumber
    And path '/api/accounts/' + accountNumber + '/deposit'
    And request { amount: -50 }
    When method POST
    Then status 400

  # Retiro
  Scenario: Retirar exitosamente
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201
    * def accountNumber = response.accountNumber
    And path '/api/accounts/' + accountNumber + '/withdraw'
    And request { amount: 200 }
    When method POST
    Then status 200
    And match response.balance == 800

  Scenario: Retiro con saldo insuficiente
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201
    * def accountNumber = response.accountNumber
    And path '/api/accounts/' + accountNumber + '/withdraw'
    And request { amount: 999999 }
    When method POST
    Then status 400

  # Consulta de saldo
  Scenario: Consultar saldo exitosamente
    And path '/api/accounts'
    And request { owner: '1234-5678-pragma', type: 'SAVINGS', balance: 1000 }
    When method POST
    Then status 201
    * def accountNumber = response.accountNumber
    And path '/api/accounts/' + accountNumber + '/balance'
    When method GET
    Then status 200
    And match response.balance == '#number'

  # Casos edge: cuenta no encontrada
  Scenario: Depositar en cuenta inexistente
    And path '/api/accounts/NONEXISTENT/deposit'
    And request { amount: 100 }
    When method POST
    Then status 404

  Scenario: Retirar de cuenta inexistente
    And path '/api/accounts/NONEXISTENT/withdraw'
    And request { amount: 100 }
    When method POST
    Then status 404

  Scenario: Consultar saldo de cuenta inexistente
    And path '/api/accounts/NONEXISTENT/balance'
    When method GET
    Then status 404
