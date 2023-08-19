/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

    private final List<String> errors = new ArrayList<>();
    private T object;

    public Result() {
    }

    public boolean success() {
        return errors.isEmpty();
    }

    public boolean failed() {
        return getErrors().size() > 0;
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

    public void setObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public void toConsole() {
        errors.forEach(error -> System.out.println(error));
    }
}
