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

import java.io.Serializable;

/**
 * Object representation of an WfMC audit suffix.
 *
 * @author Antony Lodge
 */
public class CWADSuffix implements Serializable {
    static final long serialVersionUID = 1330556381894815741L;

    private byte accountCode;
    private short extensionNumber;
    private byte extensionType;
    private short extensionLength;
    private short extensionCodePage;
    private Object extensionContent;

    public CWADSuffix() {
    }

    /**
     *
     * @param accountCode
     * @param extensionNumber
     * @param extensionType
     * @param extensionLength
     * @param extensionCodePage
     * @param extensionContent
     */
    public CWADSuffix(byte accountCode, short extensionNumber,
        byte extensionType, short extensionLength, short extensionCodePage,
        Object extensionContent) {

        this.accountCode = accountCode;
        this.extensionNumber = extensionNumber;
        this.extensionType = extensionType;
        this.extensionLength = extensionLength;
        this.extensionCodePage = extensionCodePage;
        this.extensionContent = extensionContent;
    }

    /**
     *
     * @return  Accounting Code used for item of work
     */
    public byte getAccountCode() {
        return accountCode;
    }

    /**
     *
     * @param accountCode  Accounting Code used for item of work
     */
    public void setAccountCode(byte accountCode) {
        this.accountCode = accountCode;
    }

    /**
     *
     * @return  Number of extensions in suffix information
     */
    public short getExtensionNumber() {
        return extensionNumber;
    }

    /**
     *
     * @param extensionNumber  Number of extensions in suffix information
     */
    public void setExtensionNumber(short extensionNumber) {
        this.extensionNumber = extensionNumber;
    }

    /**
     *
     * @return  Type of extension
     */
    public byte getExtensionType() {
        return extensionType;
    }

    /**
     *
     * @param extensionType  Type of extension
     */
    public void setExtensionType(byte extensionType) {
        this.extensionType = extensionType;
    }

    /**
     *
     * @return  Total length of extension values
     */
    public short getExtensionLength() {
        return extensionLength;
    }

    /**
     *
     * @param extensionLength   Total length of extension values
     */
    public void setExtensionLength(short extensionLength) {
        this.extensionLength = extensionLength;
    }

    /**
     *
     * @return
     */
    public short getExtensionCodePage() {
        return extensionCodePage;
    }

    /**
     *
     * @param extensionCodePage
     */
    public void setExtensionCodePage(short extensionCodePage) {
        this.extensionCodePage = extensionCodePage;
    }

    /**
     *
     * @return  Content, defined by Extension Type and Length
     */
    public Object getExtensionContent() {
        return extensionContent;
    }

    /**
     *
     * @param extensionContent Content, defined by Extension Type and Length
     */
    public void setExtensionContent(Object extensionContent) {
        this.extensionContent = extensionContent;
    }

    public String toString() {
        return "CWADSuffix@" + System.identityHashCode(this) + "[" +
            " accountCode=" + accountCode +
            ", extensionNumber=" + extensionNumber +
            ", extensionType=" + extensionType +
            ", extensionLength=" + extensionLength +
            ", extensionCodePage=" + extensionCodePage +
            ", extensionContent=" + extensionContent +
            "]";
    }
}