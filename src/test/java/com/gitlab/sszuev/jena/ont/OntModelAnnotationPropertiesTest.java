package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.ProfileException;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.Collectors;

public class OntModelAnnotationPropertiesTest {
    private static final String BASE = "http://www.test.com/test";
    private static final String NS = BASE + "#";

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
    public void testListAnnotationProperties1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        m.createOntProperty(NS + "ontp");
        m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listAnnotationProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(List.of("ap"), actual);

    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListAnnotationProperties1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        m.createOntProperty(NS + "ontp");
        m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listAnnotationProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(List.of("ap", "versionInfo"), actual);
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
    public void testListAnnotationProperties3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int expected1 = 6;
        int expected2 = 6;

        int actual1 = m.listAnnotationProperties().toList().size();
        int actual2 = m.listAnnotationProperties().toSet().size();

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListAnnotationProperties3b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        Assertions.assertThrows(ProfileException.class, () -> m.listAnnotationProperties().toList());
    }

}
