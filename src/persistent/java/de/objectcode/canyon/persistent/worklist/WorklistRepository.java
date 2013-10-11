package de.objectcode.canyon.persistent.worklist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.WorkItemData;
import de.objectcode.canyon.persistent.filter.CountQueryBuilder;
import de.objectcode.canyon.persistent.filter.QueryBuilder;
import de.objectcode.canyon.persistent.instance.PWorkItem;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.worklist.IActivityInfo;
import de.objectcode.canyon.worklist.spi.worklist.IApplicationData;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;
import de.objectcode.canyon.worklist.spi.worklist.IWorklistRepository;

/**
 * @author    junglas
 * @created   2. Dezember 2003
 */
public class WorklistRepository implements IWorklistRepository
{
  private final static  Log             log        = LogFactory.getLog( WorklistRepository.class );

  private               SessionFactory  m_factory;
  
  /**
   *Constructor for the WorklistRepository object
   *
   * @param factory  Description of the Parameter
   */
  public WorklistRepository( SessionFactory factory )
  {
    m_factory = factory;
  }


  /**
   * Description of the Method
   *
   * @param filter                   Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public int countWorkItems( IFilter filter )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "countWorkItems: " + filter );
    }

    try {
      Session       session  = ( Session ) TransactionLocal.get();

      QueryBuilder  builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query         query    = builder.createQuery( session, "select count(*) from o in class " + PWorkItem.class.getName(), null );

      return ( ( Integer ) query.iterate().next() ).intValue();
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Creates a work item.
   *
   * @param activityInstanceId    The activity instance id.
   * @param state                 The work item state, one of the integer values defined in
   * {@link org.wfmc.wapi.WMWorkItemState}.
   * @param participant           The name of the participant to whom the work item is
   * assigned.
   * @param performer             Description of the Parameter
   * @param applicationData       Description of the Parameter
   * @return                      The new work item.
   * @throws RepositoryException
   */
  public IWorkItem createWorkItem( String engineId, String clientId, IActivityInfo activityInfo, int state,
      String performer, String participant, IApplicationData[] applicationData )
    throws RepositoryException
  {
    try {
      Session            session           = ( Session ) TransactionLocal.get();

      PWorkItem          workItem          = new PWorkItem( engineId, clientId, activityInfo, state, performer, participant, applicationData );

      session.save( workItem );
      session.flush();

      return workItem;
    }
    catch ( HibernateException e ) {
    	String context = "engineId="+engineId+", clientId=" + clientId+ ", processName ="+(activityInfo!=null?activityInfo.getProcessName():"?")+", state="+state+", performer="+performer+", participant="+participant+", applicationData="+applicationData;
      log.error( "Exception [" + context + "]", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param workItemId               Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem findWorkItem( String workItemId )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "findWorkItem: " + workItemId );
    }

    try {
      Session  session  = ( Session ) TransactionLocal.get();

      return ( PWorkItem ) session.load( PWorkItem.class, new Long( workItemId ) );
    }
    catch ( NumberFormatException e ) {
      throw new ObjectNotFoundException( workItemId );
    }
    catch ( net.sf.hibernate.ObjectNotFoundException e ) {
      throw new ObjectNotFoundException( workItemId );
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }

/**
   * Reads a set of work items.
   *
   * @param filter                   A Filter specification.
   * @return                         An array of matching work items.
   * @exception RepositoryException  Description of the Exception
 */
  public IWorkItem[] findWorkItems(IFilter filter) throws RepositoryException {
    return findWorkItems(filter, Integer.MAX_VALUE);
  }
  
    /**
   * Reads a set of work items.
   *
   * @param filter                   A Filter specification.
   * @param maxResultSize            Maximum number of WorkItems
   * @return                         An array of matching work items.
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem[] findWorkItems( IFilter filter, int maxResultSize )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "findWorkItems: " + filter );
    }

    try {
      Session       session  = ( Session ) TransactionLocal.get();

      QueryBuilder  builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query         query    = builder.createQuery( session, "from o in class " + PWorkItem.class.getName(), "o.entityOid asc" );

      if (maxResultSize!=Integer.MAX_VALUE)
	query.setMaxResults(maxResultSize);
      
      List          result   = query.list();
      IWorkItem     ret[]      = new IWorkItem[result.size()];
      Iterator      it       = result.iterator();
      int           i;

      for ( i = 0; it.hasNext(); i++ ) {
        PWorkItem  workItem  = ( PWorkItem ) it.next();
        ret[i] = workItem;
      }

      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   * Description of the Method
   *
   * @param activityInstanceId       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem[] findWorkItems( String processInstanceId, String activityInstanceId )
    throws RepositoryException
  {
    if ( log.isDebugEnabled() ) {
      log.debug( "findWorkItems: " + activityInstanceId );
    }

    try {
      Session    session  = ( Session ) TransactionLocal.get();

      Query      query    = session.createQuery( "from o in class " + PWorkItem.class.getName() + " where o.processInstanceId = :processInstanceId and o.activityInstanceId = :activityInstanceId order by o.entityOid asc" );

      query.setString( "processInstanceId", processInstanceId );
      query.setString( "activityInstanceId", activityInstanceId );

      List       result   = query.list();
      IWorkItem  ret[]      = new IWorkItem[result.size()];
      Iterator   it       = result.iterator();
      int        i;

      for ( i = 0; it.hasNext(); i++ ) {
        PWorkItem  workItem  = ( PWorkItem ) it.next();
        ret[i] = workItem;
      }

      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }

  /**
   * Description of the Method
   *
   * @param activityInstanceId       Description of the Parameter
   * @return                         Description of the Return Value
   * @exception RepositoryException  Description of the Exception
   */
  public IWorkItem[] findWorkItemsByProcessInstanceId( String processInstanceId )
	throws RepositoryException
  {
	if ( log.isDebugEnabled() ) {
	  log.debug( "findWorkItemsByProcessInstanceId: " + processInstanceId );
	}

	try {
	  Session    session  = ( Session ) TransactionLocal.get();

	  Query      query    = session.createQuery( "from o in class " + PWorkItem.class.getName() + " where o.processInstanceId = :processInstanceId order by o.entityOid asc" );

	  query.setString( "processInstanceId", processInstanceId);

	  List       result   = query.list();
	  IWorkItem  ret[]      = new IWorkItem[result.size()];
	  Iterator   it       = result.iterator();
	  int        i;

	  for ( i = 0; it.hasNext(); i++ ) {
		PWorkItem  workItem  = ( PWorkItem ) it.next();
		ret[i] = workItem;
	  }

	  return ret;
	}
	catch ( HibernateException e ) {
	  log.error( "Exception", e );
	  throw new RepositoryException( e );
	}
  }


  /**
   *  (non-Javadoc)
   *
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IInstanceRepository#beginTransaction()
   */
  public void beginTransaction()
    throws RepositoryException
  {
    try {
      if ( TransactionLocal.get() == null ) {
        TransactionLocal.set( m_factory.openSession() );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


  /**
   *  (non-Javadoc)
   *
   * @param flush                    Description of the Parameter
   * @exception RepositoryException  Description of the Exception
   * @see                            de.objectcode.canyon.spi.instance.IInstanceRepository#endTransaction()
   */
  public void endTransaction( boolean flush )
    throws RepositoryException
  {
    try {
      Session  session  = ( Session ) TransactionLocal.get();

      if ( session != null ) {
        if ( flush ) {
          session.flush();
        }
        session.close();
        TransactionLocal.set( null );
      }
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
  }


	public IWorkItem[] findWorkItems(IFilter filter, int offset, int length, String[] sortAttrs, boolean[] sortAscending) throws RepositoryException {
		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("findWorkItems: ");

			buffer.append(offset).append(" ").append(length);

			if (filter != null) {
				buffer.append(" ").append(filter);
			}

			if (sortAttrs == null || sortAttrs.length == 0) {
				buffer.append(" no sort");
			} else {
				int i;

				buffer.append(" [");
				for (i = 0; i < sortAttrs.length; i++) {
					if (i > 0) {
						buffer.append(", ");
					}
					buffer.append(sortAttrs[i]).append(" ").append(
							sortAscending[i] ? "asc" : "desc");
				}
				buffer.append("]");
			}
			log.debug(buffer.toString());
		}
    try {
      Session       session  = ( Session ) TransactionLocal.get();

      StringBuffer sortExpr = new StringBuffer(" order by ");
      
      if ( sortAttrs == null || sortAttrs.length == 0 ) {
        sortExpr.append("o.entityOid asc");
      } else {
        for ( int i = 0; i < sortAttrs.length; i++ ) {
          if ( i > 0 ) {
            sortExpr.append(", ");
          }
          sortExpr.append("o.").append(sortAttrs[i]);
          if ( sortAscending[i] ) {
            sortExpr.append(" asc");
          } else {
            sortExpr.append(" desc");
          }
        }
      }
      
      if ( log.isDebugEnabled() ) {
        log.debug ( "SortExpr: " + sortExpr.toString() );
      }

      QueryBuilder  builder  = new QueryBuilder();

      if ( filter != null ) {
        filter.toBuilder( builder );
      }

      Query         query    = builder.createQuery( session, "from o in class " + PWorkItem.class.getName(), sortExpr.toString() );

      query.setFirstResult(offset);
      if (length!=Integer.MAX_VALUE)
      	query.setMaxResults(length);
      
      List          result   = query.list();
      IWorkItem     ret[]      = new IWorkItem[result.size()];
      Iterator      it       = result.iterator();
      int           i;

      for ( i = 0; it.hasNext(); i++ ) {
        PWorkItem  workItem  = ( PWorkItem ) it.next();
        ret[i] = workItem;
      }

      return ret;
    }
    catch ( HibernateException e ) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
    }
	}
	
	public int indexOf(WorkItemData workItemData, IFilter filter, String[] sortAttrs, boolean[] sortAscending ) throws RepositoryException {
		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("indexOf: ");

			if (filter != null) {
				buffer.append(" ").append(filter);
			}

			if (sortAttrs == null || sortAttrs.length == 0) {
				buffer.append(" no sort");
			} else {
				int i;

				buffer.append(" [");
				for (i = 0; i < sortAttrs.length; i++) {
					if (i > 0) {
						buffer.append(", ");
					}
					buffer.append(sortAttrs[i]).append(" ").append(
							sortAscending[i] ? "asc" : "desc");
				}
				buffer.append("]");
			}
			log.debug(buffer.toString());
		}
		
		if(workItemData == null) return -1;
		
		
		

    try {
  		Session       session  = ( Session ) TransactionLocal.get();
  		final CountQueryBuilder builder = new CountQueryBuilder();
  		
  		if ( filter != null ) {
        filter.toBuilder( builder );
      }

  		final Long oid = new Long(workItemData.getId());
  		
  		final Map mappings = new HashMap();
  		mappings.put("entityOid", workItemData.getId());
  		mappings.put("activityDefinitionId", workItemData.getActivityId());   
  		mappings.put("activityInstanceId", workItemData.getActivityId());
  		mappings.put("clientId", workItemData.getClientId());
  		mappings.put("name", workItemData.getName());
  		mappings.put("participant", workItemData.getParticipant());
  		mappings.put("performer", workItemData.getPerformer());
  		mappings.put("priority", Integer.toString(workItemData.getPriority()));
  		mappings.put("processDefinitionId", Long.toString(workItemData.getProcessEntityOid()));
  		mappings.put("processDefinitionVersion", workItemData.getProcessVersion());
  		mappings.put("processInstanceId", workItemData.getProcessInstanceId());
  		mappings.put("processInstanceIdPath", workItemData.getProcessInstanceIdPath());
  		mappings.put("processName", workItemData.getProcessId());
  		mappings.put("state", workItemData.getState().intValue());
  		mappings.put("completedDate", workItemData.getCompleteDate());
  		mappings.put("createdDate", workItemData.getCreatedDate());
  		mappings.put("dueDate", workItemData.getDueDate());
  		mappings.put("startedDate", workItemData.getStartedDate());

			Query         query    = builder.createQuery( session, oid, sortAttrs, sortAscending, PWorkItem.class,  "entityOid", mappings);
  		
  		log.debug("indexOf  : @memo query.getQueryString() = " + query.getQueryString());
  		
  		return ( ( Integer ) query.iterate().next() ).intValue();
		} catch (HibernateException e) {
      log.error( "Exception", e );
      throw new RepositoryException( e );
		}
	}
}
