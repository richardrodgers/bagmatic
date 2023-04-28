/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.modrepo.bagmatic.BagConstraint;
//import org.modrepo.bagmatic.TagConstraint;
//import org.modrepo.bagmatic.model.Constraint;

/*
 * from BagIt Profile Specification v 1.4 - a DTO for JSON file
 * containing user constraints. BagInfo subsection of profile.
 */

public class BagInfo {

    public BagInfo() {
    }

    @JsonProperty("Source-Organization")
    private BagitTagConstraint sourceOrganization;

    @JsonProperty("Organization-Address")
    private BagitTagConstraint organizationAddress;

    @JsonProperty("Contact-Name")
    private BagitTagConstraint contactName;

    @JsonProperty("Contact-Phone")
    private BagitTagConstraint contactPhone;

    @JsonProperty("Contact-Email")
    private BagitTagConstraint contactEmail;

    @JsonProperty("External-Description")
    private BagitTagConstraint externalDescription;

    @JsonProperty("Bagging-Date")
    private BagitTagConstraint baggingDate;

    @JsonProperty("External-Identifier")
    private BagitTagConstraint externalIdentifier;

    @JsonProperty("Bag-Size")
    private BagitTagConstraint bagSize;

    @JsonProperty("Payload-Oxum")
    private BagitTagConstraint payloadOxum;

    @JsonProperty("Bag-Group-Identifier")
    private BagitTagConstraint bagGroupIdentifier;

    @JsonProperty("Bag-Count")
    private BagitTagConstraint bagCount;

    @JsonProperty("Internal-Sender-Identifier")
    private BagitTagConstraint internalSenderIdentifier;

    @JsonProperty("Internal-Sender-Description")
    private BagitTagConstraint internalSenderDescription;

}
