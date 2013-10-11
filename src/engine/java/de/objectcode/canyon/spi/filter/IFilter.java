package de.objectcode.canyon.spi.filter;

import java.io.Serializable;

/**
 * @author    junglas
 * @created   25.03.2003
 * @version   $Id: IFilter.java,v 1.1 2003/10/17 09:07:30 junglas Exp $
 */
public interface IFilter extends Serializable
{
	public void toBuilder ( IFilterBuilder filterBuilder );
}
