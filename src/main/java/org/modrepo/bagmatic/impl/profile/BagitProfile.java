/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

    @JsonProperty("Manifests-Required")
    private List<String> manifestsRequired = new ArrayList<>();

    @JsonProperty("Manifests-Allowed")
    private List<String> manifestsAllowed = new ArrayList<>(List.of("*"));

    @JsonProperty("Allow-Fetch.txt")
    private boolean allowFetch = true;

    @JsonProperty("Fetch.txt-Required")
    private boolean requireFetch = false;

    @JsonProperty("Data-Empty")
    private boolean dataEmpty = false;

    @JsonProperty("Serialization")
    private String serialization = "optional";

    @JsonProperty("Accept-Serialization")
    private List<String> acceptSerialization = new ArrayList<>(List.of("*"));

    @NotEmpty(message = "Must accept at least one version")
    @JsonProperty("Accept-BagIt-Version")
    private List<String> acceptBagitVersion;

    @JsonProperty("Tag-Manifests-Required")
    private List<String> tagManifestsRequired = new ArrayList<>();

    @JsonProperty("Tag-Manifests-Allowed")
    private List<String> tagManifestsAllowed = new ArrayList<>(List.of("*"));

    @JsonProperty("Tag-Files-Required")
    private List<String> tagFilesRequired = new ArrayList<>();

    @JsonProperty("Tag-Files-Allowed")
    private List<String> tagfilesAllowed = new ArrayList<>(List.of("*"));
}
