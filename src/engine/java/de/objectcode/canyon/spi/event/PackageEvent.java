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

import org.wfmc.audit.WMAEventCode;

import de.objectcode.canyon.model.WorkflowPackage;

/**
 * Delivers package event notifications.
 *
 * @author    Adrian Price
 * @created   3. November 2003
 */
public final class PackageEvent extends WorkflowEvent
{
  final static          long      serialVersionUID  = 3252890089775757936L;
  /**
   * The package was created.
   */
  public final static   int       CREATED           = 0;
  /**
   * The package was removed.
   */
  public final static   int       DELETED           = 1;
  /**
   * The package was updated.
   */
  public final static   int       UPDATED           = 2;
  private final static  String[]  EVENT_TYPES       = {
      "PackageCreated",
      "PackageDeleted",
      "PackageUpdated"
      };


  /**
   * Constructs a new package event.
   *
   * @param source
   * @param id
   * @param broker
   */
  public PackageEvent( WorkflowPackage source, int id, IWorkflowEventBroker broker )
  {
    super( source, id, Package.class, EVENT_TYPES[id], source.getId(),
        broker );
  }


  /**
   * Returns the source package.
   *
   * @return   The package that fired the event.
   */
  public WorkflowPackage getPackage()
  {
    return ( WorkflowPackage ) source;
  }


  /**
   * Gets the wMAEventCode attribute of the PackageEvent object
   *
   * @return   The wMAEventCode value
   */
  public WMAEventCode getWMAEventCode()
  {
    return null;
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public String toString()
  {
    return EVENT_TYPES[_id] + "[source=" + source + ']';
  }
}
