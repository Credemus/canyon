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

package org.obe.client.api;

import org.obe.Package;
import org.wfmc.wapi.WMFilter;
import org.wfmc.wapi.WMWorkflowException;
import org.wfmc.wapi2.WAPI2;

/**
 * WAPI2 extensions for process definition and instance management.  The
 * process repository functions provide for pluggable process definition
 * language support; at present only {@link #XPDL} is supported.
 * <p/>
 * The {@link WAPI} superinterface provides many methods that accept a
 * {@link WMFilter filter} object to restrict the scope of the operation.  The
 * filter refers to attributes of process instance, activity instance, and work
 * item as appropriate.  In OBE, these system attributes are described by
 * interfaces in the {@link org.obe.client.api.model} package.
 *
 * @author Adrian Price
 * @see ProcessInstanceAttributes
 * @see ActivityInstanceAttributes
 * @see WorkItemAttributes
 */
public interface WMClient extends WAPI2 {
    /** IBM/Microsoft/BEA's Business Process Execution Language for Web Services. */
    String BPEL4WS = "text/xml/bpel4ws";
    /** BPMI's Business Process Modeling Language. */
    String BPML = "text/xml/bpml";
    /** WfMC's Workflow Process Definition Language. */
    String WPDL = "text/wpdl";
    /** IBM's Web Services Flow Language. */
    String WSFL = "text/xml/wsfl";
    /** BPMI's Web Services Process Language. */
    String WSPL = "text/xml/wspl";
    /** Microsoft's BizTalk Process Definition Language. */
    String XLANG = "text/xml/xlang";
    /** WfMC's XML Process Definition Language. */
    String XPDL = "text/xml/xpdl";

    /**
     * Returns the protocol in use by this client instance.
     *
     * @return The client protocol.
     */
    String getProtocol();

    /**
     * Creates a package using the supplied content.
     * @param pkg Package definition.
     * @return The ID of the new package.
     * @throws WMWorkflowException
     */
    String createPackage(Package pkg) throws WMWorkflowException;

    /**
     * Updates the specified process definition package.
     * @param pkg The process definition package.
     * @throws WMWorkflowException
     */
    void updatePackage(Package pkg) throws WMWorkflowException;

    /**
     * Creates a package using the supplied content in a specified format.
     * @param content Package definition in specified format.
     * @param contentType The MIME content type of the package definition, in a
     * supported format.
     * @return The ID of the new package.
     * @throws WMWorkflowException
     */
    String createPackage(String content, String contentType)
        throws WMWorkflowException;

    /**
     * Retrieves an XPDL package.
     *
     * @param packageId The package ID.
     * @return The XPDL package.
     * @throws WMWorkflowException
     */
    Package getPackage(String packageId) throws WMWorkflowException;

    /**
     * Retrieves the content of the a package in the specified format.
     * @param packageId The package ID.
     * @param contentType The MIME content type of the package definition, must
     * be a supported format.
     * @return Package definition in XPDL format.
     * @throws WMWorkflowException
     */
    String getPackageContent(String packageId, String contentType)
        throws WMWorkflowException;

    /**
     * Sets the content of the specified package.
     * @param packageId The ID of the package to update.
     * @param content Package definition in a supported format.
     * @param contentType The MIME content type of the package definition, must
     * be a supported format.
     * @return The package ID.
     * @throws WMWorkflowException
     */
    String setPackageContent(String packageId, String content,
        String contentType) throws WMWorkflowException;

    /**
     * Permanently deletes the specified process definition.
     * @param packageId The process definition ID.
     * @throws WMWorkflowException
     */
    void deletePackage(String packageId) throws WMWorkflowException;

    /**
     * Deletes a process instance from persistent storage.
     * @param processInstanceId The ID of the process instance to delete.
     * @throws WMWorkflowException
     */
    void deleteProcessInstance(String processInstanceId)
        throws WMWorkflowException;

    /**
     * Deletes a process instance from persistent storage.
     * @param processDefinitionId The ID of the process definition for which
     * to delete instances.
     * @param filter A filter specification; can be <code>null</code>.
     * @throws WMWorkflowException
     */
    void deleteProcessInstances(String processDefinitionId, WMFilter filter)
        throws WMWorkflowException;
}