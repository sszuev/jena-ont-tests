package com.gitlab.sszuev.jena.ont.testutils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MiscUtils {
    @SafeVarargs
    public static <X> Set<X> hashSetOf(X... items) {
        return new HashSet<>(Arrays.asList(items));
    }
}
