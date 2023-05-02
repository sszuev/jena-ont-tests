package com.gitlab.sszuev.jena.ont;

import org.apache.jena.ontology.OntDocumentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.provider.Arguments;

import java.util.Arrays;
import java.util.stream.Stream;

abstract class CommonOntTestBase {

    @BeforeEach
    public void beforeEach() {
        OntDocumentManager.getInstance().reset(true);
    }

    static Stream<Arguments> testsAsArguments(CommonOntTestEngine[] tests) {
        return Arrays.stream(tests).map(x -> Arguments.of(Named.of(x.testNodeName, x)));
    }
}
