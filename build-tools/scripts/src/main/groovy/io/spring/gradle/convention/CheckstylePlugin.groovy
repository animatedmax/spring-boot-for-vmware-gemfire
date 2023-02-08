/*
 * Copyright (c) VMware, Inc. 2022-2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package io.spring.gradle.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * Configures and applies the Checkstyle Gradle {@link Plugin}.
 *
 * @author Vedran Pavic
 * @author John Blum
 * @see org.gradle.api.Plugin
 * @see org.gradle.api.Project
 */
class CheckstylePlugin implements Plugin<Project> {

	static final String CHECKSTYLE_PATHNAME = 'etc/checkstyle'
	static final String CHECKSTYLE_VERSION = '8.21'

	@Override
	void apply(Project project) {

		project.plugins.withType(JavaPlugin) {

			def checkstyleDirectory = project.rootProject.file(CHECKSTYLE_PATHNAME)

			if (checkstyleDirectory?.isDirectory()) {

				project.getPluginManager().apply('checkstyle')
				project.dependencies.add('checkstyle', 'io.spring.javaformat:spring-javaformat-checkstyle')
				project.dependencies.add('checkstyle', 'io.spring.nohttp:nohttp-checkstyle')

				project.checkstyle {
					configDirectory = checkstyleDirectory
					toolVersion = CHECKSTYLE_VERSION
				}
			}
		}
	}
}
