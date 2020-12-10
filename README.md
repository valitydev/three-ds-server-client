# three-ds-server-client


Библиотека взаимодействия клиента с 3DS сервисом RBK.money


## схема интеграции

1) 3DS Versioning ```threeDsClient.threeDsVersioning(.)```
2) 3DS Method ```threeDsClient.threeDsMethod(.)```
3) 3DS Authentication ```threeDsClient.emvcoAuthentication(.)```
4) ... ? todo


todo **Добавить схему инетграции клиента с адаптерамии после тестирования 3дс транзакции с нспк**


## Настройки


```xml
<dependency>
    <groupId>com.rbkmoney</groupId>
    <artifactId>three-ds-server-client</artifactId>
    <version>${three-ds-server-client.version}</version>
</dependency>
```

```yaml
client:
    three-ds-server:
        enabled: true
        sdk-url: https://three-ds-server:8080/sdk
        versioning-url: https://three-ds-server:8080/versioning
        three-ds-method-url: https://three-ds-server:8080/three-ds-method
        readTimeout: 10000
        connectTimeout: 5000
```


## Использование


```java
@Autowired
private final ThreeDsClient threeDsClient;
```
