/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.util.Set;

import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.model.Result;

public class TagConstraint implements Constraint {

    private String name;
    private boolean required;
    private boolean repeatable;
    private Set<String> values;

    public TagConstraint() {
    }

/*
    public TagConstraint(String name, boolean required, boolean repeatable, Set<String> values) {
        this.name = name;
        this.required = required;
        this.repeatable = repeatable;
        this.values = values;
    }
*/

    @Override
    public String getType() {
        return "tag";
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public Set<String> getValues() {
        return values;
    }

    @Override
    public Result compatibleWith(Constraint other) {
        return isRefinedBy((TagConstraint)other);
    }

    public Result isRefinedBy(TagConstraint tc) {
        Result res = new Result();
        if (! name.equals(tc.name)) {
            res.addError("Constraint name mismatch");
        }
        if (repeatable != tc.repeatable) {
            res.addError("Repeatability mismatch");
        }
        if (! (values.isEmpty() || values.containsAll(tc.values))) {
            res.addError("Value list incompatible");
        }
        return res;
    }
}
