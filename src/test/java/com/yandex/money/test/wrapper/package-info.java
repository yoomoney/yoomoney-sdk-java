/**
 * Package for create stub requests
 *
 * Example:
 *
 * DefaultApiClient apiClient = new CustomApiClient(myClientId,  "https://my.stub.com")
 *
 * OAuth2Session oAuth2Session = new OAuth2Session(apiClient);
 *
 * oAuth2Session.execute(new CustomPathRequestWrapper<>(new Token.Revoke(), "/token/revoke"));
 *
 */
package com.yandex.money.test.wrapper;