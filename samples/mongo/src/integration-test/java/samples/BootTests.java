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

package samples;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import sample.Application;
import samples.pages.HomePage;
import samples.pages.LoginPage;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author @silv3rmining
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BootTests {

	private WebDriver driver;

	@Before
	public void setUp() {
		this.driver = new HtmlUnitDriver();
	}

	@Test
	public void unauthenticatedUserSentToLogInPage() {
		HomePage homePage = HomePage.go(this.driver);
		LoginPage loginPage = homePage.unauthenticated();
		loginPage.assertAt();
	}

	@Test
	public void logInViewsHomePage() {
		LoginPage loginPage = LoginPage.go(this.driver);
		loginPage.assertAt();
		HomePage homePage = loginPage.login("user", "password");
		homePage.assertAt();
		WebElement username = homePage.getDriver().findElement(By.id("un"));
		assertThat(username.getText()).isEqualTo("user");
		Set<Cookie> cookies = homePage.getDriver().manage().getCookies();
		assertThat(cookies).extracting("name").contains("SESSION");
		assertThat(cookies).extracting("name").doesNotContain("JSESSIONID");
	}

	@Test
	public void logoutSuccess() {
		LoginPage loginPage = LoginPage.go(this.driver);
		HomePage homePage = loginPage.login("user", "password");
		LoginPage successLogoutPage = homePage.logout();
		successLogoutPage.assertAt();
	}

	@Test
	public void loggedOutUserSentToLoginPage() {
		LoginPage loginPage = LoginPage.go(this.driver);
		HomePage homePage = loginPage.login("user", "password");
		homePage.logout();
		HomePage backHomePage = HomePage.go(this.driver);
		LoginPage backLoginPage = backHomePage.unauthenticated();
		backLoginPage.assertAt();
	}
}
