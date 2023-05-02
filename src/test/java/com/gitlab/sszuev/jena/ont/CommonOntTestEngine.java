package com.gitlab.sszuev.jena.ont;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.ProfileException;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.Assertions;

abstract class CommonOntTestEngine {

    public static final String BASE = "http://jena.hpl.hp.com/testing/ontology";
    public static final String NS = BASE + "#";

    protected boolean isInOWL;
    protected boolean isInOWLLite;
    protected boolean isInRDFS;
    protected String testNodeName;

    public CommonOntTestEngine(String testName, boolean isInOWL, boolean isInOWLLite, boolean isInRDFS) {
        this.testNodeName = testName;
        this.isInOWL = isInOWL;
        this.isInOWLLite = isInOWLLite;
        this.isInRDFS = isInRDFS;
    }

    protected abstract void performTest(OntModel m, ProfileLang profileLang) throws Exception;

    public void runTest() {
        try {
            runTest(ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM), isInOWL, ProfileLang.OWL);
            runTest(ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM), isInOWLLite, ProfileLang.OWL_LITE);
            runTest(ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM), isInRDFS, ProfileLang.RDFS);
        } catch (Exception ex) {
            throw new AssertionError(":: " + testNodeName + " exception", ex);
        }
    }

    /**
     * Test execution worker
     */
    protected void runTest(OntModel m, boolean inModel, ProfileLang profileLang) throws Exception {
        boolean hasProfileException = false;
        try {
            performTest(m, profileLang);
        } catch (ProfileException e) {
            hasProfileException = true;
        }
        Assertions.assertEquals(inModel, !hasProfileException,
                ":: " + testNodeName + " was " + (inModel ? "" : "not") + " expected in model " + m.getProfile().getLabel());
    }

    enum ProfileLang {
        OWL,
        OWL_LITE,
        RDFS,
    }
}
