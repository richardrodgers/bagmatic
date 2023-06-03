/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.io.IOException;

import org.modrepo.bagmatic.model.Result;

public class Bagmatic {

    public Bagmatic(){}

    public static ContextBuilder emptyBuilder() {
        return new ContextBuilder();
    }

    public static Result<ContextBuilder> platformedBuilder() throws IOException {
        var builder = new ContextBuilder();
        return builder.addProfile(ContextBuilder.class.getResourceAsStream("/platform-profile.json"));
    }
}
