# Three-DS Server Client

Библиотека взаимодействия клиента с 3DS сервисом RBK.money

Отправляем PArq, получаем ответ, перенаправляем на адаптер при этом сохраняем необходимые поля в state
Когда адаптер получает запрос, вызываем процессинг, достаем из state нужные поля и сохраняем termination_uri,
он нам пригодится позднее
Адаптер готовит html страницу, которая перенаправит пользователя на acs
Получаем от ACS html, декодируем cres, анализируем ответ, делаем платеж не успешным или инициируем
После чего достаем ранее сохранненый termination_uri и перенаправляем пользователя на него

### Настройки

Добавить в `pom.xml` в зависимости

```
<dependency>
    <groupId>com.rbkmoney</groupId>
    <artifactId>three-ds-server-client</artifactId>
    <version>${three-ds-server-client.version}</version>
</dependency>
```

В зависимостях также должны быть указаны (дополнить зависимости)

```
<dependency>
    <groupId>com.rbkmoney</groupId>
    <artifactId>damsel</artifactId>
    <version>${damsel.version}</version>
</dependency>
<dependency>
    <groupId>com.rbkmoney</groupId>
    <artifactId>cds-proto</artifactId>
    <version>${cds-proto.version}</version>
</dependency>
<dependency>
    <groupId>com.rbkmoney.adapter-thrift-lib</groupId>
    <artifactId>cds-utils</artifactId>
    <version>${adapter-thrift-lib.version}</version>
</dependency>
<dependency>
    <groupId>com.rbkmoney.adapter-thrift-lib</groupId>
    <artifactId>damsel-utils</artifactId>
    <version>${adapter-thrift-lib.version}</version>
</dependency>

```

и в `application.yml`

```
mpi:
  client:
    url: http://127.0.0.1:8090/sdk
  rest-template-settings:
    requestTimeout: 60000
    poolTimeout: 10000
    connectionTimeout: 10000
    maxTotalPooling: 200
    defaultMaxPerRoute: 200
```


При подключенной зависимости без указания настроек в `application.yml` и запуске приложения - оно выдаст ошибку, что не был указан URL и как это исправить

При создании клиента необходимо передать ObjectMapper с дополнительными настройками

```
ObjectMapper om = new ObjectMapper();
om.findAndRegisterModules();
om.registerModule(new JavaTimeModule());
```

### Использование


Для того, чтобы начать пользоваться библиотекой после подключения, необходимо просто добавить

```
@Autowired
Mpi3DsClient mpiClient;

AuthenticationResponse response = mpiClient.authentication(request);

```
