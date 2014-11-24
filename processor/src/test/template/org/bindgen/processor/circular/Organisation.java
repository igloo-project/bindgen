package org.bindgen.processor.circular;

import org.bindgen.Bindable;

@Bindable
public class Organisation<M extends Individual<O>, O extends Organisation<M, O>>
{
    public String getSomethingElse()
    {
        return "Ho ho ho!";
    }
    
    public M getMember()
    {
        return null;
    }
}
