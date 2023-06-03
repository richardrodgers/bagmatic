/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.modrepo.bagmatic.impl.profile.BagitProfile;
import org.modrepo.bagmatic.model.Context;
import org.modrepo.bagmatic.model.Result;
import org.modrepo.packr.Bag;
import org.modrepo.packr.Serde;

/*
 * Tests for context formation
 */

public class ContextTest {

    @Test
    public void validatePlatformProfile() throws IllegalStateException, IOException {
        Result<ContextBuilder> result = Bagmatic.platformedBuilder();
        assertTrue(result.success());
    }

    @Test
    public void readOneUserProfile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //ContextBuilder builder = new ContextBuilder();
        BagitProfile inputProfile = mapper.readValue(getClass().getResourceAsStream("/profiles/bagProfileFoo.json"),
                                                     BagitProfile.class);
       // builder.addProfile(getClass().getResourceAsStream("/bagProfileFoo.json"));
       assertTrue(inputProfile.bagInfo.containsKey("Bagging-Date"));
    }

    @Test
    public void validateOneUserProfile() throws IllegalStateException, IOException {
        ContextBuilder builder = Bagmatic.emptyBuilder();
        Result<ContextBuilder> result = builder.addProfile(getClass().getResourceAsStream("/profiles/bagProfileFoo.json"));
        /* 
        for (String error : result.getErrors()) {
            System.out.println(error);
            System.out.flush();
        } 
        */
        assertTrue(result.success());
    }
    
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
}
