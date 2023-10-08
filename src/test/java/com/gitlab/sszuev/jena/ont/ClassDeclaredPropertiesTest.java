package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine.NS;

public class ClassDeclaredPropertiesTest {

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties1(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass("A");
        OntClass B = m.createClass("B");
        OntClass C = m.createClass("C");
        OntClass D = m.createClass("D");
        OntClass E = m.createClass("E");
        OntClass THING = m.getOntClass(OWL2.Thing.getURI());
        A.addSuperClass(B);
        B.addSuperClass(C);
        C.addSuperClass(D);
        E.addSuperClass(THING);

        DatatypeProperty d1 = m.createDatatypeProperty("d1");
        DatatypeProperty d2 = m.createDatatypeProperty("d2");
        ObjectProperty o1 = m.createObjectProperty("o1");
        ObjectProperty o2 = m.createObjectProperty("o2");
        o1.addSuperProperty(o2);
        o2.addSuperProperty(OWL2.topObjectProperty);
        d1.addDomain(A);
        d2.addDomain(B);
        o1.addDomain(C);
        o2.addDomain(THING);

        Set<OntProperty> directDeclaredPropertiesThing = THING.listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesThing = THING.listDeclaredProperties(false).toSet();
        System.out.println("[direct]THING::" + directDeclaredPropertiesThing);
        System.out.println("[indirect]THING::" + indirectDeclaredPropertiesThing);

        Set<OntProperty> directDeclaredPropertiesA = A.listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesA = A.listDeclaredProperties(false).toSet();
        System.out.println("[direct]A::" + directDeclaredPropertiesA);
        System.out.println("[indirect]A::" + indirectDeclaredPropertiesA);

        Set<OntProperty> directDeclaredPropertiesB = B.listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesB = B.listDeclaredProperties(false).toSet();
        System.out.println("[direct]B::" + directDeclaredPropertiesB);
        System.out.println("[indirect]B::" + indirectDeclaredPropertiesB);

        Set<OntProperty> directDeclaredPropertiesC = C.listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesC = C.listDeclaredProperties(false).toSet();
        System.out.println("[direct]C::" + directDeclaredPropertiesC);
        System.out.println("[indirect]C::" + indirectDeclaredPropertiesC);

        Set<OntProperty> directDeclaredPropertiesD = D.listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesD = D.listDeclaredProperties(false).toSet();
        System.out.println("[direct]D::" + directDeclaredPropertiesD);
        System.out.println("[indirect]D::" + indirectDeclaredPropertiesD);

        Set<OntProperty> directDeclaredPropertiesE = E.listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesE = E.listDeclaredProperties(false).toSet();
        System.out.println("[direct]E::" + directDeclaredPropertiesE);
        System.out.println("[indirect]E::" + indirectDeclaredPropertiesE);

        Assertions.assertEquals(Set.of(o2), directDeclaredPropertiesThing);
        Assertions.assertEquals(Set.of(o2), indirectDeclaredPropertiesThing);

        Assertions.assertEquals(Set.of(d1), directDeclaredPropertiesA);
        Assertions.assertEquals(Set.of(d1, d2, o1, o2), indirectDeclaredPropertiesA);

        Assertions.assertEquals(Set.of(d2), directDeclaredPropertiesB);
        Assertions.assertEquals(Set.of(d2, o1, o2), indirectDeclaredPropertiesB);

        Assertions.assertEquals(Set.of(o1), directDeclaredPropertiesC);
        Assertions.assertEquals(Set.of(o1, o2), indirectDeclaredPropertiesC);

        Assertions.assertEquals(Set.of(o2), directDeclaredPropertiesD);
        Assertions.assertEquals(Set.of(o2), indirectDeclaredPropertiesD);

        Assertions.assertEquals(Set.of(o2), directDeclaredPropertiesE);
        Assertions.assertEquals(Set.of(o2), indirectDeclaredPropertiesE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties2(TestSpec spec) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //    A
        OntModel m = TestModelFactory.createClassesDBFCEA(ModelFactory.createOntologyModel(spec.inst));

        Resource d1 = m.createResource(TestModelFactory.NS + "d1", OWL2.DatatypeProperty).addProperty(RDF.type, RDF.Property);
        Resource d2 = m.createResource(TestModelFactory.NS + "d2", OWL2.DatatypeProperty).addProperty(RDF.type, RDF.Property);
        Resource o1 = m.createResource(TestModelFactory.NS + "o1", OWL2.ObjectProperty).addProperty(RDF.type, RDF.Property);
        Resource o2 = m.createResource(TestModelFactory.NS + "o2", OWL2.ObjectProperty).addProperty(RDF.type, RDF.Property);
        o1.addProperty(RDFS.subClassOf, o2);
        o2.addProperty(RDFS.subClassOf, OWL2.topObjectProperty);
        d1.addProperty(RDFS.domain, m.getResource(TestModelFactory.NS + "A"));
        d2.addProperty(RDFS.domain, m.getResource(TestModelFactory.NS + "B"));
        o1.addProperty(RDFS.domain, m.getResource(TestModelFactory.NS + "C"));
        o2.addProperty(RDFS.domain, m.getResource(TestModelFactory.NS + "F"));
        o2.addProperty(RDFS.range, m.getResource(TestModelFactory.NS + "E"));
        d2.addProperty(RDFS.range, m.getResource(TestModelFactory.NS + "D"));

        Set<OntProperty> directDeclaredPropertiesA = m.getOntClass(TestModelFactory.NS + "A").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesA = m.getOntClass(TestModelFactory.NS + "A").listDeclaredProperties(false).toSet();
        System.out.println("[direct]A::" + directDeclaredPropertiesA);
        System.out.println("[indirect]A::" + indirectDeclaredPropertiesA);

        Set<OntProperty> directDeclaredPropertiesB = m.getOntClass(TestModelFactory.NS + "B").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesB = m.getOntClass(TestModelFactory.NS + "B").listDeclaredProperties(false).toSet();
        System.out.println("[direct]B::" + directDeclaredPropertiesB);
        System.out.println("[indirect]B::" + indirectDeclaredPropertiesB);

        Set<OntProperty> directDeclaredPropertiesC = m.getOntClass(TestModelFactory.NS + "C").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesC = m.getOntClass(TestModelFactory.NS + "C").listDeclaredProperties(false).toSet();
        System.out.println("[direct]C::" + directDeclaredPropertiesC);
        System.out.println("[indirect]C::" + indirectDeclaredPropertiesC);

        Set<OntProperty> directDeclaredPropertiesD = m.getOntClass(TestModelFactory.NS + "D").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesD = m.getOntClass(TestModelFactory.NS + "D").listDeclaredProperties(false).toSet();
        System.out.println("[direct]D::" + directDeclaredPropertiesD);
        System.out.println("[indirect]D::" + indirectDeclaredPropertiesD);

        Set<OntProperty> directDeclaredPropertiesE = m.getOntClass(TestModelFactory.NS + "E").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesE = m.getOntClass(TestModelFactory.NS + "E").listDeclaredProperties(false).toSet();
        System.out.println("[direct]E::" + directDeclaredPropertiesE);
        System.out.println("[indirect]E::" + indirectDeclaredPropertiesE);

        Set<OntProperty> directDeclaredPropertiesF = m.getOntClass(TestModelFactory.NS + "F").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesF = m.getOntClass(TestModelFactory.NS + "F").listDeclaredProperties(false).toSet();
        System.out.println("[direct]F::" + directDeclaredPropertiesF);
        System.out.println("[indirect]F::" + indirectDeclaredPropertiesF);

        Assertions.assertEquals(Set.of(d1), directDeclaredPropertiesA);
        Assertions.assertEquals(Set.of(o1, o2, d1, d2), indirectDeclaredPropertiesA);

        Assertions.assertEquals(Set.of(d2), directDeclaredPropertiesB);
        Assertions.assertEquals(Set.of(d2), indirectDeclaredPropertiesB);

        Assertions.assertEquals(Set.of(o1), directDeclaredPropertiesC);
        Assertions.assertEquals(Set.of(o1, d2), indirectDeclaredPropertiesC);

        Assertions.assertEquals(Set.of(), directDeclaredPropertiesD);
        Assertions.assertEquals(Set.of(), indirectDeclaredPropertiesD);

        Assertions.assertEquals(Set.of(), directDeclaredPropertiesE);
        Assertions.assertEquals(Set.of(o2), indirectDeclaredPropertiesE);

        Assertions.assertEquals(Set.of(o2), directDeclaredPropertiesF);
        Assertions.assertEquals(Set.of(o2), indirectDeclaredPropertiesF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties3(TestSpec spec) {
        OntModel m0 = ModelFactory.createOntologyModel(spec.inst);

        // in model M0, p0 has class c0 in the domain
        OntClass c0 = m0.createClass(TestModelFactory.NS + "c0");
        ObjectProperty p0 = m0.createObjectProperty(TestModelFactory.NS + "p0");
        p0.setDomain(c0);

        // in model M1, class c1 is a subClass of c0
        OntModel m1 = ModelFactory.createOntologyModel(spec.inst);
        OntClass c1 = m1.createClass(TestModelFactory.NS + "c1");
        c1.addSuperClass(c0);

        // simulate imports
        m1.addSubModel(m0);

        // get a c0 reference from m1
        OntClass cc0 = m1.getOntClass(TestModelFactory.NS + "c0");
        Assertions.assertNotNull(cc0);

        JunitExtensions.assertValues("", c1.listDeclaredProperties(), p0);
        JunitExtensions.assertValues("", c0.listDeclaredProperties(false), p0);
        JunitExtensions.assertValues("", cc0.listDeclaredProperties(false), p0);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties4(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);

        // a simple class hierarchy  organism -> vertebrate -> mammal -> dog
        OntClass organism = m.createClass(TestModelFactory.NS + "Organism");
        OntClass vertebrate = m.createClass(TestModelFactory.NS + "Vertebrate");
        OntClass mammal = m.createClass(TestModelFactory.NS + "Mammal");
        OntClass dog = m.createClass(TestModelFactory.NS + "Dog");

        organism.addSubClass(vertebrate);
        vertebrate.addSubClass(mammal);
        mammal.addSubClass(dog);

        // hair as a covering
        OntClass covering = m.createClass(TestModelFactory.NS + "Covering");
        Individual hair = m.createIndividual(TestModelFactory.NS + "hair", covering);

        // various properties
        DatatypeProperty limbsCount = m.createDatatypeProperty(TestModelFactory.NS + "limbsCount");
        DatatypeProperty hasCovering = m.createDatatypeProperty(TestModelFactory.NS + "hasCovering");
        DatatypeProperty numYoung = m.createDatatypeProperty(TestModelFactory.NS + "numYoung");

        // vertebrates have limbs, mammals have live young
        limbsCount.addDomain(vertebrate);
        numYoung.addDomain(mammal);

        // mammals have-covering = hair
        Restriction r = m.createRestriction(hasCovering);
        r.convertToHasValueRestriction(hair);
        mammal.addSuperClass(r);

        Assertions.assertEquals(Set.of(hasCovering), organism.listDeclaredProperties().toSet());
        Assertions.assertEquals(Set.of(limbsCount, hasCovering), vertebrate.listDeclaredProperties().toSet());
        Assertions.assertEquals(Set.of(limbsCount, hasCovering, numYoung), mammal.listDeclaredProperties().toSet());
        Assertions.assertEquals(Set.of(limbsCount, hasCovering, numYoung), dog.listDeclaredProperties().toSet());
        Assertions.assertEquals(Set.of(hasCovering), r.listDeclaredProperties().toSet());

        Assertions.assertEquals(Set.of(hasCovering), organism.listDeclaredProperties(true).toSet());
        Assertions.assertEquals(Set.of(limbsCount), vertebrate.listDeclaredProperties(true).toSet());
        Assertions.assertEquals(Set.of(numYoung), mammal.listDeclaredProperties(true).toSet());
        Assertions.assertEquals(Set.of(), dog.listDeclaredProperties(true).toSet());
        Assertions.assertEquals(Set.of(hasCovering), r.listDeclaredProperties(true).toSet());

        Assertions.assertEquals(Set.of(hasCovering), organism.listDeclaredProperties(false).toSet());
        Assertions.assertEquals(Set.of(hasCovering, limbsCount), vertebrate.listDeclaredProperties(false).toSet());
        Assertions.assertEquals(Set.of(hasCovering, numYoung, limbsCount), mammal.listDeclaredProperties(false).toSet());
        Assertions.assertEquals(Set.of(hasCovering, numYoung, limbsCount), dog.listDeclaredProperties(false).toSet());
        Assertions.assertEquals(Set.of(hasCovering), r.listDeclaredProperties(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties5(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(TestModelFactory.NS + "A");
        OntClass C = m.createClass(TestModelFactory.NS + "C");
        C.addSuperClass(A);

        OntProperty p = m.createOntProperty(TestModelFactory.NS + "p");
        OntProperty q = m.createOntProperty(TestModelFactory.NS + "q");
        OntProperty s = m.createOntProperty(TestModelFactory.NS + "s");

        p.setDomain(A);
        q.setDomain(A);
        s.setDomain(C);

        Assertions.assertEquals(Set.of(p, q, s), C.listDeclaredProperties().toSet());
        Assertions.assertEquals(Set.of(p, q, s), C.listDeclaredProperties(false).toSet());
        Assertions.assertEquals(Set.of(s), C.listDeclaredProperties(true).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties6(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/frame-view-test-ldp.rdf");

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");

        ObjectProperty pA = m.getObjectProperty(NS + "pA");
        ObjectProperty pB = m.getObjectProperty(NS + "pB");
        ObjectProperty pC = m.getObjectProperty(NS + "pC");
        ObjectProperty qA = m.getObjectProperty(NS + "qA");
        ObjectProperty global = m.getObjectProperty(NS + "global");
        ObjectProperty qB = m.getObjectProperty(NS + "qB");

        JunitExtensions.assertValues(A.listDeclaredProperties(false), pA, qA, global, qB);
        JunitExtensions.assertValues(A.listDeclaredProperties(true), pA, qA, global, qB);

        JunitExtensions.assertValues(B.listDeclaredProperties(false), pA, pB, qA, global, qB);
        JunitExtensions.assertValues(B.listDeclaredProperties(true), pB);

        JunitExtensions.assertValues(C.listDeclaredProperties(false), pA, pB, pC, qA, global, qB);
        JunitExtensions.assertValues(C.listDeclaredProperties(true), pC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListDeclaredProperties7a(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/frame-view-test-ldp.rdf");

        OntClass Union1 = m.getOntClass(NS + "Union1");
        OntClass Union2 = m.getOntClass(NS + "Union2");
        OntClass Intersect1 = m.getOntClass(NS + "Intersect1");
        OntClass Intersect2 = m.getOntClass(NS + "Intersect2");
        OntClass HasAnn = m.getOntClass(NS + "HasAnn");

        AnnotationProperty ann = m.getAnnotationProperty(NS + "ann");
        ObjectProperty global = m.getObjectProperty(NS + "global");
        ObjectProperty qB = m.getObjectProperty(NS + "qB");

        JunitExtensions.assertValues(Union1.listDeclaredProperties(false), global, qB);
        JunitExtensions.assertValues(Union2.listDeclaredProperties(false), global, qB);
        JunitExtensions.assertValues(Union1.listDeclaredProperties(true), global, qB);
        JunitExtensions.assertValues(Union2.listDeclaredProperties(true), global, qB);

        JunitExtensions.assertValues(Intersect1.listDeclaredProperties(false), qB, global);
        JunitExtensions.assertValues(Intersect2.listDeclaredProperties(false), qB, global);
        JunitExtensions.assertValues(Intersect1.listDeclaredProperties(true), qB, global);
        JunitExtensions.assertValues(Intersect2.listDeclaredProperties(true), qB, global);

        JunitExtensions.assertValues(HasAnn.listDeclaredProperties(false), ann, global, qB);
        JunitExtensions.assertValues(HasAnn.listDeclaredProperties(true), ann, global, qB);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListDeclaredProperties7b(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/frame-view-test-ldp.rdf");
        OntClass Union1 = m.getOntClass(NS + "Union1");
        OntClass Union2 = m.getOntClass(NS + "Union2");
        OntClass Intersect1 = m.getOntClass(NS + "Intersect1");
        OntClass Intersect2 = m.getOntClass(NS + "Intersect2");
        OntClass HasAnn = m.getOntClass(NS + "HasAnn");
        AnnotationProperty ann = m.getAnnotationProperty(NS + "ann");
        ObjectProperty global = m.getObjectProperty(NS + "global");
        ObjectProperty unionP = m.getObjectProperty(NS + "unionP");

        JunitExtensions.assertValues(Union1.listDeclaredProperties(false), global, unionP);
        JunitExtensions.assertValues(Union2.listDeclaredProperties(false), global, unionP);
        JunitExtensions.assertValues(Union1.listDeclaredProperties(true));
        JunitExtensions.assertValues(Union2.listDeclaredProperties(true));
        JunitExtensions.assertValues(Union2.listDeclaredProperties(true));

        JunitExtensions.assertValues(Intersect1.listDeclaredProperties(false), global);
        JunitExtensions.assertValues(Intersect2.listDeclaredProperties(false), global);
        JunitExtensions.assertValues(Intersect1.listDeclaredProperties(true), global);
        JunitExtensions.assertValues(Intersect2.listDeclaredProperties(true), global);

        JunitExtensions.assertValues(HasAnn.listDeclaredProperties(false), ann, global);
        JunitExtensions.assertValues(HasAnn.listDeclaredProperties(true), ann, global);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testHasDeclaredProperties1(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass c1 = m.createUnionClass(null, m.createList(OWL2.Thing));
        OntClass c2 = m.createEnumeratedClass(null, m.createList(
                m.createIndividual("i1", OWL2.NamedIndividual),
                m.createIndividual("i2", OWL2.NamedIndividual)
        ));
        OntClass c3 = m.createClass("C3"); // root
        OntClass c4 = m.createClass("C4"); // root
        OntClass c5 = m.createClass("C5"); // root

        DatatypeProperty d1 = m.createDatatypeProperty("d1");
        DatatypeProperty d2 = m.createDatatypeProperty("d2");
        ObjectProperty o1 = m.createObjectProperty("o1");
        ObjectProperty o2 = m.createObjectProperty("o2"); // global
        Property o3 = OWL2.topObjectProperty.inModel(m);
        Property d3 = OWL2.bottomDataProperty.inModel(m);

        c1.addSuperClass(c2);
        c2.addSuperClass(c3);
        c5.addSuperClass(OWL2.Thing);
        d1.addDomain(c1);
        d2.addDomain(c2);
        o1.addDomain(c3);


        Assertions.assertFalse(c1.hasDeclaredProperty(d3, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(o3, false));

        Assertions.assertTrue(c1.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(o2, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(d1, true));
        Assertions.assertFalse(c1.hasDeclaredProperty(d2, true));
        Assertions.assertFalse(c1.hasDeclaredProperty(o1, true));
        Assertions.assertFalse(c1.hasDeclaredProperty(o2, true));

        Assertions.assertFalse(c2.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(o2, false));
        Assertions.assertFalse(c2.hasDeclaredProperty(d1, true));
        Assertions.assertTrue(c2.hasDeclaredProperty(d2, true));
        Assertions.assertFalse(c2.hasDeclaredProperty(o1, true));
        Assertions.assertFalse(c2.hasDeclaredProperty(o2, true));

        Assertions.assertFalse(c3.hasDeclaredProperty(d1, false));
        Assertions.assertFalse(c3.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c3.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c3.hasDeclaredProperty(o2, false));
        Assertions.assertFalse(c3.hasDeclaredProperty(d1, true));
        Assertions.assertFalse(c3.hasDeclaredProperty(d2, true));
        Assertions.assertTrue(c3.hasDeclaredProperty(o1, true));
        Assertions.assertTrue(c3.hasDeclaredProperty(o2, true));

        Assertions.assertFalse(c4.hasDeclaredProperty(d1, false));
        Assertions.assertFalse(c4.hasDeclaredProperty(d2, false));
        Assertions.assertFalse(c4.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c4.hasDeclaredProperty(o2, false));
        Assertions.assertFalse(c4.hasDeclaredProperty(d1, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(d2, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(o1, true));
        Assertions.assertTrue(c4.hasDeclaredProperty(o2, true));

        Assertions.assertFalse(c5.hasDeclaredProperty(d1, false));
        Assertions.assertFalse(c5.hasDeclaredProperty(d2, false));
        Assertions.assertFalse(c5.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c5.hasDeclaredProperty(o2, false));
        Assertions.assertFalse(c5.hasDeclaredProperty(d1, true));
        Assertions.assertFalse(c5.hasDeclaredProperty(d2, true));
        Assertions.assertFalse(c5.hasDeclaredProperty(o1, true));
        Assertions.assertTrue(c5.hasDeclaredProperty(o2, true));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
    })
    public void testHasDeclaredProperties2(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass c1 = m.createUnionClass(null, m.createList(OWL2.Thing));
        OntClass c2 = m.createEnumeratedClass(null, m.createList(
                m.createIndividual("i1", OWL2.NamedIndividual),
                m.createIndividual("i2", OWL2.NamedIndividual)
        ));
        OntClass c3 = m.createClass("C3"); // root
        OntClass c4 = m.createClass("C4"); // root
        OntClass c5 = m.createClass("C5"); // root

        DatatypeProperty d1 = m.createDatatypeProperty("d1");
        DatatypeProperty d2 = m.createDatatypeProperty("d2");
        ObjectProperty o1 = m.createObjectProperty("o1");
        ObjectProperty o2 = m.createObjectProperty("o2"); // global
        Property o3 = OWL2.topObjectProperty.inModel(m);
        Property d3 = OWL2.bottomDataProperty.inModel(m);

        c1.addSuperClass(c2);
        c2.addSuperClass(c3);
        c5.addSuperClass(OWL2.Thing);
        d1.addDomain(c1);
        d2.addDomain(c2);
        o1.addDomain(c3);


        Assertions.assertFalse(c1.hasDeclaredProperty(d3, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(o3, false));

        Assertions.assertTrue(c1.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(o2, false));
        Assertions.assertTrue(c1.hasDeclaredProperty(d1, true));
        Assertions.assertTrue(c1.hasDeclaredProperty(d2, true));
        Assertions.assertTrue(c1.hasDeclaredProperty(o1, true));
        Assertions.assertTrue(c1.hasDeclaredProperty(o2, true));

        Assertions.assertTrue(c2.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(o2, false));
        Assertions.assertTrue(c2.hasDeclaredProperty(d1, true));
        Assertions.assertTrue(c2.hasDeclaredProperty(d2, true));
        Assertions.assertTrue(c2.hasDeclaredProperty(o1, true));
        Assertions.assertTrue(c2.hasDeclaredProperty(o2, true));

        Assertions.assertTrue(c3.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c3.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c3.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c3.hasDeclaredProperty(o2, false));
        Assertions.assertTrue(c3.hasDeclaredProperty(d1, true));
        Assertions.assertTrue(c3.hasDeclaredProperty(d2, true));
        Assertions.assertTrue(c3.hasDeclaredProperty(o1, true));
        Assertions.assertTrue(c3.hasDeclaredProperty(o2, true));

        Assertions.assertTrue(c4.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c4.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c4.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c4.hasDeclaredProperty(o2, false));
        Assertions.assertFalse(c4.hasDeclaredProperty(d1, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(d2, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(o1, true));
        Assertions.assertFalse(c4.hasDeclaredProperty(o2, true));

        Assertions.assertTrue(c5.hasDeclaredProperty(d1, false));
        Assertions.assertTrue(c5.hasDeclaredProperty(d2, false));
        Assertions.assertTrue(c5.hasDeclaredProperty(o1, false));
        Assertions.assertTrue(c5.hasDeclaredProperty(o2, false));
        Assertions.assertFalse(c5.hasDeclaredProperty(d1, true));
        Assertions.assertFalse(c5.hasDeclaredProperty(d2, true));
        Assertions.assertFalse(c5.hasDeclaredProperty(o1, true));
        Assertions.assertFalse(c5.hasDeclaredProperty(o2, true));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testHasDeclaredProperties3(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/frame-view-test-ldp.rdf");
        OntClass A = m.getOntClass(NS + "A");

        ObjectProperty pA = m.getObjectProperty(NS + "pA");
        ObjectProperty pB = m.getObjectProperty(NS + "pB");

        Assertions.assertTrue(A.hasDeclaredProperty(pA, false));
        Assertions.assertFalse(A.hasDeclaredProperty(pB, false));

        Assertions.assertTrue(A.hasDeclaredProperty(pA, true));
        Assertions.assertFalse(A.hasDeclaredProperty(pB, true));
    }
}
