/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.util.Set;

public interface Job {

    Set<Constraint> getTypedConstraints(String type);
    Result meldConstraints(Set<Constraint> constraints);
    Result addTemplates(Set<Template> templates);
    Assembler newAssembler();
}