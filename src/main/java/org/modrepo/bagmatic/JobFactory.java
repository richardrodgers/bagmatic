/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.util.HashSet;
import java.util.Set;

import org.modrepo.bagmatic.impl.EphemeralJob;
import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.model.Job;


public class JobFactory {

    private static Set<Job> jobs = new HashSet<>();

    /**
     * Creates a new session with passed platform constraints
     * @param platformConstraints environmental/operational constraints
     * @return a new session
     */
    public static Job newSession(Set<Constraint> platformConstraints) {
        Job job = new EphemeralJob(platformConstraints);
        jobs.add(job);
        return job;
    }

    public static Job newJob(Delivery delivery) {
        return new EphemeralJob(delivery);
    }

    public static void endSession(Job job) {
        jobs.remove(job);
    }
}
