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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.mgmtp.jfunk.common.util.Configuration;
import com.mgmtp.jfunk.core.exception.PatternException;

/**
 * This steps searches the current HTML page for a given regex pattern. If the pattern contains a grouping expression it is
 * possible to store the corresponding value in a property key.
 */
public class CheckHtml4Pattern extends WebDriverStep {
	private final Pattern pattern;
	private final boolean mustExist;
	private final String groupKey;
	private String groupValue;

	@Inject
	Configuration config;

	/**
	 * Creates a new instance of CheckHtml4Pattern. The given regex pattern must exist on the page.
	 * 
	 * @param regex
	 *            the regex pattern to search for
	 */
	public CheckHtml4Pattern(final String regex) {
		this(regex, null, true);
	}

	/**
	 * Creates a new instance of CheckHtml4Pattern. Depending on the value of the parameter {@code mustExist} the pattern must
	 * exist or must not exist on the page.
	 * 
	 * @param regex
	 *            the regex pattern to search for
	 * @param mustExist
	 *            if {@code true} the regex pattern has to exist on the page, if {@code false} the pattern must not exist
	 */
	public CheckHtml4Pattern(final String regex, final boolean mustExist) {
		this(regex, null, mustExist);
	}

	/**
	 * Creates a new instance of CheckHtml4Pattern. Depending on the value of the parameter {@code mustExist} the pattern must
	 * exist or must not exist on the page. Additionally, the name of a property key can be passed in which is then used to store
	 * the value of the grouping expression.
	 * 
	 * @param regex
	 *            the regex pattern to search for
	 * @param groupKey
	 *            if the regex pattern matches, the regex pattern has a grouping expression and the parameter {@code groupKey} is
	 *            not null, the corresponding value will be stored as a property with the given property key. If the regex pattern
	 *            consists of more than one grouping expression, the first one will be used.
	 * @param mustExist
	 *            if {@code true} the regex pattern has to exist on the page, if {@code false} the pattern must not exist
	 */
	public CheckHtml4Pattern(final String regex, final String groupKey, final boolean mustExist) {
		pattern = Pattern.compile(regex);
		this.groupKey = groupKey;
		this.mustExist = mustExist;
	}

	/**
	 * Returns the value of the first grouping expression in the regex pattern.
	 * 
	 * @return the value of the first grouping expression in the regex pattern. Can be {@code null} if the regex pattern didn't
	 *         match or didn't contain a grouping expression.
	 */
	public String getGroupValue() {
		return groupValue;
	}

	@Override
	public void execute() {
		String pageSource = getWebDriver().getPageSource().replaceAll("\\s+", " ");
		if (log.isTraceEnabled()) {
			log.trace("pageSource=" + pageSource);
		}
		if (mustExist) {
			log.info("Regex pattern " + pattern + " has to match");
		} else {
			log.info("Regex pattern " + pattern + " must not match");
		}
		Matcher matcher = pattern.matcher(pageSource);
		boolean match = matcher.matches();
		if (match && matcher.groupCount() > 0) {
			// grouping stuff
			groupValue = matcher.group(1);
			if (groupKey != null && groupKey.length() > 0) {
				log.info("Setting " + groupKey + " = " + groupValue);
				config.put(groupKey, groupValue);
			}
		}
		if (mustExist != match) {
			throw new PatternException("HTML code", pattern, mustExist);
		}
	}
}