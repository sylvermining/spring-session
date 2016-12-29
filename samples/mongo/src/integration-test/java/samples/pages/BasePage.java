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

package samples.pages;

import org.openqa.selenium.WebDriver;

/**
 * @author @silv3rmining
 */
public abstract class BasePage {

	private static final String WEB_PORT = "server.port";

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
}