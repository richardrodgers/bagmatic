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
    public String bagitProfileIdentifier;

    @NotBlank( message = "Source-Organization required")
    @JsonProperty("Source-Organization")
    public String sourceOrganization;

    @JsonProperty("Contact-Name")
    public String contactName;

    @NotBlank( message = "External-Description required")
    @JsonProperty("External-Description")
    public String externalDescription;

    @NotBlank( message = "Version required")
    @JsonProperty("Version")
    public String version;

    @NotBlank( message = "BagIt-Profile-Version required")
    @JsonProperty("BagIt-Profile-Version")
    public String bagitProfileVersion = "1.1.0";  // default for un-versioned
}
