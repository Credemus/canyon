package de.objectcode.canyon.bpe.util;

import java.io.IOException;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author junglas
 */
public class DomSerializer
{
  public static Document toDocument ( IDomSerializable serializable )
  {
    Document document = DocumentFactory.getInstance().createDocument();

    Element root = document.addElement(serializable.getElementName());
    serializable.toDom(root);
    
    return document;
  }
  
  public static String toString ( IDomSerializable serializable ) throws IOException
  {
    Document document = toDocument(serializable);
    StringWriter writer = new StringWriter();
    XMLWriter xmlWriter = new XMLWriter(writer, new OutputFormat("  ", true));
    
    xmlWriter.write(document);
    
    return writer.toString();
  }
}
