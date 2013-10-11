package de.objectcode.canyon.web;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import de.objectcode.canyon.web.worklist.ProcessListForm;

/**
 * @struts.form name="testForm"
 * 
 * @author junglas
 */
public class TestForm extends ActionForm
{
  private final static  Log                log              = LogFactory.getLog( ProcessListForm.class );

  /**
   * Description of the Method
   *
   * @param mapping  Description of the Parameter
   * @param request  Description of the Parameter
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    try {
      InitialContext  ctx   = new InitialContext();

    }
    catch ( Exception e ) {
      log.error( "Exception", e );
    }
  }
}
