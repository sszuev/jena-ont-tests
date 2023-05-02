package com.gitlab.sszuev.jena.ont;

import org.apache.jena.ontology.AllDifferent;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Profile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.gitlab.sszuev.jena.ont.JunitExtensions.assertValues;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllDifferentTest {

    @Test
    public void test() {
        new AllDifferentTestData().runTest();
    }

    static class AllDifferentTestData extends OntTestEngine {

        public AllDifferentTestData() {
            super("AllDifferent.distinctMembers", true, true, false);
        }

        @Override
        protected void performTest(OntModel m, ProfileLang profileLang) {
            Profile prof = m.getProfile();
            AllDifferent a = m.createAllDifferent();
            OntResource b = m.getResource(NS + "b").as(OntResource.class);
            OntResource c = m.getResource(NS + "c").as(OntResource.class);

            a.addDistinctMember(b);
            assertEquals(1, a.getCardinality(prof.DISTINCT_MEMBERS()), "Cardinality should be 1");
            assertEquals(1, a.getDistinctMembers().size(), "List size should be 1");
            assertTrue(a.hasDistinctMember(b), "a should have b as distinct");

            a.addDistinctMember(c);
            assertEquals(1, a.getCardinality(prof.DISTINCT_MEMBERS()), "Cardinality should be 1");
            assertEquals(2, a.getDistinctMembers().size(), "List size should be 2");
            assertValues(testNodeName, List.of(b, c), a.listDistinctMembers().toList());

            assertTrue(a.hasDistinctMember(b), "a should have b as distinct");
            assertTrue(a.hasDistinctMember(c), "a should have c as distinct");

            a.setDistinctMembers(m.createList(b));
            assertEquals(1, a.getCardinality(prof.DISTINCT_MEMBERS()), "Cardinality should be 1");
            assertEquals(1, a.getDistinctMembers().size(), "List size should be 1");
            assertTrue(a.hasDistinctMember(b), "a should have b as distinct");
            assertFalse(a.hasDistinctMember(c), "a should not have c as distinct");

            a.removeDistinctMember(b);
            assertFalse(a.hasDistinctMember(b), "a should have not b as distinct");
        }
    }
}
