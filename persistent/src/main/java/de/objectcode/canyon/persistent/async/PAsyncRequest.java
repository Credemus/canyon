package de.objectcode.canyon.persistent.async;

/**
 * @hibernate.class table="PASYNCREQUEST"
 * 
 * @author junglas
 */
public class PAsyncRequest
{
  private long   m_id;
  private Object m_request;

  public PAsyncRequest ( )
  {
  }

  public PAsyncRequest ( AsyncRequest request )
  {
    m_request = request;
  }

  /**
   * @param id
   *          The id to set.
   */
  public void setId ( long id )
  {
    m_id = id;
  }

  /**
   * @param request
   *          The request to set.
   */
  public void setRequest ( Object request )
  {
    m_request = request;
  }

  /**
   * @hibernate.id generator-class="native" column="ENTITYID" type="long" unsaved-value="0"
   * 
   * @return
   */
  public long getId ( )
  {
    return m_id;
  }

  /**
   * @hibernate.property column="REQUESTDATA" type="de.objectcode.canyon.persistent.util.SerializableType"
   * not-null="false"
   * 
   * @return Returns the request.
   */
  public Object getRequest ( )
  {
    return m_request;
  }
}
