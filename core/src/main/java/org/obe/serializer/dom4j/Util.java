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

package org.obe.serializer.dom4j;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.dom4j.Element;
import org.dom4j.QName;
import org.obe.util.Duration;

/** Dom4J XPDL Serializer utility class.

    @author Anthony Eden
*/

class Util{
    
    /* ISO standard date format. */
    private static final DateFormat STANDARD_DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
    
    /** Noop constructor. */
    
    private Util(){
        // no op
    }
    
    /** Add a child element with the specific name to the given parent
        element and return the child element.  This method will use the 
        namespace of the parent element for the child element's namespace.
        
        @param parent The parent element
        @param name The new child element name
        @return The child element
    */
    
    static Element addElement(Element parent, String name){
        return parent.addElement(new QName(name, parent.getNamespace()));
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @return The child element
    */
    
    static Element addElement(Element parent, String name, Date value){
        return addElement(parent, name, value, null);
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.  If the given value is null then the default value is
        used.  If the value is null then this method will not add the child
        element and will return null.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @param defaultValue The default value (if the value is null)
        @return The child element
    */
    
    static Element addElement(Element parent, String name, Date value,
    Date defaultValue){
        Element child = null;
        
        if(value == null){
            value = defaultValue;
        }
        
        if(value != null){
            child = addElement(parent, name);
            child.addText(STANDARD_DF.format(value));
        }
        
        return child;
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @return The child element
    */
    
    static Element addElement(Element parent, String name, String value){
        return addElement(parent, name, value, null);
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.  If the given value is null then the default value is
        used.  If the value is null then this method will not add the child
        element and will return null.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @param defaultValue The default value (if the value is null)
        @return The child element
    */
    
    static Element addElement(Element parent, String name, String value, 
    String defaultValue){
        Element child = null;
        
        if(value == null){
            value = defaultValue;
        }
        
        if(value != null){
            child = addElement(parent, name);
            child.addText(value);
        }
        
        return child;
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @return The child element
    */
    
    static Element addElement(Element parent, String name, URL value){
        return addElement(parent, name, value, null);
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.  If the given value is null then the default value is
        used.  If the value is null then this method will not add the child
        element and will return null.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @param defaultValue The default value (if the value is null)
        @return The child element
    */
    
    static Element addElement(Element parent, String name, URL value,
    URL defaultValue){
        Element child = null;
        
        if(value == null){
            value = defaultValue;
        }
        
        if(value != null){
            child = addElement(parent, name);
            child.addText(value.toString());
        }
        
        return child;
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @return The child element
    */
    
    static Element addElement(Element parent, String name, Duration value){
        return addElement(parent, name, value, null);
    }
    
    /** Add a child element with the specific name and the given value to 
        the given parent element and return the child element.  This method 
        will use the namespace of the parent element for the child element's
        namespace.  If the given value is null then the default value is
        used.  If the value is null then this method will not add the child
        element and will return null.
        
        @param parent The parent element
        @param name The new child element name
        @param value The value
        @param defaultValue The default value (if the value is null)
        @return The child element
    */
    
    static Element addElement(Element parent, String name, Duration value,
    Duration defaultValue){
        Element child = null;
        
        if(value == null){
            value = defaultValue;
        }
        
        if(value != null){
            child = addElement(parent, name);
            child.addText(value.toString());
        }
        
        return child;
    }
    
}
