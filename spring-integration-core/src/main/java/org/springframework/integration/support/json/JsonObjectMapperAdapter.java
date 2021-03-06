/*
 * Copyright 2013-2019 the original author or authors.
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

package org.springframework.integration.support.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import org.springframework.integration.mapping.support.JsonHeaders;

/**
 * Simple {@linkplain JsonObjectMapper} adapter implementation, if there is no need
 * to provide entire operations implementation.
 *
 * @author Artem Bilan
 * @author Gary Russell
 * @since 3.0
 */
public abstract class JsonObjectMapperAdapter<N, P> implements JsonObjectMapper<N, P> {

	@Override
	public String toJson(Object value) throws IOException {
		return null;
	}

	@Override
	public void toJson(Object value, Writer writer) throws IOException {
	}

	@Override
	public N toJsonNode(Object value) throws IOException {
		return null;
	}

	@Override
	public <T> T fromJson(Object json, Class<T> valueType) throws IOException {
		return null;
	}

	@Override
	public <T> T fromJson(P parser, Type valueType) throws IOException {
		return null;
	}

	@Override
	public <T> T fromJson(Object json, Map<String, Object> javaTypes) throws IOException {
		return null;
	}

	@Override
	public void populateJavaTypes(Map<String, Object> map, Object object) {
		map.put(JsonHeaders.TYPE_ID, object.getClass());
		if (object instanceof Collection && !((Collection<?>) object).isEmpty()) {
			Object firstElement = ((Collection<?>) object).iterator().next();
			map.put(JsonHeaders.CONTENT_TYPE_ID, firstElement != null ? firstElement.getClass() : Object.class);
		}
		if (object instanceof Map && !((Map<?, ?>) object).isEmpty()) {
			Object firstValue = ((Map<?, ?>) object).values().iterator().next();
			map.put(JsonHeaders.CONTENT_TYPE_ID, firstValue != null ? firstValue.getClass() : Object.class);
			Object firstKey = ((Map<?, ?>) object).keySet().iterator().next();
			map.put(JsonHeaders.KEY_TYPE_ID, firstKey != null ? firstKey.getClass() : Object.class);
		}
	}

}
