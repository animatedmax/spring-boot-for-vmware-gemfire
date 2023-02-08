/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.geode.cache.support;

import org.apache.geode.cache.CacheLoader;

/**
 * The {@link CacheLoaderSupport} interface is an extension of {@link CacheLoader} and a {@link FunctionalInterface}
 * useful in Lambda expressions.
 *
 * @author John Blum
 * @see java.lang.FunctionalInterface
 * @see org.apache.geode.cache.CacheLoader
 * @since 1.0.0
 */
@FunctionalInterface
public interface CacheLoaderSupport<K, V> extends CacheLoader<K, V> {

	/**
	 * Closes any resources opened and used by this {@link CacheLoader}.
	 *
	 * @see org.apache.geode.cache.CacheLoader#close()
	 */
	@Override
	default void close() {}

}
