/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.modrepo.bagmatic.model.Assembler;
import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.Delivery;
import org.modrepo.bagmatic.model.Result;
import org.modrepo.bagmatic.model.Session;
import org.modrepo.bagmatic.model.Template;
/**
 * EphemeralSession is an in-memory session, where configuration state is not
 * persisted.
 */
public class EphemeralSession implements Session {

    private HashMap<String, Constraint> constraints = new HashMap<>();

    public EphemeralSession() {
    }

    public EphemeralSession(Set<Constraint> platformConstraints) {
        for (Constraint ct : platformConstraints) {
            constraints.put(ct.getName(), ct);
        }
    }

    public EphemeralSession(Delivery delivery) {
    }

    @Override
    public Set<Constraint> getTypedConstraints(String type) {
        return constraints.values()
                .stream()
                .filter(ct -> ct.getType().equals(type))
                .collect(Collectors.toSet());
    }

    @Override
    public Result meldConstraints(Set<Constraint> newConstraints) {
        // create a copy of constraints to operate on, so failure amounts
        // to simply not installing modified copy
        var newCts = (HashMap<String, Constraint>)constraints.clone();
        var result = new Result();
        for (Constraint nct : newConstraints) {
            // determine compatibility of new constraint
            var key = nct.getName();
            if (! newCts.containsKey(key)) {
                // new always compatible
                newCts.put(key, nct);
            } else {
                var old = newCts.get(key);
                var res = old.compatibleWith(nct);
                if (res.success()) {
                    newCts.put(key, nct);
                } else {
                    result.addErrors(res.getErrors());
                }
            }
        }
        if (result.success()) {
            constraints = newCts;
        }
        return result;
    }

    @Override
    public Result addTemplates(Set<Template> templates) {
        return new Result();
    }

    @Override
    public Assembler newAssembler() {
        return new NoCopyAssembler();
    }
}