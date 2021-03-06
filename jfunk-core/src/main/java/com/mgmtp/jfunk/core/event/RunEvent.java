/*
 * Copyright (c) 2015 mgm technology partners GmbH
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
package com.mgmtp.jfunk.core.event;

import static com.google.common.collect.Maps.newHashMapWithExpectedSize;

import java.util.Map;

/**
 * @author rnaegele
 */
public class RunEvent extends AbstractBaseEvent {

	private final Map<String, Object> parameters = newHashMapWithExpectedSize(2);

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public Object getParameter(final String name) {
		return parameters.get(name);
	}

	public void addParameter(final String name, final Object value) {
		parameters.put(name, value);
	}
}
