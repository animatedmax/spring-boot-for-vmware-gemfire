/*
 * Copyright (c) VMware, Inc. 2022-2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package org.springframework.gradle.maven;

import java.net.URI;
import java.time.Duration;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import io.github.gradlenexus.publishplugin.NexusPublishExtension;
import io.github.gradlenexus.publishplugin.NexusPublishPlugin;

/**
 * Enables a Gradle {@link Project} to publish to Maven Central using Sonatype's Nexus Repository Manager.
 *
 * @author Rob Winch
 * @author John Blum
 * @see <a href="https://github.com/gradle-nexus/publish-plugin">Nexus Publish Gradle Plugin</a>
 */
public class SpringNexusPublishPlugin implements Plugin<Project> {

	private static final String SONATYPE_NEXUS_URL = "https://s01.oss.sonatype.org/service/local/";
	private static final String SONATYPE_SNAPSHOT_REPOSITORY_URL = "https://s01.oss.sonatype.org/content/repositories/snapshots/";

	@Override
	public void apply(Project project) {

		project.getPlugins().apply(NexusPublishPlugin.class);

		NexusPublishExtension nexusPublishExtension = project.getExtensions().findByType(NexusPublishExtension.class);

		// TODO: Why did we not simply use/configure the 'sonatype' repository and instead add a repo ('ossrh')?
		//  See here: https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
		// NOTE: Careful, the keyword 'ossrh' is referred to in names in the Spring Build Conventions Gradle Plugins,
		// such as, but not limited to:
		// * 'ossrhUsername'
		// * 'publishToOssrh'
		// * 'closeAndReleaseOssrhStagingRepository'
		nexusPublishExtension.getRepositories().create("ossrh", nexusRepository -> {
			nexusRepository.getNexusUrl().set(URI.create(SONATYPE_NEXUS_URL));
			nexusRepository.getSnapshotRepositoryUrl().set(URI.create(SONATYPE_SNAPSHOT_REPOSITORY_URL));
		});

		nexusPublishExtension.getClientTimeout().set(Duration.ofMinutes(3));
		nexusPublishExtension.getConnectTimeout().set(Duration.ofMinutes(3));
	}
}
