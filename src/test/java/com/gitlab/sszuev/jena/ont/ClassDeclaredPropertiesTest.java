package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;

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
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
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
        OntModel m = TestModelFactory.createClassesDBFCEA(ModelFactory.createOntologyModel(spec.spec));

        Resource d1 = m.createResource(NS + "d1", OWL2.DatatypeProperty).addProperty(RDF.type, RDF.Property);
        Resource d2 = m.createResource(NS + "d2", OWL2.DatatypeProperty).addProperty(RDF.type, RDF.Property);
        Resource o1 = m.createResource(NS + "o1", OWL2.ObjectProperty).addProperty(RDF.type, RDF.Property);
        Resource o2 = m.createResource(NS + "o2", OWL2.ObjectProperty).addProperty(RDF.type, RDF.Property);
        o1.addProperty(RDFS.subClassOf, o2);
        o2.addProperty(RDFS.subClassOf, OWL2.topObjectProperty);
        d1.addProperty(RDFS.domain, m.getResource(NS + "A"));
        d2.addProperty(RDFS.domain, m.getResource(NS + "B"));
        o1.addProperty(RDFS.domain, m.getResource(NS + "C"));
        o2.addProperty(RDFS.domain, m.getResource(NS + "F"));
        o2.addProperty(RDFS.range, m.getResource(NS + "E"));
        d2.addProperty(RDFS.range, m.getResource(NS + "D"));

        Set<OntProperty> directDeclaredPropertiesA = m.getOntClass(NS + "A").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesA = m.getOntClass(NS + "A").listDeclaredProperties(false).toSet();
        System.out.println("[direct]A::" + directDeclaredPropertiesA);
        System.out.println("[indirect]A::" + indirectDeclaredPropertiesA);

        Set<OntProperty> directDeclaredPropertiesB = m.getOntClass(NS + "B").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesB = m.getOntClass(NS + "B").listDeclaredProperties(false).toSet();
        System.out.println("[direct]B::" + directDeclaredPropertiesB);
        System.out.println("[indirect]B::" + indirectDeclaredPropertiesB);

        Set<OntProperty> directDeclaredPropertiesC = m.getOntClass(NS + "C").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesC = m.getOntClass(NS + "C").listDeclaredProperties(false).toSet();
        System.out.println("[direct]C::" + directDeclaredPropertiesC);
        System.out.println("[indirect]C::" + indirectDeclaredPropertiesC);

        Set<OntProperty> directDeclaredPropertiesD = m.getOntClass(NS + "D").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesD = m.getOntClass(NS + "D").listDeclaredProperties(false).toSet();
        System.out.println("[direct]D::" + directDeclaredPropertiesD);
        System.out.println("[indirect]D::" + indirectDeclaredPropertiesD);

        Set<OntProperty> directDeclaredPropertiesE = m.getOntClass(NS + "E").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesE = m.getOntClass(NS + "E").listDeclaredProperties(false).toSet();
        System.out.println("[direct]E::" + directDeclaredPropertiesE);
        System.out.println("[indirect]E::" + indirectDeclaredPropertiesE);

        Set<OntProperty> directDeclaredPropertiesF = m.getOntClass(NS + "F").listDeclaredProperties(true).toSet();
        Set<OntProperty> indirectDeclaredPropertiesF = m.getOntClass(NS + "F").listDeclaredProperties(false).toSet();
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

}
