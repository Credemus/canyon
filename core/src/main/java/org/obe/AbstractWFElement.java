/*
 *  Copyright (C) 2002-2003 Aetrion LLC.
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions, and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions, and the disclaimer that follows
 *  these conditions in the documentation and/or other materials
 *  provided with the distribution.
 *
 *  3. The names "OBE" and "Open Business Engine" must not be used to
 *  endorse or promote products derived from this software without prior
 *  written permission.  For written permission, please contact
 *  obe@aetrion.com.
 *
 *  4. Products derived from this software may not be called "OBE" or
 *  "Open Business Engine", nor may "OBE" or "Open Business Engine"
 *  appear in their name, without prior written permission from
 *  Aetrion LLC (obe@aetrion.com).
 *
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
 *
 *  For more information on OBE, please see
 *  <http://www.openbusinessengine.org/>.
 */
package org.obe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of the WFElement interface which implements base
 * functionality used by all WFElement implementations. This class is not
 * threadsafe.
 *
 * @author Anthony Eden
 */
public abstract class AbstractWFElement implements WFElement, Serializable {
    final static long serialVersionUID = 5028805438136004652L;

    private String _id;
    private String _name;
    private String _description;
    private Map<String, Object> _extendedAttributes;

    /**
     * Constructs a new AbstractWFElement.
     *
     * @param id    The element ID
     * @param name  The element name
     */
    protected AbstractWFElement(String id, String name) {
        setId(id);
        setName(name);
    }

    /**
     * Gets the workflow element's ID.
     *
     * @return   The ID
     */
    public String getId() {
        return _id;
    }

    /**
     * Sets the element's unique ID.
     *
     * @param id  The new unique ID which cannot be null
     */
    public void setId(String id) {
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        _id = id;
    }

    /**
     * Gets the workflow element's name.
     *
     * @return   The name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the workflow element's name.
     *
     * @param name  The new name
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Gets the workflow element's description.
     *
     * @return   The description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Sets the workflow element's description.
     *
     * @param description  The new description
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * Gets a Map of extended attributes.
     *
     * @return   A Map of extended attributes
     */
    public Map<String, Object> getExtendedAttributes() {
        if (_extendedAttributes == null)
            _extendedAttributes = new HashMap();
        return _extendedAttributes;
    }

    /**
     * Returns the hash code.
     *
     * @return   The hash code
     */
    public int hashCode() {
        return _id.hashCode();
    }

    /**
     * Returns true if the specified object is equal to this object.
     *
     * @param obj  The object to compare
     * @return     True if the objects are equal
     */
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() &&
            _id.equals(((AbstractWFElement)obj)._id);
    }
}