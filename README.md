# 📜 AOP Logging Library

Uma biblioteca para **log automático** de chamadas em **controllers Spring Boot**, utilizando **Aspect-Oriented Programming (AOP)**.

## 🚀 Funcionalidades

- **Intercepta chamadas a controllers (`@RestController`) automaticamente**
- **Registra logs de entrada e saída dos métodos**
- **Mede o tempo de execução**
- **Captura exceções e loga os erros**

---

## 📦 Instalação

### 1️⃣ Adicione a dependência ao `pom.xml` do seu projeto:

```xml
<dependency>
    <groupId>com.log</groupId>
    <artifactId>aop-lib</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
