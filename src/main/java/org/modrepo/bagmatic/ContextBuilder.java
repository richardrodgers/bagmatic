/**
 * Copyright (c) 2023 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.modrepo.bagmatic.impl.profile.BagitProfile;
import org.modrepo.bagmatic.model.Context;
import org.modrepo.bagmatic.model.Result;
import org.modrepo.bagmatic.model.ntree.Node;
import org.modrepo.bagmatic.model.ntree.Tree;

/** 
 * Builder class for constructing and dispensing Contexts
 */
public class ContextBuilder {

    record Profile(String localName, String profileId, BagitProfile profile){}

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient client = HttpClient.newBuilder()
                                          .version(Version.HTTP_1_1)
                                          .followRedirects(Redirect.NORMAL)
                                          .build();
    private Tree<Profile> profileTree = new Tree<Profile>();
    private Node<Profile> tipNode = profileTree.getRoot();
    
    ContextBuilder() {}

    /*
     * Public API
     * Merge passed profile to current tip node. Return version merged with tip
     * if it is valid and can be merged, else result with errors. If tip node
     * empty, just install - no merge possible.
     */
    public Result<BagitProfile> merge(String localName, String profileId) {
        var result = fetchProfile(profileId);
        if (result.success()) {
            if (profileTree.isEmpty()) { // nothing to merge - install as root
                profileTree.setRoot(new Node<Profile>(new Profile(localName, profileId, result.getObject())));
                tipNode = profileTree.getRoot();
            } else { // try to merge with tip node
                var mergeResult = mergeProfile(result.getObject());
                if (mergeResult.success()) {
                    var node = new Node<Profile>(new Profile(localName, profileId, mergeResult.getObject()));
                    tipNode.addChild(node);
                    tipNode = node;
                }
            }
        }
        return result;
    }

    /*
     * Public API
     * Move tip node to parent of current. Return parent profile or empty option
     * if parent is root 
     */
    public Optional<BagitProfile> parent() {
        if (tipNode == null || tipNode.getParent() == null) {
            return Optional.empty();
        }
        tipNode = tipNode.getParent();
        return Optional.ofNullable(tipNode.getData().profile);
    }

    /*
     * Public API
     * Move tip node to named sibling of current. Return sibling profile or empty option
     * if sibling name cannot be found
     */
    public Optional<BagitProfile> sibling(String name) {
        var match = tipNode.getSiblings().stream()
                                         .filter(nd -> nd.getData().localName.equals(name))
                                         .findFirst();
        if (match.isPresent()) {
            tipNode = match.get();
            return Optional.of(tipNode.getData().profile);
            //return Optional.ofNullable(tipNode.getData().profile);
        }
        return Optional.empty();
    }
    
    /*
     * Public API
     * Return a context wrapping tipNode merged profile.
     */
    public Context build() {
        return new Context(tipNode.getData().profile);
    }

    /* 
    public Result<ContextBuilder> addTemplate(String name, InputStream templIn) throws IllegalStateException, IOException {
        // for now, just use profiles - semantics not quite right
        // for all templates, but covers easy cases
        //return addProfile(name, templIn);
    }
    */

    /*
     * Read, parse and validate profile from source
     */
    private Result<BagitProfile> fetchProfile(String profileId) {
        var result = new Result<BagitProfile>();
        if (profileId.startsWith("classpath:")) {
            try (InputStream profIn = ContextBuilder.class.getResourceAsStream(profileId.substring(10))) {
                result.setObject(mapper.readValue(profIn, BagitProfile.class));
            } catch (Exception e) {
                result.addError(e.getMessage());
            }
        } else if (profileId.startsWith("http")) {
             try {
                var request = HttpRequest.newBuilder()
                                         .uri(URI.create(profileId))
                                         .header("Accept", "application/json")
                                         .build();
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    result.setObject(mapper.readValue(response.body(), BagitProfile.class));
                } else {
                    result.addError("Non-success code from host: " + response.statusCode());
                }
            } catch (Exception e) {
                result.addError(e.getMessage());
            }
        } else {
            // assume local filesystem reference
            try (InputStream profIn = Files.newInputStream(Paths.get(profileId))) {
                result.setObject(mapper.readValue(profIn, BagitProfile.class));
            } catch (Exception e) {
                result.addError(e.getMessage());
            }
        }
        if (result.success()) {
            Set<ConstraintViolation<BagitProfile>> violations = validator.validate(result.getObject());
            if (violations.size() > 0) {
                result.addError("Bagit Profile invalid");
                var vIter = violations.iterator();
                while (vIter.hasNext()) {
                    result.addError(vIter.next().getMessage());
                }
            }
        }
        return result;
    }

    private Result<BagitProfile> mergeProfile(BagitProfile prof) {
        // element-wise comparison/merge - TODO
        var result = new Result<BagitProfile>();
        result.setObject(prof);
        return result;
    }
}
