/*
 * Copyright (c) 2014 mgm technology partners GmbH
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
package com.mgmtp.jfunk.data.generator.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mgmtp.jfunk.data.generator.GeneratorConstants;

/**
 * Represents the {@code indexed fields} element in the "indexed-fields" configuration file (defined
 * by {@link GeneratorConstants#INDEXED_FIELDS_FILE}).
 * 
 */
public class IndexedFields {

	private final Map<String, Set<FieldSet>> fieldSetsMap = Maps.newHashMap();

	public void addFieldSet(final FieldSet fieldSet) {
		Set<FieldSet> fieldSets = fieldSetsMap.get(fieldSet.getDataKey());
		if (fieldSets == null) {
			fieldSets = Sets.newLinkedHashSet();
			fieldSetsMap.put(fieldSet.getDataKey(), fieldSets);
		}
		fieldSets.add(fieldSet);
	}

	public Set<FieldSet> getFieldSets(final String dataKey) {
		Set<FieldSet> result = fieldSetsMap.get(dataKey);
		if (result != null) {
			return Collections.unmodifiableSet(result);
		}
		return Collections.emptySet();
	}

	public Collection<Set<FieldSet>> getFieldSets() {
		return Collections.unmodifiableCollection(fieldSetsMap.values());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}