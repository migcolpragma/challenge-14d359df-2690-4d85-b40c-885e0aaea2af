Feature: Gestión de Cuentas Bancarias
  Como cliente del banco
  Quiero gestionar mis cuentas bancarias
  Para realizar operaciones financieras de forma segura

  # --- Apertura de cuenta ---

  Scenario: Apertura exitosa de cuenta de ahorro
    Given Una cuenta con saldo inicial de 1000 y tipo "SAVINGS" asociada a un cliente existente
    When Se crea la cuenta
    Then La cuenta es válida y se registra correctamente

  Scenario: Apertura de cuenta con tipo inválido
    Given Una cuenta con saldo inicial de 500 y tipo "INVALID"
    When Se intenta crear la cuenta
    Then La creación falla y se lanza una excepción de negocio

  Scenario: Apertura de cuenta con saldo negativo
    Given Una cuenta con saldo inicial de -100 y tipo "SAVINGS"
    When Se intenta crear la cuenta
    Then La creación falla y se lanza una excepción de negocio

  Scenario: Apertura de cuenta con número duplicado
    Given Una cuenta con saldo inicial de 200 y tipo "SAVINGS" asociada a un cliente existente
    When Se intenta crear la cuenta
    Then La creación falla y se lanza una excepción de negocio
