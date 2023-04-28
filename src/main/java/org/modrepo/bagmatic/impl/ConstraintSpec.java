/**
 * Copyright (c) 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modrepo.bagmatic.BagConstraint;
import org.modrepo.bagmatic.TagConstraint;
import org.modrepo.bagmatic.model.Constraint;

public class ConstraintSpec {

    public ConstraintSpec() {
    }

    private List<BagConstraint> bagConstraints;
    private List<TagConstraint> tagConstraints;

    public Set<Constraint> getBagConstraints() {
        return Set.copyOf(bagConstraints);
    }

    public Set<Constraint> getTagConstraints() {
        return Set.copyOf(tagConstraints);
    }

    public Set<Constraint> getConstraints() {
        Set<Constraint> combined = new HashSet<>();
        combined.addAll(bagConstraints);
        combined.addAll(tagConstraints);
        return combined;
    }
}
