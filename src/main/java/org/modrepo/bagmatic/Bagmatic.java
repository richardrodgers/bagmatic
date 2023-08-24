/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

public class Bagmatic {

    private static final String platformId = "classpath:/platform-profile.json";

    Bagmatic(){}

    /*
     * Return a builder with an empty root node
     */
    public static ContextBuilder emptyBuilder() {
        return new ContextBuilder();
    }

    /*
     * Return a builder with root node set to the 'platform' profile
     * Almost always the preferred method, since applications may encounter
     * unexpected and difficult to rectify errors due to unmanifested constraints
     * in the supporting libraries using an empty builder. Note also that it is
     * assumed that the platform profile always validates (it is user-inacessible)
     */
    public static ContextBuilder platformBuilder() {
        var builder = new ContextBuilder();
        builder.merge("platform", platformId);
        return builder;
    }
}
