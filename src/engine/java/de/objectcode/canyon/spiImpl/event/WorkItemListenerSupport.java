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

import de.objectcode.canyon.model.activity.Activity;
import de.objectcode.canyon.spi.event.ApplicationEvent;
import de.objectcode.canyon.spi.event.IWorkItemListener;
import de.objectcode.canyon.spi.event.IWorkflowEventBroker;
import de.objectcode.canyon.spi.event.WorkItemEvent;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * Supports work item listeners.  This class maintains a list of
 * {@link org.obe.spi.event.WorkItemListener}s, and provides a set of
 * <code>fireWorkItem&lt;Event&gt;({@link org.obe.spi.model.WorkItemDescriptor} source,
 * {@link Activity} definition)</code> methods to notify the listeners of
 * events.
 *
 * @author    Adrian Price
 * @created   3. November 2003
 */
public final class WorkItemListenerSupport extends AbstractListenerSupport
{
  private final static  Log       _logger               = LogFactory.getLog(
      WorkItemListenerSupport.class );
  private final static  String[]  NOTIFICATION_METHODS  = {
      "workItemAborted",
      "workItemAssigned",
      "workItemCompleted",
      "workItemCreated",
      "workItemResumed",
      "workItemStarted",
      "workItemStopped",
      "workItemSuspended",
      "workItemTerminated",
      "workItemDeprived"
      };


  /**
   *Constructor for the WorkItemListenerSupport object
   *
   * @param eventBroker  Description of the Parameter
   */
  public WorkItemListenerSupport( IWorkflowEventBroker eventBroker )
  {
    super( eventBroker, WorkItemEvent.class, IWorkItemListener.class,
        NOTIFICATION_METHODS );
  }


  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param id    Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemEvent( IWorkItem src, int id, Activity defn )
  {
    fire( src, id, defn );
  }


  protected ApplicationEvent createWorkItemEvent(IWorkItem src, int id, Activity defn) {
  	return createEvent(src,id,defn);
  }
  
  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemAborted( IWorkItem src, Activity defn )
  {
    fireEvent( createWorkItemAborted(src, defn));
  }

  public ApplicationEvent createWorkItemAborted( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.ABORTED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemAssigned( IWorkItem src, Activity defn )
  {
    fireEvent(createWorkItemAssigned( src,defn ));
  }

  public ApplicationEvent createWorkItemAssigned( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.ASSIGNED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemCompleted( IWorkItem src, Activity defn )
  {
    fireEvent(createWorkItemCompleted( src, defn ));
  }

  public ApplicationEvent createWorkItemCompleted( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.COMPLETED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemCreated( IWorkItem src )
  {
    fireEvent( createWorkItemCreated( src));
  }

  public ApplicationEvent createWorkItemCreated( IWorkItem src )
  {
    return createWorkItemEvent( src, WorkItemEvent.CREATED, null );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemResumed( IWorkItem src, Activity defn )
  {
    fireEvent( createWorkItemResumed(src,defn ));
  }

  public ApplicationEvent createWorkItemResumed( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.RESUMED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemStarted( IWorkItem src, Activity defn )
  {
    fireEvent( createWorkItemStarted(src, defn ));
  }

  public ApplicationEvent createWorkItemStarted( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.STARTED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemStopped( IWorkItem src, Activity defn )
  {
    fireEvent( createWorkItemStopped(src,defn ));
  }

  public ApplicationEvent createWorkItemStopped( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.STOPPED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemSuspended( IWorkItem src, Activity defn )
  {
    fireEvent(createWorkItemSuspended(  src,defn ));
  }

  public ApplicationEvent createWorkItemSuspended( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.SUSPENDED, defn );
  }

  /**
   * Description of the Method
   *
   * @param src   Description of the Parameter
   * @param defn  Description of the Parameter
   */
  public void fireWorkItemDeprived( IWorkItem src, Activity defn )
  {
    fireEvent( createWorkItemDeprived(src,defn ));
  }

  public void fireWorkItemTerminated( IWorkItem src, Activity defn )
  {
    fireEvent( createWorkItemTerminated(src,defn ));
  }

  public ApplicationEvent createWorkItemTerminated( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.TERMINATED, defn );
  }

  public ApplicationEvent createWorkItemDeprived( IWorkItem src, Activity defn )
  {
    return createWorkItemEvent( src, WorkItemEvent.DEPRIVED, defn );
  }

}
