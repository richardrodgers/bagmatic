/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.util.Set;

public interface Assembler {

    Result assemble();
    Result ready();
    Result addTemplates(Set<Template> templates);
}
