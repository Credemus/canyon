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
package de.objectcode.canyon.spiImpl.evaluator;

import java.util.Date;

import de.objectcode.canyon.model.participant.Participant;

/**
 * Implements WfMC-prescribed functions available to scripted expressions.
 *
 * @author    Adrian Price
 * @created   27. Oktober 2003
 */
public class WfMCFunctions
{
  /**
   *Constructor for the WfMCFunctions object
   */
  public WfMCFunctions() { }


  /**
   * Description of the Method
   *
   * @param p  Description of the Parameter
   * @return   Description of the Return Value
   */
  public static Participant[] alternateOf( Participant p )
  {
    // TODO: implement WfMCFunctions.alternateOf
    throw new UnsupportedOperationException( "alternateOf" );
  }


  /**
   * Description of the Method
   *
   * @param p  Description of the Parameter
   * @return   Description of the Return Value
   */
  public static Participant coordinatorOf( Participant p )
  {
    // TODO: implement WfMCFunctions.coordinatorOf
    throw new UnsupportedOperationException( "coordinatorOf" );
  }


  /**
   * Description of the Method
   *
   * @param p  Description of the Parameter
   * @return   Description of the Return Value
   */
  public static Participant managerOf( Participant p )
  {
    // TODO: implement WfMCFunctions.managerOf
    throw new UnsupportedOperationException( "managerOf" );
  }


  /**
   * Description of the Method
   *
   * @param p  Description of the Parameter
   * @return   Description of the Return Value
   */
  public static Participant[] superiorOf( Participant p )
  {
    // TODO: implement WfMCFunctions.superiorOf
    throw new UnsupportedOperationException( "superiorOf" );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public static Date currentDate()
  {
    return new Date();
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public static Participant currentActor()
  {
//        Participant participant = null;
//        WorkflowContext ctx = WorkflowContext.peekContext();
//        if (ctx != null)
//            participant = ctx.getActivityInstance().getParticipants();
//        return participant;
    // TODO: implement WfMCFunctions.currentActor
    throw new UnsupportedOperationException( "currentActor" );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public static String currentResponsible()
  {
    // TODO: implement WfMCFunctions.currentResponsible
    throw new UnsupportedOperationException( "currentResponsible" );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public static String performerOfLast()
  {
    // TODO: implement WfMCFunctions.performerOfLast
    throw new UnsupportedOperationException( "performerOfLast" );
  }


  /**
   * Description of the Method
   *
   * @return   Description of the Return Value
   */
  public static String currentPerformer()
  {
    // TODO: implement WfMCFunctions.currentPerformer
    throw new UnsupportedOperationException( "currentPerformer" );
  }
}
