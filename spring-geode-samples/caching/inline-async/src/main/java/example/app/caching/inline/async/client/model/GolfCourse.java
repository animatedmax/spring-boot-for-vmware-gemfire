/*
 * Copyright (c) VMware, Inc. 2023. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package example.app.caching.inline.async.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.gemfire.util.CollectionUtils;
import org.springframework.util.Assert;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Abstract Data Type (ADT) modeling a golf course.
 *
 * @author John Blum
 * @since 1.4.0
 */
@Getter
@ToString(of = "name")
@EqualsAndHashCode(of = "name")
@RequiredArgsConstructor(staticName = "newGolfCourse")
@SuppressWarnings("unused")
public class GolfCourse {

	public static final int STANDARD_PAR_FOR_COURSE = 72;

	public static final Set<Integer> VALID_PARS_FOR_HOLE =
		Collections.unmodifiableSet(CollectionUtils.asSet(3, 4, 5));

	@NonNull
	private final String name;

	private final List<Integer> parForHole = new ArrayList<>(18);

	public int getPar(int hole) {

		assertValidHoleNumber(hole);

		return this.parForHole.get(indexForHole(hole));
	}

	public int getParForCourse() {

		return this.parForHole.stream()
			.reduce(Integer::sum)
			.orElse(STANDARD_PAR_FOR_COURSE);
	}

	public GolfCourse withHole(int holeNumber, int par) {

		assertValidHoleNumber(holeNumber);
		assertValidParForHole(par, holeNumber);

		this.parForHole.add(indexForHole(holeNumber), par);

		return this;
	}

	private void assertValidHoleNumber(int hole) {
		Assert.isTrue(isValidHoleNumber(hole),
			() -> String.format("Hole number [%d] must be 1 through 18", hole));
	}

	private void assertValidParForHole(int par, int hole) {
		Assert.isTrue(isValidPar(par),
			() -> String.format("Par [%1$d] for hole [%2$d] must be in [%3$s]", par, hole, VALID_PARS_FOR_HOLE));
	}

	private int indexForHole(int hole) {
		return hole - 1;
	}

	public boolean isValidHoleNumber(int hole) {
		return hole >= 1 && hole <= 18;
	}

	public boolean isValidPar(int par) {
		return VALID_PARS_FOR_HOLE.contains(par);
	}
}
