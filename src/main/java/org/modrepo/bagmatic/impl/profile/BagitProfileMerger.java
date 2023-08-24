/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic.impl.profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // Manifests-Required
        merged.manifestsRequired = Stream.concat(base.manifestsRequired.stream(), leaf.manifestsRequired.stream())
                                         .distinct().toList();
        // Manifests-Allowed
        merged.manifestsAllowed = common(base.manifestsAllowed, leaf.manifestsAllowed);
        // TODO - add test to make sure all required are allowed

        // Allow-Fetch.txt
        if (base.allowFetch != leaf.allowFetch) {
            // cannot reconcile - fail with error
            result.addError("Conflicting allow-fetch status");
        } else {
            merged.allowFetch = base.allowFetch;
        }
        // Fetch.txt-Required
        if (base.requireFetch != leaf.requireFetch) {
            // cannot reconcile - fail with error
            result.addError("Conflicting require Fetch status");
        } else {
            merged.requireFetch = base.requireFetch;
        }
        // Data-Empty
        if (base.dataEmpty != leaf.dataEmpty) {
            // cannot reconcile - fail with error
            result.addError("Conflicting dataEmpty status");
        } else {
            merged.dataEmpty = base.dataEmpty;
        }
        // Serialization
        if (! base.serialization.equals(leaf.serialization)) {
            // cannot reconcile - fail with error
            result.addError("Conflicting serialization status");
        } else {
            merged.serialization = base.serialization;
        }
        // Accept-Serialization
        merged.acceptSerialization = common(base.acceptSerialization, leaf.acceptSerialization);
        if (merged.serialization.equals("required") || merged.serialization.equals("optional")) {
            if (merged.acceptSerialization.size() < 1) {
                result.addError("At least one serialization format must be accepted");
            }
        }
        // Accept-BagIt-Version
        merged.acceptBagitVersion = common(base.acceptBagitVersion, leaf.acceptBagitVersion);
        if (merged.acceptBagitVersion.size() < 1) {
            result.addError("At least one BagIt versison must be accepted");
        }
        // Tag-Manifests-Required
        merged.tagManifestsRequired = Stream.concat(base.tagManifestsRequired.stream(), leaf.tagManifestsRequired.stream())
                                      .distinct().toList();
        // Tag-Manifests-Allowed
        merged.tagManifestsAllowed = common(base.tagManifestsAllowed, leaf.tagManifestsAllowed);

        // Tag-Files-Required
        merged.tagFilesRequired = Stream.concat(base.tagFilesRequired.stream(), leaf.tagFilesRequired.stream())
                                      .distinct().toList();
        // meld Tag-Files-Allowed
        merged.tagfilesAllowed = common(base.tagfilesAllowed, leaf.tagfilesAllowed);

        if (result.success()) {
            result.setObject(merged);
        }
        return result;
    }

    private static List<String> common(List<String> list1, List<String> list2) {
        return Stream.concat(list1.stream(), list2.stream())
                     .filter(m -> list1.contains(m) && list2.contains(m))
                     .distinct().toList();
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
