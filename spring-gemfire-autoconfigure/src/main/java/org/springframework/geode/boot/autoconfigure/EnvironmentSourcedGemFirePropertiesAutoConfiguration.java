/*
 * Copyright (c) VMware, Inc. 2022. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.geode.boot.autoconfigure;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.geode.cache.GemFireCache;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.data.gemfire.CacheFactoryBean;
import org.springframework.data.gemfire.GemFireProperties;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.config.annotation.PeerCacheConfigurer;
import org.springframework.data.gemfire.util.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring Boot {@link EnableAutoConfiguration auto-configuration} enabling the processing of
 * {@literal gemfire.properties}, or Geode {@link Properties} declared in Spring Boot {@literal application.properties}.
 *
 * @author John Blum
 * @see Properties
 * @see GemFireCache
 * @see SpringBootConfiguration
 * @see EnableAutoConfiguration
 * @see Bean
 * @see Ordered
 * @see Order
 * @see ConfigurableEnvironment
 * @see EnumerablePropertySource
 * @see MutablePropertySources
 * @see CacheFactoryBean
 * @see GemFireProperties
 * @see ClientCacheConfigurer
 * @see PeerCacheConfigurer
 * @see <a href="https://geode.apache.org/docs/guide/112/reference/topics/gemfire_properties.html">Geode Properties</a>
 * @since 1.3.0
 */
@SpringBootConfiguration
@ConditionalOnClass({ GemFireCache.class, CacheFactoryBean.class })
@AutoConfigureBefore({ ClientCacheAutoConfiguration.class })
@SuppressWarnings("unused")
public class EnvironmentSourcedGemFirePropertiesAutoConfiguration {

	private static final String GEMFIRE_PROPERTY_PREFIX = GemFireProperties.GEMFIRE_PROPERTY_NAME_PREFIX;

	private final Logger logger = LoggerFactory.getLogger(EnvironmentSourcedGemFirePropertiesAutoConfiguration.class);

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	@SuppressWarnings("all")
	public ClientCacheConfigurer clientCacheGemFirePropertiesConfigurer(ConfigurableEnvironment environment) {
		return (beanName, bean) -> configureGemFireProperties(environment, bean);
	}

	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
	@SuppressWarnings("all")
	public PeerCacheConfigurer peerCacheGemFirePropertiesConfigurer(ConfigurableEnvironment environment) {
		return (beanName, bean) -> configureGemFireProperties(environment, bean);
	}

	protected void configureGemFireProperties(@NonNull ConfigurableEnvironment environment,
			@NonNull CacheFactoryBean cache) {

		Assert.notNull(environment, "Environment must not be null");
		Assert.notNull(cache, "CacheFactoryBean must not be null");

		MutablePropertySources propertySources = environment.getPropertySources();

		if (propertySources != null) {

			Set<String> gemfirePropertyNames = propertySources.stream()
				.filter(EnumerablePropertySource.class::isInstance)
				.map(EnumerablePropertySource.class::cast)
				.map(EnumerablePropertySource::getPropertyNames)
				.map(propertyNamesArray -> ArrayUtils.nullSafeArray(propertyNamesArray, String.class))
				.flatMap(Arrays::stream)
				.filter(this::isGemFireDotPrefixedProperty)
				.filter(this::isValidGemFireProperty)
				.collect(Collectors.toSet());

			Properties gemfireProperties = cache.getProperties();

			gemfirePropertyNames.stream()
				.filter(gemfirePropertyName -> isNotSet(gemfireProperties, gemfirePropertyName))
				.filter(this::isValidGemFireProperty)
				.forEach(gemfirePropertyName -> {

					String propertyName = normalizeGemFirePropertyName(gemfirePropertyName);
					String propertyValue = environment.getProperty(gemfirePropertyName);

					if (StringUtils.hasText(propertyValue)) {
						gemfireProperties.setProperty(propertyName, propertyValue);
					}
					else {
						getLogger().warn("Apache Geode Property [{}] was not set", propertyName);
					}
				});

			cache.setProperties(gemfireProperties);
		}
	}

	protected Logger getLogger() {
		return this.logger;
	}

	private boolean isGemFireDotPrefixedProperty(@NonNull String propertyName) {
		return StringUtils.hasText(propertyName) && propertyName.startsWith(GEMFIRE_PROPERTY_PREFIX);
	}

	private boolean isNotSet(Properties gemfireProperties, String propertyName) {
		return !gemfireProperties.containsKey(normalizeGemFirePropertyName(propertyName));
	}

	private boolean isValidGemFireProperty(String propertyName) {
		try {
			GemFireProperties.from(normalizeGemFirePropertyName(propertyName));
			return true;
		}
		catch (IllegalArgumentException cause) {
			getLogger().warn(String.format("[%s] is not a valid Apache Geode property", propertyName));
			// TODO: uncomment line below and replace line above when SBDG is rebased on SDG 2.3.0.RC2 or later.
			//getLogger().warn(cause.getMessage());
			return false;
		}
	}

	private String normalizeGemFirePropertyName(@NonNull String propertyName) {

		int index = propertyName.lastIndexOf(".");

		return index > -1 ? propertyName.substring(index + 1) : propertyName;
	}
}
