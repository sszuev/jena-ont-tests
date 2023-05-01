package org.apache.jena;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class IOTestUtils {

    public static String normalize(String resource) {
        if (resource == null) {
            return null;
        }
        String file;
        if (resource.startsWith("file:")) {
            file = resource.replace("file:", "/");
        } else if (resource.startsWith("/")) {
            file = resource;
        } else {
            file = "/" + resource;
        }
        Path res;
        try {
            res = Paths.get(Objects.requireNonNull(IOTestUtils.class.getResource(file)).toURI());
        } catch (Exception e) {
            throw new IllegalStateException("Can't load resource " + resource, e);
        }
        return res.toUri().toString();
    }

    public static void main(String ... args) {
        String n = "file:testing/ontology/owl/list-syntax/test.rdf";
        System.out.println(normalize(n));
    }
}
