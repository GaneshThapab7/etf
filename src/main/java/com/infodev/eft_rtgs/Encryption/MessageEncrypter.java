package com.infodev.eft_rtgs.Encryption;



/*import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.Provider;
import java.security.Signer;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.crypto.*;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import com.sun.org.apache.xml.internal.security.utils.IdResolver;
import sun.jvm.hotspot.runtime.SignatureInfo;*/


public class MessageEncrypter {


   /* private X509Certificate signerCertificate;
    private Key keySelector;
    private Signer personalKey;


    public Document sign(Document doc) throws Exception {




         String xadesNS = "http://uri.etsi.org/01903/v1.3.2#";
         String signedpropsIdSuffix = "-signedprops";
         String providerName = System.getProperty("jsr105Provider",
                "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
                (Provider) Class.forName(providerName).newInstance());
// 1. Prepare KeyInfo
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509IssuerSerial x509is = kif.newX509IssuerSerial(
                signerCertificate.getIssuerX500Principal().toString(),
                signerCertificate.getSerialNumber());
        X509Data x509data = kif.newX509Data(Collections.singletonList(x509is));
        final String keyInfoId = "_" + UUID.randomUUID().toString();
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509data), keyInfoId);
// 2. Prepare references
        List<Reference> refs = new ArrayList<Reference>();
        Reference ref1 = fac.newReference("#" + keyInfoId,
                fac.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null)),
                null, null);
        refs.add(ref1);
         String signedpropsId = "_" + UUID.randomUUID().toString() + signedpropsIdSuffix;
        Reference ref2 = fac.newReference("#" + signedpropsId,
                fac.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE
                        , (XMLStructure) null)),
                "http://uri.etsi.org/01903/v1.3.2#SignedProperties", null);
        refs.add(ref2);
        Reference ref3 = fac.newReference(null,
                fac.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE
                        , (XMLStructure) null)),
                null, null);
        refs.add(ref3);
        SignedInfo si = fac.newSignedInfo(
                fac.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (XMLStructure) null),
                fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), refs);
// 3. Create element AppHdr/Sgntr that will contain the <ds:Signature>
        Node appHdr = null;
        NodeList sgntrList = doc.getElementsByTagName("AppHdr");
        if (sgntrList.getLength() != 0)
            appHdr = sgntrList.item(0);
        if (appHdr == null)
            throw new Exception("mandatory element AppHdr is missing in the document to be signed");
                    Node sgntr = appHdr.appendChild(doc.createElementNS(appHdr.getNamespaceURI(), "Sgntr"));
        DOMSignContext dsc = new DOMSignContext(personalKey.getPrivateKey(), sgntr);
        dsc.putNamespacePrefix(XMLSignature.XMLNS, "ds");
// 4. Set up <ds:Object> with <QualifiyingProperties> inside that includes SigningTime
        Element QPElement = doc.createElementNS(xadesNS, "xades:QualifyingProperties");
        Element SPElement = doc.createElementNS(xadesNS, "xades:SignedProperties");
        SPElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xades", xadesNS);
        SPElement.setAttributeNS(null, "Id", signedpropsId);
        dsc.setIdAttributeNS(SPElement, null, "Id");

       // IdResolver.registerElementById(SPElement, signedpropsId);
        IdResolver.registerElementById(SPElement, null);
        QPElement.appendChild(SPElement);
        Element SSPElement = doc.createElementNS(xadesNS, "xades:SignedSignatureProperties");
        SPElement.appendChild(SSPElement);
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String signingTime = df.format(new Date());
        Element STElement = doc.createElementNS(xadesNS, "xades:SigningTime");
        STElement.appendChild(doc.createTextNode(signingTime));
        SSPElement.appendChild(STElement);
        DOMStructure qualifPropStruct = new DOMStructure(QPElement);
        List<DOMStructure> xmlObj = new ArrayList<DOMStructure>();
        xmlObj.add(qualifPropStruct);
        XMLObject object = fac.newXMLObject(xmlObj, null, null, null);
        List<XMLObject> objects = Collections.singletonList(object);
// 5. Set up custom URIDereferencer to process Reference without URI.
// This Reference points to element <Document> of MX message
        final NodeList docNodes = doc.getElementsByTagName("Document");
        final Node docNode = docNodes.item(0);
        ByteArrayOutputStream refOutputStream = new ByteArrayOutputStream();
        Transformer xform = TransformerFactory.newInstance().newTransformer();
        xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xform.transform(new DOMSource(docNode), new StreamResult(refOutputStream));
        InputStream refInputStream = new ByteArrayInputStream(refOutputStream.toByteArray());
        dsc.setURIDereferencer(new NoUriDereferencer(refInputStream));
// 6. sign it!
        XMLSignature signature = fac.newXMLSignature(si, ki, objects, null, null);
        signature.sign(dsc);
        return doc;
    }





    public SignatureInfo verify(String dataPDU,Document doc) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String xpathExpression = "//*[local-name()='Signature']";
        NodeList nodes = (NodeList) xpath.evaluate(xpathExpression, doc.getDocumentElement(),
                XPathConstants.NODESET);
        if (nodes == null || nodes.getLength() == 0)
            throw new Exception("Signature is missing in the document");
        Node nodeSignature = nodes.item(0);
        final KeySelector mockKeySelector = new KeySelector() {
            @Override
            public KeySelectorResult select(KeyInfo keyInfo, Purpose purpose, AlgorithmMethod
                    method, XMLCryptoContext context) throws KeySelectorException {
                return new KeySelectorResult() {
                    @Override
                    public Key getKey() {
                        return signerCertificate.getPublicKey();
                    }
                };
            }
        };
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
        DOMValidateContext valContext = new DOMValidateContext(keySelector, nodeSignature);// This Reference points to element <Document> of MX message
        final NodeList docNodes = doc.getElementsByTagName("Document");
        final Node docNode = docNodes.item(0);
        ByteArrayOutputStream refOutputStream = new ByteArrayOutputStream();
        Transformer xform = TransformerFactory.newInstance().newTransformer();
        xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        xform.transform(new DOMSource(docNode), new StreamResult(refOutputStream));
        InputStream refInputStream = new ByteArrayInputStream(refOutputStream.toByteArray());

        NodeList nl = doc.getElementsByTagNameNS("http://uri.etsi.org/01903/v1.3.2#",
                "SignedProperties");
        if (nl.getLength() == 0)
            throw new Exception("SignerProperties is missing in signature");
        Element elemSignedProps = (Element) nl.item(0);
        valContext.setIdAttributeNS(elemSignedProps, null, "Id");
        XMLSignature signature = fac.unmarshalXMLSignature(valContext);
        boolean coreValidity = signature.validate(valContext);
        if (coreValidity)
        {
// signature verified
        }
        else
        {
// signature verification failed
            System.out.println("Signature failed core validation");
            boolean sv = signature.getSignatureValue().validate(valContext);
            System.out.println("signature validation status: " + sv);
// check the validation status of each Reference
            Iterator i = signature.getSignedInfo().getReferences().iterator();
            for (int j=0; i.hasNext(); j++) {
                final Reference ref = (Reference) i.next();
                final String refURI = ref.getURI();
                boolean refValid = ref.validate(valContext);
                System.out.println("ref["+j+"] validity status: " + refValid + ", ref URI: [" +
                        refURI + "]");
            }
        }

        return null;
    }

*/

    }
