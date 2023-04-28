/**
 * Copyright (c) 2021 Richard Rodgers
 * SPDX-Licence-Identifier: Apache-2.0
 */
package org.modrepo.bagmatic;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.modrepo.bagmatic.model.Result;
import org.modrepo.bagmatic.impl.ConstraintSpec;
import org.modrepo.bagmatic.impl.profile.BagitProfile;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "bagmatic", mixinStandardHelpOptions = true, version = "1.0")
public class Bagmatic implements Callable<Integer> {

    @Option(names = "-c", description = "Files containing user constraints")
    private List<File> ucFiles = new ArrayList<File>();

    @Option(names = "-t", description = "File containing user template")
    private File tpFile;

    public Bagmatic(){}

    @Override
    public Integer call() throws Exception {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        var mapper = new ObjectMapper();
        // 
        try (InputStream platformIn = getClass().getResourceAsStream("/platform-profile.json")) {
            BagitProfile platformCts = mapper.readValue(platformIn, BagitProfile.class);
            Set<ConstraintViolation<BagitProfile>> violations = validator.validate(platformCts);
            if (violations.size() > 0) {
                System.out.println("Platform Bagit Profile invalid:");
                var vIter = violations.iterator();
                while (vIter.hasNext()) {
                    System.out.println(vIter.next().getMessage());
                }
                return -2;
            }
        } catch (Exception e) {
            System.out.println("Missing or unreadable platform Bagit Profile");
            return -1;
        }
        for (File ucf : ucFiles) {
            BagitProfile userCts = mapper.readValue(ucf, BagitProfile.class);
            //Result res = session.meldConstraints(userCts.getConstraints());
        }
        /* 
        if (res.success()) {
            System.out.println("Cool");
        } else {
            for (String error : res.getErrors()) {
                System.out.println(error);
            }
        }
        //System.out.println("PC: " + session.getTypedConstraints("bag").iterator().next().getName());
        */
        return 0;
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new Bagmatic()).execute(args);
        System.exit(exitCode);
    }
}
