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

import java.util.HashMap;

import org.wfmc.audit.WMAEventCode;

/**
 * Base class for workflow notification events.  Sub-classes must follow a
 * specific design pattern: they must supply a single, public constructor that
 * takes the following arguments: <code>(Object source, int id,
 * WorkflowEventBroker [, org.obe.WFElement definition])</code>.  The
 * <code>source</code> and <code>definition</code> arguments are sub-classes of
 * <code>Object</code> and <code>WFElement</code> respectively.  The
 * <code>definition</code> argument is optional.  The event ids should be
 * declared by the event subclass, and must be contiguous and 0-based.
 *
 * @author    Adrian Price
 * @created   3. November 2003
 * @see       AbstractListenerSupport
 */
public abstract class WorkflowEvent extends ApplicationEvent
{
  final static         long                 serialVersionUID  = -1796915832725988942L;
  protected final      long                 _timestamp;
  protected final      int                  _id;
  protected transient  IWorkflowEventBroker  _broker;


  /**
   * Construct a new <code>WorkflowEvent</code>.
   *
   * @param source       The entity that is the source of this event.
   * @param id           The event ID code.  <em>N.B. Event codes are defined by
   * sub-classes; they must be contiguous and 0-based.</em>
   * @param sourceClass  The SPI interface implemented by the
   * <code>source</code> object.
   * @param eventType    The event name.  This has the form
   * <code>&lt;event-class&gt;</code>
   * @param key          The primary key of the <code>source</code> entity.
   * @param broker       Description of the Parameter
   * @see                AbstractListenerSupport#AbstractListenerSupport
   */
  protected WorkflowEvent( Object source, int id, Class sourceClass,
      String eventType, Object key, IWorkflowEventBroker broker )
  {
    super( null, source, eventType, new Object[]{key},
        ApplicationEvent.JAVA_TYPE, sourceClass.getName(),
        new HashMap() );
    _broker = broker;
    _id = id;
    _timestamp = System.currentTimeMillis();
  }


  /**
   * Returns the event id, as defined in subclasses.
   *
   * @return   The event id.
   */
  public final int getId()
  {
    return _id;
  }


  /**
   * Returns the system time at which the event occurred.
   *
   * @return   System time in milliseconds.
   */
  public long getTimestamp()
  {
    return _timestamp;
  }


  /**
   * Returns the WfMC Interface 5 audit event code (if defined).
   *
   * @return   Audit event code.
   */
  public abstract WMAEventCode getWMAEventCode();
}
