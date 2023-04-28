/**
 * Copyright (c) 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private final List<String> errors = new ArrayList<>();

    public Result() {
    }

    public boolean success() {
        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addErrors(List<String> newerrors) {
        errors.addAll(newerrors);
    }
}
