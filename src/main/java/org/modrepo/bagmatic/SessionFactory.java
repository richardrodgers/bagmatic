/**
 * Copyright (c) 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.util.HashSet;
import java.util.Set;

import org.modrepo.bagmatic.impl.EphemeralSession;
import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.model.Session;


public class SessionFactory {

    private static Set<Session> sessions = new HashSet<>();

    /**
     * Creates a new session with passed platform constraints
     * @param platformConstraints environmental/operational constraints
     * @return a new session
     */
    public static Session newSession(Set<Constraint> platformConstraints) {
        Session session = new EphemeralSession(platformConstraints);
        sessions.add(session);
        return session;
    }

    public static Session newSession(Delivery delivery) {
        return new EphemeralSession(delivery);
    }

    public static void endSession(Session session) {
        sessions.remove(session);
    }
}
