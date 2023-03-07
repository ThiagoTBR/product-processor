# Product Processor

Projeto para carregar produtos a partir de arquivos Json para gravar banco de dados MongoDB e retornar a distribuição um produto para a quantidade de lojistas informada.

## Começando

Essas instruções permitirão que você obtenha uma cópia do projeto em operação na sua máquina local para fins de desenvolvimento e teste.


### 📋 Pré-requisitos

De que coisas você precisa para instalar o software e como instalá-lo?

```
Java 8
MongoDB
Maven
Docker
```

### 🔧 Instalação

Clone o repositório:

```
git clone https://github.com/ThiagoTBR/product-processor.git
```

Entre na pasta raiz do projeto:

```
cd product-processor
```

Compile o projeto com o Maven:

```
mvn clean install
```

Execute o projeto:

```
java -jar target/product-processor-0.0.1-SNAPSHOT.jar

```

### 🚀 Utilizando a aplicação.

Acessando a aplicação:

```
http://localhost:8080/swagger-ui/index.html

```

Iniciando a leitura dos arquivos Json para popular a base de dados MondoDB:

```
http://localhost:8080/swagger-ui/index.html#/product-controller/startReadingFilesUsingPOST

```

Listando todos os produtos paginados:

```
http://localhost:8080/swagger-ui/index.html#/product-controller/getProductsUsingGET

```

Listando produtos por nome:

```
http://localhost:8080/swagger-ui/index.html#/product-controller/findByProductNameUsingGET

```

Calculando dados para distribuir um produto para a quantidade de lojistas informada:

```
http://localhost:8080/swagger-ui/index.html#/product-controller/calculateProductSplitUsingGET

```



## ⚙️ Executando os testes unitários

Entre na pasta do projeto:

```
cd product-processor
```

Execute os testes com o Maven:

```
mvn clean test
```

## 📦 Implantação

Entre na pasta raiz do projeto:

```
cd product-processor
```

Gerar a imagem do Docker::

```
docker build -t product-processor .
```

Subir os contêineres:

```
docker-compose up
```

## ✒️ Autores

* **Thiago Albuquerque**

