package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
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

import static com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine.NS;
import static com.gitlab.sszuev.jena.ont.testutils.JunitExtensions.assertValues;
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
        IOTestUtils.readResourceModel(mNoInf, "/jena/frame-view-test-ldp.rdf");

        mInf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        IOTestUtils.readResourceModel(mInf, "/jena/frame-view-test-ldp.rdf");

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
        assertValues("", noinfA.listDeclaredProperties(false), noinfPa, noinfQa, noinfG, noinfQb);
    }

    @Test
    public void testHasDPNoinfANodirect() {
        assertTrue(noinfA.hasDeclaredProperty(noinfPa, false));
        assertFalse(noinfA.hasDeclaredProperty(noinfPb, false));
    }

    @Test
    public void testLDPNoinfADirect() {
        assertValues("", noinfA.listDeclaredProperties(true), noinfPa, noinfQa, noinfG, noinfQb);
    }

    @Test
    public void testLDPInfANodirect() {
        assertValues("", infA.listDeclaredProperties(false), infPa, infQa, infQb, noinfG);
    }

    @Test
    public void testLDPInfADirect() {
        assertValues("", infA.listDeclaredProperties(true), infPa, infQa, infQb, noinfG);
    }

    @Test
    public void testLDPNoinfBNodirect() {
        assertValues("", noinfB.listDeclaredProperties(false), noinfPa, noinfPb, noinfQa, noinfG, noinfQb);
    }

    @Test
    public void testLDPNoinfBDirect() {
        assertValues("", noinfB.listDeclaredProperties(true),
                noinfPb);
    }

    @Test
    public void testLDPInfBNodirect() {
        assertValues("", infB.listDeclaredProperties(false),
                infPa, infPb, infQa, infQb, infG);
    }

    @Test
    public void testLDPInfBDirect() {
        assertValues("", infB.listDeclaredProperties(true), infPb);
    }

    @Test
    public void testLDPNoinfCNodirect() {
        assertValues("", noinfC.listDeclaredProperties(false),
                noinfPa, noinfPb, noinfPc, noinfQa, noinfG, noinfQb);
    }

    @Test
    public void testLDPNoinfCDirect() {
        assertValues("", noinfC.listDeclaredProperties(true), noinfPc);
    }

    @Test
    public void testLDPInfCNodirect() {
        assertValues("", infC.listDeclaredProperties(false),
                infPa, infPb, infPc, infQa, infQb, infG);
    }

    @Test
    public void testLDPInfCDirect() {
        assertValues("", infC.listDeclaredProperties(true), infPc);
    }


    @Test
    public void testLDPNoinfAnnNodirect() {
        assertValues("", noinfAnn.listDeclaredProperties(false), noinfPann, noinfG, noinfQb);
    }

    @Test
    public void testLDPNoinfAnnDirect() {
        assertValues("", noinfAnn.listDeclaredProperties(true), noinfPann, noinfG, noinfQb);
    }

    @Test
    public void testLDPInfAnnNodirect() {
        assertValues("", infAnn.listDeclaredProperties(false), noinfPann, noinfG);
    }

    @Test
    public void testLDPInfAnnDirect() {
        assertValues("", infAnn.listDeclaredProperties(true), noinfPann, noinfG);
    }


    @Test
    public void testLDPNoinfUnionNodirect() {
        assertValues("", noinfUnion1.listDeclaredProperties(false), noinfG, noinfQb);
        assertValues("", noinfUnion2.listDeclaredProperties(false), noinfG, noinfQb);
    }

    @Test
    public void testLDPInfUnionNodirect() {
        assertValues("", infUnion1.listDeclaredProperties(false), infPunion, infG);
        assertValues("", infUnion2.listDeclaredProperties(false), infPunion, infG);
    }

    @Test
    public void testLDPNoinfIntersectNodirect() {
        assertValues("", noinfIntersect1.listDeclaredProperties(false), noinfG, noinfQb);
        assertValues("", noinfIntersect2.listDeclaredProperties(false), noinfG, noinfQb);
    }

    @Test
    public void testLDPInfIntersectNodirect() {
        assertValues("", infIntersect1.listDeclaredProperties(false), infG);
        assertValues("", infIntersect2.listDeclaredProperties(false), infG);
    }

    @Test
    public void testLDCNoinfPaNodirect() {
        assertValues("", noinfPa.listDeclaringClasses(false), noinfA, noinfB, noinfC);
    }

    @Test
    public void testLDCInfPaNodirect() {
        assertValues("", infPa.listDeclaringClasses(false), infA, infB, infC);
    }

    @Test
    public void testLDCNoinfPbNodirect() {
        assertValues("", noinfPb.listDeclaringClasses(false), noinfB, noinfC);
    }

    @Test
    public void testLDCInfPbNodirect() {
        assertValues("", infPb.listDeclaringClasses(false), infC, infB);
    }

    @Test
    public void testLDCNoinfPcNodirect() {
        assertValues("", noinfPc.listDeclaringClasses(false), noinfC);
    }

    @Test
    public void testLDCInfPcNodirect() {
        assertValues("", infPc.listDeclaringClasses(false), infC);
    }

    @Test
    public void testLDCNoinfPaDirect() {
        assertValues("", noinfPa.listDeclaringClasses(true), noinfA);
    }

    @Test
    public void testLDCInfPaDirect() {
        assertValues("", infPa.listDeclaringClasses(true), infA);
    }

    @Test
    public void testLDCNoinfPbDirect() {
        assertValues("", noinfPb.listDeclaringClasses(true), noinfB);
    }

    @Test
    public void testLDCInfPbDirect() {
        assertValues("", infPb.listDeclaringClasses(true), infB);
    }

    @Test
    public void testLDCNoinfPcDirect() {
        assertValues("", noinfPc.listDeclaringClasses(true), noinfC);
    }

    @Test
    public void testLDCInfPcDirect() {
        assertValues("", infPc.listDeclaringClasses(true), infC);
    }

    @Test
    public void testLDCNoinfGDirect() {
        assertValues("", 2, noinfG.listDeclaringClasses(true),
                noinfA, noinfAnn, noinfUnion1, noinfUnion2, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2);
    }

    @Test
    public void testLDCInfGDirect() {
        assertValues("", 1, infG.listDeclaringClasses(true), infA, infAnn, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2);
    }

    @Test
    public void testLDCNoinfGNodirect() {
        assertValues("", 2, noinfG.listDeclaringClasses(false),
                noinfA, noinfB, noinfC, noinfUnion1, noinfUnion2, noinfAnn, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2);
    }

    @Test
    public void testLDCInfGNodirect() {
        assertValues("", 2, infG.listDeclaringClasses(false),
                infA, infB, infC, infAnn, noinfUnion1, noinfUnion2, mNoInf.getOntClass(NS + "Joint"), noinfIntersect1, noinfIntersect2);
    }

}
