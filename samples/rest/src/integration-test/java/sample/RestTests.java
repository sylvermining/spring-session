/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import org.junit.Before;
import org.junit.Test;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author @silv3rmining
 */
public class RestTests {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BASIC = "Basic ";
	private static final String X_AUTH_TOKEN = "x-auth-token";
	private static final String WEB_PORT = "geb.build.port";

	private TestRestTemplate restTemplate;

	private String baseUrl;

	@Before
	public void setUp() {
		this.baseUrl = "http://localhost:" + System.getProperty(WEB_PORT);
		this.restTemplate = new TestRestTemplate();
	}

	@Test
	public void unauthenticatedUserSentToLogInPage() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		ResponseEntity<String> entity = getForUser(this.baseUrl + "/",
				headers, String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	public void authenticateWithBasicWorks() {
		String auth = getAuth("user", "password");
		HttpHeaders headers = getHttpHeaders();
		headers.set(AUTHORIZATION, BASIC + auth);
		ResponseEntity<User> entity = getForUser(this.baseUrl + "/",
				headers, User.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getHeaders().containsKey(X_AUTH_TOKEN)).isTrue();
		assertThat(entity.getBody().getUsername()).isEqualTo("user");
	}

	@Test
	public void authenticateWithXAuthTokenWorks() {
		String auth = getAuth("user", "password");
		HttpHeaders headers = getHttpHeaders();
		headers.set(AUTHORIZATION, BASIC + auth);
		ResponseEntity<User> entity = getForUser(this.baseUrl + "/",
				headers, User.class);

		String token = entity.getHeaders().getFirst(X_AUTH_TOKEN);

		HttpHeaders authTokenHeader = new HttpHeaders();
		authTokenHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		authTokenHeader.set(X_AUTH_TOKEN, token);
		ResponseEntity<User> authTokenResponse = getForUser(this.baseUrl + "/",
				authTokenHeader, User.class);
		assertThat(authTokenResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(authTokenResponse.getBody().getUsername()).isEqualTo("user");
	}

	@Test
	public void logout() {
		String auth = getAuth("user", "password");
		HttpHeaders headers = getHttpHeaders();
		headers.set(AUTHORIZATION, BASIC + auth);
		ResponseEntity<User> entity = getForUser(this.baseUrl + "/",
				headers, User.class);

		String token = entity.getHeaders().getFirst(X_AUTH_TOKEN);

		HttpHeaders logoutHeader = getHttpHeaders();
		logoutHeader.set(X_AUTH_TOKEN, token);
		ResponseEntity<User> logoutResponse = getForUser(this.baseUrl + "/logout",
				logoutHeader, User.class);
		assertThat(logoutResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	private <T> ResponseEntity<T> getForUser(String resourceUrl, HttpHeaders headers, Class<T> type) {
		ResponseEntity<T> response = this.restTemplate.exchange(resourceUrl,
				HttpMethod.GET, new HttpEntity<T>(headers), type);
		return response;
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private String getAuth(String user, String password) {
		String auth = user + ":" + password;
		return new String(Base64.encodeBase64(auth.getBytes()));
	}
}
