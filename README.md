# Yandex.Money SDK for Java

## Overview

This Java library contains classes that allows you to do payments using Yandex.Money public API.

## Requirements

The library uses:

* [OkHttp][1] 1.6.0
* [Google GSON][2] 2.3
* [Joda-Time][7] 2.5

## Usage

### Gradle Dependency (jCenter)

To use the library in your project write this code to your build.gradle:

```java
buildscript {
    repositories {
        jcenter()
    }
}

dependencies {
    compile 'com.yandex.money.api:yandex-money-sdk-java:2.0.3'
}
```

### App Registration

To be able to use the library you: the first thing you need to do is to register your application and get your unique *client id*. To do that please follow the steps described on [this page][3] (also available in [Russian][4]).

### Conception

All API methods are represented as classes in package `com.yandex.money.api.methods`.

Every instance of your application should have an unique id. To get it use API method `instance-id` passing your *client id*. Once obtained *instance id* can be used in other API methods.

**Do NOT request *instance id* every time you need to call API method. Obtain it once and reuse it.**

### Creating `YandexMoney` Context

In order to call API methods you may want to use `YandexMoney` class as a context for all of your requests. You may also want to pass you own HTTP client so there are two constructors in `YandexMoney` class.

### Performing Request

YandexMoney instance can perform a request (call of API method). For example, if you want to get `instance id` using YandexMoney instance you can do it like this:

```Java
...
final String clientId = "[your_client_id]";
YandexMoney ym = new YandexMoney(clientId);
InstanceId response = ym.execute(new InstanceId.Request(clientId));
// handling the response
...
```

All requests are performed synchronously so you may want to call these methods in background thread.

### Payment

There are two API methods you should call when you performing a payment: `request-external-payment` and `process-external-payment`. Corresponding classes in the library are `RequestExternalPayment` and `ProcessExternalPayment`.

When you do `RequestExternalPayment`, you create payment's context:

```Java
...
String patternId = ... // depends on your implementation
Map<String, String> params = ... // depends on your implementation
RequestExternalPayment rep = ym.execute(new RequestExternalPayment.Request.newInstance(instanceId, patternId, params));
String requestId = rep.getRequestId();
...
```

Payment considered completed when all required information is entered and `ProcessExternalPayment` completed successfully.

There are four statuses of `ProcessExternalPayment` (see `Status` class and API documentation for more details):

|Status           |Meaning                                          |
|:----------------|:------------------------------------------------|
|Success          |Payment processed successfully.                  |
|Refused          |Payment refused.                                 |
|In Progress      |Payment in progress.                             |
|Ext Auth Required|External authentication required (i.e. 3D Secure)|

So to process payment you should do something like this:

```Java
public void processPayment(String requestId, ProcessExternalPayment.Request pepRequest)
        throws Exception {

    ProcessExternalPayment pep = ym.execute(pepRequest);
    switch (pep.getStatus()) {
        case SUCCESS:
            // payment succeeded
            break;
        case REFUSED:
            // payment refused
            break;
        case IN_PROGRESS:
            processPayment(requestId, pepRequest);
            break;
        case EXT_AUTH_REQUIRED:
            // show web page for 3D Secure authentication
            break;
    }
}
```

## Links

1. Yandex.Money API (in [English][5], in [Russian][6])

[1]: http://square.github.io/okhttp/
[2]: https://code.google.com/p/google-gson/
[3]: http://api.yandex.com/money/doc/dg/tasks/register-client.xml
[4]: http://api.yandex.ru/money/doc/dg/tasks/register-client.xml
[5]: http://api.yandex.com/money/
[6]: http://api.yandex.ru/money/
[7]: http://www.joda.org/joda-time/
