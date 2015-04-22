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
package com.mgmtp.jfunk.data.generator.constraint;

import org.jdom.Element;

import com.mgmtp.jfunk.common.random.MathRandom;
import com.mgmtp.jfunk.data.generator.Generator;
import com.mgmtp.jfunk.data.generator.constraint.base.SourceConstraint;
import com.mgmtp.jfunk.data.generator.control.FieldCase;
import com.mgmtp.jfunk.data.generator.util.XMLTags;

/**
 * The constraint maps a null value of an embedded constraint to a fixed value.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code 
 * <constraint id="c1" class="com.mgmtp.jfunk.data.generator.constraint.NullMapping">
 *   <constant>0,00</constant>
 *   <constraint>
 *     <constraint_ref id="..."/>
 *   </constraint>
 * </constraint>
 * }
 * </pre>
 * 
 * If the embedded constraint returns {@code null}, c1 will be set to 0,00. Otherwise the value of
 * the embedded constraint is used as the value for c1.
 * 
 */
public class NullMapping extends SourceConstraint {

	private final String defaultValue;

	public NullMapping(final MathRandom random, final Element element, final Generator generator) {
		super(random, element, generator);
		defaultValue = element.getChildText(XMLTags.CONSTANT);
	}

	/**
	 * Calls {@link #initValues(FieldCase)} on the source constraint. If this returns {@code null}
	 * the defaulValue will be returned
	 */
	@Override
	public String initValuesImpl(final FieldCase c) {
		if (c == FieldCase.NULL) {
			return null;
		}
		String value = source.initValues(c);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
}