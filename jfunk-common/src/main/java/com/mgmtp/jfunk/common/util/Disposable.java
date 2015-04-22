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
package com.mgmtp.jfunk.common.util;

/**
 * Interface for some disposal logic. Implementers of this interface may be registered to be called
 * either after module or after script execution.
 * 
 * @param <T>
 *            type of the object to be disposed
 * @author rnaegele
 */
public interface Disposable<T> {

	/**
	 * Performs some disposal logic.
	 * 
	 * @param source
	 *            the object subject to some disposal logic
	 */
	void dispose(T source);
}
