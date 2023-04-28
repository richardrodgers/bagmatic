/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.util.Set;

import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.model.Result;

public class BagConstraint implements Constraint {

    private String name;
    private Set<String> values;

    public BagConstraint() {
    }

    public BagConstraint(String name, Set<String> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String getType() {
        return "bag";
    }

    @Override
    public String getName() {
        return name;
    }

    public Set<String> getValues() {
        return values;
    }

    @Override
    public Result compatibleWith(Constraint other) {
        return isRefinedBy((BagConstraint)other);
    }

    public Result isRefinedBy(BagConstraint bc) {
        Result res = new Result();
        if (! name.equals(bc.name)) {
            res.addError("Constraint name mismatch");
        }
        if (! (values.isEmpty() || values.containsAll(bc.values))) {
            res.addError("Value list incompatible");
        }
        return res;
    }
}
