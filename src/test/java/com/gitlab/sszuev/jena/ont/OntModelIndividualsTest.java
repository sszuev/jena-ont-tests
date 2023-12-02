package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine.NS;

public class OntModelIndividualsTest {

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
    public void testListIndividuals1(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");
        Assertions.assertEquals(Set.of(NS + "A0", NS + "A1", NS + "C0", NS + "a0", NS + "a1", NS + "a2", NS + "z0", NS + "z1"),
                m.listIndividuals().mapWith(Resource::getURI).toSet());
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
    public void testListIndividuals2(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        Resource a0 = m.createResource(NS + "A0");
        m.add(a0, RDF.type, OWL.Class);
        m.add(OWL.Class, RDF.type, RDFS.Class);
        Assertions.assertTrue(m.listIndividuals().toList().isEmpty());
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
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListIndividuals3(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        Resource a0 = m.createResource(NS + "A0");
        m.add(a0, RDF.type, OWL.Class);
        m.add(OWL.Class, RDF.type, OWL.Class);
        Assertions.assertTrue(m.listIndividuals().toList().isEmpty());
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
            "RDFS_MEM_TRANS_INF",
    })
    public void testListIndividuals4(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.add(m.createResource(NS + "A0"), RDF.type, OWL.Class);
        Assertions.assertTrue(m.listIndividuals().toList().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
    })
    public void testListIndividuals5(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        // A0 should be an individual, since we are punning
        Resource a0 = m.createResource(NS + "A0");
        Resource a1 = m.createResource(NS + "A1");
        m.add(a0, RDF.type, OWL.Class);
        m.add(a1, RDF.type, OWL.Class);
        m.add(a0, RDF.type, a1);
        Assertions.assertEquals(List.of(NS + "A0"), m.listIndividuals().mapWith(Resource::getURI).toList());
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
    public void testListIndividuals6a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        Assertions.assertEquals(
                List.of(
                        "urn:x-hp:eg/DTPGraphics",
                        "urn:x-hp:eg/budgetGraphics",
                        "urn:x-hp:eg/gamingGraphics"),
                Iter.asStream(m.listIndividuals()).distinct().map(Resource::getURI).sorted().collect(Collectors.toList()));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListIndividuals6b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        Assertions.assertEquals(
                Arrays.asList(
                        null,
                        null,
                        null,
                        "urn:x-hp:eg/DTPGraphics",
                        "urn:x-hp:eg/budgetGraphics",
                        "urn:x-hp:eg/gamingGraphics"
                ),
                Iter.asStream(m.listIndividuals())
                        .map(Resource::getURI)
                        .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
                        .collect(Collectors.toList())
        );
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListIndividuals6c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        Assertions.assertEquals(0, m.listIndividuals().toList().size());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testListIndividuals6d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        Assertions.assertEquals(
                Arrays.asList(
                        null, null, null,
                        "urn:x-hp:eg/Bundle",
                        "urn:x-hp:eg/Computer",
                        "urn:x-hp:eg/DTPGraphics",
                        "urn:x-hp:eg/GameBundle",
                        "urn:x-hp:eg/GamingComputer",
                        "urn:x-hp:eg/GraphicsCard",
                        "urn:x-hp:eg/MotherBoard",
                        "urn:x-hp:eg/budgetGraphics",
                        "urn:x-hp:eg/gamingGraphics",
                        "urn:x-hp:eg/hasBundle",
                        "urn:x-hp:eg/hasComponent",
                        "urn:x-hp:eg/hasGraphics",
                        "urn:x-hp:eg/hasMotherBoard"
                ),
                Iter.asStream(m.listIndividuals())
                        .map(Resource::getURI)
                        .sorted(Comparator.nullsFirst(Comparator.naturalOrder()))
                        .collect(Collectors.toList())
        );
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
    public void testListIndividuals7(TestSpec spec) {
        OntModel schema = ModelFactory.createOntologyModel(spec.inst);
        Model data = ModelFactory.createDefaultModel();
        Resource c = schema.createResource("http://example.com/foo#AClass");
        Resource i = data.createResource("http://example.com/foo#anInd");
        schema.add(c, RDF.type, OWL.Class);
        data.add(i, RDF.type, c);

        OntModel composite = ModelFactory.createOntologyModel(spec.inst, schema);
        composite.addSubModel(data);

        Assertions.assertEquals(
                List.of("http://example.com/foo#anInd"),
                composite.listIndividuals().mapWith(Resource::getURI).toList()
        );
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testListIndividuals8(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass c0 = OWL2.Thing.inModel(m).as(OntClass.class);
        OntClass c1 = m.createClass(NS + "C1");
        OntClass c2 = m.createClass(NS + "C2");
        OntClass c3 = m.createClass(NS + "C3");

        Individual i1 = c0.createIndividual(NS + "I1");
        Individual i2 = m.createIndividual(NS + "I2", OWL2.NamedIndividual);
        Individual i3 = m.createIndividual(NS + "I3", OWL2.NamedIndividual);
        Individual i4 = m.createIndividual(NS + "I4", OWL2.NamedIndividual);
        Individual i6 = c3.createIndividual();
        Individual i5 = c1.createIndividual(NS + "I5");
        c2.createIndividual(NS + "I5");
        c2.createIndividual(NS + "I3");
        c3.createIndividual(NS + "I3");

        i1.setSameAs(i2);
        i3.addDifferentFrom(i4);
        i3.addDifferentFrom(i6);

        Assertions.assertEquals(6, m.listStatements(null, RDF.type, (RDFNode) null)
                .filterKeep(x -> x.getObject().canAs(OntClass.class)).toList().size());
        List<Individual> found = m.listIndividuals().toList();
        Assertions.assertEquals(4, found.size());
        Assertions.assertEquals(1, found.stream().filter(RDFNode::isAnon).count());
        Assertions.assertEquals(Set.of(i1, i3, i5), found.stream().filter(it -> !it.isAnon()).collect(Collectors.toSet()));
    }

    @ParameterizedTest
    @EnumSource(TestSpec.class)
    public void testListIndividuals9(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        m.createResource("x", m.createResource("X"));
        m.createResource().addProperty(RDF.type, m.createResource("Y"));

        OntClass clazz = m.createClass("Q");
        clazz.createIndividual("q");
        clazz.createIndividual();

        m.write(System.out, "ttl");
        List<Individual> individuals = m.listIndividuals().toList();

        int expectedNumOfIndividuals = spec == TestSpec.RDFS_MEM_RDFS_INF ? 4 : 2;
        Assertions.assertEquals(expectedNumOfIndividuals, individuals.size());
    }
}
