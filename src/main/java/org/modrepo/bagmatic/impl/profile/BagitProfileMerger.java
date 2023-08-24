/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modrepo.bagmatic.model.Result;

/*
 * Methods to merge 2 BagIt profiles if compatible. If not compatible,
 * returns failed result with reason.
 */
public class BagitProfileMerger {
    /*
     * Construct new profile in stages: copy the 'BagIt-Profile-Info' section from the
     * leaf profile (ancestors will each have the info from their original - unmerged - profile).
     * Next, the 'Bag-Info' specs. Divide all specs from both profiles into 2 sets:
     * the disjunctive union (aka 'symmetrical difference') where each spec occurs only once
     * so by definition is compatible, and then the intersection, tested pair-wise for compatibilty.
     * Finally test all remaining fields and insert the 'melded' version (greatest common compatible form).
     */
    public static Result<BagitProfile> mergeProfiles(BagitProfile base, BagitProfile leaf) {
        var merged = new BagitProfile();
        merged.bagitProfileInfo = leaf.bagitProfileInfo;
        var result = new Result<BagitProfile>();
        var infoSpecs = disjunctiveUnion(base.bagInfo, leaf.bagInfo);
        for (String name : intersection(base.bagInfo, leaf.bagInfo).keySet()) {
            var result2 = meld(name, base.bagInfo.get(name), leaf.bagInfo.get(name));
            if (result2.success()) {
                infoSpecs.put(name, result2.getObject());
            } else {
                result.addError(result2.getErrors().get(0));
                break;
            }
        }
        // now check remaining constraints

        // meld Manifests-Required
        merged.manifestsRequired = base.manifestsRequired;
        // meld Manifests-Allowed
        merged.manifestsAllowed = base.manifestsAllowed;

        // meld Allow-Fetch.txt
        if (base.allowFetch != leaf.allowFetch) {
            // cannot reconcile - fail with error
            result.addError("Conflicting allow-fetch status");
        } else {
            merged.allowFetch = base.allowFetch;
        }
        // meld Fetch.txt-Required

        // meld Data-Empty
        if (base.dataEmpty != leaf.dataEmpty) {
            // cannot reconcile - fail with error
            result.addError("Conflicting dataEmpty status");
        } else {
            merged.dataEmpty = base.dataEmpty;
        }
        // meld Serialization
        merged.serialization = base.serialization;
        // meld Accept-Serialization
        merged.acceptSerialization = base.acceptSerialization;
        // meld Accept-BagIt-Version
        merged.acceptBagitVersion = base.acceptBagitVersion;

        // meld Tag-Manifests-Required
        merged.tagFilesRequired = base.tagFilesRequired;
        // meld Tag-Manifests-Allowed
        merged.tagManifestsAllowed = base.tagManifestsAllowed;

        // meld Tag-Files-Required
        merged.tagFilesRequired = base.tagFilesRequired;//
        // meld Tag-Files-Allowed
        merged.tagfilesAllowed = base.tagfilesAllowed;

        if (result.success()) {
            result.setObject(merged);
        }
        return result;
    }

    private static Map<String, BagitTagConstraint> disjunctiveUnion(Map<String, BagitTagConstraint> map1,
                                                                    Map<String, BagitTagConstraint> map2) {
        var union = new HashMap<String, BagitTagConstraint>();
        union.putAll(map1);
        map2.keySet().stream()
                     .filter(key -> union.containsKey(key))
                     .forEach(union::remove);
        var right = map2.keySet().stream()
                                 .filter(key -> ! union.containsKey(key))
                                 .collect(Collectors.toMap(Function.identity(), map1::get));
        union.putAll(right);
        return union;
    }

    private static Map<String, BagitTagConstraint> intersection(Map<String, BagitTagConstraint> map1,
                                                                Map<String, BagitTagConstraint> map2) {
        return map1.keySet()
                   .stream()
                   .distinct()
                   .filter(key -> map2.containsKey(key))
                   .collect(Collectors.toMap(Function.identity(), map1::get));
    }

    private static Result<BagitTagConstraint> meld(String key, BagitTagConstraint spec1, BagitTagConstraint spec2) {
        var result = new Result<BagitTagConstraint>();
        var melded = new BagitTagConstraint();
        melded.setRequired(spec1.isRequired() || spec2.isRequired());
        var s2Vals = spec2.getValues();
        melded.setValues(spec1.getValues().stream()
                              .filter(v -> (s2Vals.isEmpty() || s2Vals.contains(v))).toList());
        if (! spec1.isRepeatable() == (spec2.isRepeatable())) {
            // cannot reconcile - fail with error
            result.addError("Conflicting repeatable status");
        } else {
            melded.setRepeatable(spec1.isRepeatable());
        }
        if (result.success()) {
            result.setObject(melded);
        }
        return result;              
    }
}
