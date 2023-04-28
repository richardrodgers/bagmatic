/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

public interface Constraint {

    String getType();
    String getName();
    Result compatibleWith(Constraint other);
}