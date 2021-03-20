package com.company;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;


public class XMLEventReaderInitializer implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private XMLEventReader xmlEventReader;

    public XMLEventReader getXmlEventReader() {
        return xmlEventReader;
    }

    public void setXmlEventReader(XMLEventReader xmlEventReader) {
        this.xmlEventReader = xmlEventReader;
    }

    public XMLEventReaderInitializer(InputStream is) throws XMLStreamException {
        this.xmlEventReader = FACTORY.createXMLEventReader(is);
    }

    @Override
    public void close() throws XMLStreamException {
        if (xmlEventReader != null) {
            xmlEventReader.close();
        }
    }
}
