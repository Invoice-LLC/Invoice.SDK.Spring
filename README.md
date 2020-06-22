<h1>Invoice Payment Module</h1>

<h3>Требования</h3>
1. Spring Boot
2. Spring Data JPA

<h3>Установка</h3>

1.Скачайте [библиотеку](https://invoice.su)

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

<h3>Пример использования:</h3>
```java
@RestController()
public class CheckoutController {
    @Autowired
    OrderRepo orderRepo;

    @Autowired
    InvoicePaymentManager paymentManager;

    @GetMapping("/order/{id}/payment")
    public List<InvoicePayment> getPayments(@PathVariable Integer id) {
        Order order = orderRepo.findFirstById(id);
        if(order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        return paymentManager.getPaymentsByCustomParameters("order", String.valueOf(id));
    }

    @PostMapping("/order/{id}/payment")
    public Map<String, Object> createPayment(@PathVariable Integer id) {
        Order order = orderRepo.findFirstById(id);

        if(order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        Map<String, String> customParameters = new HashMap<>();
        customParameters.put("mail", order.mail);
        customParameters.put("order", String.valueOf(id));

        InvoicePayment payment = paymentManager.createPayment(order.amount, "Тестовый заказ", new ArrayList<>(), customParameters);

        Map<String,Object> paymentInfo = new HashMap<>();
        paymentInfo.put("payment_url", payment.getPaymentUrl());
        paymentInfo.put("qr_code", payment.getQr());
        paymentInfo.put("parameters", customParameters);

        return paymentInfo;
    }

    @PostMapping("/order")
    public String createOrder(@RequestBody CreateOrderForm form) {
        if(form.amount <= 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
        Order order = new Order();
        order.amount = form.amount;
        order.mail = form.mail;

        orderRepo.save(order);
        return String.valueOf(order.id);
    }
}
```