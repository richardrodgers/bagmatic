/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.io.IOException;

//import java.util.Set;

import org.modrepo.packr.Bag;

public class Context {

    //Set<Constraint> getTypedConstraints(String type);
    //Result meldConstraints(Set<Constraint> constraints);
    //Result addTemplates(Set<Template> templates);
    //Assembler newAssembler();
    public Result conforms(Bag bag) {
        var result = new Result();
        // don't bother checking conformance unless bag is well-formed
        try {
            if (! bag.isValid()) {
                result.addError("Bag is invalid - no further checking attempted");
            } else {
                ;
            }
        } catch (IOException ioe) {
            result.addError("IO error reading bag");
        }
        return result;
    }
}