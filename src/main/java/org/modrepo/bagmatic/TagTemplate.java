/**
 * Copyright 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.util.List;

import org.modrepo.bagmatic.model.Template;

public class TagTemplate implements Template {

    private final String name;
    private final boolean edit;
    private final List<String> values;

    public TagTemplate(String name, boolean edit, List<String> values) {
        this.name = name;
        this.edit = edit;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public boolean isEditable() {
        return edit;
    }

    public List<String> getValues() {
        return values;
    }
}
