# Casos de Uso — Sistema de Gestión de Cuentas Bancarias

---

## CU-01: Apertura de Cuenta

**Actor:** Cliente  
**Caso de uso:** Abrir una nueva cuenta bancaria  
**Precondiciones:**
- El cliente existe en el sistema (ownerId válido)
- El número de cuenta no existe previamente

**Pasos:**
1. El cliente proporciona número de cuenta, saldo inicial, tipo de cuenta y su ID
2. El sistema valida que el tipo de cuenta sea SAVINGS, CHECKING o INVESTMENT
3. El sistema valida que el número de cuenta no esté duplicado
4. El sistema valida que el saldo inicial sea mayor o igual a cero
5. El sistema registra la cuenta con estado ACTIVE

**Resultado:** La cuenta queda registrada con estado ACTIVE y el saldo inicial indicado

**Escenarios alternativos:**
- Tipo de cuenta inválido → lanza `IllegalArgumentException("Invalid account type")`
- Número de cuenta duplicado → lanza `IllegalArgumentException("Account number already exists")`
- Saldo inicial negativo → lanza `IllegalArgumentException("Balance cannot be null or negative")`

---

## CU-02: Depósito

**Actor:** Cliente  
**Caso de uso:** Depositar fondos en una cuenta existente  
**Precondiciones:**
- La cuenta existe en el sistema
- El monto a depositar es mayor a cero

**Pasos:**
1. El cliente proporciona el número de cuenta y el monto a depositar
2. El sistema valida que el monto sea positivo
3. El sistema verifica que la cuenta exista
4. El sistema suma el monto al saldo actual
5. El sistema persiste el nuevo saldo

**Resultado:** El saldo de la cuenta se incrementa con el monto depositado

**Escenarios alternativos:**
- Monto cero o negativo → lanza `IllegalArgumentException("Deposit amount must be positive")`
- Cuenta inexistente → lanza `IllegalArgumentException("Account not found")`

---

## CU-03: Retiro

**Actor:** Cliente  
**Caso de uso:** Retirar fondos de una cuenta existente  
**Precondiciones:**
- La cuenta existe en el sistema
- El monto a retirar es mayor a cero
- El saldo disponible es mayor o igual al monto a retirar

**Pasos:**
1. El cliente proporciona el número de cuenta y el monto a retirar
2. El sistema valida que el monto sea positivo
3. El sistema verifica que la cuenta exista
4. El sistema verifica que el saldo sea suficiente
5. El sistema resta el monto al saldo actual
6. El sistema persiste el nuevo saldo

**Resultado:** El saldo de la cuenta se reduce con el monto retirado (puede quedar en cero)

**Escenarios alternativos:**
- Monto cero o negativo → lanza `IllegalArgumentException("Withdrawal amount must be positive")`
- Cuenta inexistente → lanza `IllegalArgumentException("Account not found")`
- Fondos insuficientes → lanza `IllegalArgumentException("Insufficient funds")`

---

## CU-04: Consulta de Saldo

**Actor:** Cliente / Sistema  
**Caso de uso:** Consultar el saldo actual de una cuenta  
**Precondiciones:**
- La cuenta existe en el sistema

**Pasos:**
1. El cliente o sistema proporciona el número de cuenta
2. El sistema verifica que la cuenta exista
3. El sistema retorna el saldo actual

**Resultado:** Se retorna el saldo actual de la cuenta

**Escenarios alternativos:**
- Cuenta inexistente → lanza `IllegalArgumentException("Account not found")`
