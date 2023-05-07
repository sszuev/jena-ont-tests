package com.gitlab.sszuev.jena.ont.testutils;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.junit.jupiter.api.Assertions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class JunitExtensions {

    public static void assertValues(String testCase, ExtendedIterator<?> expected, Object... actual) {
        assertValues(testCase, 0, List.of(actual), expected.toList());
    }

    public static void assertValues(String testCase, int expectedAnonsCount, ExtendedIterator<?> expected, Object... actual) {
        assertValues(testCase, expectedAnonsCount, List.of(actual), expected.toList());
    }

    public static void assertValues(String testCase, Collection<?> expected, Collection<?> actual) {
        assertValues(testCase, 0, expected, actual);
    }

    public static void assertValues(String testCase, int expectedAnonsCount, Collection<?> expected, Collection<?> actual) {
        int actualAnonsCount = 0;
        for (Object av : actual) {
            if (expectedAnonsCount > 0 && isAnonValue(av)) {
                actualAnonsCount++;
                continue;
            }
            Assertions.assertTrue(expected.contains(av), ":: " + testCase + " test found unexpected value: " + av);
        }
        for (Object ev : expected) {
            Assertions.assertTrue(expected.contains(ev), ":: " + testCase + " test failed to find expected value: " + ev);
        }
        if (expectedAnonsCount == 0) {
            Assertions.assertEquals(expected.size(), actual.size(), ":: " + testCase + " test; collections sizes are different");
        }
        Assertions.assertEquals(expectedAnonsCount, actualAnonsCount, ":: " + testCase + " test did not find the right number of anon");
    }

    public static boolean iteratorContains(Iterator<?> it, Object target ) {
        while (it.hasNext()) {
            if (it.next().equals( target )) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAnonValue(Object node) {
        if (node instanceof RDFNode) {
            return ((RDFNode) node).isAnon();
        }
        if (node instanceof Statement) {
            return ((Statement) node).getSubject().isAnon() || ((Statement) node).getObject().isAnon();
        }
        return false;
    }
}
