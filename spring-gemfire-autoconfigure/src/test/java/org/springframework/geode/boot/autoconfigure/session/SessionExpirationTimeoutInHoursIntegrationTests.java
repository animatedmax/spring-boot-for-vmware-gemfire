/*
 * Copyright (c) VMware, Inc. 2022. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.geode.boot.autoconfigure.session;

import java.time.Duration;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.session.Session;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration Tests for {@link Session} {@literal expiration timeout} in {@literal hours}.
 *
 * @author John Blum
 * @see java.time.Duration
 * @see org.springframework.boot.test.context.SpringBootTest
 * @see org.springframework.geode.boot.autoconfigure.session.AbstractSessionExpirationTimeoutInTimeUnitIntegrationTests
 * @see org.springframework.session.Session
 * @see org.springframework.test.context.junit4.SpringRunner
 * @since 2.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
	classes = AbstractSessionExpirationTimeoutInTimeUnitIntegrationTests.TestConfiguration.class,
	properties = "spring.session.timeout=2h",
	webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
public class SessionExpirationTimeoutInHoursIntegrationTests
		extends AbstractSessionExpirationTimeoutInTimeUnitIntegrationTests{

	@Override
	protected int getExpectedMaxInactiveIntervalInSeconds() {
		return Long.valueOf(Duration.ofHours(2).getSeconds()).intValue();
	}
}
