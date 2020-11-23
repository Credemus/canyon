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

import java.util.EventListener;

/**
 * The listener interface for receiving process instance events.
 *
 * @author    Adrian Price
 * @created   3. November 2003
 */
public interface IProcessInstanceListener extends EventListener
{
  /**
   * A process instance was aborted.
   *
   * @param e  The process instance event.
   */
  void processInstanceAborted( ProcessInstanceEvent e );


  /**
   * A process instance was completed.
   *
   * @param e  The process instance event.
   */
  void processInstanceCompleted( ProcessInstanceEvent e );


  /**
   * A process instance was created.
   *
   * @param e  The process instance event.
   */
  void processInstanceCreated( ProcessInstanceEvent e );


  /**
   * A process instance was deleted.  N.B. This method is called
   * <em>before</em> the instance is physically deleted, in order for a
   * listener to access the source process instance before it has been
   * deleted.
   *
   * @param e  The process instance event.
   */
  void processInstanceDeleted( ProcessInstanceEvent e );


  /**
   * A process instance was resumed.
   *
   * @param e  The process instance event.
   */
  void processInstanceResumed( ProcessInstanceEvent e );


  /**
   * A process instance was started.
   *
   * @param e  The process instance event.
   */
  void processInstanceStarted( ProcessInstanceEvent e );


  /**
   * A process instance was suspended.
   *
   * @param e  The process instance event.
   */
  void processInstanceSuspended( ProcessInstanceEvent e );


  /**
   * A process instance was terminated.
   *
   * @param e  The process instance event.
   */
  void processInstanceTerminated( ProcessInstanceEvent e );
}
