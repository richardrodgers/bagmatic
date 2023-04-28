/**
 * Copyright (c) 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.util.Set;

public interface Session {

    Set<Constraint> getTypedConstraints(String type);
    Result meldConstraints(Set<Constraint> constraints);
    Result addTemplates(Set<Template> templates);
    Assembler newAssembler();
}