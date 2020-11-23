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
package de.objectcode.canyon.spiImpl.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.spi.calendar.IBusinessCalendar;


/**
 * A basic business calendar implementation.
 *
 * @author    Adrian Price
 * @created   20. Oktober 2003
 */
public class DefaultBusinessCalendar implements IBusinessCalendar
{

  /**
   * Description of the Method
   *
   * @param base    Description of the Parameter
   * @param field   Description of the Parameter
   * @param amount  Description of the Parameter
   * @return        Description of the Return Value
   */
  public Date add( String client, Date base, int field, int amount )
  {
    GregorianCalendar  cal  = new GregorianCalendar();
    cal.setTime( base );
    cal.add( field, amount );
    return cal.getTime();
  }


  /**
   * Description of the Method
   *
   * @param base    Description of the Parameter
   * @param amount  Description of the Parameter
   * @param unit    Description of the Parameter
   * @return        Description of the Return Value
   */
  public Date add( String client, Date base, int amount, DurationUnit unit )
  {
    Date  result;
    if ( amount == 0 ) {
      result = base;
    } else {
      int  field;
      switch ( unit.getValue() ) {
        case DurationUnit.YEAR_INT:
          field = Calendar.YEAR;
          break;
        case DurationUnit.MONTH_INT:
          field = Calendar.MONTH;
          break;
        case DurationUnit.DAY_INT:
          field = Calendar.DATE;
          break;
        case DurationUnit.HOUR_INT:
          field = Calendar.HOUR;
          break;
        case DurationUnit.MINUTE_INT:
          field = Calendar.MINUTE;
          break;
        case DurationUnit.SECOND_INT:
          field = Calendar.SECOND;
          break;
        default:
          throw new IllegalArgumentException( unit.toString() );
      }
      result = add( client, base, field, amount );
    }
    return result;
  }
  
  public long add(String client, long base, Duration duration) {
    return base+duration.getMillis();
  }  
}
