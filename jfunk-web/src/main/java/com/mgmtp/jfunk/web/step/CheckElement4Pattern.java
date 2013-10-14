/*
 * Copyright (c) 2013 mgm technology partners GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mgmtp.jfunk.web.step;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.mgmtp.jfunk.core.exception.StepException;

/**
 * Checks a WebElement for a given pattern.
 */
public class CheckElement4Pattern extends WebDriverStep {
	private final By by;
	private final Pattern pattern;

	/**
	 * @param by
	 *            By means of this value the HTML element is searched after
	 * @param pattern
	 *            a regular expression pattern which must match the element's content
	 */
	public CheckElement4Pattern(final By by, final String pattern) {
		this.by = by;
		this.pattern = Pattern.compile(pattern);
	}

	/**
	 * @throws StepException
	 *             if element specified by {@link By} object in the constructor cannot be found or the regex does not match
	 */
	@Override
	public void execute() throws StepException {
		log.info("Executing: " + this);

		List<WebElement> webElements = getWebDriver().findElements(by);
		if (webElements.isEmpty()) {
			throw new StepException("Could not find any matching element");
		}

		/*
		 * If the search using the By object does find more than one matching element we are looping through all elements if we
		 * find at least one which matches the criteria below. If not, an exception is thrown.
		 */
		for (WebElement element : webElements) {
			if (element.isDisplayed()) {
				if (element.isEnabled()) {
					String actualValue = element.getText();
					Matcher m = pattern.matcher(actualValue);
					if (!m.matches()) {
						throw new StepException("Found a matching element, but the regex '" + pattern + "'does not match.");
					}
					log.debug("Found matching element");
					return;
				}
			}
		}

		throw new StepException("All elements matching by=" + by + " were either invisible or disabled");
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("by", by);
		tsb.append("pattern", pattern.toString());
		return tsb.toString();
	}
}