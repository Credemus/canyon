/*--

 Copyright (C) 2002 Anthony Eden.
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
 	me@anthonyeden.com.

 4. Products derived from this software may not be called "OBE" or
 	"Open Business Engine", nor may "OBE" or "Open Business Engine"
 	appear in their name, without prior written permission from
 	Anthony Eden (me@anthonyeden.com).

 In addition, I request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by
      Anthony Eden (http://www.anthonyeden.com/)."

 THIS SOFTWARE IS PROVIdED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIdENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see <http://www.openbusinessengine.org/>.

 */

package org.wfmc.audit;

/**
 * Section 8.10 of the Interface 5 WFMC standards
 * @author Antony Lodge
 */
public class SessionManagementAuditData extends AuditBase {
    static final long serialVersionUID = -3124899135217676983L;

    private String messageId;
    private String correspondentDomainId;
    private String correspondentNodeId;

    /**
     *
     */
    public SessionManagementAuditData() {
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     */
    public SessionManagementAuditData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix) {

        super(cwadPrefix, cwadSuffix);
    }

    /**
     *
     * @param cwadPrefix
     * @param cwadSuffix
     * @param messageId
     * @param correspondentDomainId
     * @param correspondentNodeId
     */
    public SessionManagementAuditData(CWADPrefix cwadPrefix,
        CWADSuffix cwadSuffix, String messageId,
        String correspondentDomainId,
        String correspondentNodeId) {

        super(cwadPrefix, cwadSuffix);
        this.messageId = messageId;
        this.correspondentDomainId = correspondentDomainId;
        this.correspondentNodeId = correspondentNodeId;
    }

    /**
     *
     * @return Message Id associated with event
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     *
     * @param messageId Message Id associated with event
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     *
     * @return DomainId of accepting the session request
     */
    public String getCorrespondentDomainId() {
        return correspondentDomainId;
    }

    /**
     *
     * @param correspondentDomainId DomainId of accepting the session request
     */
    public void setCorrespondentDomainId(String correspondentDomainId) {
        this.correspondentDomainId = correspondentDomainId;
    }

    /**
     *
     * @return Node Id of accepting the session request
     */
    public String getCorrespondentNodeId() {
        return correspondentNodeId;
    }

    /**
     *
     * @param correspondentNodeId Node Id of accepting the session request
     */
    public void setCorrespondentNodeId(String correspondentNodeId) {
        this.correspondentNodeId = correspondentNodeId;
    }

    public String toString() {
        return "SessionManagementAuditData@" +
            System.identityHashCode(this) + "[" +
            " cwadPrefix=" + getCwadPrefix() +
            ", messageId=" + messageId +
            ", correspondentDomainId=" + correspondentDomainId +
            ", correspondentNodeId=" + correspondentNodeId +
            ", cwadSuffix=" + getCwadSuffix() +
            "]";
    }
}