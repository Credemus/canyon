package de.objectcode.canyon.bpe.connector;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import de.objectcode.canyon.bpe.engine.variable.ComplexType;
import de.objectcode.canyon.bpe.engine.variable.ComplexValue;
import de.objectcode.canyon.model.ExtendedAttribute;
import de.objectcode.canyon.model.data.BasicType;
import de.objectcode.canyon.model.data.ParameterMode;
import de.objectcode.canyon.spi.tool.Parameter;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;

/**
 * @author junglas
 * @created 21. Juni 2004
 */
public class BasicApplicationData implements IApplicationData {
  private String m_id;
  private String m_name;
  private String m_description;
  private ExtendedAttribute[] m_extendedAttributes;
  private Map<String, Parameter> m_parameters;


  /**
   * Constructor for the BasicApplicationData object
   *
   * @param applicationData Description of the Parameter
   */
  BasicApplicationData(ComplexValue applicationData) {
    m_id = (String) applicationData.get("id");
    m_name = (String) applicationData.get("name");
    m_description = (String) applicationData.get("description");

    Map<String, Object> extendedAttributes = (Map<String, Object>) applicationData.get("extendedAttributes");

    if (extendedAttributes != null) {
      m_extendedAttributes = new ExtendedAttribute[extendedAttributes.size()];
      Iterator<String> it = extendedAttributes.keySet().iterator();
      int i;

      for (i = 0; it.hasNext(); i++) {
        String name = it.next();
        String value = (String) extendedAttributes.get(name);

        m_extendedAttributes[i] = new ExtendedAttribute(name, value);
      }
    } else {
      m_extendedAttributes = new ExtendedAttribute[0];
    }

    m_parameters = new LinkedHashMap<String, Parameter>();

    ComplexValue inValues = (ComplexValue) applicationData.get("in");

    if (inValues != null) {
      ComplexType type = inValues.getType();
      String[] names = type.getPropertyNames();
      int i;

      for (i = 0; i < names.length; i++) {
        ComplexType.PropertyDefinition propertyDefinition = type.getPropertyDefinition(names[i]);

        Parameter parameter = new Parameter(names[i], names[i], BasicType.fromClass(propertyDefinition.getType()),
                ParameterMode.IN, propertyDefinition.getLabel(), inValues.get(names[i]));

        m_parameters.put(names[i], parameter);
      }
    }

    ComplexValue outValues = (ComplexValue) applicationData.get("out");

    if (outValues != null) {
      ComplexType type = outValues.getType();
      String[] names = type.getPropertyNames();
      int i;

      for (i = 0; i < names.length; i++) {
        ComplexType.PropertyDefinition propertyDefinition = type.getPropertyDefinition(names[i]);

        Parameter parameter = m_parameters.get(names[i]);

        if (parameter == null) {
          parameter = new Parameter(names[i], names[i], BasicType.fromClass(propertyDefinition.getType()),
                  ParameterMode.OUT, propertyDefinition.getLabel(), outValues.get(names[i]));
        } else {
          parameter = new Parameter(names[i], names[i], parameter.dataType,
                  ParameterMode.INOUT, propertyDefinition.getLabel(), parameter.value);
        }

        m_parameters.put(names[i], parameter);
      }
    }
  }


  /**
   * @return The description value
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#getDescription()
   */
  public String getDescription() {
    return m_description;
  }


  /**
   * @return The extendedAttributes value
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#getExtendedAttributes()
   */
  public ExtendedAttribute[] getExtendedAttributes() {
    return m_extendedAttributes;
  }


  /**
   * @return The id value
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#getId()
   */
  public String getId() {
    return m_id;
  }


  /**
   * @return The name value
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#getName()
   */
  public String getName() {
    return m_name;
  }


  /**
   * @return The parameters value
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#getParameters()
   */
  public Parameter[] getParameters() {
    Parameter[] parameters = new Parameter[m_parameters.size()];

    m_parameters.values().toArray(parameters);

    return parameters;
  }


  /**
   * @see de.objectcode.canyon.worklist.spi.worklist.IApplicationData#setParameterValue(java.lang.String, java.lang.Object)
   */
  public void setParameterValue(String formalName, Object value) {
    Parameter parameter = (Parameter) m_parameters.get(formalName);

    if (parameter != null && parameter.mode != ParameterMode.IN) {
      m_parameters.put(formalName, new Parameter(formalName, parameter.actualName,
              parameter.dataType, parameter.mode, parameter.description, value));
    }
  }
}
