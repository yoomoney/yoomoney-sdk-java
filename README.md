# Yandex.Money SDK for Java

## Overview

This Java library contains classes that allows you to do payments and call other methods of Yandex.Money public API.

## Requirements

The library uses:

* [OkHttp][1] 3.4.1
* [Google GSON][2] 2.7
* [Joda-Time][7] 2.9.4

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
    compile 'com.yandex.money.api:yandex-money-sdk-java:6.1.4'
}
```

### App Registration

To be able to use the library you: the first thing you need to do is to register your application and get your unique
*client id*. To do that please follow the steps described on [this page][3] (also available in [Russian][4]).

### Conception

All API methods are represented as classes in package `com.yandex.money.api.methods`.

Some methods require an unique id to get the response. To get it use API method `instance-id` passing your *client id*.
Once obtained *instance id* can be used to perform those methods.

**Do NOT request *instance id* every time you need to call an API method. Obtain it once, store it safely and reuse it
in future requests.**

### Performing Request

To perform request from `com.yandex.money.api.methods` package you will need to use `ApiClient`. For your convenience
there is default implementation of the `ApiClient` called  `DefaultApiClient`. It is suitable for most cases. So the
very first thing you need to do, is to create `ApiClient`.

The minimal implementation will look like this:

```Java
ApiClient client = new DefaultApiClient.Builder()
    .setClientId("your_client_id_here")
    .create();
```

If you want to perform a method that requires user's authentication you need request access token. The easiest way to
do this is to get `AuthorizationData` from a client:

```Java
AuthorizationParameters parameters = new AuthorizationParameters.Builder()
    ...
    .create();
AuthorizationData data = client.createAuthorizationData(parameters);
```

Provided `AuthorizationParameters` allows you to set request parameters as described [here][9]. When created
`AuthorizationData` will have URL and POST parameters for OAuth2 authorization.

To get the result from redirect uri you may want to use `AuthorizationCodeResponse.parse(redirectUri)` method. If
successful the instance of `AuthorizationCodeResponse` will contain temporary authorization code that must be
immediately exchanged for an access token:

```Java
// parse redirect uri from web browser
AuthorizationCodeResponse response = AuthorizationCodeResponse.parse(redirectUri);

if (response.error == null) {
    // try to get OAuth2 access token
    Token token = client.execute(new Token.Request(response.code, client.getClientId(), myRedirectUri, myClientSecret));
    if (token.error == null) {
        ... // store token.accessToken safely for future uses

        // and authorize client to perform methods that require user's authentication
        client.setAccessToken(token.accessToken);
    } else {
        handleAuthorizationError(token.error);
    }
} else {
    handleAuthorizationError(token.error);
}
```

Now you can perform any request with authorized client. For instance, if you want to get `InstanceId` you can do it like
this:

```Java
InstanceId instanceId = client.execute(new InstanceId.Request(clientId));
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
[9]: https://tech.yandex.com/money/doc/dg/reference/request-access-token-docpage/
