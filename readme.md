# Sistema de Atendimento Ubots com Fila

Este é um sistema de atendimento REST em Java 17, desenvolvido com Spring Boot. Cada atendente pode atender até 3 clientes simultaneamente. Caso o limite de atendimentos seja atingido, os clientes adicionais são colocados em uma fila de espera. Assim que o atendimento de um cliente é finalizado, o próximo da fila é atendido automaticamente.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.0.x**
- **Maven 3.9.9**
- **H2 Database (em memória)**
- **Spring Data JPA**
- **Spring Web**

## Requisitos

- **Java 17** ou superior
- **Maven 3.9** ou superior