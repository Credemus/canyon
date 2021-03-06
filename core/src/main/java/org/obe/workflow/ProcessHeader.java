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

package org.obe.workflow;

import java.io.Serializable;
import java.util.Date;

import org.obe.util.Duration;
import org.obe.util.DurationUnit;
import org.obe.util.TimeEstimation;

public class ProcessHeader implements Serializable {
    static final long serialVersionUID = 1047558258115813116L;

    private Date created;
    private String priority;
    private Duration limit;
    private Duration validFrom;
    private Duration validTo;
    private TimeEstimation timeEstimation;
    private String description;

    private DurationUnit durationUnit;

    /** Construct a new ProcessHeader. */

    public ProcessHeader(){

    }

    /** Get the date on which the workflow process was created.

        @return The process creation date
    */

    public Date getCreated(){
        return created;
    }

    /** Set the date on which the workflow process was created.

        @param created The created date
    */

    public void setCreated(Date created){
        this.created = created;
    }

    public String getPriority(){
        return priority;
    }

    public void setPriority(String priority){
        this.priority = priority;
    }

    public Duration getLimit(){
        return limit;
    }

    public void setLimit(Duration limit){
        this.limit = limit;
    }

    public Duration getValidFrom(){
        return validFrom;
    }

    public void setValidFrom(Duration validFrom){
        this.validFrom = validFrom;
    }

    public Duration getValidTo(){
        return validTo;
    }

    public void setValidTo(Duration validTo){
        this.validTo = validTo;
    }

    public TimeEstimation getTimeEstimation(){
        return timeEstimation;
    }

    public void setTimeEstimation(TimeEstimation timeEstimation){
        this.timeEstimation = timeEstimation;
    }

    /** Get the default duration unit for durations which do not
        specify the unit.

        @return The default duration unit
    */

    public DurationUnit getDurationUnit(){
        return durationUnit;
    }

    public void setDurationUnit(DurationUnit durationUnit){
        this.durationUnit = durationUnit;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

}
