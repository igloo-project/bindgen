package org.bindgen.processor.circular;

import org.bindgen.Bindable;

@Bindable
public class Individual<O extends Organisation<?, ?>>
{
    public String getSomething()
    {
        return "Ha ha ha!";
    }
    
    public O getOrganisation()
    {
        return null;
    }
}
