package com.gitlab.sszuev.jena.ont;

import org.apache.jena.IOTestUtils;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.gitlab.sszuev.jena.ont.CommonOntTestEngine.NS;
import static com.gitlab.sszuev.jena.ont.JunitExtensions.assertValues;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrameViewTest {

    private OntModel mInf;
    private OntModel mNoInf;

    private OntClass infA;
    private OntClass infB;
    private OntClass infC;

    private OntClass noinfA;
    private OntClass noinfB;
    private OntClass noinfC;

    private ObjectProperty noinfG;
    private ObjectProperty infG;

    private ObjectProperty noinfPa;
    private ObjectProperty noinfPb;
    private ObjectProperty noinfPc;

    private ObjectProperty infPa;
    private ObjectProperty infPb;
    private ObjectProperty infPc;

    private ObjectProperty noinfQa;
    private ObjectProperty noinfQb;

    private ObjectProperty infQa;
    private ObjectProperty infQb;

    private OntClass infAnn;
    private OntClass noinfAnn;
    AnnotationProperty infPann;
    AnnotationProperty noinfPann;

    private OntClass infUnion1;
    private OntClass infUnion2;
    private OntClass noinfUnion1;
    private OntClass noinfUnion2;
    private ObjectProperty infPunion;

    private OntClass infIntersect1;
    private OntClass infIntersect2;
    private OntClass noinfIntersect1;
    private OntClass noinfIntersect2;

    @BeforeEach
    public void setUp() {
        OntDocumentManager.getInstance().reset();
        OntDocumentManager.getInstance().clearCache();

        mNoInf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        mNoInf.read(IOTestUtils.normalize("file:testing/ontology/owl/list-syntax/test-ldp.rdf"));

        mInf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        mInf.read(IOTestUtils.normalize("file:testing/ontology/owl/list-syntax/test-ldp.rdf"));

        infA = mInf.getOntClass(NS + "A");
        infB = mInf.getOntClass(NS + "B");
        infC = mInf.getOntClass(NS + "C");


        noinfA = mNoInf.getOntClass(NS + "A");
        noinfB = mNoInf.getOntClass(NS + "B");
        noinfC = mNoInf.getOntClass(NS + "C");

        noinfG = mNoInf.getObjectProperty(NS + "global");
        infG = mInf.getObjectProperty(NS + "global");

        noinfPa = mNoInf.getObjectProperty(NS + "pA");
        noinfPb = mNoInf.getObjectProperty(NS + "pB");
        noinfPc = mNoInf.getObjectProperty(NS + "pC");

        infPa = mInf.getObjectProperty(NS + "pA");
        infPb = mInf.getObjectProperty(NS + "pB");
        infPc = mInf.getObjectProperty(NS + "pC");

        noinfQa = mNoInf.getObjectProperty(NS + "qA");
        noinfQb = mNoInf.getObjectProperty(NS + "qB");

        infQa = mInf.getObjectProperty(NS + "qA");
        infQb = mInf.getObjectProperty(NS + "qB");

        infAnn = mInf.getOntClass(NS + "HasAnn");
        noinfAnn = mNoInf.getOntClass(NS + "HasAnn");
        infPann = mInf.getAnnotationProperty(NS + "ann");
        noinfPann = mNoInf.getAnnotationProperty(NS + "ann");

        infUnion1 = mInf.getOntClass(NS + "Union1");
        infUnion2 = mInf.getOntClass(NS + "Union2");
        noinfUnion1 = mNoInf.getOntClass(NS + "Union1");
        noinfUnion2 = mNoInf.getOntClass(NS + "Union2");
        infPunion = mInf.getObjectProperty(NS + "unionP");
        mNoInf.getObjectProperty(NS + "unionP");

        infIntersect1 = mInf.getOntClass(NS + "Intersect1");
        infIntersect2 = mInf.getOntClass(NS + "Intersect2");
        noinfIntersect1 = mNoInf.getOntClass(NS + "Intersect1");
        noinfIntersect2 = mNoInf.getOntClass(NS + "Intersect2");
        mInf.getObjectProperty(NS + "intersectP");
        mNoInf.getObjectProperty(NS + "intersectP");
    }

    @AfterEach
    public void tearDown() {
        mInf.close();
        mNoInf.close();
    }

    @Test
    public void testLDPNoinfANodirect() {
        assertValues("", noinfA.listDeclaredProperties(false), new Object[]{noinfPa, noinfQa, noinfG, noinfQb});
    }

    @Test
    public void testHasDPNoinfANodirect() {
        assertTrue(noinfA.hasDeclaredProperty(noinfPa, false));
        assertFalse(noinfA.hasDeclaredProperty(noinfPb, false));
    }

    @Test
    public void testLDPNoinfADirect() {
        assertValues("", noinfA.listDeclaredProperties(true), new Object[]{noinfPa, noinfQa, noinfG, noinfQb});
    }

    @Test
    public void testLDPInfANodirect() {
        assertValues("", infA.listDeclaredProperties(false), new Object[]{infPa, infQa, infQb, noinfG});
    }

    @Test
    public void testLDPInfADirect() {
        assertValues("", infA.listDeclaredProperties(true), new Object[]{infPa, infQa, infQb, noinfG});
    }

    @Test
    public void testLDPNoinfBNodirect() {
        assertValues("", noinfB.listDeclaredProperties(false), new Object[]{noinfPa, noinfPb, noinfQa, noinfG, noinfQb});
    }

    @Test
    public void testLDPNoinfBDirect() {
        assertValues("", noinfB.listDeclaredProperties(true),
                new Object[]{noinfPb});
    }

    @Test
    public void testLDPInfBNodirect() {
        assertValues("", infB.listDeclaredProperties(false),
                new Object[]{infPa, infPb, infQa, infQb, infG});
    }

    @Test
    public void testLDPInfBDirect() {
        assertValues("", infB.listDeclaredProperties(true), new Object[]{infPb});
    }

    @Test
    public void testLDPNoinfCNodirect() {
        assertValues("", noinfC.listDeclaredProperties(false),
                new Object[]{noinfPa, noinfPb, noinfPc, noinfQa, noinfG, noinfQb});
    }

    @Test
    public void testLDPNoinfCDirect() {
        assertValues("", noinfC.listDeclaredProperties(true), new Object[]{noinfPc});
    }

    @Test
    public void testLDPInfCNodirect() {
        assertValues("", infC.listDeclaredProperties(false),
                new Object[]{infPa, infPb, infPc, infQa, infQb, infG});
    }

    @Test
    public void testLDPInfCDirect() {
        assertValues("", infC.listDeclaredProperties(true), new Object[]{infPc});
    }


    @Test
    public void testLDPNoinfAnnNodirect() {
        assertValues("", noinfAnn.listDeclaredProperties(false), new Object[]{noinfPann, noinfG, noinfQb});
    }

    @Test
    public void testLDPNoinfAnnDirect() {
        assertValues("", noinfAnn.listDeclaredProperties(true), new Object[]{noinfPann, noinfG, noinfQb});
    }

    @Test
    public void testLDPInfAnnNodirect() {
        assertValues("", infAnn.listDeclaredProperties(false), new Object[]{noinfPann, noinfG});
    }

    @Test
    public void testLDPInfAnnDirect() {
        assertValues("", infAnn.listDeclaredProperties(true), new Object[]{noinfPann, noinfG});
    }


    @Test
    public void testLDPNoinfUnionNodirect() {
        assertValues("", noinfUnion1.listDeclaredProperties(false), new Object[]{noinfG, noinfQb});
        assertValues("", noinfUnion2.listDeclaredProperties(false), new Object[]{noinfG, noinfQb});
    }

    @Test
    public void testLDPInfUnionNodirect() {
        assertValues("", infUnion1.listDeclaredProperties(false), new Object[]{infPunion, infG});
        assertValues("", infUnion2.listDeclaredProperties(false), new Object[]{infPunion, infG});
    }

    @Test
    public void testLDPNoinfIntersectNodirect() {
        assertValues("", noinfIntersect1.listDeclaredProperties(false), new Object[]{noinfG, noinfQb});
        assertValues("", noinfIntersect2.listDeclaredProperties(false), new Object[]{noinfG, noinfQb});
    }

    @Test
    public void testLDPInfIntersectNodirect() {
        assertValues("", infIntersect1.listDeclaredProperties(false), new Object[]{infG});
        assertValues("", infIntersect2.listDeclaredProperties(false), new Object[]{infG});
    }

    @Test
    public void testLDCNoinfPaNodirect() {
        assertValues("", noinfPa.listDeclaringClasses(false), new Object[]{noinfA, noinfB, noinfC});
    }

    @Test
    public void testLDCInfPaNodirect() {
        assertValues("", infPa.listDeclaringClasses(false), new Object[]{infA, infB, infC});
    }

    @Test
    public void testLDCNoinfPbNodirect() {
        assertValues("", noinfPb.listDeclaringClasses(false), new Object[]{noinfB, noinfC});
    }

    @Test
    public void testLDCInfPbNodirect() {
        assertValues("", infPb.listDeclaringClasses(false), new Object[]{infC, infB});
    }

    @Test
    public void testLDCNoinfPcNodirect() {
        assertValues("", noinfPc.listDeclaringClasses(false), new Object[]{noinfC});
    }

    @Test
    public void testLDCInfPcNodirect() {
        assertValues("", infPc.listDeclaringClasses(false), new Object[]{infC});
    }

    @Test
    public void testLDCNoinfPaDirect() {
        assertValues("", noinfPa.listDeclaringClasses(true), new Object[]{noinfA});
    }

    @Test
    public void testLDCInfPaDirect() {
        assertValues("", infPa.listDeclaringClasses(true), new Object[]{infA});
    }

    @Test
    public void testLDCNoinfPbDirect() {
        assertValues("", noinfPb.listDeclaringClasses(true), new Object[]{noinfB});
    }

    @Test
    public void testLDCInfPbDirect() {
        assertValues("", infPb.listDeclaringClasses(true), new Object[]{infB});
    }

    @Test
    public void testLDCNoinfPcDirect() {
        assertValues("", noinfPc.listDeclaringClasses(true), new Object[]{noinfC});
    }

    @Test
    public void testLDCInfPcDirect() {
        assertValues("", infPc.listDeclaringClasses(true), new Object[]{infC});
    }

    @Test
    public void testLDCNoinfGDirect() {
        assertValues("", noinfG.listDeclaringClasses(true),
                new Object[]{noinfA, noinfAnn, noinfUnion1, noinfUnion2, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2}, 2);
    }

    @Test
    public void testLDCInfGDirect() {
        assertValues("", infG.listDeclaringClasses(true), new Object[]{infA, infAnn, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2}, 1);
    }

    @Test
    public void testLDCNoinfGNodirect() {
        assertValues("", noinfG.listDeclaringClasses(false),
                new Object[]{noinfA, noinfB, noinfC, noinfUnion1, noinfUnion2, noinfAnn, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2}, 2);
    }

    @Test
    public void testLDCInfGNodirect() {
        assertValues("", infG.listDeclaringClasses(false),
                new Object[]{infA, infB, infC, infAnn, noinfUnion1, noinfUnion2, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2}, 2);
    }

}
