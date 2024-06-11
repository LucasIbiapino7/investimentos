# Investimentos
# Sobre o projeto
 O projeto busca simular uma carteira de investimentos em ativos financeiros, onde utilizamos a API da Brapi.dev para obtermos as cotações das ações no dia. Além disso, a API conta com 
 Controle de Acesso, onde cada usuário tem acesso apenas aos seus portfólios e as suas ações, possibilidade do cadastrode novos usuários, tratamento de exceções e entre outras funcionalidades.
 O objetivo é pôr em Prática os conhecimentos obtidos sobre API Rest utilizando o Framework Spring Boot.
 
 # Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- Maven
- JPA / Hibernate
- Banco em memória H2
- Spring Security
- OpenFeign

# Competências
- Desenvolvimento de API Rest com Java e Spring Boot
- Realização de casos de uso
- Consultas a banco de dados relacional com Spring Data JPA
- Tratamento de erros com respostas HTTP customizadas
- Controle de acesso por perfil de usuário e rotas
- Utilização do Bean Validation para Validação
- Validações personalizadas
- Teste unitários e de integração

# Funcionalidades

## Login e controle de acesso:
Um usuário já cadastrado pode fazer Login e receberá um Token de acesso:

```json
{
    "access_token": "eyJraWQiOiI1MzUxNzFhYS1lNzkxLTQ4Y2ItODc1OC00NjEwMGM4N2QzZTQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjoibXljbGllbnRpZCIsIm5iZiI6MTcxODExMDc4MywiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzE4MTk3MTgzLCJpYXQiOjE3MTgxMTA3ODMsImp0aSI6IjQ0NjFhMDM2LWFlMzUtNDNiYy1iMjRkLTExNGQyMDZmYjIyMyIsImF1dGhvcml0aWVzIjpbIlJPTEVfQ0xJRU5UIl0sInVzZXJuYW1lIjoibHVjYXNAZ21haWwuY29tIn0.jdOsYPBYecaIZ1G63lLrKRLFi4snGJILlWV737CmQPysVvcI4SR2Kga8sHZFf9zlT68xiNdJQpganVLA_ECMUwPhZF_biSK6aGEaxk4DwXtyyDyLzwJ7k8T-gjJouzXdzYr2acI9jhrX3YN1ebqytdGoBW0d6Vnzg5uso763TaCaPY7_YEnZhW_YeYDk6BRiQAlacdvOvYlXbC2QinTwpr-A6JLo8Am9-DOwtph-MMgKFsXKIWEPiaFHK1hwS_rwqO1p9DmMUdi20I92esbxzeno2kmeov9k4rj87HMay4uuFYBUnzlcJZ_bXvrlE5I_UUxCVCe711yspf3Om_Ulzg",
    "token_type": "Bearer",
    "expires_in": 86399
}
```

Novos usuários podem se cadastrar com 
  - POST na rota: /users
corpo da requisição:

```json
{
  "firstName": "celso",
  "lastName": "dutra",
  "birthDate": "2006-03-05",
  "email": "celsoa@gmail",
  "password": "123456"
}
```
Usuários com idade menor que 18 anos não são cadastrados: 

```json
{
    "timestamp": "2024-06-11T13:06:20.276463800Z",
    "status": 422,
    "error": "Dados inválidos",
    "path": "/users",
    "errors": [
        {
            "fieldName": "birthDate",
            "message": "Idade Menor que 18 anos"
        }
    ]
}
```

Usuários que tentam se cadastrar com um email que já existe também não conseguem:

```json
{
    "timestamp": "2024-06-11T13:07:27.234344700Z",
    "status": 422,
    "error": "Dados inválidos",
    "path": "/users",
    "errors": [
        {
            "fieldName": "email",
            "message": "email já existe"
        }
    ]
}
```

Também temos um EndPoint para obtermos as informações de um usuário Logado
  - GET na rota: /users

```json
{
    "id": 1,
    "firstName": "Lucas",
    "lastName": "Ibiapino",
    "birthDate": "2002-07-25",
    "email": "lucas@gmail.com"
}
```

## Account e funcionalidades
A partir do momento em que o usuário está logado, ele pode cadastrar uma conta, o que lhe possibilitará fazer depósitos, e posteriormente comprar ativos.
  - POST na rota: /accounts
cria uma conta associada ao usuário.
exemplo de resposta:

```json
{
    "id": 1,
    "name": "Minha Conta",
    "description": "A conta do lucas",
    "balance": 0.0,
    "portfolioNumber": 0,
    "userDTO" : {
            "id": 1,
            "firstName": "Lucas",
            "lastName": "Ibiapino",
            "birthDate": "2002-07-25",
     }
}
```

  - POST na rota: /accounts
Simula um depósito, de maneira simples, verificando se o usuário já criou uma conta e recebendo a senha para comparar com a senha do usuário salva no banco de dados:
corpo da requisição:

```json
{
    "amount": 1000.0,
    "password": 123456
}
```

resposta:

```
{
    "balance": 1000.0
}
```

Consequentemente, temos EndPoints para simular um saque, onde recebemos o valor, verificamos se usuário tem esse valor e se a senha fornecida equivale a senha salva no Banco de Dados. 
Ademais, temos um EndPoint que apenas verifica o saldo da conta de um usuário.
ex de resposta caso a senha fornecida não seja a do usuário:

```json
{
    "timestamp": "2024-06-11T13:15:50.232576700Z",
    "status": 400,
    "error": "Senha Incorreta!",
    "path": "/accounts/deposit"
}
```

## Portfólios e funcionalidades
 A partir do momento em que o usuário estiver logado e com uma Account cadastrada, ele pode criar portfólios. A ideia é simular que ele possa organizar diferentes tipos de ações 
 da maneira que ele quiser.
  - POST na rota /portfolios
Cria um novo portfolio, onde o usuário passa uma descrição para seu portfolio:
exemplo de resposta:

```json
{
    "id": 3,
    "description": "Conta Para Manter ativos",
    "totalValue": 0.0,
    "accountId": 1,
    "stockPortfolios": []
}
```

Lembrando, que sempre verificamos se o usuário está cadastrado. Usuários não cadastrados recebem um 401 Unauthorized caso tentem acessar essa rota.

  - Get na rota /portfolios
Pega todos os portfólios associados a um usuário:

```json
[
    {
        "id": 1,
        "description": "Ativos",
        "totalValue": 0.0,
        "accountId": 1,
        "stockPortfolios": [
            {
                "symbol": "AMZO34",
                "quantity": 5,
                "price": 40.0,
                "valuePurchased": 200.0
            },
            {
                "symbol": "PETR4",
                "quantity": 10,
                "price": 10.0,
                "valuePurchased": 100.0
            }
        ]
    }
]
```

  - Get na rota /portfolios/{id}
Essa rota obtém as informações de um Portfólio, ideal para criarmos no FrontEnd uma página com informações do portfólio do usuário e posteriormente adicionamos ações.

```josn
{
    "id": 1,
    "description": "Ativos",
    "totalValue": 0.0,
    "accountId": 1,
    "stockPortfolios": [
        {
            "symbol": "PETR4",
            "quantity": 10,
            "price": 10.0,
            "valuePurchased": 100.0
        },
        {
            "symbol": "AMZO34",
            "quantity": 5,
            "price": 40.0,
            "valuePurchased": 200.0
        }
    ]
}
```

Caso um outro usuário tente acessar essa rota, seu acesso é negado.

## Stocks e Funcionalidades
para simular as ações, temos 2 EndPoints:
  - GET na rota: /stocks?search=
Onde enviamos uma requisição para API da Brapi.dev para recuperamos uma lista de ações de acordo com o parâmetro “search”, exemplo para search igual a “AMZ”:

```json
{
    "stocks": [
        {
            "stock": "AMZO34",
            "name": "AMAZON DRN"
        }
    ]
}
```

Retornamos o stock, que é como o id da Stock e seu nome.

Posteriormente, para obtermos mais informações sobre essa ação, temos: 
  - GET na rota /stocks/{stockId}. ex: /stocks/AMZO34

```json
{
    "symbol": "AMZO34",
    "longName": "Amazon.com, Inc.",
    "regularMarketPrice": 50.0
}
```

Recebemos como resposta o seu id (symbol), seu nome e o valor dela no dia, que usaremos para simular posteriormente uma compra em um Portfólio.

## Possíveis fluxos:
Alguns outros endpoints fazem sentido no contexto de um Fluxo, vamos ver:

Imaginemos que o usuário já fez login, criou sua conta, depositou um valor, criou um Portfolio e pegou informações de uma ação, no caso a AMZO34. (Todos Endpoints já mostrados acima) e agora 
quer comprar algumas ações: 
  - POST na rota: /portfolios/{idPortifolio}/purchased
corpo da requisição:

```json
{
    "stockId": "AMZO34",
    "longName": "Amazon.com, Inc.",
    "regularMarketPlace": 40,
    "quantity": 5
}
```

Recebemos do Front o StockId, que foi obtido anteriormente, assim como o nome e o preço, e além disso informamos a quantidade de ações.
Nesse caso, colocamos um preço diferente do obtido apenas por fins de melhorar o exemplo. como resposta, obtemos:

resposta:

```json
{
    "balance": 800.0,
    "stocksDTOList": [
        {
            "name": "AMZO34",
            "quantity": 5,
            "price": 40.0,
            "valuePurchased": 200.0
        }
    ],
    "total": 200.0
}
```

Enviamos como resposta o saldo atualizado da conta, assim como uma lista com as ações daquele portfólio, tendo o nome (stockId), a quantidade comprada. o preço comprado, 
uma vez que esses valores variam ao longo dos dias, e o valor comprado, além do valor total de ações daquele portfólio. Lembrando que verificamos se o saldo do indivíduo é o 
suficiente, se está cadastrado e entre outras verificações já mostradas anteriormente.

Posteriormente, o usuário pode querer vender suas ações, estão temos uma rota que ajuda ele a tomar essa decisão:
  - GET na rota: /portfolios/{portfolioId}/comparison

resposta:

```json
[
    {
        "symbol": "AMZO34",
        "pricePurchased": 40.0,
        "priceActual": 50.0,
        "variation": 25.0,
        "quantity": 5,
        "totalValue": 200.0,
        "totalValueSale": 250.0
    }
]
```

Retornamos o valor que a ação foi comprada, o seu preço atual (obtido da API da Brapi), a variação do preço comprado pro preço atual, 
a quantidade, o valor total comprado e o valor caso você vendesse tudo.

Caso o usuário queira vender, temos uma rota pra passar para o Front as informações de uma ação específica: 

  - GET na rota: /portfolios/{portifolioId}/{stockId} ex: /portfolios/1/AMZO34

enviamos como resposta:

```json
{
    "symbol": "AMZO34",
    "quantity": 5,
    "price": 40.0,
    "valuePurchased": 200.0
}
```

nesse momento, o usuário poderia vender essa ação pelo EndPoint, sendo necessário informar a quantidade e a senha do usuário: 

  - PUT na rota: /portfolios/{portifolioId}/{stockId}/sale ex: /portfolios/1/AMZO34/sale

corpo da requisição:

```json
{
    "quantity": 5,
    "password": "123456"
}
```

Enviamos como resposta: 

```json
{
    "priceSale": 50.0,
    "pricePurchased": 40.0,
    "quantitySale": 5,
    "saleValue": 250.0
}
````

o preço da venda, preço da compra, quantidade vendida e valor da venda. Além disso, atualizamos o Saldo do usuário, a quantidade e valor das ações. 

# Autor

Lucas Ibiapino do Nascimento Duarte
