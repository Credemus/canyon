/*
 *  --
 *  Copyright (C) 2002-2003 Aetrion LLC.
 *  All rights reserved.
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions, and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions, and the disclaimer that follows
 *  these conditions in the documentation and/or other materials
 *  provided with the distribution.
 *  3. The names "OBE" and "Open Business Engine" must not be used to
 *  endorse or promote products derived from this software without prior
 *  written permission.  For written permission, please contact
 *  obe@aetrion.com.
 *  4. Products derived from this software may not be called "OBE" or
 *  "Open Business Engine", nor may "OBE" or "Open Business Engine"
 *  appear in their name, without prior written permission from
 *  Aetrion LLC (obe@aetrion.com).
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 *  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 *  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 *  STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 *  IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *  For more information on OBE, please see
 *  <http://www.openbusinessengine.org/>.
 */
package de.objectcode.canyon.spi.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.Map;

/**
 * Delivers an external event from a third party application or source.  This
 * is OBE's primary asynchronous integration mechanism.
 *
 * @author    Adrian Price
 * @created   22. Oktober 2003
 * @see       ApplicationEventBroker
 * @see       ApplicationEventListener
 */
public class ApplicationEvent extends EventObject
{
  final static            long      serialVersionUID  = 2966489532857498559L;
  /**
   * A timed event that fires at a predetermined moment in time. The formal
   * parameters are:
   * <table>
   *  <tr><th>Id</th><th>DataType</th><th>Mode</th><th>Description</th></tr>
   *  <tr><td>date</td><td>DATETIME</td><td>IN</td>
   *      <td>The date-time at which the event must fire.</td></tr>
   *  <tr><td>subscriberId</td><td>STRING</td><td>IN</td>
   *      <td>A unique ID for this subscriber.</td></tr>
   * </table>
   */
  public final static     String    TIMEOUT_EVENT     = "Timeout";
  public final static     String    EXCEPTION_EVENT   = "Exception";
  public final static     String    JAVA_TYPE         = "application/x-java-object";
  public final static     String    XML_TYPE          = "text/xml";
  protected final static  Map       EMPTY_MAP         = Collections.EMPTY_MAP;
  protected final static  Object[]  EMPTY_KEYS        = {};
  private final           String    _eventType;
  private final           String    _contentType;
  private final           String    _schema;
  private final           Map       _attributes;
  private                 Object[]  _keys;
  private                 Object    _application;


  /**
   * Constructs an application event object.
   *
   * @param applicationContext  An arbitrary application context.
   * @param payload             The event data payload.
   * @param id                  The event type, as registered in the
   * {@link ApplicationEventBroker}.  Cannot be <code>null</code>.
   * @param contentType         The MIME content type of the <code>payload</code>
   * object.  Cannot be <code>null</code>.
   * @param schema              The schema for the <code>payload</code> object.  Cannot be
   * <code>null</code>.
   * @param keys                The key values, calculated from the <code>payload</code>
   * object according to the definition for <code>id</code>.
   * @param attributes          Additional meta-data describing the
   * <code>payload</code> object.
   */
  public ApplicationEvent( Object applicationContext, Object payload,
      String id, Object[] keys, String contentType, String schema,
      Map attributes )
  {

    super( payload );

    // The meta-data are mandatory.
    if ( id == null || contentType == null || schema == null ) {
      throw new IllegalArgumentException();
    }

    _application = applicationContext;
    _eventType = id;
    _keys = keys != null ? keys : EMPTY_KEYS;
    _contentType = contentType;
    _schema = schema;
    _attributes = attributes != null ? attributes : EMPTY_MAP;
  }


  /**
   * Sets the keys for the payload.
   *
   * @param keys  Event keys.
   */
  public void setKeys( Object[] keys )
  {
    _keys = keys;
  }


  /**
   * Sets the application context.
   *
   * @param application  The application context.
   */
  public void setApplication( Object application )
  {
    _application = application;
  }


  /**
   * Returns the MIME content type of the event data.  The
   * <a href="http://www.iana.org/assignments/media-types/">
   * content type</a> indicates the type of object in the payload.  For
   * example, an XML document would have a content type of
   * <code>text/xml</code>; an MS-Word document would be
   * <code>application/msword</code>, a Java object would use the extension
   * type <code>application/x-java-object</code>, and so on.
   *
   * @return   The MIME content type.
   * @see      #getSchema
   */
  public String getContentType()
  {
    return _contentType;
  }


  /**
   * Returns the business name for the application event.  This must have been
   * registered in the application event broker by a prior call to
   * {@link ApplicationEventBroker#createEventType}.
   *
   * @return   The event type.
   */
  public String getEventType()
  {
    return _eventType;
  }


  /**
   * The name of the abstract schema for the payload (the source object).  The
   * interpretation depends on the content type. For example, the schema for a
   * content type of <code>text/xml</code> would be the URI of the schema
   * location or the Public or System ID of its DTD; if these are undefined,
   * the tag name of the document element. The schema for a Java object is
   * simply its fully qualified class name, or possibly the name of an
   * interface that it implements.
   *
   * @return   The abstract schema name.
   * @see      #getContentType
   */
  public String getSchema()
  {
    return _schema;
  }


  /**
   * Returns the key values for the payload.  The application event broker
   * calculates the key values from the payload by evaluating the key value
   * expressions in the event type definition.
   *
   * @return   Key values for the payload.
   */
  public Object[] getKeys()
  {
    return _keys.length != 0 ? ( Object[] ) _keys.clone() : _keys;
  }


  /**
   * Returns a custom attribute associated with the payload.  Custom
   * attributes provide additional metadata about the payload.
   *
   * @param key  The attribute name.
   * @return     The attribute value, if defined.
   */
  public Object getAttribute( String key )
  {
    return _attributes.get( key );
  }


  /**
   * Returns a map of all custom attributes associated with the payload.
   *
   * @return   Attribute map, keyed on attribute name.
   * @see      #getAttribute
   */
  public Map getAttributes()
  {
    return _attributes;
  }


  /**
   * Returns the application context.  This object is accessible through the
   * {@link WorkflowContext#getApplication()} method.
   *
   * @return   Application context.
   */
  public Object getApplication()
  {
    return _application;
  }


  /**
   * Returns the event payload object, typically an application-specific bean.
   * This is a synonym for <code>getSource</code>.
   *
   * @return   The event payload.
   */
  public Object getPayload()
  {
    return getSource();
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    return "ApplicationEvent[id='" + _eventType +
        "', contentType='" + _contentType +
        "', schema='" + _schema +
        "', keys=" + ( _keys == null
        ? null : "length:" + _keys.length + Arrays.asList( _keys ) ) +
        ", attributes=" + ( _attributes == null
        ? null : "size:" + _attributes.size() + _attributes ) +
        ", payload=" + getSource() +
        ']';
  }
}
