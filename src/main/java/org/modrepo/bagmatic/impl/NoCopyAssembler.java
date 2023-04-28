/**
 * Copyright (c) 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl;

import java.util.Set;

import org.modrepo.bagmatic.model.Assembler;
import org.modrepo.bagmatic.model.Result;
import org.modrepo.bagmatic.model.Template;

/**
 * NoCopyAssembler operates without copying payloads or tag files until
 * the bag is created. It stores Path references instead.
 */
public class NoCopyAssembler implements Assembler {

    public NoCopyAssembler() {
    }

    @Override
    public Result assemble() {
        return new Result();
    }

    @Override
    public Result ready() {
        return new Result(); 
    }

    @Override
    public Result addTemplates(Set<Template> templates) {
        return new Result(); 
    }

}