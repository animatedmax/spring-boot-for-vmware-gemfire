/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.geode.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.client.ClientCache;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * The {@link UseMemberName} annotation configures the {@literal name} of the member in the Apache Geode
 * distributed system, whether the member is a {@link ClientCache client} in the client/server topology
 * or a {@link Cache peer Cache member} in the cluster using the P2P topology.
 *
 * @author John Blum
 * @see java.lang.annotation.Documented
 * @see java.lang.annotation.Inherited
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.Target
 * @see org.apache.geode.cache.Cache
 * @see org.apache.geode.cache.client.ClientCache
 * @see org.springframework.context.annotation.Import
 * @see org.springframework.core.annotation.AliasFor
 * @see org.springframework.geode.config.annotation.MemberNameConfiguration
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(MemberNameConfiguration.class)
public @interface UseMemberName {

	/**
	 * Alias for the {@link String name} of the Apache Geode distributed system member.
	 *
	 * @see #value()
	 */
	@AliasFor("value")
	String name() default "";

	/**
	 * {@link String Name} used for the Apache Geode distributed system member.
	 *
	 * @see #name()
	 */
	@AliasFor("name")
	String value() default "";

}
