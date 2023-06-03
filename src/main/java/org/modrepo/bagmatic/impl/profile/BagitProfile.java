/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public BagitProfileInfo bagitProfileInfo;

    @JsonProperty("Bag-Info")
    public Map<String, BagitTagConstraint> bagInfo = new HashMap<>();

    @JsonProperty("Manifests-Required")
    public List<String> manifestsRequired = new ArrayList<>();

    @JsonProperty("Manifests-Allowed")
    public List<String> manifestsAllowed = new ArrayList<>(List.of("*"));

    @JsonProperty("Allow-Fetch.txt")
    public boolean allowFetch = true;

    @JsonProperty("Fetch.txt-Required")
    public boolean requireFetch = false;

    @JsonProperty("Data-Empty")
    public boolean dataEmpty = false;

    @JsonProperty("Serialization")
    public String serialization = "optional";

    @JsonProperty("Accept-Serialization")
    public List<String> acceptSerialization = new ArrayList<>(List.of("*"));

    @NotEmpty(message = "Must accept at least one version")
    @JsonProperty("Accept-BagIt-Version")
    public List<String> acceptBagitVersion;

    @JsonProperty("Tag-Manifests-Required")
    public List<String> tagManifestsRequired = new ArrayList<>();

    @JsonProperty("Tag-Manifests-Allowed")
    public List<String> tagManifestsAllowed = new ArrayList<>(List.of("*"));

    @JsonProperty("Tag-Files-Required")
    public List<String> tagFilesRequired = new ArrayList<>();

    @JsonProperty("Tag-Files-Allowed")
    public List<String> tagfilesAllowed = new ArrayList<>(List.of("*"));
}
