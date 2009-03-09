package org.exigencecorp.bindgen.example;

import static bindgen.BindKeyword.bind;
import junit.framework.Assert;
import junit.framework.TestCase;

public class BindKeywordTest extends TestCase {

    public void testWithFieldExample() {
        FieldExample e = new FieldExample("name");
        Assert.assertEquals("name", bind(e).name().get());
    }

}
