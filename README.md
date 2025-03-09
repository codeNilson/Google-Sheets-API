# Google Sheet API - GSAPI

GSAPI Core é uma biblioteca Java para interagir com a API do Google Sheets.
Ela oferece métodos simples para autenticação, leitura, escrita, atualização e exclusão de dados
em planilhas.

## Status do Projeto
Este projeto está em andamento. Novas funcionalidades, testes adicionais e melhorias na documentação estão sendo desenvolvidos. Em breve, a biblioteca estará disponível no Maven Repository, e uma documentação mais detalhada será publicada.

## Requisitos
- Java 8 ou superior
- Maven
## Instalação
Adicione a dependência abaixo no seu arquivo `pom.xml`:

```pom.xml
<dependency>
  <groupId>io.github.codenilson</groupId>
  <artifactId>gsapi-core</artifactId>
  <version>1.0.0</version>
</dependency>
```


## Uso
### Autenticação
Para utilizar a API do Google Sheets, você precisa de um arquivo de credenciais JSON.
Obtenha-o no [Google Cloud Platform](https://console.cloud.google.com/).

### Exemplo de Uso



```java
import io.github.codenilson.gsapi_core.GsAPI;
import io.github.codenilson.gsapi_core.DataValue;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        String applicationName = "My Application";
        String spreadsheetId = "your-spreadsheet-id";
        String jsonPath = "path/to/credentials.json";

        GsAPI gsAPI = new GsAPI(applicationName, spreadsheetId, jsonPath);

        // Ler dados de uma planilha
        Optional<List<DataValue>> data = gsAPI.getSheet("Sheet1!A1:D10");
        data.ifPresent(values -> values.forEach(System.out::println));

        // Adicionar dados a uma planilha
        List<List<Object>> valuesToAppend = List.of(
            List.of("Value1", "Value2"),
            List.of("Value3", "Value4")
        );
        gsAPI.appendSheet("Sheet1!A1", valuesToAppend);

        // Atualizar dados em uma planilha
        List<List<Object>> valuesToUpdate = List.of(
            List.of("UpdatedValue1", "UpdatedValue2")
        );
        gsAPI.updateSheet("Sheet1!A1", valuesToUpdate);

        // Deletar dados de uma planilha
        gsAPI.deleteRow("Sheet1!A2");
    }
}
```

## Testes
Para executar os testes, utilize o seguinte comando:
`mvn test`
## Contribuição
Contribuições são sempre bem-vindas! Para enviar suas alterações, siga os passos abaixo:
1. Faça um fork do projeto.
   2. Crie uma branch para sua feature:
 git checkout -b feature/nova-feature
3. Realize commit das suas alterações:
 git commit -am 'Adiciona nova feature'
4. Envie sua branch para o repositório remoto:
 git push origin feature/nova-feature
5. Abra um novo Pull Request.
