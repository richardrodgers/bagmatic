/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.modrepo.bagmatic.impl.profile.BagitProfile;
import static org.modrepo.bagmatic.Bagmatic.*;
import org.modrepo.bagmatic.model.Result;

/*
 * Tests for context formation
 */

public class ContextTest {

    private static final String testProf = "classpath:/profiles/bagProfileFoo.json";
    private static final String testProfBar = "classpath:/profiles/bagProfileBar.json";

    @Test
    public void installProfileToEmptyBuilder() {
        var builder = emptyBuilder();
        Result<BagitProfile> result = builder.merge("testProf", testProf);
        assertTrue(result.success());
        assertTrue(result.getObject().bagInfo.containsKey("Bagging-Date"));
    }

    @Test
    public void mergeProfileToPlatformBuilder() {
        var builder = platformBuilder();
        Result<BagitProfile> result = builder.merge("testProf", testProf);
        assertTrue(result.success());
        assertTrue(result.getObject().bagInfo.containsKey("Bagging-Date"));
    }

    @Test
    public void parentOfEmptyBuilderEmpty() {
        assert(emptyBuilder().parent().isEmpty());
    }

    @Test
    public void parentOfPlatformBuilderEmpty() {
        assert(platformBuilder().parent().isEmpty());
    }

    @Test
    public void createSiblingOnPlatform() {
        var builder = platformBuilder();
        var result2 = builder.merge("testProf", testProf);
        assertTrue(result2.success());
        var parent = builder.parent();
        assertTrue(parent.isPresent());
        var result = builder.merge("testProfBar", testProfBar);
        assertTrue(result.success());
        assertTrue(result.getObject().bagInfo.containsKey("Contact-Email"));
        var parent2 = builder.parent();
        assertTrue(parent2.isPresent());
    }

    @Test
    public void findSiblingOnPlatform() {
        var builder = platformBuilder();
        var result1 = builder.merge("testProf", testProf);
        assertTrue(result1.success());
        assertTrue(result1.getObject().bagInfo.containsKey("Contact-Phone"));
        var opt1 = builder.parent();
        assertTrue(opt1.get().bagitProfileInfo.version.equals("0.1"));
        var result2 = builder.merge("testProfBar", testProfBar);
        assertTrue(result2.success());
        assertTrue(result2.getObject().bagInfo.containsKey("Contact-Email"));
        // now find sibling
        var sibOpt = builder.sibling("testProf");
        assertTrue(sibOpt.isPresent());
        assertTrue(sibOpt.get().bagInfo.containsKey("Bagging-Date"));
    }

    /* 
    @Test
    public void bagConformsToContext() throws IOException {
        ContextBuilder builder = Bagmatic.emptyBuilder();
        Result<ContextBuilder> result = builder.addProfile(getClass().getResourceAsStream("/profiles/bagProfileFoo.json"));
        if (result.success()) {
            Context context = builder.build();
            Bag bag1 = Serde.fromStream(getClass().getResourceAsStream("/bags/bag1.zip"), "zip");
            assertFalse(context.conforms(bag1).success());
            Bag bag2 = Serde.fromStream(getClass().getResourceAsStream("/bags/bag2.zip"), "zip");
            assertTrue(context.conforms(bag2).success());
        }
    }
    */
}
