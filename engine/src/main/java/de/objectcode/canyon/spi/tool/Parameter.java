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
package de.objectcode.canyon.spi.tool;

import java.io.Serializable;

import de.objectcode.canyon.model.data.DataType;
import de.objectcode.canyon.model.data.ParameterMode;

/**
 * Description of the Class
 *
 * @author junglas
 * @created 22. Oktober 2003
 */
public final class Parameter implements Serializable {
  static final long serialVersionUID = 2454945467359500162L;

  public final String formalName;
  public final String actualName;
  public final DataType dataType;
  public final ParameterMode mode;
  public final Object value;
  public final String description;


  /**
   * Constructor for the Parameter object
   *
   * @param formalName Description of the Parameter
   * @param actualName Description of the Parameter
   * @param dataType   Description of the Parameter
   * @param mode       Description of the Parameter
   * @param value      Description of the Parameter
   */
  public Parameter(String formalName, String actualName, DataType dataType,
                   ParameterMode mode, String description, Object value) {

    if (formalName == null) {
      throw new IllegalArgumentException(
              "Formal parameter name cannot be null");
    }
    this.formalName = formalName;
    this.actualName = actualName;
    this.dataType = dataType;
    this.mode = mode;
    this.value = value;
    this.description = description;
  }


  public String getName() {
    return formalName;
  }

  public Object getValue() {
    return value;
  }

  public String toString() {
    StringBuilder buffer = new StringBuilder("Parameter[");
    buffer.append("formalName=").append(formalName);
    buffer.append(", actualName=").append(actualName);
    buffer.append(", dataType=").append(dataType);
    buffer.append(", mode=").append(mode);
    buffer.append(", description=").append(description);
    buffer.append(", value=").append(value);

    return buffer.toString();
  }
}
