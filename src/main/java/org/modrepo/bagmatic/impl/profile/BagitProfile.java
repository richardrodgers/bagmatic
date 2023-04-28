/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.modrepo.bagmatic.BagConstraint;
//import org.modrepo.bagmatic.TagConstraint;
//import org.modrepo.bagmatic.model.Constraint;

/*
 * from BagIt Profile Specification v 1.4 - a DTO for JSON file
 * containing user constraints 
 */

public class BagitProfile {

    public BagitProfile() {
    }

    @NotNull(message = "Profile Info section required")
    @Valid
    @JsonProperty("BagIt-Profile-Info")
    private BagitProfileInfo bagitProfileInfo;

    @JsonProperty("Bag-Info")
    private BagInfo bagInfo;

    @JsonProperty("Manifests-Allowed")
    private List<String> manifestsAllowed;

    @JsonProperty("Accept-Serialization")
    private List<String> acceptSerialization;

    @JsonProperty("Accept-BagIt-Version")
    private List<String> acceptBagitVersion;
}
