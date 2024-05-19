package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

/*
Test XML Request throw Intellij client:
POST http://localhost:8080/(un)safeProcessXml
Content-Type: application/xml

<?xml version="1.0"?>
<!DOCTYPE root [
        <!ENTITY xxe SYSTEM "file:///C:/Windows/System32/drivers/etc/hosts">
        ]>
<root>&xxe;</root>
 */
@RestController
public class XMLController {

    @PostMapping("/unsafeProcessXml")
    public String unsafeProcessXml(@RequestBody String xmlData) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            // Das Parsen des XML-Dokuments ohne Sicherheitsmaßnahmen
            Document doc = db.parse(new InputSource(new StringReader(xmlData)));


            return "XML unsicher verarbeitet";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler bei der Verarbeitung von XML";
        }
    }

    @PostMapping("/safeProcessXml")
    public ResponseEntity<String> safeProcessXml(@RequestBody String xmlData) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(xmlData)));

            String result = doc.getDocumentElement().getTextContent();

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Fehler bei der Verarbeitung von XML: " + e.getMessage());
        }
    }

}
