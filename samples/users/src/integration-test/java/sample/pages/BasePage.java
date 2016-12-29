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

package sample.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author @silv3rmining
 */
public abstract class BasePage {

	private static final String WEB_PORT = "geb.build.port";

	private WebDriver driver;

	public BasePage(WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public static void get(WebDriver driver, String get) {
		String baseUrl = "http://localhost:" + System.getProperty(WEB_PORT);
		driver.get(baseUrl + get);
	}

	public static void getFrom(WebDriver driver, String resourceUrl) {
		driver.get(resourceUrl);
	}

	public static HomePage home(WebDriver driver, String resourceUrl) {
		getFrom(driver, resourceUrl);
		return PageFactory.initElements(driver, HomePage.class);
	}

	public static LinkPage linkPage(WebDriver driver, String resourceUrl) {
		getFrom(driver, resourceUrl);
		return PageFactory.initElements(driver, LinkPage.class);
	}

	public static String getSwitchElementId(String user) {
		return "switchAccount" + user;
	}

	public WebElement getElementById(String id) {
		return this.driver.findElement(By.id(id));
	}

	public String getContentAttributeByElementId(String id, String attribute) {
		WebElement element = getElementById(id);
		assertThat(element.getAttribute(attribute)).isNotEmpty();
		return element.getAttribute(attribute);
	}

}
