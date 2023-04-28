/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.modrepo.bagmatic.BagConstraint;
//import org.modrepo.bagmatic.TagConstraint;
//import org.modrepo.bagmatic.model.Constraint;

/*
 * from BagIt Profile Specification v 1.4 - a DTO for JSON file
 * containing user constraints - this is the (mandatory) 
 * profile information section.
 */

public class BagitProfileInfo {

    public BagitProfileInfo() {
    }

    @NotBlank( message = "BagIt-Profile-Identifier required" )
    @JsonProperty("BagIt-Profile-Identifier")
    private String bagitProfileIdentifier;

    @NotBlank( message = "Source-Organization required")
    @JsonProperty("Source-Organization")
    private String sourceOrganization;

    @JsonProperty("Contact-Name")
    private String contactName;

    @NotBlank( message = "External-Description required")
    @JsonProperty("External-Description")
    private String externalDescription;

    @NotBlank( message = "Version required")
    @JsonProperty("Version")
    private String version;

    @NotBlank( message = "BagIt-Profile-Version required")
    @JsonProperty("BagIt-Profile-Version")
    private String bagitProfileVersion;
}
