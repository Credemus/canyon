package de.objectcode.canyon.bpe.util;

import org.dom4j.Element;

/**
 * @author junglas
 */
public interface IDomSerializable {
  String getElementName();

  void toDom(Element element);
}
