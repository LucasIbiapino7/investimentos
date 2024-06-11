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

# Funcionalidades

## Login e controle de acesso:
Um usuário já cadastrado pode fazer Login e receberá um Token de acesso:
![img-1](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/1.png)

Novos usuários podem se cadastrar com 
  - POST na rota: /users
corpo da requisição:

![img-2](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/2.png)

Usuários com idade menor que 18 anos não são cadastrados: 

![img-3](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/3.png)

Usuários que tentam se cadastrar com um email que já existe também não conseguem:

![img-4](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/4.png)

Também temos um EndPoint para obtermos as informações de um usuário Logado
  - GET na rota: /users

![img-5](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/5.png)

## Account e funcionalidades
A partir do momento em que o usuário está logado, ele pode cadastrar uma conta, o que lhe possibilitará fazer depósitos, e posteriormente comprar ativos.
  - POST na rota: /accounts
cria uma conta associada ao usuário.
exemplo de resposta:

![img-6](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/6.png)

  - POST na rota: /accounts
Simula um depósito, de maneira simples, verificando se o usuário já criou uma conta e recebendo a senha para comparar com a senha do usuário salva no banco de dados:
corpo da requisição:

![img-7](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/7.png)

resposta:

![img-8](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/8.png)

Consequentemente, temos EndPoints para simular um saque, onde recebemos o valor, verificamos se usuário tem esse valor e se a senha fornecida equivale a senha salva no Banco de Dados. 
Ademais, temos um EndPoint que apenas verifica o saldo da conta de um usuário.
ex de resposta caso a senha fornecida não seja a do usuário:

![img-9](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/9.png)

## Portfólios e funcionalidades
 A partir do momento em que o usuário estiver logado e com uma Account cadastrada, ele pode criar portfólios. A ideia é simular que ele possa organizar diferentes tipos de ações 
 da maneira que ele quiser.
  - POST na rota /portfolios
Cria um novo portfolio, onde o usuário passa uma descrição para seu portfolio:
exemplo de resposta:

![img-10](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/10.png)

Lembrando, que sempre verificamos se o usuário está cadastrado. Usuários não cadastrados recebem um 401 Unauthorized caso tentem acessar essa rota.

  - Get na rota /portfolios
Pega todos os portfólios associados a um usuário:

![img-11](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/11.png)

  - Get na rota /portfolios/{id}
Essa rota obtém as informações de um Portfólio, ideal para criarmos no FrontEnd uma página com informações do portfólio do usuário e posteriormente adicionamos ações.

![img-12](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/12.png)

Caso um outro usuário tente acessar essa rota, seu acesso é negado.

## Stocks e Funcionalidades
para simular as ações, temos 2 EndPoints:
  - GET na rota: /stocks?search=
Onde enviamos uma requisição para API da Brapi.dev para recuperamos uma lista de ações de acordo com o parâmetro “search”, exemplo para search igual a “AMZ”:

![img-13](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/13.png)

Retornamos o stock, que é como o id da Stock e seu nome.

Posteriormente, para obtermos mais informações sobre essa ação, temos: 
  - GET na rota /stocks/{stockId}. ex: /stocks/AMZO34

![img-14](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/14.png)

Recebemos como resposta o seu id (symbol), seu nome e o valor dela no dia, que usaremos para simular posteriormente uma compra em um Portfólio.

## Possíveis fluxos:
Alguns outros endpoints fazem sentido no contexto de um Fluxo, vamos ver:

Imaginemos que o usuário já fez login, criou sua conta, depositou um valor, criou um Portfolio e pegou informações de uma ação, no caso a AMZO34. (Todos Endpoints já mostrados acima) e agora 
quer comprar algumas ações: 
  - POST na rota: /portfolios/{idPortifolio}/purchased
corpo da requisição:

![img-15](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/15.png)

Recebemos do Front o StockId, que foi obtido anteriormente, assim como o nome e o preço, e além disso informamos a quantidade de ações.
Nesse caso, colocamos um preço diferente do obtido apenas por fins de melhorar o exemplo. como resposta, obtemos:

resposta:

![img-16](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/16.png)

Enviamos como resposta o saldo atualizado da conta, assim como uma lista com as ações daquele portfólio, tendo o nome (stockId), a quantidade comprada. o preço comprado, 
uma vez que esses valores variam ao longo dos dias, e o valor comprado, além do valor total de ações daquele portfólio. Lembrando que verificamos se o saldo do indivíduo é o 
suficiente, se está cadastrado e entre outras verificações já mostradas anteriormente.

Posteriormente, o usuário pode querer vender suas ações, estão temos uma rota que ajuda ele a tomar essa decisão:
  - GET na rota: /portfolios/{portfolioId}/comparison

resposta:

![img-17](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/17.png)

Retornamos o valor que a ação foi comprada, o seu preço atual (obtido da API da Brapi), a variação do preço comprado pro preço atual, 
a quantidade, o valor total comprado e o valor caso você vendesse tudo.

Caso o usuário queira vender, temos uma rota pra passar para o Front as informações de uma ação específica: 

  - GET na rota: /portfolios/{portifolioId}/{stockId} ex: /portfolios/1/AMZO34

enviamos como resposta:

![img-18](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/18.png)

nesse momento, o usuário poderia vender essa ação pelo EndPoint: 

  - PUT na rota: /portfolios/{portifolioId}/{stockId}/sale ex: /portfolios/1/AMZO34/sale
Enviamos como resposta: 

![img-19](https://github.com/LucasIbiapino7/imgs-investimentos/blob/main/19.png)

o preço da venda, preço da compra, quantidade vendida e valor da venda. Além disso, atualizamos o Saldo do usuário, a quantidade e valor das ações. 

# Autor

Lucas Ibiapino do Nascimento Duarte
