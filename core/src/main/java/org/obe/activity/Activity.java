/*-- 

 Copyright (C) 2002-2003 Aetrion LLC.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The names "OBE" and "Open Business Engine" must not be used to 
 	endorse or promote products derived from this software without prior 
 	written permission.  For written permission, please contact 
 	obe@aetrion.com.
 
 4. Products derived from this software may not be called "OBE" or
 	"Open Business Engine", nor may "OBE" or "Open Business Engine" 
 	appear in their name, without prior written permission from 
 	Aetrion LLC (obe@aetrion.com).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT, 
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see 
 <http://www.openbusinessengine.org/>.
 
 */

package org.obe.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.obe.AbstractWFElement;
import org.obe.transition.Transition;
import org.obe.transition.TransitionRestriction;
import org.obe.util.Deadline;
import org.obe.util.Duration;
import org.obe.util.SimulationInformation;
import org.obe.workflow.WorkflowProcess;

public class Activity extends AbstractWFElement {
  static final long serialVersionUID = -7464664489372049213L;

  /**
   * The default start mode.
   */
  public static final AutomationMode DEFAULT_START_MODE =
          AutomationMode.AUTOMATIC;

  /**
   * The default finish mode.
   */
  public static final AutomationMode DEFAULT_FINISH_MODE =
          AutomationMode.AUTOMATIC;

  private final WorkflowProcess workflowProcess;
  private Duration limit;
  private Implementation implementation;
  private Route route;
  private BlockActivity blockActivity;
  private String performer;
  private AutomationMode startMode = DEFAULT_START_MODE;
  private AutomationMode finishMode = DEFAULT_FINISH_MODE;
  private CompletionStrategy completionStrategy;
  private String priority;
  private final List<Deadline> deadlines = new ArrayList<Deadline>();
  private SimulationInformation simulationInformation;
  private URL documentation;
  private URL icon;
  private final List<TransitionRestriction> transitionRestrictions = new ArrayList<TransitionRestriction>();
  private final Map<String, Transition> afferentTransitions = new HashMap<String, Transition>();
  private final Map<String, Transition> efferentTransitions = new HashMap<String, Transition>();

  /**
   * Construct a new Activity with the given ID and name.
   *
   * @param id   The Activity ID
   * @param name The Activity name
   */

  public Activity(String id, String name, WorkflowProcess workflowProcess) {
    super(id, name);

    this.workflowProcess = workflowProcess;
  }

  /**
   * Get the WorkflowProcess in which this activity is declared.
   *
   * @return The WorkflowProcess
   */
  public WorkflowProcess getWorkflowProcess() {
    return workflowProcess;
  }

  /**
   * Get the time limit allowed for the execution of the activity.
   *
   * @return The time limit
   */

  public Duration getLimit() {
    return limit;
  }

  /**
   * Set the time limit allowed for the execution of the activity.
   *
   * @param limit The time limit
   */

  public void setLimit(Duration limit) {
    this.limit = limit;
  }

  /**
   * Get the implementation object for the activity.  If this method
   * returns null then the <code>getRoute()</code> must return a valid
   * Route object.
   *
   * @return The Implementation object
   */

  public Implementation getImplementation() {
    return implementation;
  }

  /**
   * Set the implementation object for the activity.
   *
   * @param implementation The new Implementation object
   */

  public void setImplementation(Implementation implementation) {
    this.implementation = implementation;
  }

  /**
   * Get the Route object for the activity.  If the
   * <code>getImplementation()</code> method returns null then this method
   * must return a valid Route object.
   *
   * @return The Route object
   */

  public Route getRoute() {
    return route;
  }

  /**
   * Set the Route object for the activity.
   *
   * @param route The new Route object
   */

  public void setRoute(Route route) {
    this.route = route;
  }

  public BlockActivity getBlockActivity() {
    return blockActivity;
  }

  public void setBlockActivity(BlockActivity blockActivity) {
    this.blockActivity = blockActivity;
  }

  /**
   * Get the ID of the activity performer.
   *
   * @return The ID of the performer
   */

  public String getPerformer() {
    return performer;
  }

  /**
   * Set the ID of the activity performer.
   *
   * @param performer The ID of the performer
   */

  public void setPerformer(String performer) {
    this.performer = performer;
  }

  /**
   * Get the start mode for the activity.  The start mode is used to
   * determine if the activity should start automatically or manually.
   * The default value is <code>AutomationMode.AUTOMATIC</code>.
   *
   * @return The start mode
   */

  public AutomationMode getStartMode() {
    return startMode;
  }

  /**
   * Set the start mode for the activity.  The start mode is used to
   * determine if the activity should start automatically or manually.
   *
   * @param startMode The new start mode
   */

  public void setStartMode(AutomationMode startMode) {
    if (startMode == null) {
      startMode = DEFAULT_START_MODE;
    }
    this.startMode = startMode;
  }

  /**
   * Get the finish mode for the activity.  The finish mode is used to
   * determine if the activity should complete automatically or manually.
   * The default value is <code>AutomationMode.AUTOMATIC</code>.
   *
   * @return The finish mode
   */

  public AutomationMode getFinishMode() {
    return finishMode;
  }

  /**
   * Set the finish mode for the activity.  The finish mode is used to
   * determine if the activity should complete automatically or manually.
   *
   * @param finishMode The new finish mode
   */

  public void setFinishMode(AutomationMode finishMode) {
    if (finishMode == null) {
      finishMode = DEFAULT_FINISH_MODE;
    }
    this.finishMode = finishMode;
  }

  /**
   * Returns the completion strategy for this activity.
   *
   * @return Completion Strategy.
   */
  public CompletionStrategy getCompletionStrategy() {
    return completionStrategy;
  }

  /**
   * Sets the completion strategy for this activity.
   *
   * @param strategy Completion Strategy.
   */
  public void setCompletionStrategy(CompletionStrategy strategy) {
    this.completionStrategy = strategy;
  }

  /**
   * Get the priority of the activity.
   *
   * @return The priority
   */

  public String getPriority() {
    return priority;
  }

  /**
   * Set the priority of the activity.
   *
   * @param priority The new priority
   */

  public void setPriority(String priority) {
    this.priority = priority;
  }

  /**
   * Get the activity deadlines.
   *
   * @return The deadline
   */

  public List<Deadline> getDeadlines() {
    return deadlines;
  }

  /**
   * Get the SimulationInformation for the activity.  This information can
   * be used to make estimations for the execution time of an activity
   * which can then be used to test a workflow definition timing.
   *
   * @return The SimulationInformation
   */

  public SimulationInformation getSimulationInformation() {
    return simulationInformation;
  }

  /**
   * Set the SimulationInformation for the activity.
   *
   * @param simulationInformation The new SimulationInformation
   */

  public void setSimulationInformation(
          SimulationInformation simulationInformation) {
    this.simulationInformation = simulationInformation;
  }

  /**
   * Get the URL for the documentation for the activity.  This URL should
   * point to documentation which can be used by developers, administrators
   * and other users to understand what the activity does.
   *
   * @return The URL for the activity documentation
   */

  public URL getDocumentation() {
    return documentation;
  }

  /**
   * Get the URL for the documentation for the activity.
   *
   * @param documentation The new URL for the activity documentation
   */

  public void setDocumentation(URL documentation) {
    this.documentation = documentation;
  }

  /**
   * Get the URL for the icon for the activity.  The icon is used to
   * represent the activity in a graphical representatioin of the
   * workflow process.
   *
   * @return The URL for the activity's icon
   */

  public URL getIcon() {
    return icon;
  }

  /**
   * Set the URL for the icon for the activity.
   *
   * @param icon The new URL for the activity's icon
   */

  public void setIcon(URL icon) {
    this.icon = icon;
  }

  /**
   * Get a List of transition restrictions for transitions which connect
   * to this activity.
   *
   * @return a List of transition restrictions
   */

  public List<TransitionRestriction> getTransitionRestrictions() {
    return transitionRestrictions;
  }

  public Map<String, Transition> getAfferentTransitions() {
    return afferentTransitions;
  }

  public Map<String, Transition> getEfferentTransitions() {
    return efferentTransitions;
  }

  public String toString() {
    return "Activity[id='" + getId() + ", name='" + getName() + "']";
  }

  public boolean isExitActivity() {
    return efferentTransitions.size() == 0;
  }

  public boolean isStartActivity() {
    return afferentTransitions.size() == 0;
  }
}