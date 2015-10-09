# Yandex.Money SDK for Java

## Overview

This Java library contains classes that allows you to do payments using Yandex.Money public API.

## Requirements

The library uses:

* [OkHttp][1] 2.3.0
* [Google GSON][2] 2.3.1
* [Joda-Time][7] 2.7

## Usage

### Gradle Dependency (jCenter)

[![Download](https://api.bintray.com/packages/yandex-money/maven/yandex-money-sdk-java/images/download.svg)]
(https://bintray.com/yandex-money/maven/yandex-money-sdk-java/_latestVersion)

To use the library in your project write this code to your build.gradle:

```java
buildscript {
    repositories {
        jcenter()
    }
}

dependencies {
    compile 'com.yandex.money.api:yandex-money-sdk-java:4.2.3'
}
```

### App Registration

To be able to use the library you: the first thing you need to do is to register your application and get your unique *client id*. To do that please follow the steps described on [this page][3] (also available in [Russian][4]).

### Conception

All API methods are represented as classes in package `com.yandex.money.api.methods`.

Some methods require an unique id to get the response. To get it use API method `instance-id` passing your *client id*. Once obtained *instance id* can be used to perform those methods.

**Do NOT request *instance id* every time you need to call an API method. Obtain it once and reuse it.**

### Performing Request

To perform request from `com.yandex.money.api.methods` package you may want to instantiate `OAuth2Session` providing `ApiClient`. For your convenience we also include `DefaultApiClient`. Also most of the methods require authorization token that will be used to authorize a user.

```Java
OAuth2Session session = new OAuth2Session(new DefaultApiClient("your_client_id"));
session.setAccessToken("access_token");
```

Once a session was created, you can perform any requests both synchronously and asynchronously using `execute` and `enqueue` methods respectively. For example:

```Java
InstanceId instanceId = session.execute(new InstanceId.Request(clientId));
// do something with instance id
```

## Links

1. Yandex.Money API (in [English][5], in [Russian][6])
2. [Yandex.Money Java SDK on Bintray][8]

[1]: http://square.github.io/okhttp/
[2]: https://code.google.com/p/google-gson/
[3]: http://api.yandex.com/money/doc/dg/tasks/register-client.xml
[4]: http://api.yandex.ru/money/doc/dg/tasks/register-client.xml
[5]: http://api.yandex.com/money/
[6]: http://api.yandex.ru/money/
[7]: http://www.joda.org/joda-time/
[8]: https://bintray.com/yandex-money/maven/yandex-money-sdk-java/view
