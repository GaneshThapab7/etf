package com.infodev.eft_rtgs.Encryption;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import java.io.InputStream;

public class NoUriDereferencer implements URIDereferencer {
    private InputStream inputStream;
    public NoUriDereferencer(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    @Override
    public Data dereference(URIReference uriRef, XMLCryptoContext ctx)

            throws URIReferenceException
    {
        if (uriRef.getURI() != null)
        {
            URIDereferencer defaultDereferencer =
                    XMLSignatureFactory.getInstance("DOM").getURIDereferencer();
            return defaultDereferencer.dereference(uriRef, ctx);
        }
        Data data = new OctetStreamData(inputStream);
        return data;
    }
}
