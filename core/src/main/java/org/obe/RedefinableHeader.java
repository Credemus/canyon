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

package org.obe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Header information which can be used for entities such as the package
    or individual workflow processes.

    @author Anthony Eden
*/

public class RedefinableHeader implements Serializable {
    static final long serialVersionUID = -7749373588413143522L;

    private String author;
    private String version;
    private String codepage;
    private String countrykey;
    private List<String> responsibles;
    private PublicationStatus publicationStatus;

    /** Construct a new RedefinableHeader. */

    public RedefinableHeader(){
        responsibles = new ArrayList<String>();
    }

    /** Get the entity's author.

        @return The entity's author
    */

    public String getAuthor(){
        return author;
    }

    /** Set the entity's author.

        @param author The entity's new author
    */

    public void setAuthor(String author){
        this.author = author;
    }

    /** Get the entity's version.

        @return The version
    */

    public String getVersion(){
        return version;
    }

    /** Set the entity's version.

        @param version The version
    */

    public void setVersion(String version){
        this.version = version;
    }

    public String getCodepage(){
        return codepage;
    }

    public void setCodepage(String codepage){
        this.codepage = codepage;
    }

    public String getCountrykey(){
        return countrykey;
    }

    public void setCountrykey(String countrykey){
        this.countrykey = countrykey;
    }

    /** Get a List of responsible participants' ids.

        @return A List of participant ids
    */

    public List<String> getResponsibles(){
        return responsibles;
    }

    /** Get the publication status for this workflow definition.

        @return The publication status
    */

    public PublicationStatus getPublicationStatus(){
        return publicationStatus;
    }

    /** Set the publication status for this workflow definition.

        @param publicationStatus The new publication status
    */

    public void setPublicationStatus(PublicationStatus publicationStatus){
        this.publicationStatus = publicationStatus;
    }

}
