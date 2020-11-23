package de.objectcode.canyon.bpe.util;

import org.dom4j.Element;

/**
 * @author junglas
 */
public interface IDomSerializable
{
  public String getElementName();
  
  public void toDom(Element element);
}
