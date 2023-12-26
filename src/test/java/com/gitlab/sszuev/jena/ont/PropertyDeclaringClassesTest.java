package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesBCA;

public class PropertyDeclaringClassesTest {

    private static void debug(Resource r) {
        Set<String> direct = r.as(OntProperty.class).listDeclaringClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirect = r.as(OntProperty.class).listDeclaringClasses(false).mapWith(Resource::getLocalName).toSet();
        System.out.println("DIRECT::" + r.getLocalName() + " :: " + direct);
        System.out.println("INDIRECT::" + r.getLocalName() + " :: " + indirect);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListDeclaringClasses1a(TestSpec spec) {
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));

        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");

        OntProperty p0 = m.createResource(NS + "p0", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .as(OntProperty.class);
        OntProperty pA = m.createResource(NS + "pA", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, A).as(OntProperty.class);
        OntProperty pB = m.createResource(NS + "pB", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, B).as(OntProperty.class);
        OntProperty pC = m.createResource(NS + "pC", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, C).as(OntProperty.class);

        pA.addProperty(RDFS.subPropertyOf, pB);
        pB.addProperty(RDFS.subPropertyOf, pC);

        debug(p0);
        debug(pA);
        debug(pB);
        debug(pC);

        Assertions.assertEquals(Set.of(B, C), p0.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), p0.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(A), pA.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A), pA.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(B), pB.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), pB.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(C), pC.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), pC.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListDeclaringClasses1b(TestSpec spec) {
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));

        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");

        OntProperty p0 = m.createResource(NS + "p0", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .as(OntProperty.class);
        OntProperty pA = m.createResource(NS + "pA", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, A).as(OntProperty.class);
        OntProperty pB = m.createResource(NS + "pB", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, B).as(OntProperty.class);
        OntProperty pC = m.createResource(NS + "pC", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, C).as(OntProperty.class);

        pA.addProperty(RDFS.subPropertyOf, pB);
        pB.addProperty(RDFS.subPropertyOf, pC);

        debug(p0);
        debug(pA);
        debug(pB);
        debug(pC);

        Assertions.assertEquals(Set.of(B, C), p0.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), p0.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(A), pA.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A), pA.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(B, C), pB.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), pB.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(B, C), pC.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), pC.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListDeclaringClasses1c(TestSpec spec) {
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));

        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");

        OntProperty p0 = m.createResource(NS + "p0", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .as(OntProperty.class);
        OntProperty pA = m.createResource(NS + "pA", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, A).as(OntProperty.class);
        OntProperty pB = m.createResource(NS + "pB", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, B).as(OntProperty.class);
        OntProperty pC = m.createResource(NS + "pC", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, C).as(OntProperty.class);

        pA.addProperty(RDFS.subPropertyOf, pB);
        pB.addProperty(RDFS.subPropertyOf, pC);

        debug(p0);
        debug(pA);
        debug(pB);
        debug(pC);

        Assertions.assertEquals(Set.of(), p0.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), p0.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(A), pA.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A), pA.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(B), pB.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), pB.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(C), pC.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(A, B, C), pC.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListDeclaringClasses2a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        Resource c1 = m.createClass(":C1");
        Resource c2 = m.createClass(":C2");
        Resource c3 = m.createClass(":C3");
        Resource c4 = m.createClass(":C4");
        Resource c5 = OWL.Thing.inModel(m);
        Resource c6 = OWL.Nothing.inModel(m);

        OntProperty p1 = m.createResource(":p1", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p2 = m.createResource(":p2", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p3 = m.createResource(":p3", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p4 = m.createResource(":p4", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p5 = m.createResource(":p5", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p6 = m.createResource(":p6", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p7 = m.createResource(":p7", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p8 = OWL2.topObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p9 = OWL2.bottomDataProperty.inModel(m).addProperty(RDF.type, OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p10 = OWL2.bottomObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);

        p1.addProperty(RDFS.subPropertyOf, p2);
        p2.addProperty(RDFS.subPropertyOf, p3);
        p5.addProperty(RDFS.subPropertyOf, p6);

        c1.addProperty(RDFS.subClassOf, c2);
        c2.addProperty(RDFS.subClassOf, c3);
        c1.addProperty(RDFS.subClassOf, c4);

        p1.addProperty(RDFS.domain, c1);
        p2.addProperty(RDFS.domain, c2);
        p4.addProperty(RDFS.domain, c4);
        p6.addProperty(RDFS.domain, c3);
        p7.addProperty(RDFS.domain, c1);
        p8.addProperty(RDFS.domain, c5);
        p9.addProperty(RDFS.domain, c6);

        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c2), p2.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2), p2.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p3.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p3.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c4), p4.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c4), p4.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p5.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p5.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p6.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p6.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p8.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p8.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p9.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p9.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p10.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p10.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListDeclaringClasses2b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        Resource c1 = m.createClass(":C1");
        Resource c2 = m.createClass(":C2");
        Resource c3 = m.createClass(":C3");
        Resource c4 = m.createClass(":C4");
        Resource c5 = OWL.Thing.inModel(m);
        Resource c6 = OWL.Nothing.inModel(m);

        OntProperty p1 = m.createResource(":p1", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p2 = m.createResource(":p2", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p3 = m.createResource(":p3", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p4 = m.createResource(":p4", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p5 = m.createResource(":p5", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p6 = m.createResource(":p6", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p7 = m.createResource(":p7", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p8 = OWL2.topObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p9 = OWL2.bottomDataProperty.inModel(m).addProperty(RDF.type, OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p10 = OWL2.bottomObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);

        p1.addProperty(RDFS.subPropertyOf, p2);
        p2.addProperty(RDFS.subPropertyOf, p3);
        p5.addProperty(RDFS.subPropertyOf, p6);

        c1.addProperty(RDFS.subClassOf, c2);
        c2.addProperty(RDFS.subClassOf, c3);
        c1.addProperty(RDFS.subClassOf, c4);

        p1.addProperty(RDFS.domain, c1);
        p2.addProperty(RDFS.domain, c2);
        p4.addProperty(RDFS.domain, c4);
        p6.addProperty(RDFS.domain, c3);
        p7.addProperty(RDFS.domain, c1);
        p8.addProperty(RDFS.domain, c5);
        p9.addProperty(RDFS.domain, c6);

        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c2), p2.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2), p2.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(), p3.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p3.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c4), p4.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c4), p4.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(), p5.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p5.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p6.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p6.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(), p8.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p8.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(), p9.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p9.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(), p10.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p10.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListDeclaringClasses2c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        Resource c1 = m.createClass(":C1");
        Resource c2 = m.createClass(":C2");
        Resource c3 = m.createClass(":C3");
        Resource c4 = m.createClass(":C4");
        Resource c5 = OWL.Thing.inModel(m);
        Resource c6 = OWL.Nothing.inModel(m);

        OntProperty p1 = m.createResource(":p1", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p2 = m.createResource(":p2", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p3 = m.createResource(":p3", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p4 = m.createResource(":p4", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p5 = m.createResource(":p5", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p6 = m.createResource(":p6", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p7 = m.createResource(":p7", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p8 = OWL2.topObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p9 = OWL2.bottomDataProperty.inModel(m).addProperty(RDF.type, OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p10 = OWL2.bottomObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);

        p1.addProperty(RDFS.subPropertyOf, p2);
        p2.addProperty(RDFS.subPropertyOf, p3);
        p5.addProperty(RDFS.subPropertyOf, p6);

        c1.addProperty(RDFS.subClassOf, c2);
        c2.addProperty(RDFS.subClassOf, c3);
        c1.addProperty(RDFS.subClassOf, c4);

        p1.addProperty(RDFS.domain, c1);
        p2.addProperty(RDFS.domain, c2);
        p4.addProperty(RDFS.domain, c4);
        p6.addProperty(RDFS.domain, c3);
        p7.addProperty(RDFS.domain, c1);
        p8.addProperty(RDFS.domain, c5);
        p9.addProperty(RDFS.domain, c6);

        debug(p1);
        debug(p2);
        debug(p3);
        debug(p4);
        debug(p5);
        debug(p6);
        debug(p7);
        debug(p8);
        debug(p9);
        debug(p10);

        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c2), p2.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2), p2.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p3.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p3.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c4), p4.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c4), p4.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p5.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p5.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p6.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p6.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p8.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p8.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p9.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p9.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p10.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p10.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListDeclaringClasses2d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        Resource c1 = m.createClass(":C1");
        Resource c2 = m.createClass(":C2");
        Resource c3 = m.createClass(":C3");
        Resource c4 = m.createClass(":C4");
        Resource c5 = OWL.Thing.inModel(m);
        Resource c6 = OWL.Nothing.inModel(m);

        OntProperty p1 = m.createResource(":p1", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p2 = m.createResource(":p2", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p3 = m.createResource(":p3", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p4 = m.createResource(":p4", OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p5 = m.createResource(":p5", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p6 = m.createResource(":p6", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p7 = m.createResource(":p7", OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p8 = OWL2.topObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p9 = OWL2.bottomDataProperty.inModel(m).addProperty(RDF.type, OWL.DatatypeProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);
        OntProperty p10 = OWL2.bottomObjectProperty.inModel(m).addProperty(RDF.type, OWL.ObjectProperty).addProperty(RDF.type, RDF.Property).as(OntProperty.class);

        p1.addProperty(RDFS.subPropertyOf, p2);
        p2.addProperty(RDFS.subPropertyOf, p3);
        p5.addProperty(RDFS.subPropertyOf, p6);

        c1.addProperty(RDFS.subClassOf, c2);
        c2.addProperty(RDFS.subClassOf, c3);
        c1.addProperty(RDFS.subClassOf, c4);

        p1.addProperty(RDFS.domain, c1);
        p2.addProperty(RDFS.domain, c2);
        p4.addProperty(RDFS.domain, c4);
        p6.addProperty(RDFS.domain, c3);
        p7.addProperty(RDFS.domain, c1);
        p8.addProperty(RDFS.domain, c5);
        p9.addProperty(RDFS.domain, c6);

        debug(p1);
        debug(p2);
        debug(p3);
        debug(p4);
        debug(p5);
        debug(p6);
        debug(p7);
        debug(p8);
        debug(p9);
        debug(p10);

        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c2), p2.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2), p2.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p3.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p3.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c4), p4.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c4), p4.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p5.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p5.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p6.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p6.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p8.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p8.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(), p9.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(), p9.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4), p10.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4), p10.listDeclaringClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListDeclaringClasses3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        Resource c1 = m.createClass(":C1");
        Resource c2 = m.createClass(":C2");
        Resource c3 = m.createClass(":C3");
        Resource c4 = m.createClass(":C4");
        Resource c5 = m.createClass(":C5");
        Resource c6 = m.createClass(":C6");

        OntProperty p1 = m.createResource(":p1", RDF.Property).as(OntProperty.class);
        OntProperty p2 = m.createResource(":p2", RDF.Property).as(OntProperty.class);
        OntProperty p3 = m.createResource(":p3", RDF.Property).as(OntProperty.class);
        OntProperty p4 = m.createResource(":p4", RDF.Property).as(OntProperty.class);
        OntProperty p5 = m.createResource(":p5", RDF.Property).as(OntProperty.class);
        OntProperty p6 = m.createResource(":p6", RDF.Property).as(OntProperty.class);
        OntProperty p7 = m.createResource(":p7", RDF.Property).as(OntProperty.class);
        OntProperty p8 = m.createResource(":p8", RDF.Property).as(OntProperty.class);
        OntProperty p9 = m.createResource(":p9", RDF.Property).as(OntProperty.class);
        OntProperty p10 = m.createResource(":p10", RDF.Property).as(OntProperty.class);

        p1.addProperty(RDFS.subPropertyOf, p2);
        p2.addProperty(RDFS.subPropertyOf, p3);
        p5.addProperty(RDFS.subPropertyOf, p6);

        c1.addProperty(RDFS.subClassOf, c2);
        c2.addProperty(RDFS.subClassOf, c3);
        c1.addProperty(RDFS.subClassOf, c4);

        p1.addProperty(RDFS.domain, c1);
        p2.addProperty(RDFS.domain, c2);
        p4.addProperty(RDFS.domain, c4);
        p6.addProperty(RDFS.domain, c3);
        p7.addProperty(RDFS.domain, c1);
        p8.addProperty(RDFS.domain, c5);
        p9.addProperty(RDFS.domain, c6);

        debug(p1);
        debug(p2);
        debug(p3);
        debug(p4);
        debug(p5);
        debug(p6);
        debug(p7);
        debug(p8);
        debug(p9);
        debug(p10);

        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p1.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c2), p2.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2), p2.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4, c5, c6), p3.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4, c5, c6), p3.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c4), p4.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c4), p4.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4, c5, c6), p5.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4, c5, c6), p5.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3), p6.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3), p6.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1), p7.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c5), p8.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c5), p8.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c6), p9.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c6), p9.listDeclaringClasses(false).toSet());

        Assertions.assertEquals(Set.of(c3, c4, c5, c6), p10.listDeclaringClasses(true).toSet());
        Assertions.assertEquals(Set.of(c1, c2, c3, c4, c5, c6), p10.listDeclaringClasses(false).toSet());
    }
}
