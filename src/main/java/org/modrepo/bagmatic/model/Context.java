/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.model;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modrepo.bagmatic.impl.profile.BagitProfile;
import org.modrepo.bagmatic.impl.profile.BagitTagConstraint;


//import java.util.Set;

import org.modrepo.packr.Bag;

public class Context {

    private BagitProfile mergedProfile;

    public Context(BagitProfile profile) {
        mergedProfile = profile;
    }
    
    //Set<Constraint> getTypedConstraints(String type);
    //Result meldConstraints(Set<Constraint> constraints);
    //Result addTemplates(Set<Template> templates);
    //Assembler newAssembler();

    public Map<String, BagitTagConstraint> getTagConstraints() {
        return mergedProfile.bagInfo;
    }

    public Result<String> conforms(Bag bag) {
        Result<String> result = new Result<>();
        // don't bother checking conformance unless bag is well-formed
        try {
            if (! bag.isValid()) {
                result.addError("Bag is invalid - no further checking attempted");
            } else {
                // compare bag with profile
                result = conformsToProfile(bag);
            }
        } catch (IOException ioe) {
            result.addError("IO error reading bag");
        }
        return result;
    }

    private Result<String> conformsToProfile(Bag bag) throws IOException {
        Result<String> result = new Result<>();
        var mdNames = bag.metadataNames();
        // check all profile constraints
        // check for profile link - if none, trivially conforms
        /*
        if (! mdNames.contains("BagIt-Profile-Identifier")) {
            return result; 
        }
        */
        for (Map.Entry<String, BagitTagConstraint> entry : mergedProfile.bagInfo.entrySet()) {
            // 3 conditions: is field present if required, are values permitted, are repeats permitted
            if (entry.getValue().isRequired() && ! mdNames.contains(entry.getKey())) {
                result.addError("Required property '" + entry.getKey() + "' missing");
            }
            var bagVals = bag.metadata(entry.getKey());
            if (bagVals != null && bagVals.size() > 0) {  // property defined in bag
                if (! entry.getValue().isRepeatable() && bagVals.size() > 1) {
                    result.addError("Non-repeatable property '" + entry.getKey() + "' repeated");
                }
                var profVals = entry.getValue().getValues();
                if (profVals.size() > 0 && !profVals.containsAll(bagVals)) {
                    result.addError("Disallowed value for property '" + entry.getKey() + "'");
                }
            }
        }
        // at least one required manifest algorithm must be declared
        var manifestsRequired = mergedProfile.manifestsRequired;
        if (manifestsRequired.isEmpty()) {
            result.addError("Profile must declare some manifest algorithms");
        } else if (! algolTags(bag).containsAll(manifestsRequired)) {
           result.addError("Required manifest algorithms missing");
        }
        // optional allowed manifest
        var manifestsAllowed = mergedProfile.manifestsAllowed;
        if (! manifestsAllowed.get(0).equals("*")) {
            if (! manifestsAllowed.containsAll(algolTags(bag))) {
                result.addError("Disallowed mainfest algorithms present");
            }
        }
         // tag manifest algorithm optional
         var tagManifestsRequired = mergedProfile.tagManifestsRequired;
         if (! tagManifestsRequired.isEmpty()) {
            if (! algolTags(bag).containsAll(tagManifestsRequired)) {
                result.addError("Required tag manifest algorithms missing");
            }
         }
         // optional allowed manifest
         var tagManifestsAllowed = mergedProfile.tagManifestsAllowed;
         if (! tagManifestsAllowed.get(0).equals("*")) {
             if (! tagManifestsAllowed.containsAll(algolTags(bag))) {
                 result.addError("Disallowed tag mainfest algorithms present");
             }
         }
         // Accept Bagit version must contain at least one entry
         var acceptBagitVersion = mergedProfile.acceptBagitVersion;
         if (acceptBagitVersion == null || acceptBagitVersion.isEmpty()) {
             result.addError("Profile must declare some acceptable BagIt versions");
         } else if (! acceptBagitVersion.contains(bag.bagItVersion())) {
            result.addError("Bag encoded in unacceptable BagIt version");
         }
        result.toConsole();
        return result;
    }

    private Set<String> algolTags(Bag bag) throws IOException {
        Set<String> names = bag.csAlgorithms();
        return names.stream().map(n -> n.toLowerCase().replace("-","")).collect(Collectors.toSet());
    }
}