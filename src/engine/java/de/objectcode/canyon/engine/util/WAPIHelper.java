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
package de.objectcode.canyon.engine.util;

import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wfmc.wapi.WMActivityInstance;
import org.wfmc.wapi.WMActivityInstanceState;
import org.wfmc.wapi.WMAttribute;
import org.wfmc.wapi.WMFilter;
import org.wfmc.wapi.WMParticipant;
import org.wfmc.wapi.WMProcessDefinition;
import org.wfmc.wapi.WMProcessDefinitionState;
import org.wfmc.wapi.WMProcessInstance;
import org.wfmc.wapi.WMProcessInstanceState;
import org.wfmc.wapi.WMWorkItem;
import org.wfmc.wapi.WMWorkItemState;
import org.wfmc.wapi.WMWorkflowException;

import de.objectcode.canyon.base.WMActivityInstanceImpl;
import de.objectcode.canyon.base.WMAttributeImpl;
import de.objectcode.canyon.base.WMParticipantImpl;
import de.objectcode.canyon.base.WMProcessDefinitionImpl;
import de.objectcode.canyon.base.WMProcessInstanceImpl;
import de.objectcode.canyon.base.WMWorkItemImpl;
import de.objectcode.canyon.model.process.WorkflowProcess;
import de.objectcode.canyon.spi.filter.CompareFilter;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IAttributeInstance;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * Creates WAPI value objects from run-time entities.
 * TODO: use the factory pattern to enable different API implementations to
 * use this class.
 *
 * @author    Adrian Price
 * @created   28. Oktober 2003
 */
public final class WAPIHelper
{
  private final static  Log  log  = LogFactory.getLog( WAPIHelper.class );


  // Prevent instantiation.
  /**
   *Constructor for the WAPIHelper object
   */
  private WAPIHelper() { }


  /**
   * Description of the Method
   *
   * @param workItem  Description of the Parameter
   * @return          Description of the Return Value
   */
  public final static WMWorkItem fromWorkItem( IWorkItem workItem )
  {
    return new WMWorkItemImpl( workItem.getName(), workItem.getWorkItemId(),
        workItem.getActivityInstanceId(),
        workItem.getActivityDefinitionId(), workItem.getProcessInstanceId(),
        workItem.getProcessDefinitionId().getId(),
        WMWorkItemState.fromInt( workItem.getState() ),
        workItem.getPriority(),
        new WMParticipantImpl( workItem.getParticipant() ),
        workItem.getPerformer(), workItem.getCompletedDate(),
        workItem.getDueDate(), workItem.getStartedDate() );
  }


  /**
   * Description of the Method
   *
   * @param attr  Description of the Parameter
   * @return      Description of the Return Value
   */
  public final static WMAttribute fromAttributeInstance(
      IAttributeInstance attr )
  {

    return new WMAttributeImpl( attr.getName(), attr.getType(),
        attr.getValue() );
  }


  /**
   * Description of the Method
   *
   * @param attrs  Description of the Parameter
   * @return       Description of the Return Value
   */
  public final static WMAttribute[] fromAttributeInstances(
      IAttributeInstance[] attrs )
  {

    WMAttribute[]  wmAttrs  = new WMAttribute[attrs.length];
    for ( int i = 0; i < attrs.length; i++ ) {
      IAttributeInstance  attr  = attrs[i];
      if ( attr == null ) {
        break;
      }
      wmAttrs[i] = new WMAttributeImpl( attr.getName(), attr.getType(),
          attr.getValue() );
    }
    return wmAttrs;
  }


  /**
   * Description of the Method
   *
   * @param workflow  Description of the Parameter
   * @return          Description of the Return Value
   */
  public static WMProcessDefinition fromWorkflowProcess(
      WorkflowProcess workflow )
  {
    return new WMProcessDefinitionImpl( workflow.getId(),
        workflow.getPackage().getId(), workflow.getName(),
        WMProcessDefinitionState.fromInt( workflow.getState() ) );
  }


  /**
   * Description of the Method
   *
   * @param workflows  Description of the Parameter
   * @return           Description of the Return Value
   */
  public static WMProcessDefinition[] fromWorkflowProcesses(
      WorkflowProcess[] workflows )
  {

    WMProcessDefinition[]  wmWorkflows  =
        new WMProcessDefinition[workflows.length];
    for ( int i = 0; i < workflows.length; i++ ) {
      WorkflowProcess  workflow  = workflows[i];
      if ( workflow == null ) {
        break;
      }
      wmWorkflows[i] = new WMProcessDefinitionImpl( workflow.getId(),
          workflow.getPackage().getId(), workflow.getName(),
          WMProcessDefinitionState.fromInt( workflow.getState() ) );
    }
    return wmWorkflows;
  }


  /**
   * Description of the Method
   *
   * @param process  Description of the Parameter
   * @return         Description of the Return Value
   */
  public static WMProcessInstance fromProcessInstance(
      IProcessInstance process )
  {

    return new WMProcessInstanceImpl( process.getName(),
        process.getProcessInstanceId(), process.getProcessDefinitionId().getId(),
        WMProcessInstanceState.fromInt( process.getState() ),
        process.getPriority(),
        fromParticipantNames( process.getParticipants() ),
        process.getCompletedDate(), process.getCreatedDate(),
        process.getDueDate(), process.getStartedDate() );
  }


  /**
   * Description of the Method
   *
   * @param processes  Description of the Parameter
   * @return           Description of the Return Value
   */
  public static WMProcessInstance[] fromProcessInstances(
      IProcessInstance[] processes )
  {

    WMProcessInstance[]  wmProcesses  =
        new WMProcessInstance[processes.length];
    for ( int i = 0; i < processes.length; i++ ) {
      IProcessInstance  process  = processes[i];
      if ( process == null ) {
        break;
      }
      wmProcesses[i] = new WMProcessInstanceImpl( process.getName(),
          process.getProcessInstanceId(),
          process.getProcessDefinitionId().getId(),
          WMProcessInstanceState.fromInt( process.getState() ),
          process.getPriority(),
          fromParticipantNames( process.getParticipants() ),
          process.getCompletedDate(), process.getCreatedDate(),
          process.getDueDate(), process.getStartedDate() );
    }
    return wmProcesses;
  }


  /**
   * Description of the Method
   *
   * @param activity  Description of the Parameter
   * @return          Description of the Return Value
   */
  public static WMActivityInstance fromActivityInstance(
      IActivityInstance activity )
  {

    return new WMActivityInstanceImpl( activity.getName(),
        activity.getActivityInstanceId(),
        activity.getActivityDefinitionId(), activity.getProcessInstance().getProcessInstanceId(),
        activity.getProcessDefinitionId().getId(),
        WMActivityInstanceState.fromInt( activity.getState() ),
        activity.getPriority(),
        fromParticipantNames( activity.getParticipants() ),
        activity.getCompletedDate(), activity.getDueDate(),
        activity.getStartedDate() );
  }


  /**
   * Description of the Method
   *
   * @param activities  Description of the Parameter
   * @return            Description of the Return Value
   */
  public static WMActivityInstance[] fromActivityInstances(
      IActivityInstance[] activities )
  {

    WMActivityInstance[]  wmActivities  =
        new WMActivityInstance[activities.length];
    for ( int i = 0; i < activities.length; i++ ) {
      IActivityInstance  activity  = activities[i];
      if ( activity == null ) {
        break;
      }
      wmActivities[i] = new WMActivityInstanceImpl( activity.getName(),
          activity.getActivityInstanceId(),
          activity.getActivityDefinitionId(),
          activity.getProcessInstance().getProcessInstanceId(),
          activity.getProcessDefinitionId().getId(),
          WMActivityInstanceState.fromInt( activity.getState() ),
          activity.getPriority(),
          fromParticipantNames( activity.getParticipants() ),
          activity.getCompletedDate(), activity.getDueDate(),
          activity.getStartedDate() );
    }
    return wmActivities;
  }


  /**
   * Description of the Method
   *
   * @param workitems  Description of the Parameter
   * @return           Description of the Return Value
   */
  public static WMWorkItem[] fromWorkItems( IWorkItem[] workitems )
  {
    WMWorkItem[]  wmWorkItems  =
        new WMWorkItem[workitems.length];
    for ( int i = 0; i < workitems.length; i++ ) {
      IWorkItem  workItem  = workitems[i];
      if ( workItem == null ) {
        break;
      }
      wmWorkItems[i] = new WMWorkItemImpl( workItem.getName(),
          workItem.getWorkItemId(), workItem.getActivityInstanceId(),
          workItem.getActivityDefinitionId(),
          workItem.getProcessInstanceId(),
          workItem.getProcessDefinitionId().getId(),
          WMWorkItemState.fromInt( workItem.getState() ),
          workItem.getPriority(),
          new WMParticipantImpl( workItem.getParticipant() ),
          workItem.getPerformer(), workItem.getCompletedDate(),
          workItem.getDueDate(), workItem.getStartedDate() );
    }
    return wmWorkItems;
  }


  /**
   * Description of the Method
   *
   * @param participantNames  Description of the Parameter
   * @return                  Description of the Return Value
   */
  private static WMParticipant[] fromParticipantNames(
      String[] participantNames )
  {

    WMParticipant[]  participants
         = new WMParticipant[participantNames.length];
    for ( int i = 0; i < participantNames.length; i++ ) {
      participants[i] = new WMParticipantImpl( participantNames[i] );
    }
    return participants;
  }


  /**
   * Description of the Method
   *
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception WMWorkflowException  Description of the Exception
   */
  public static IFilter fromWMFilter( WMFilter filter )
    throws WMWorkflowException
  {
    if ( filter.getFilterType() == WMFilter.SIMPLE_TYPE ) {
      if ( filter.getComparison() == WMFilter.EQ ) {
        return new CompareFilter( filter.getAttributeName(), CompareFilter.EQ, filter.getFilterValue() );
      } else if ( filter.getComparison() == WMFilter.NE ) {
        return new CompareFilter( filter.getAttributeName(), CompareFilter.EQ, filter.getFilterValue() );
      } else if ( filter.getComparison() == WMFilter.GE ) {
        return new CompareFilter( filter.getAttributeName(), CompareFilter.GE, filter.getFilterValue() );
      } else if ( filter.getComparison() == WMFilter.GT ) {
        return new CompareFilter( filter.getAttributeName(), CompareFilter.GT, filter.getFilterValue() );
      } else if ( filter.getComparison() == WMFilter.LE) {
        return new CompareFilter( filter.getAttributeName(), CompareFilter.LE, filter.getFilterValue() );
      } else if ( filter.getComparison() == WMFilter.LT) {
        return new CompareFilter( filter.getAttributeName(), CompareFilter.LT, filter.getFilterValue() );
      }
    } else {
      try {
        SQLQueryParser  parser  = new SQLQueryParser( new StringReader( filter.getFilterString() ) );

        return parser.SQLOrExpr();
      }
      catch ( Exception e ) {
        log.error( "Exception", e );
        throw new WMWorkflowException( e );
      }
    }
    return null;
  }
}
