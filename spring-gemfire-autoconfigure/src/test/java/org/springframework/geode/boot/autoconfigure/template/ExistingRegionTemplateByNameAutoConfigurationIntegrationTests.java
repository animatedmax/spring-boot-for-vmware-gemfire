/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.geode.boot.autoconfigure.template;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientRegionShortcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport;
import org.springframework.data.gemfire.tests.mock.annotation.EnableGemFireMockObjects;
import org.springframework.geode.boot.autoconfigure.RegionTemplateAutoConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests for {@link RegionTemplateAutoConfiguration} using explicitly declared {@link Region}
 * bean definition and existing, "named" {@link GemfireTemplate} in a Spring {@link ApplicationContext}.
 *
 * @author John Blum
 * @see org.junit.Test
 * @see org.apache.geode.cache.Region
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.test.context.SpringBootTest
 * @see org.springframework.data.gemfire.GemfireTemplate
 * @see org.springframework.data.gemfire.tests.integration.IntegrationTestsSupport
 * @see org.springframework.data.gemfire.tests.mock.annotation.EnableGemFireMockObjects
 * @see org.springframework.geode.boot.autoconfigure.RegionTemplateAutoConfiguration
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@SuppressWarnings("unused")
public class ExistingRegionTemplateByNameAutoConfigurationIntegrationTests extends IntegrationTestsSupport {

	@Autowired
	private GemfireTemplate exampleTemplate;

	@Resource(name = "Example")
	private Region<Object, Object> example;

	@Test
	public void exampleRegionTemplateIsPresent() {

		assertThat(this.exampleTemplate).isNotNull();
		assertThat(this.exampleTemplate.getRegion()).isEqualTo(this.example);
	}

	@SpringBootApplication
	@EnableGemFireMockObjects
	static class TestConfiguration {

		@Bean("Example")
		public ClientRegionFactoryBean<Object, Object> exampleRegion(GemFireCache gemfireCache) {

			ClientRegionFactoryBean<Object, Object> exampleRegion = new ClientRegionFactoryBean<>();

			exampleRegion.setCache(gemfireCache);
			exampleRegion.setShortcut(ClientRegionShortcut.LOCAL);

			return exampleRegion;
		}

		@Bean
		@DependsOn("Example")
		public GemfireTemplate exampleTemplate(GemFireCache gemfireCache) {
			return new GemfireTemplate(gemfireCache.getRegion("/Example"));
		}
	}
}
