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

import java.util.ArrayList;
import java.util.List;

/**
 * A Package is the top-level element in an XPDL document.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public class Package extends AbstractWFElement {
    static final long serialVersionUID = -6038418003633747926L;

    private PackageHeader packageHeader;
    private RedefinableHeader redefinableHeader;
    private ConformanceClass conformanceClass;
    private Script script;
    private List externalPackages = new ArrayList();
    private List typeDeclarations = new ArrayList();
    private List participants = new ArrayList();
    private List applications = new ArrayList();
    private List procedures = new ArrayList();
    private List dataFields = new ArrayList();
    private List eventTypes = new ArrayList();
    private List workflowProcesses = new ArrayList();

    /** Construct a new Package.

     @param id The package ID
     @param name The package name
     @param packageHeader The PackageHeader object
     */

    public Package(String id, String name, PackageHeader packageHeader) {
        super(id, name);

        this.packageHeader = packageHeader;
    }

    /** Return the description specified in the package header.

     @return The package header description
     */

    public String getDescription() {
        return packageHeader.getDescription();
    }

    /** Set the description in the package header.

     @param description The description
     */

    public void setDescription(String description) {
        packageHeader.setDescription(description);
    }
    
    /** Get the PackageHeader.
    
        @return The PackageHeader
    */

    public PackageHeader getPackageHeader() {
        return packageHeader;
    }
    
    /** Set the PackageHeader.  This method will throw an 
        IllegalArgumentException if the argument is null.
        
        @param packageHeader The new package header
    */

    public void setPackageHeader(PackageHeader packageHeader) {
        if (packageHeader == null) {
            throw new IllegalArgumentException("Package header cannot be null");
        }

        this.packageHeader = packageHeader;
    }
    
    /** Get the RedefinableHeader.
    
        @return The RedefinableHeader
    */

    public RedefinableHeader getRedefinableHeader() {
        return redefinableHeader;
    }
    
    /** Set the RedefinableHeader.
    
        @param redefinableHeader The RedefinableHeader
    */

    public void setRedefinableHeader(RedefinableHeader redefinableHeader) {
        this.redefinableHeader = redefinableHeader;
    }
    
    /** Get the ConformanceClass.
    
        @return The ConformanceClass
    */

    public ConformanceClass getConformanceClass() {
        return conformanceClass;
    }
    
    /** Set the ConformanceClass.
    
        @param conformanceClass
    */

    public void setConformanceClass(ConformanceClass conformanceClass) {
        this.conformanceClass = conformanceClass;
    }

    /** Get an object defining the scripting language to use for expressions.
    
        @return The Script
    */
    
    public Script getScript() {
        return script;
    }
    
    /** Set the script language for expressions.
    
        @param script The new script language
    */

    public void setScript(Script script) {
        this.script = script;
    }

    public List getExternalPackages() {
        return externalPackages;
    }

    public List getTypeDeclarations() {
        return typeDeclarations;
    }

    public List getParticipants() {
        return participants;
    }

    public List getApplications() {
        return applications;
    }

    public List getEventTypes() {
        return eventTypes;
    }

    public List getProcedures() {
        return procedures;
    }

    public List getDataFields() {
        return dataFields;
    }

    public List getWorkflowProcesses() {
        return workflowProcesses;
    }

    public String toString() {
        return "Package[packageId='" + getId() + "', name='" + getName() + "']";
    }
}