package com.gitlab.sszuev.jena.ont.common;

import org.apache.jena.ontology.OntDocumentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.stream.Stream;

public abstract class CommonOntTestBase {

    @BeforeEach
    public void beforeEach() {
        OntDocumentManager.getInstance().reset(true);
    }

    public static Stream<Arguments> testsAsArguments(EngineWithName[] tests) {
        return Arrays.stream(tests).map(x -> Arguments.of(Named.of(x.getName(), x)));
    }
}
