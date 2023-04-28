/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import java.util.ArrayList;
import java.util.List;

import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.model.Result;

public class BagitTagConstraint implements Constraint {

    private boolean required;
    private List<String> values = new ArrayList<String>();
    private boolean repeatable = true;
    private String description;

    public BagitTagConstraint() {
    }

    public BagitTagConstraint(boolean required, List<String> values, boolean repeatable, String description) {
        this.required = required;
        this.values = values;
        this.repeatable = repeatable;
        this.values = values;
        this.description = description;
    }

    @Override
    public String getType() {
        return "tag";
    }

    @Override
    public String getName() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public Result compatibleWith(Constraint other) {
        return isRefinedBy((BagitTagConstraint)other);
    }

    public Result isRefinedBy(BagitTagConstraint tc) {
        Result res = new Result();
        if (! description.equals(tc.description)) {
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
