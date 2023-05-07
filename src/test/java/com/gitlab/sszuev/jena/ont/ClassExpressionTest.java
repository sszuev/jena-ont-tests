package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.common.CommonOntTestBase;
import com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine;
import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import org.apache.jena.ontology.AllValuesFromRestriction;
import org.apache.jena.ontology.CardinalityQRestriction;
import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.ComplementClass;
import org.apache.jena.ontology.DataRange;
import org.apache.jena.ontology.EnumeratedClass;
import org.apache.jena.ontology.HasValueRestriction;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.IntersectionClass;
import org.apache.jena.ontology.MaxCardinalityQRestriction;
import org.apache.jena.ontology.MaxCardinalityRestriction;
import org.apache.jena.ontology.MinCardinalityQRestriction;
import org.apache.jena.ontology.MinCardinalityRestriction;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Profile;
import org.apache.jena.ontology.QualifiedRestriction;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.ontology.UnionClass;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ClosableIterator;
import org.apache.jena.util.iterator.NullIterator;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.gitlab.sszuev.jena.ont.testutils.JunitExtensions.assertValues;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassExpressionTest extends CommonOntTestBase {

    static Stream<Arguments> argumentsStream() {
        return testsAsArguments(getTests());
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    public void test(CommonOntTestEngine test) {
        test.runTest();
    }

    private static void readTestModel(Model m, CommonOntTestEngine.ProfileLang profileLang) {
        IOTestUtils.readResourceModel(m,
                CommonOntTestEngine.ProfileLang.RDFS == profileLang ? "/jena/class-exceptions-test-rdfs.rdf" : "/jena/class-exceptions-test-owl.rdf");
    }

    public static CommonOntTestEngine[] getTests() {
        return new CommonOntTestEngine[]{
                new CommonOntTestEngine("OntClass.super-class", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        A.addSuperClass(B);
                        assertEquals(1, A.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 1");
                        assertEquals(B, A.getSuperClass(), "A should have super-class B");

                        A.addSuperClass(C);
                        assertEquals(2, A.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 2");
                        assertValues(testNodeName, A.listSuperClasses(), C, B);

                        A.setSuperClass(C);
                        assertEquals(1, A.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 1");
                        assertEquals(C, A.getSuperClass(), "A shuold have super-class C");
                        assertFalse(A.hasSuperClass(B, false), "A shuold not have super-class B");

                        A.removeSuperClass(B);
                        assertEquals(1, A.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 1");
                        A.removeSuperClass(C);
                        assertEquals(0, A.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("OntClass.sub-class", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        A.addSubClass(B);
                        assertEquals(1, B.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 1");
                        assertEquals(B, A.getSubClass(), "A should have sub-class B");

                        A.addSubClass(C);
                        assertEquals(2, B.getCardinality(prof.SUB_CLASS_OF()) + C.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 2");
                        assertValues(testNodeName, A.listSubClasses(), C, B);

                        A.setSubClass(C);
                        assertEquals(1, B.getCardinality(prof.SUB_CLASS_OF()) + C.getCardinality(prof.SUB_CLASS_OF()), "Cardinality should be 1");
                        assertEquals(C, A.getSubClass(), "A shuold have sub-class C");
                        assertFalse(A.hasSubClass(B, false), "A shuold not have sub-class B");

                        A.removeSubClass(B);
                        assertTrue(A.hasSubClass(C, false), "A should have sub-class C");
                        A.removeSubClass(C);
                        assertFalse(A.hasSubClass(C, false), "A should not have sub-class C");
                    }
                },
                new CommonOntTestEngine("OntClass.equivalentClass", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        A.addEquivalentClass(B);
                        assertEquals(1, A.getCardinality(prof.EQUIVALENT_CLASS()), "Cardinality should be 1");
                        assertEquals(B, A.getEquivalentClass(), "A have equivalentClass B");

                        A.addEquivalentClass(C);
                        assertEquals(2, A.getCardinality(prof.EQUIVALENT_CLASS()), "Cardinality should be 2");
                        assertValues(testNodeName, A.listEquivalentClasses(), C, B);

                        A.setEquivalentClass(C);
                        assertEquals(1, A.getCardinality(prof.EQUIVALENT_CLASS()), "Cardinality should be 1");
                        assertEquals(C, A.getEquivalentClass(), "A should have equivalentClass C");
                        assertFalse(A.hasEquivalentClass(B), "A should not have equivalentClass B");

                        A.removeEquivalentClass(B);
                        assertEquals(1, A.getCardinality(prof.EQUIVALENT_CLASS()), "Cardinality should be 1");
                        A.removeEquivalentClass(C);
                        assertEquals(0, A.getCardinality(prof.EQUIVALENT_CLASS()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("OntClass.disjointWith", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        A.addDisjointWith(B);
                        assertEquals(1, A.getCardinality(prof.DISJOINT_WITH()), "Cardinality should be 1");
                        assertEquals(B, A.getDisjointWith(), "A have be disjoint with B");

                        A.addDisjointWith(C);
                        assertEquals(2, A.getCardinality(prof.DISJOINT_WITH()), "Cardinality should be 2");
                        assertValues(testNodeName, List.of(C, B), A.listDisjointWith().toList());

                        A.setDisjointWith(C);
                        assertEquals(1, A.getCardinality(prof.DISJOINT_WITH()), "Cardinality should be 1");
                        assertEquals(C, A.getDisjointWith(), "A should be disjoint with C");
                        assertFalse(A.isDisjointWith(B), "A should not be disjoint with B");

                        A.removeDisjointWith(B);
                        assertEquals(1, A.getCardinality(prof.DISJOINT_WITH()), "Cardinality should be 1");
                        A.removeDisjointWith(C);
                        assertEquals(0, A.getCardinality(prof.DISJOINT_WITH()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("EnumeratedClass.oneOf", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        EnumeratedClass A = m.createEnumeratedClass(NS + "A", null);
                        OntResource a = m.getResource(NS + "a").as(OntResource.class);
                        OntResource b = m.getResource(NS + "b").as(OntResource.class);

                        A.addOneOf(a);
                        assertEquals(1, A.getCardinality(prof.ONE_OF()), "Cardinality should be 1");
                        assertEquals(1, A.getOneOf().size(), "Size should be 1");
                        assertTrue(A.getOneOf().contains(a), "A should have a as enumerated member");

                        A.addOneOf(b);
                        assertEquals(1, A.getCardinality(prof.ONE_OF()), "Cardinality should be 1");
                        assertEquals(2, A.getOneOf().size(), "Size should be 2");
                        assertValues(testNodeName, A.listOneOf(), a, b);

                        A.setOneOf(m.createList(b));
                        assertEquals(1, A.getCardinality(prof.ONE_OF()), "Cardinality should be 1");
                        assertEquals(1, A.getOneOf().size(), "Size should be 1");
                        assertTrue(A.hasOneOf(b), "A should have b in the enum");
                        assertFalse(A.hasOneOf(a), "A should not have a in the enum");

                        A.removeOneOf(a);
                        assertTrue(A.hasOneOf(b), "Should have b as an enum value");
                        A.removeOneOf(b);
                        assertFalse(A.hasOneOf(b), "Should not have b as an enum value");
                    }
                },
                new CommonOntTestEngine("IntersectionClass.intersectionOf", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        IntersectionClass A = m.createIntersectionClass(NS + "A", null);
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        A.addOperand(B);
                        assertEquals(1, A.getCardinality(prof.INTERSECTION_OF()), "Cardinality should be 1");
                        assertEquals(1, A.getOperands().size(), "Size should be 1");
                        assertTrue(A.getOperands().contains(B), "A should have a as intersection member");

                        A.addOperand(C);
                        assertEquals(1, A.getCardinality(prof.INTERSECTION_OF()), "Cardinality should be 1");
                        assertEquals(2, A.getOperands().size(), "Size should be 2");
                        assertValues(testNodeName, A.listOperands(), B, C);

                        ClosableIterator<? extends Resource> i = A.listOperands();
                        assertTrue(i.next() instanceof OntClass, "Argument should be an OntClass");
                        i.close();

                        A.setOperands(m.createList(C));
                        assertEquals(1, A.getCardinality(prof.INTERSECTION_OF()), "Cardinality should be 1");
                        assertEquals(1, A.getOperands().size(), "Size should be 1");
                        assertTrue(A.hasOperand(C), "A should have C in the intersection");
                        assertFalse(A.hasOperand(B), "A should not have B in the intersection");

                        A.removeOperand(B);
                        assertTrue(A.hasOperand(C), "Should have C as an operand");
                        A.removeOperand(C);
                        assertFalse(A.hasOperand(C), "Should not have C as an operand");
                    }
                },
                new CommonOntTestEngine("UnionClass.unionOf", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        UnionClass A = m.createUnionClass(NS + "A", null);
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        A.addOperand(B);
                        assertEquals(1, A.getCardinality(prof.UNION_OF()), "Cardinality should be 1");
                        assertEquals(1, A.getOperands().size(), "Size should be 1");
                        assertTrue(A.getOperands().contains(B), "A should have a as union member");

                        A.addOperand(C);
                        assertEquals(1, A.getCardinality(prof.UNION_OF()), "Cardinality should be 1");
                        assertEquals(2, A.getOperands().size(), "Size should be 2");
                        assertValues(testNodeName, A.listOperands(), B, C);

                        ClosableIterator<? extends Resource> i = A.listOperands();
                        assertTrue(i.next() instanceof OntClass, "Argument should be an OntClass");
                        i.close();

                        A.setOperands(m.createList(C));
                        assertEquals(1, A.getCardinality(prof.UNION_OF()), "Cardinality should be 1");
                        assertEquals(1, A.getOperands().size(), "Size should be 1");
                        assertTrue(A.hasOperand(C), "A should have C in the union");
                        assertFalse(A.hasOperand(B), "A should not have B in the union");

                        A.removeOperand(B);
                        assertTrue(A.hasOperand(C), "Should have C as an operand");
                        A.removeOperand(C);
                        assertFalse(A.hasOperand(C), "Should not have C as an operand");
                    }
                },
                new CommonOntTestEngine("ComplementClass.complementOf", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        ComplementClass A = m.createComplementClass(NS + "A", null);
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");
                        boolean ex = false;

                        try {
                            A.addOperand(B);
                        } catch (UnsupportedOperationException e) {
                            ex = true;
                        }
                        assertTrue(ex, "Should fail to add to a complement");

                        ex = false;
                        try {
                            A.addOperands(new NullIterator<>());
                        } catch (UnsupportedOperationException e) {
                            ex = true;
                        }
                        assertTrue(ex, "Should fail to add to a complement");

                        ex = false;
                        try {
                            A.setOperands(m.createList(C));
                        } catch (UnsupportedOperationException e) {
                            ex = true;
                        }
                        assertTrue(ex, "Should fail to set a list to a complement");

                        A.setOperand(B);
                        assertEquals(1, A.getCardinality(prof.COMPLEMENT_OF()), "Cardinality should be 1");
                        assertEquals(B, A.getOperand(), "Complement should be B");
                        assertValues(testNodeName, A.listOperands(), B);

                        A.setOperand(C);
                        assertEquals(1, A.getCardinality(prof.COMPLEMENT_OF()), "Cardinality should be 1");
                        assertTrue(A.hasOperand(C), "A should have C in the complement");
                        assertFalse(A.hasOperand(B), "A should not have B in the complement");

                        A.removeOperand(B);
                        assertTrue(A.hasOperand(C), "Should have C as an operand");
                        A.removeOperand(C);
                        assertFalse(A.hasOperand(C), "Should not have C as an operand");
                    }
                },
                new CommonOntTestEngine("Restriction.onProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntProperty q = m.createObjectProperty(NS + "q");
                        OntClass B = m.createClass(NS + "B");

                        Restriction A = m.createAllValuesFromRestriction(NS + "A", p, B);

                        assertEquals(p, A.getOnProperty(), "Restriction should be on property p");
                        assertTrue(A.onProperty(p), "Restriction should be on property p");
                        assertFalse(A.onProperty(q), "Restriction should not be on property q");
                        assertEquals(1, A.getCardinality(prof.ON_PROPERTY()), "cardinality should be 1 ");

                        A.setOnProperty(q);

                        assertEquals(q, A.getOnProperty(), "Restriction should be on property q");
                        assertFalse(A.onProperty(p), "Restriction should not be on property p");
                        assertTrue(A.onProperty(q), "Restriction should not on property q");
                        assertEquals(1, A.getCardinality(prof.ON_PROPERTY()), "cardinality should be 1 ");

                        A.removeOnProperty(p);
                        assertTrue(A.onProperty(q), "Should have q as on property");
                        A.removeOnProperty(q);
                        assertFalse(A.onProperty(q), "Should not have q as on property");
                    }
                },
                new CommonOntTestEngine("AllValuesFromRestriction.allValuesFrom", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        AllValuesFromRestriction A = m.createAllValuesFromRestriction(NS + "A", p, B);

                        assertEquals(B, A.getAllValuesFrom(), "Restriction should be all values from B");
                        assertTrue(A.hasAllValuesFrom(B), "Restriction should be all values from B");
                        assertFalse(A.hasAllValuesFrom(C), "Restriction should not be all values from C");
                        assertEquals(1, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 1 ");

                        A.setAllValuesFrom(C);

                        assertEquals(C, A.getAllValuesFrom(), "Restriction should be all values from C");
                        assertFalse(A.hasAllValuesFrom(B), "Restriction should not be all values from B");
                        assertTrue(A.hasAllValuesFrom(C), "Restriction should be all values from C");
                        assertEquals(1, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 1 ");

                        A.removeAllValuesFrom(C);

                        assertFalse(A.hasAllValuesFrom(C), "Restriction should not be some values from C");
                        assertEquals(0, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("AllValuesFromRestriction.allValuesFrom.datatype", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        AllValuesFromRestriction A = m.createAllValuesFromRestriction(NS + "A", p, XSD.gDay);

                        assertEquals(XSD.gDay, A.getAllValuesFrom(), "Restriction should be all values from gDay");
                        assertTrue(A.hasAllValuesFrom(XSD.gDay), "Restriction should be all values from gDay");
                        assertFalse(A.hasAllValuesFrom(XSD.decimal), "Restriction should not be all values from decimal");
                        assertEquals(1, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 1 ");

                        A.setAllValuesFrom(XSD.gMonth);

                        assertEquals(XSD.gMonth, A.getAllValuesFrom(), "Restriction should be all values from gMonth");
                        assertFalse(A.hasAllValuesFrom(XSD.gDay), "Restriction should not be all values from gDay");
                        assertTrue(A.hasAllValuesFrom(XSD.gMonth), "Restriction should be all values from gMonth");
                        assertEquals(1, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 1 ");

                        A.removeAllValuesFrom(XSD.gMonth);

                        assertFalse(A.hasAllValuesFrom(XSD.gMonth), "Restriction should not be some values from gMonth");
                        assertEquals(0, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("AllValuesFromRestriction.allValuesFrom.literal", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        AllValuesFromRestriction A = m.createAllValuesFromRestriction(NS + "A", p, RDFS.Literal);

                        assertEquals(RDFS.Literal, A.getAllValuesFrom(), "Restriction should be all values from literal");
                        assertTrue(A.hasAllValuesFrom(RDFS.Literal), "Restriction should be all values from literal");
                        assertFalse(A.hasAllValuesFrom(XSD.decimal), "Restriction should not be all values from decimal");
                        assertEquals(1, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 1 ");
                    }
                },
                new CommonOntTestEngine("AllValuesFromRestriction.allValuesFrom.datarange", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        Literal x = m.createTypedLiteral(1);
                        Literal y = m.createTypedLiteral(2);
                        DataRange dr = m.createDataRange(m.createList(x, y));
                        OntProperty p = m.createObjectProperty(NS + "p");

                        AllValuesFromRestriction A = m.createAllValuesFromRestriction(NS + "A", p, dr);

                        assertEquals(dr, A.getAllValuesFrom(), "Restriction should be all values from dr");
                        assertTrue(A.getAllValuesFrom() instanceof DataRange, "value should be a datarange");
                        assertTrue(A.hasAllValuesFrom(dr), "Restriction should be all values from dr");
                        assertFalse(A.hasAllValuesFrom(XSD.decimal), "Restriction should not be all values from decimal");
                        assertEquals(1, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 1 ");

                        A.removeAllValuesFrom(dr);

                        assertFalse(A.hasAllValuesFrom(dr), "Restriction should not be some values from gMonth");
                        assertEquals(0, A.getCardinality(prof.ALL_VALUES_FROM()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("HasValueRestriction.hasValue", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass B = m.createClass(NS + "B");
                        Individual b = m.createIndividual(B);
                        OntClass C = m.createClass(NS + "C");
                        Individual c = m.createIndividual(C);

                        HasValueRestriction A = m.createHasValueRestriction(NS + "A", p, b);

                        assertEquals(b, A.getHasValue(), "Restriction should be has value b");
                        assertTrue(A.getHasValue() instanceof Individual);
                        assertTrue(A.hasValue(b), "Restriction should be to have value b");
                        assertFalse(A.hasValue(c), "Restriction should not be have value c");
                        assertEquals(1, A.getCardinality(prof.HAS_VALUE()), "cardinality should be 1 ");

                        A.setHasValue(c);

                        assertEquals(c, A.getHasValue(), "Restriction should be has value c");
                        assertFalse(A.hasValue(b), "Restriction should not be to have value b");
                        assertTrue(A.hasValue(c), "Restriction should not be have value c");
                        assertEquals(1, A.getCardinality(prof.HAS_VALUE()), "cardinality should be 1 ");

                        A.removeHasValue(c);

                        assertFalse(A.hasValue(b), "Restriction should not be to have value b");
                        assertFalse(A.hasValue(c), "Restriction should not be have value c");
                        assertEquals(0, A.getCardinality(prof.HAS_VALUE()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("SomeValuesFromRestriction.someValuesFrom", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass B = m.createClass(NS + "B");
                        OntClass C = m.createClass(NS + "C");

                        SomeValuesFromRestriction A = m.createSomeValuesFromRestriction(NS + "A", p, B);

                        assertEquals(B, A.getSomeValuesFrom(), "Restriction should be some values from B");
                        assertTrue(A.hasSomeValuesFrom(B), "Restriction should be some values from B");
                        assertFalse(A.hasSomeValuesFrom(C), "Restriction should not be some values from C");
                        assertEquals(1, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 1 ");

                        A.setSomeValuesFrom(C);

                        assertEquals(C, A.getSomeValuesFrom(), "Restriction should be some values from C");
                        assertFalse(A.hasSomeValuesFrom(B), "Restriction should not be some values from B");
                        assertTrue(A.hasSomeValuesFrom(C), "Restriction should be some values from C");
                        assertEquals(1, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 1 ");

                        A.removeSomeValuesFrom(C);

                        assertFalse(A.hasSomeValuesFrom(C), "Restriction should not be some values from C");
                        assertEquals(0, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("SomeValuesFromRestriction.SomeValuesFrom.datatype", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        SomeValuesFromRestriction A = m.createSomeValuesFromRestriction(NS + "A", p, XSD.gDay);

                        assertEquals(XSD.gDay, A.getSomeValuesFrom(), "Restriction should be some values from gDay");
                        assertTrue(A.hasSomeValuesFrom(XSD.gDay), "Restriction should be some values from gDay");
                        assertFalse(A.hasSomeValuesFrom(XSD.decimal), "Restriction should not be some values from decimal");
                        assertEquals(1, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 1 ");

                        A.setSomeValuesFrom(XSD.gMonth);

                        assertEquals(XSD.gMonth, A.getSomeValuesFrom(), "Restriction should be some values from gMonth");
                        assertFalse(A.hasSomeValuesFrom(XSD.gDay), "Restriction should not be some values from gDay");
                        assertTrue(A.hasSomeValuesFrom(XSD.gMonth), "Restriction should be some values from gMonth");
                        assertEquals(1, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 1 ");

                        A.removeSomeValuesFrom(XSD.gMonth);

                        assertFalse(A.hasSomeValuesFrom(XSD.gMonth), "Restriction should not be some values from gMonth");
                        assertEquals(0, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("SomeValuesFromRestriction.SomeValuesFrom.literal", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        SomeValuesFromRestriction A = m.createSomeValuesFromRestriction(NS + "A", p, RDFS.Literal);

                        assertEquals(RDFS.Literal, A.getSomeValuesFrom(), "Restriction should be some values from literal");
                        assertTrue(A.hasSomeValuesFrom(RDFS.Literal), "Restriction should be some values from literal");
                        assertFalse(A.hasSomeValuesFrom(XSD.decimal), "Restriction should not be some values from decimal");
                        assertEquals(1, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 1 ");
                    }
                },
                new CommonOntTestEngine("SomeValuesFromRestriction.SomeValuesFrom.datarange", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        Literal x = m.createTypedLiteral(1);
                        Literal y = m.createTypedLiteral(2);
                        DataRange dr = m.createDataRange(m.createList(x, y));
                        OntProperty p = m.createObjectProperty(NS + "p");

                        SomeValuesFromRestriction A = m.createSomeValuesFromRestriction(NS + "A", p, dr);

                        assertEquals(dr, A.getSomeValuesFrom(), "Restriction should be some values from dr");
                        assertTrue(A.getSomeValuesFrom() instanceof DataRange, "value should be a datarange");
                        assertTrue(A.hasSomeValuesFrom(dr), "Restriction should be some values from dr");
                        assertFalse(A.hasSomeValuesFrom(XSD.decimal), "Restriction should not be some values from decimal");
                        assertEquals(1, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 1 ");

                        A.removeSomeValuesFrom(dr);

                        assertFalse(A.hasSomeValuesFrom(dr), "Restriction should not be some values from gMonth");
                        assertEquals(0, A.getCardinality(prof.SOME_VALUES_FROM()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("CardinalityRestriction.cardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        CardinalityRestriction A = m.createCardinalityRestriction(NS + "A", p, 3);

                        assertEquals(3, A.getCardinality(), "Restriction should be cardinality 3");
                        assertTrue(A.hasCardinality(3), "Restriction should be cardinality 3");
                        assertFalse(A.hasCardinality(2), "Restriction should not be cardinality 2");
                        assertEquals(1, A.getCardinality(prof.CARDINALITY()), "cardinality should be 1 ");

                        A.setCardinality(2);

                        assertEquals(2, A.getCardinality(), "Restriction should be cardinality 2");
                        assertFalse(A.hasCardinality(3), "Restriction should not be cardinality 3");
                        assertTrue(A.hasCardinality(2), "Restriction should be cardinality 2");
                        assertEquals(1, A.getCardinality(prof.CARDINALITY()), "cardinality should be 1 ");

                        A.removeCardinality(2);

                        assertFalse(A.hasCardinality(3), "Restriction should not be cardinality 3");
                        assertFalse(A.hasCardinality(2), "Restriction should not be cardinality 2");
                        assertEquals(0, A.getCardinality(prof.CARDINALITY()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("MinCardinalityRestriction.minCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        MinCardinalityRestriction A = m.createMinCardinalityRestriction(NS + "A", p, 3);

                        assertEquals(3, A.getMinCardinality(), "Restriction should be min cardinality 3");
                        assertTrue(A.hasMinCardinality(3), "Restriction should be min cardinality 3");
                        assertFalse(A.hasMinCardinality(2), "Restriction should not be min cardinality 2");
                        assertEquals(1, A.getCardinality(prof.MIN_CARDINALITY()), "cardinality should be 1 ");

                        A.setMinCardinality(2);

                        assertEquals(2, A.getMinCardinality(), "Restriction should be min cardinality 2");
                        assertFalse(A.hasMinCardinality(3), "Restriction should not be min cardinality 3");
                        assertTrue(A.hasMinCardinality(2), "Restriction should be min cardinality 2");
                        assertEquals(1, A.getCardinality(prof.MIN_CARDINALITY()), "cardinality should be 1 ");

                        A.removeMinCardinality(2);

                        assertFalse(A.hasMinCardinality(3), "Restriction should not be cardinality 3");
                        assertFalse(A.hasMinCardinality(2), "Restriction should not be cardinality 2");
                        assertEquals(0, A.getCardinality(prof.MIN_CARDINALITY()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("MaxCardinalityRestriction.maxCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");

                        MaxCardinalityRestriction A = m.createMaxCardinalityRestriction(NS + "A", p, 3);

                        assertEquals(3, A.getMaxCardinality(), "Restriction should be max cardinality 3");
                        assertTrue(A.hasMaxCardinality(3), "Restriction should be max cardinality 3");
                        assertFalse(A.hasMaxCardinality(2), "Restriction should not be max cardinality 2");
                        assertEquals(1, A.getCardinality(prof.MAX_CARDINALITY()), "cardinality should be 1 ");

                        A.setMaxCardinality(2);

                        assertEquals(2, A.getMaxCardinality(), "Restriction should be max cardinality 2");
                        assertFalse(A.hasMaxCardinality(3), "Restriction should not be max cardinality 3");
                        assertTrue(A.hasMaxCardinality(2), "Restriction should be max cardinality 2");
                        assertEquals(1, A.getCardinality(prof.MAX_CARDINALITY()), "cardinality should be 1 ");

                        A.removeMaxCardinality(2);

                        assertFalse(A.hasMaxCardinality(3), "Restriction should not be cardinality 3");
                        assertFalse(A.hasMaxCardinality(2), "Restriction should not be cardinality 2");
                        assertEquals(0, A.getCardinality(prof.MAX_CARDINALITY()), "cardinality should be 0 ");
                    }
                },
                new CommonOntTestEngine("QualifiedRestriction.hasClassQ", false, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass c = m.createClass(NS + "C");
                        OntClass d = m.createClass(NS + "D");

                        String nameA = "ABCBA";
                        QualifiedRestriction A = m.createMaxCardinalityQRestriction(NS + nameA, p, 3, c);

                        assertEquals(c, A.getHasClassQ(), "Restriction should hasClassQ c");
                        assertTrue(A.hasHasClassQ(c), "Restriction should be hasClassQ c");
                        assertFalse(A.hasHasClassQ(d), "Restriction should not be hasClassQ d");

                        A.setHasClassQ(d);

                        assertEquals(d, A.getHasClassQ(), "Restriction should hasClassQ d");
                        assertTrue(A.hasHasClassQ(d), "Restriction should be hasClassQ d");
                        assertFalse(A.hasHasClassQ(c), "Restriction should not be hasClassQ c");

                        assertTrue(m.getResource(NS + nameA).canAs(QualifiedRestriction.class), "Should be a qualified restriction");
                        A.removeHasClassQ(d);
                        assertFalse(m.getResource(NS + nameA).canAs(QualifiedRestriction.class), "Should not be a qualified restriction");
                    }
                },
                new CommonOntTestEngine("CardinalityQRestriction.cardinality", false, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass c = m.createClass(NS + "C");

                        CardinalityQRestriction A = m.createCardinalityQRestriction(NS + "A", p, 3, c);

                        assertEquals(3, A.getCardinalityQ(), "Restriction should cardinality 3");
                        assertTrue(A.hasCardinalityQ(3), "Restriction should be cardinality 3");
                        assertFalse(A.hasCardinalityQ(1), "Restriction should not be cardinality 1");

                        A.setCardinalityQ(1);

                        assertEquals(1, A.getCardinalityQ(), "Restriction should cardinality 1");
                        assertFalse(A.hasCardinalityQ(3), "Restriction should not be cardinality 3");
                        assertTrue(A.hasCardinalityQ(1), "Restriction should be cardinality 1");

                        assertTrue(m.getResource(NS + "A").canAs(CardinalityQRestriction.class), "Should be a qualified cardinality restriction");
                        A.removeCardinalityQ(1);
                        assertFalse(m.getResource(NS + "A").canAs(CardinalityQRestriction.class), "Should not be a qualified cardinality restriction");
                    }
                },
                new CommonOntTestEngine("MinCardinalityQRestriction.minCardinality", false, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass c = m.createClass(NS + "C");

                        MinCardinalityQRestriction A = m.createMinCardinalityQRestriction(NS + "A", p, 3, c);

                        assertEquals(3, A.getMinCardinalityQ(), "Restriction should min cardinality 3");
                        assertTrue(A.hasMinCardinalityQ(3), "Restriction should be min cardinality 3");
                        assertFalse(A.hasMinCardinalityQ(1), "Restriction should not be min cardinality 1");

                        A.setMinCardinalityQ(1);

                        assertEquals(1, A.getMinCardinalityQ(), "Restriction should min cardinality 1");
                        assertFalse(A.hasMinCardinalityQ(3), "Restriction should not be min cardinality 3");
                        assertTrue(A.hasMinCardinalityQ(1), "Restriction should be min cardinality 1");

                        assertTrue(m.getResource(NS + "A").canAs(MinCardinalityQRestriction.class), "Should be a qualified min cardinality restriction");
                        A.removeMinCardinalityQ(1);
                        assertFalse(m.getResource(NS + "A").canAs(MinCardinalityQRestriction.class), "Should not be a qualified min cardinality restriction");
                    }
                },
                new CommonOntTestEngine("MaxCardinalityQRestriction.maxCardinality", false, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntClass c = m.createClass(NS + "C");

                        MaxCardinalityQRestriction A = m.createMaxCardinalityQRestriction(NS + "A", p, 3, c);

                        assertEquals(3, A.getMaxCardinalityQ(), "Restriction should max cardinality 3");
                        assertTrue(A.hasMaxCardinalityQ(3), "Restriction should be max cardinality 3");
                        assertFalse(A.hasMaxCardinalityQ(1), "Restriction should not be max cardinality 1");

                        A.setMaxCardinalityQ(1);

                        assertEquals(1, A.getMaxCardinalityQ(), "Restriction should max cardinality 1");
                        assertFalse(A.hasMaxCardinalityQ(3), "Restriction should not be max cardinality 3");
                        assertTrue(A.hasMaxCardinalityQ(1), "Restriction should be max cardinality 1");

                        assertTrue(m.getResource(NS + "A").canAs(MaxCardinalityQRestriction.class), "Should be a qualified max cardinality restriction");
                        A.removeMaxCardinalityQ(1);
                        assertFalse(m.getResource(NS + "A").canAs(MaxCardinalityQRestriction.class), "Should not be a qualified max cardinality restriction");
                    }
                },

                // from file
                new CommonOntTestEngine("OntClass.subclass.fromFile", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) throws IOException {
                        readTestModel(m, profileLang);

                        OntClass A = m.createClass(NS + "ClassA");
                        OntClass B = m.createClass(NS + "ClassB");

                        assertValues(testNodeName, A.listSuperClasses(), B);
                        assertValues(testNodeName, B.listSubClasses(), A);
                    }
                },
                new CommonOntTestEngine("OntClass.equivalentClass.fromFile", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) throws IOException {
                        readTestModel(m, profileLang);

                        OntClass A = m.createClass(NS + "ClassA");
                        OntClass C = m.createClass(NS + "ClassC");

                        assertTrue(A.hasEquivalentClass(C), "A should be equivalent to C");
                    }
                },
                new CommonOntTestEngine("OntClass.disjoint.fromFile", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) throws IOException {
                        readTestModel(m, profileLang);
                        OntClass A = m.createClass(NS + "ClassA");
                        OntClass D = m.createClass(NS + "ClassD");

                        assertTrue(A.isDisjointWith(D), "A should be disjoint with D");
                    }
                },

                // type testing
                new CommonOntTestEngine("OntClass.isEnumeratedClass", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        Individual x = m.createIndividual(NS + "x", b);
                        Individual y = m.createIndividual(NS + "y", b);
                        OntClass a = m.createEnumeratedClass(NS + "A", m.createList(x, y));

                        assertTrue(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertFalse(a.isUnionClass(), "union class test not correct");
                        assertFalse(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.isIntersectionClass", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        OntClass c = m.createClass(NS + "C");
                        OntClass a = m.createIntersectionClass(NS + "A", m.createList(b, c));

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertTrue(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.isUnionClass", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        OntClass c = m.createClass(NS + "C");
                        OntClass a = m.createUnionClass(NS + "A", m.createList(b, c));

                        assertFalse(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(a.isUnionClass(), "union class test not correct");
                        assertFalse(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.isComplementClass", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        OntClass a = m.createComplementClass(NS + "A", b);

                        assertFalse(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertFalse(a.isUnionClass(), "union class test not correct");
                        assertTrue(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.isRestriction", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass a = m.createRestriction(null);

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertTrue(a.isRestriction(), "restriction test not correct");
                    }
                },

                // conversion
                new CommonOntTestEngine("OntClass.toEnumeratedClass", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass a = m.createClass(NS + "A");

                        assertFalse(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertFalse(a.isUnionClass(), "union class test not correct");
                        assertFalse(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");

                        OntClass b = m.createClass(NS + "B");
                        Individual x = m.createIndividual(NS + "x", b);
                        Individual y = m.createIndividual(NS + "y", b);
                        a = a.convertToEnumeratedClass(m.createList(x, y));

                        assertTrue(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertFalse(a.isUnionClass(), "union class test not correct");
                        assertFalse(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.toIntersectionClass", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass a = m.createClass(NS + "A");

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");

                        OntClass b = m.createClass(NS + "B");
                        OntClass c = m.createClass(NS + "C");
                        a = a.convertToIntersectionClass(m.createList(b, c));

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertTrue(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.toUnionClass", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass a = m.createClass(NS + "A");

                        assertFalse(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertFalse(a.isUnionClass(), "union class test not correct");
                        assertFalse(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");

                        OntClass b = m.createClass(NS + "B");
                        OntClass c = m.createClass(NS + "C");
                        a = a.convertToUnionClass(m.createList(b, c));

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.toComplementClass", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass a = m.createClass(NS + "A");

                        assertFalse(a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertFalse(a.isUnionClass(), "union class test not correct");
                        assertFalse(a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");

                        OntClass b = m.createClass(NS + "B");
                        a = a.convertToComplementClass(b);

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.toRestriction", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass a = m.createClass(NS + "A");

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertFalse(a.isRestriction(), "restriction test not correct");

                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        a = a.convertToRestriction(p);

                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isEnumeratedClass(), "enumerated class test not correct");
                        assertFalse(a.isIntersectionClass(), "intersection class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isUnionClass(), "union class test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isComplementClass(), "complement class test not correct");
                        assertTrue(a.isRestriction(), "restriction test not correct");
                    }
                },


                // restriction type testing
                new CommonOntTestEngine("Restriction.isAllValuesFrom", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createAllValuesFromRestriction(null, p, b);

                        assertTrue(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.isSomeValuesFrom", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createSomeValuesFromRestriction(null, p, b);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertTrue(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.isHasValue", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass b = m.createClass(NS + "B");
                        Individual x = m.createIndividual(b);
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createHasValueRestriction(null, p, x);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.isCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createCardinalityRestriction(null, p, 3);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertTrue(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.isMinCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createMinCardinalityRestriction(null, p, 1);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertTrue(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.isMaxCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createMaxCardinalityRestriction(null, p, 5);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertTrue(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },

                // restriction conversions
                new CommonOntTestEngine("Restriction.convertToAllValuesFrom", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createRestriction(p);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");

                        OntClass b = m.createClass(NS + "B");
                        a = a.convertToAllValuesFromRestriction(b);

                        assertTrue(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.convertToSomeValuesFrom", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createRestriction(p);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");

                        OntClass b = m.createClass(NS + "B");
                        a = a.convertToSomeValuesFromRestriction(b);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertTrue(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.convertToHasValue", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createRestriction(p);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");

                        OntClass b = m.createClass(NS + "B");
                        Individual x = m.createIndividual(b);
                        a = a.convertToHasValueRestriction(x);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.convertCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createRestriction(p);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");

                        a = a.convertToCardinalityRestriction(3);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertTrue(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.convertMinCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createRestriction(p);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");

                        a = a.convertToMinCardinalityRestriction(3);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertTrue(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("Restriction.convertMaxCardinality", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        Restriction a = m.createRestriction(p);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertFalse(a.isMaxCardinalityRestriction(), "max cardinality test not correct");

                        a = a.convertToMaxCardinalityRestriction(3);

                        assertFalse(a.isAllValuesFromRestriction(), "all values from test not correct");
                        assertFalse(a.isSomeValuesFromRestriction(), "some values from test not correct");
                        assertTrue(profileLang == ProfileLang.OWL_LITE || !a.isHasValueRestriction(), "has value test not correct");
                        assertFalse(a.isCardinalityRestriction(), "cardinality test not correct");
                        assertFalse(a.isMinCardinalityRestriction(), "min cardinality test not correct");
                        assertTrue(a.isMaxCardinalityRestriction(), "max cardinality test not correct");
                    }
                },
                new CommonOntTestEngine("OntClass.listInstances", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");

                        Individual a0 = m.createIndividual(A);
                        Individual a1 = m.createIndividual(NS + "a1", A);
                        Individual b0 = m.createIndividual(B);
                        /*Individual b1 =*/
                        m.createIndividual(NS + "b1", B);
                        b0.addRDFType(A);

                        assertValues(testNodeName, A.listInstances(), a0, a1, b0);
                    }
                },
                new CommonOntTestEngine("OntClass.listDefinedProperties", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        //OntClass B = m.createClass( NS + "B" );
                        OntClass C = m.createClass(NS + "C");

                        OntProperty p = m.createOntProperty(NS + "p");
                        OntProperty q = m.createOntProperty(NS + "q");
                        OntProperty r = m.createOntProperty(NS + "r");
                        OntProperty s = m.createOntProperty(NS + "s");

                        p.setDomain(A);
                        q.setDomain(A);
                        s.setDomain(C);

                        if (profileLang == ProfileLang.RDFS) {
                            assertValues(testNodeName, A.listDeclaredProperties(), p, q, r);
                        } else {
                            Restriction r0 = m.createRestriction(r);
                            C.addSuperClass(r0);

                            assertValues(testNodeName, A.listDeclaredProperties(), p, q, r);

                            assertValues(testNodeName, C.listDeclaredProperties(), s, r);

                            assertValues(testNodeName, r0.listDeclaredProperties(), r);
                        }
                    }
                },
                new CommonOntTestEngine("OntClass.listDefinedProperties.notAll", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass C = m.createClass(NS + "C");
                        C.addSuperClass(A);

                        OntProperty p = m.createOntProperty(NS + "p");
                        OntProperty q = m.createOntProperty(NS + "q");
                        OntProperty s = m.createOntProperty(NS + "s");

                        p.setDomain(A);
                        q.setDomain(A);
                        s.setDomain(C);

                        assertValues(testNodeName, List.of(p, q, s), C.listDeclaredProperties().toList());
                        assertValues(testNodeName, C.listDeclaredProperties(false), p, q, s);
                        assertValues(testNodeName, C.listDeclaredProperties(true), s);

                        assertNotNull(C.listDeclaredProperties(true).next(), "declared property should be an ont prop");
                        assertNotNull(C.listDeclaredProperties(false).next(), "declared property should be an ont prop");
                    }
                },
                new CommonOntTestEngine("DataRange.oneOf", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Literal x = m.createTypedLiteral(42);
                        Literal y = m.createTypedLiteral(true);
                        Literal z = m.createTypedLiteral("life");
                        RDFList list = m.createList(x, y);

                        DataRange d0 = m.createDataRange(list);

                        assertTrue(d0.hasOneOf(x), "datarange should contain x");
                        assertTrue(d0.hasOneOf(y), "datarange should contain y");
                        assertFalse(d0.hasOneOf(z), "datarange should not contain z");

                        d0.removeOneOf(z);
                        assertTrue(d0.hasOneOf(x), "datarange should contain x");
                        assertTrue(d0.hasOneOf(y), "datarange should contain y");
                        assertFalse(d0.hasOneOf(z), "datarange should not contain z");

                        d0.removeOneOf(x);
                        assertFalse(d0.hasOneOf(x), "datarange should not contain x");
                        assertTrue(d0.hasOneOf(y), "datarange should contain y");
                        assertFalse(d0.hasOneOf(z), "datarange should not contain z");

                        d0.addOneOf(z);
                        assertEquals(2, d0.getOneOf().size(), "datarange should be size 2");
                        assertValues(testNodeName, d0.listOneOf(), y, z);

                        d0.setOneOf(m.createList(x));
                        assertValues(testNodeName, d0.listOneOf(), x);
                    }
                },

                // Removal

                new CommonOntTestEngine("Remove intersection", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String ns = "http://example.com/foo#";
                        OntClass a = m.createClass(ns + "A");
                        OntClass b = m.createClass(ns + "B");

                        long old = m.size();

                        RDFList members = m.createList(a, b);
                        IntersectionClass intersectionClass = m.createIntersectionClass(null, members);
                        intersectionClass.remove();

                        assertEquals(old, m.size());
                    }
                },
                new CommonOntTestEngine("Remove union", true, false, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String ns = "http://example.com/foo#";
                        OntClass a = m.createClass(ns + "A");
                        OntClass b = m.createClass(ns + "B");

                        long old = m.size();

                        RDFList members = m.createList(a, b);
                        UnionClass unionClass = m.createUnionClass(null, members);
                        unionClass.remove();

                        assertEquals(old, m.size());
                    }
                }
        };
    }

}
