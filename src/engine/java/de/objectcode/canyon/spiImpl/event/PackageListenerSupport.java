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
package de.objectcode.canyon.spiImpl.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.model.WorkflowPackage;
import de.objectcode.canyon.spi.event.ApplicationEvent;
import de.objectcode.canyon.spi.event.IPackageListener;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.event.PackageEvent;

/**
 * Supports package listeners.  This class maintains a list of
 * {@link org.obe.spi.event.PackageListener}s, and provides a set of
 * <code>firePackage&lt;Event&gt;({@link Package} source)</code> methods to
 * notify the listeners of events.
 *
 * @author    Adrian Price
 * @created   4. November 2003
 */
public final class PackageListenerSupport extends AbstractListenerSupport
{
  private final static  Log       _logger               = LogFactory.getLog(
      PackageListenerSupport.class );
  private final static  String[]  NOTIFICATION_METHODS  = {
      "packageCreated",
      "packageDeleted",
      "packageUpdated"
      };


  /**
   *Constructor for the PackageListenerSupport object
   *
   * @param eventBroker  Description of the Parameter
   */
  public PackageListenerSupport( IWorkflowEventBroker eventBroker )
  {
    super( eventBroker, PackageEvent.class, IPackageListener.class,
        NOTIFICATION_METHODS );
  }


  /**
   * Gets the logger attribute of the PackageListenerSupport object
   *
   * @return   The logger value
   */
  public Log getLogger()
  {
    return _logger;
  }


  /**
   * Description of the Method
   *
   * @param source  Description of the Parameter
   */
  public void firePackageCreated( WorkflowPackage source )
  {
    fireEvent( createPackageCreated(source));
  }


  public ApplicationEvent createPackageCreated( WorkflowPackage source )
  {
    return createEvent( source, PackageEvent.CREATED, null );
  }

  /**
   * Description of the Method
   *
   * @param source  Description of the Parameter
   */
  public void firePackageDeleted( WorkflowPackage source )
  {
    fireEvent(createPackageDeleted( source));
  }

  public ApplicationEvent createPackageDeleted( WorkflowPackage source )
  {
    return createEvent( source, PackageEvent.DELETED, null );
  }

  /**
   * Description of the Method
   *
   * @param source  Description of the Parameter
   */
  public void firePackageUpdated( WorkflowPackage source )
  {
    fireEvent(createPackageUpdated( source));
  }

  public ApplicationEvent createPackageUpdated( WorkflowPackage source )
  {
    return createEvent( source, PackageEvent.UPDATED, null );
  }
}
