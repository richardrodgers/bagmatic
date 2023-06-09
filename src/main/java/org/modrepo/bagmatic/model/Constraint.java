/**
 * Copyright 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

public interface Constraint {

    String getType();
    String getName();
    boolean isRequired();
    boolean isRepeatable();
    Result<String> compatibleWith(Constraint other);
}