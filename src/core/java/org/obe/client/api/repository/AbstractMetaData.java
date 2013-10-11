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

package org.obe.client.api.repository;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract base class for meta-data implementations.
 *
 * @author Adrian Price
 */
public abstract class AbstractMetaData implements Serializable {
    static final long serialVersionUID = 3738728655687826825L;
    private static final Map _primitiveTypes = new HashMap();
    protected static final String BOOLEAN_TYPE = "*B";
    protected static final String BYTE_TYPE = "*Y";
    protected static final String CHAR_TYPE = "*C";
    protected static final String DOUBLE_TYPE = "*D";
    protected static final String FLOAT_TYPE = "*F";
    protected static final String INT_TYPE = "*I";
    protected static final String LONG_TYPE = "*L";
    protected static final String SHORT_TYPE = "*S";
    protected static final String VOID_TYPE = "*V";
    // The name of the implementing class.
    protected String _implClass;
    // Constructor argument signature for the implementing class.
    protected String[] _implCtorSig;
    protected String _id;
    protected String _displayName;
    protected String _description;
    protected String _docUrl;
    protected String _author;

    static {
        _primitiveTypes.put(BOOLEAN_TYPE, Boolean.TYPE);
        _primitiveTypes.put(BYTE_TYPE, Byte.TYPE);
        _primitiveTypes.put(CHAR_TYPE, Character.TYPE);
        _primitiveTypes.put(DOUBLE_TYPE, Double.TYPE);
        _primitiveTypes.put(FLOAT_TYPE, Float.TYPE);
        _primitiveTypes.put(INT_TYPE, Integer.TYPE);
        _primitiveTypes.put(LONG_TYPE, Long.TYPE);
        _primitiveTypes.put(SHORT_TYPE, Short.TYPE);
        _primitiveTypes.put(VOID_TYPE, Void.TYPE);
    }

    public static Class[] classesForNames(String[] classNames)
        throws ClassNotFoundException {

        int length = classNames == null ? 0 : classNames.length;
        Class[] classes = new Class[length];
        for (int i = 0; i < length; i++) {
            String argClassName = classNames[i];
            if (argClassName.charAt(0) == '*')
                classes[i] = (Class)_primitiveTypes.get(argClassName);
            else
                classes[i] = Class.forName(argClassName);
        }
        return classes;
    }

    // No-args ctor must be public to keep Castor happy.
    public AbstractMetaData() {
    }

    protected AbstractMetaData(String implClass, String[] implCtorSig,
        String id, String displayName, String description, String docUrl,
        String author) {

        _implClass = implClass;
        _implCtorSig = implCtorSig;
        _id = id;
        _displayName = displayName;
        _description = description;
        _docUrl = docUrl;
        _author = author;
    }

    protected final Object createImplementation(Object[] ctorArgs)
        throws RepositoryException {

        try {
            // This is all done by reflection to eliminate dependencies of
            // client api classes upon engine classes.  This method will fail
            // if called in a client environment.
            Class clazz = Class.forName(_implClass);
            Class[] implCtorSig = classesForNames(_implCtorSig);
            Constructor ctor = clazz.getConstructor(implCtorSig);
            return ctor.newInstance(ctorArgs);
        } catch (Exception e) {
            throw new RepositoryException(e);
        }
    }

    public abstract Object createImplementation() throws RepositoryException;

    public final String getImplClass() {
        return _implClass;
    }

    public void setImplClass(String implClass) {
        _implClass = implClass;
    }

    public final String[] getImplCtorSig() {
        return _implCtorSig;
    }

    public void setImplCtorSig(String[] implCtorSig) {
        _implCtorSig = implCtorSig;
    }

    public final String getId() {
        return _id;
    }

    public final void setId(String id) {
        _id = id;
    }

    public final String getDisplayName() {
        return _displayName;
    }

    public final void setDisplayName(String displayName) {
        _displayName = displayName;
    }

    public final String getDescription() {
        return _description;
    }

    public final void setDescription(String description) {
        _description = description;
    }

    public final String getDocUrl() {
        return _docUrl;
    }

    public final void setDocUrl(String docUrl) {
        _docUrl = docUrl;
    }

    public final String getAuthor() {
        return _author;
    }

    public final void setAuthor(String author) {
        _author = author;
    }

    public String toString() {
        String className = getClass().getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        return className +
            "[id='" + _id +
            "', implClass='" + _implClass +
            "', implCtorSig=" +
            (_implCtorSig == null
            ? null
            : "length:" + _implCtorSig.length + Arrays.asList(_implCtorSig)) +
            ", displayName='" + _displayName +
            "', description='" + _description +
            "', docUrl='" + _docUrl +
            "', author='" + _author +
            "']";
    }
}