package org.apache.tapestry5.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.internal.util.Defense;

public class MyPrimaryKeyEncoder<K extends Serializable, V> implements PrimaryKeyEncoder<K, V> {
	
	private final Map<K, V> keyToValue = new LinkedHashMap<K, V>();

	private final Map<V, K> valueToKey = CollectionFactory.newMap();

	private K currentKey;

	public final void add(K key, V value) {
		Defense.notNull(key, "key");
		Defense.notNull(value, "value");

		V existing = keyToValue.get(key);
		if (existing != null)
			throw new IllegalArgumentException(PublicUtilMessages.duplicateKey(key, value, existing));

		keyToValue.put(key, value);

		valueToKey.put(value, key);
	}

	public final void clear() {
		keyToValue.clear();
		valueToKey.clear();
		currentKey = null;
	}

	public final List<V> getValues() {
		List<V> result = CollectionFactory.newList();

		for (Map.Entry<K, V> entry : keyToValue.entrySet()) {
			result.add(entry.getValue());
		}

		return result;
	}

	public final K toKey(V value) {
		Defense.notNull(value, "value");

		currentKey = valueToKey.get(value);

		if (currentKey == null)
			throw new IllegalArgumentException(PublicUtilMessages.missingValue(value, valueToKey.keySet()));

		return currentKey;
	}

	public final V toValue(K key) {
		V result = keyToValue.get(key);

		if (result == null) {
			result = provideMissingObject(key);

			currentKey = key;
		} else {
			currentKey = key;
		}

		return result;
	}

	protected V provideMissingObject(K key) {
		return null;
	}

	public final void deleted(K key) {

		if (keyToValue.containsKey(key)) {
			keyToValue.remove(key);
			valueToKey.remove(keyToValue.get(key));
		}

	}

	/**
	 * 删除当前key-value对
	 */
	public final void deleted() {
		deleted(currentKey);
	}

	public final void deleted(V value) {

		if (valueToKey.containsKey(value)) {
			valueToKey.remove(value);
			keyToValue.remove(valueToKey.get(value));
		}

	}

	/**
	 * Does nothing. Subclasses may override as necessary.
	 */
	public void prepareForKeys(List<K> keys) {
	}

}
