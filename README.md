<h1>Invoice Payment Module</h1>

<h3>Требования</h3>
1. Spring Boot
2. Spring Data JPA

<h3>Установка</h3>

1.Скачайте [библиотеку](https://github.com/Invoice-LLC/Invoice.SDK.Spring/raw/master/invoice.jar)

2.**Maven**<br>
Добавьте зависимость в pom.xml
```xml
<dependency>
    <groupId>su.invoice</groupId>
    <artifactId>Invoice.Spring</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/invoice.jar</systemPath>
</dependency>
```
3.Добавьте в главный класс следующие аннотации:
```java
@ComponentScans(@ComponentScan(basePackages = {"su.invoice.spring.starter", "su.invoice.spring.controllers"}))
@EntityScan({"su.invoice.spring.database.entities", "your.entities.package"})
@EnableJpaRepositories(basePackages = {"su.invoice.spring.database", "your.repos.package"})
```
4.В файле src/main/resources/application.properties необходимо задать следующие настройик:
```
su.invoice.module.apiKey=Ваш_API_Ключ
su.invoice.module.login=Ваш_логин_от_ЛК_Invoice
su.invoice.module.defaultTerminalName=Имя_терминала
su.invoice.module.successUrl=Ссылка_при_успешной_оплате
su.invoice.module.failUrl=Ссылка_при_неудачной_оплате
```
5.В личном кабинете Invoice(Настройки->Уведомления->Добавить) добавьте уведомление с типом WebHook и адресом, который вы задали в конфиге(например: %url%/invoice/notification)

<h3>Пример</h3>
```java

```
