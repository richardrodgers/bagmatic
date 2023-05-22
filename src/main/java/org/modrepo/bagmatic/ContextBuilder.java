/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.modrepo.bagmatic.impl.profile.BagitProfile;
import org.modrepo.bagmatic.model.Constraint;
import org.modrepo.bagmatic.model.Context;
import org.modrepo.bagmatic.model.Result;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

/** 
 * Builder class for constructing Contexts
*/
public class ContextBuilder {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private ObjectMapper mapper = new ObjectMapper();
    private BagitProfile inputProfile;
    private BagitProfile mergedProfile;

    public ContextBuilder() {}

    public Result addProfile(InputStream profIn) throws IllegalStateException, IOException {
        var result = validateProfile(profIn);
        if (result.success()) {
            result = mergeProfiles();
        }
        profIn.close();
        return result;
    }

    public Result addTemplate(InputStream templIn) throws IllegalStateException, IOException {
        // for now, just use profiles - semantics not quite right
        // for all templates, but covers easy cases
        return addProfile(templIn);
    }

    public Context build() {
        return new Context();
    }

    private Result validateProfile(InputStream profIn) {
        var result = new Result();
        try  {
            inputProfile = mapper.readValue(profIn, BagitProfile.class);
            Set<ConstraintViolation<BagitProfile>> violations = validator.validate(inputProfile);
            if (violations.size() > 0) {
                result.addError("Platform Bagit Profile invalid");
                var vIter = violations.iterator();
                while (vIter.hasNext()) {
                    result.addError(vIter.next().getMessage());
                }
            }
        } catch (IOException e) {
            result.addError("Missing or unreadable platform Bagit Profile");
        }
        return result;
    }

    private Result mergeProfiles() {
        if (mergedProfile == null) {
            mergedProfile = inputProfile;
            inputProfile = null;
        } else {
            // element-wise comparison/merge of input and merged profiles
            // skip profileInfo section for now
            
        }
        return new Result();
    }
}
