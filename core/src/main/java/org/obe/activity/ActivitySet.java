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

import org.obe.transition.Transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of activities and related transitions.
 *
 * @author Anthony Eden
 */

public class ActivitySet implements Serializable {
  static final long serialVersionUID = 7301415215949413883L;

  private String id;
  private final List<Activity> activities;
  private final List<Transition> transitions;

  /**
   * Construct a new ActivitySet.
   *
   * @param id The unique ID
   */

  public ActivitySet(String id) {
    setId(id);

    this.activities = new ArrayList<Activity>();
    this.transitions = new ArrayList<Transition>();
  }

  /**
   * Get the ID for the activity set.
   *
   * @return The activity set ID
   */

  public String getId() {
    return id;
  }

  /**
   * Set the activity set ID.  The id must be unique for all activity
   * sets in the process instance and can not be null.
   *
   * @param id The activity set ID
   */

  public void setId(String id) {
    if (id == null) {
      throw new IllegalArgumentException("ID can not be null");
    }
    this.id = id;
  }

  /**
   * Get a list of activities in the set.
   *
   * @return List of activities in the set
   */

  public List<Activity> getActivities() {
    return activities;
  }

  /**
   * Get a list of transitions in the set.
   *
   * @return List of transitions in the set
   */

  public List<Transition> getTransitions() {
    return transitions;
  }

}
