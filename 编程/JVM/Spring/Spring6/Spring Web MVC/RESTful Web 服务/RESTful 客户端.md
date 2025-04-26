`RestTemplate` 用于同步访问 RESTful 服务，`WebClient` 用于异步访问 RESTful 服务。

# `RestTemplate`

同步访问 RESTful 服务

```java
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
    return restTemplate;
}

class MyErrorHandler extends DefaultResponseErrorHandler {

    private static Logger LOGGER = LogManager.getLogger();
  
    @Override
    protected void handleError(ClientHttpResponse response, 
                               HttpStatusCode statusCode) throws IOException {
        LOGGER.warn("Status code received from service: {}", statusCode);
        String body = StreamUtils.copyToString(response.getBody(), 
                                               StandardCharsets.UTF_8);
        LOGGER.warn("Response body: {}", body);
        super.handleError(response, statusCode);
    }
}
```

```java
public static void main(String[] args) {
    ConfigurableApplicationContext context = 
                               SpringApplication.run(BankApp.class);
    RestTemplate restTemplate = context.getBean(RestTemplate.class);
    getFixedDepositList(restTemplate);
}

void getFixedDepositList(RestTemplate restTemplate) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    HttpEntity<List<FixedDepositDetails>> request = new HttpEntity<>(headers);
    ParameterizedTypeReference<List<FixedDepositDetails>> typeRef
            = new ParameterizedTypeReference<>() {};

    ResponseEntity<List<FixedDepositDetails>> response = restTemplate
            .exchange("http://localhost:8080/fixedDeposits",
                      HttpMethod.GET, request, typeRef);

    List<FixedDepositDetails> depositDetails = response.getBody();
    LOGGER.info("List of fixed deposit details: \n{}", depositDetails);
}
```

# `WebClient`

异步访问 RESTful 服务

>[!note] 依赖：
> - `implementation 'org.springframework.boot:spring-boot-starter-webflux'`  
> - `testImplementation 'io.projectreactor:reactor-test'`

```java
WebClient webClient = WebClient.create("http://localhost:8080");
webClient.get().uri("/fixedDeposits")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve() // 发送请求并接收相应
        .bodyToFlux(FixedDepositDetails.class)
        .subscribe(fdd -> LOGGER.info("getFixedDepositList: receive {}", fdd));
```

‍
