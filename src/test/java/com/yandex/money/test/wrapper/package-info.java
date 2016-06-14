/**
 * Package for create stub requests
 *
 * Example:
 *
 *
 * DefaultApiClient apiClient = new CustomApiClient(myClientId,  "https://my.stub.com")
 *
 * String tokenRevokePath = "/token/revoke"
 *
 *  OAuth2Session oAuth2Session = new OAuth2Session(apiClient);
 *  oAuth2Session.execute(new CustomPathRequestWrapper<>(new Token.Revoke(), tokenRevokePath);
 *
 */
package com.yandex.money.test.wrapper;