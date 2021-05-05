package de.objectcode.canyon.persistent.instance;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.persistent.filter.QueryBuilder;
import de.objectcode.canyon.persistent.process.PProcessDefinitionID;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;
import de.objectcode.canyon.spi.TransactionLocal;
import de.objectcode.canyon.spi.filter.CompareFilter;
import de.objectcode.canyon.spi.filter.IFilter;
import de.objectcode.canyon.spi.instance.IActivityInstance;
import de.objectcode.canyon.spi.instance.IInstanceRepository;
import de.objectcode.canyon.spi.instance.IProcessInstance;
import de.objectcode.canyon.worklist.spi.worklist.IWorkItem;

/**
 * @author junglas
 * @created 20. Oktober 2003
 */
public class InstanceRepository implements IInstanceRepository {
  private final static Log log = LogFactory.getLog(InstanceRepository.class);

  private SessionFactory m_factory;


  /**
   * Constructor for the InstanceRepository object
   *
   * @param sessionFactory Description of the Parameter
   */
  public InstanceRepository(SessionFactory sessionFactory) {
    m_factory = sessionFactory;
  }


  /**
   * Creates a new process instance for the given process definition.
   *
   * @param processDefinitionId      The process definition ID.
   * @param parentActivityInstanceId The ID of the parent activity instance,
   *                                 if any.
   * @param processInstanceName      The name of the process instance.
   * @param priority                 Process instance priority.
   * @param state                    The instance state, one of the integer values defined in
   *                                 {@link org.wfmc.wapi.WMProcessInstanceState}.
   * @param participants             The list of process participants.
   * @param processDefinitionVersion Description of the Parameter
   * @return The new process instance.
   * @throws RepositoryException Workflow client exception.
   */
  public IProcessInstance createProcessInstance(String processDefinitionId, String processDefinitionVersion,
                                                String parentActivityInstanceId, String processInstanceName,
                                                int priority, int state, String[] participants)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("createProcessInstance: " + processDefinitionId + " " + processDefinitionVersion + " " +
              parentActivityInstanceId + " " + processInstanceName + " " + priority + " " + state);
    }
    try {
      Session session = (Session) TransactionLocal.get();

      PProcessInstance processInstance;
      if (parentActivityInstanceId != null) {
        PActivityInstance activityInstance = (PActivityInstance) session.load(PActivityInstance.class, new Long(parentActivityInstanceId));

        processInstance = new PProcessInstance(new PProcessDefinitionID(processDefinitionId, processDefinitionVersion), processInstanceName, priority,
                state, new Date(), null, participants);

        processInstance.setParentActivityInstance(activityInstance);

        session.save(processInstance);
      } else {
        processInstance = new PProcessInstance(new PProcessDefinitionID(processDefinitionId, processDefinitionVersion), processInstanceName, priority,
                state, new Date(), null, participants);

        session.save(processInstance);
      }

      session.flush();

      return processInstance;
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(parentActivityInstanceId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(parentActivityInstanceId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }

  /**
   * Get the specified process instance.
   *
   * @param processInstanceId The process instance id
   * @return The process instance
   * @throws RepositoryException Workflow client exception
   */
  public IProcessInstance findProcessInstance(String processInstanceId)
          throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      return (PProcessInstance) session.load(PProcessInstance.class, new Long(processInstanceId));
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(processInstanceId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(processInstanceId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Description of the Method
   *
   * @param activityInstanceId Description of the Parameter
   * @return Description of the Return Value
   * @throws RepositoryException Description of the Exception
   */
  public IProcessInstance[] findChildProcessInstances(String activityInstanceId)
          throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      Query query = session.createQuery("from o in class " + PProcessInstance.class.getName() + " where o.parentActivityInstance = :activityInstance");

      query.setLong("activityInstance", Long.parseLong(activityInstanceId));

      List result = query.list();
      IProcessInstance ret[] = new IProcessInstance[result.size()];
      Iterator it = result.iterator();
      int i;

      for (i = 0; it.hasNext(); i++) {
        PProcessInstance processInstance = (PProcessInstance) it.next();
        ret[i] = processInstance;
      }

      return ret;
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Description of the Method
   *
   * @param processDefinitionId Description of the Parameter
   * @param filter              Description of the Parameter
   * @return Description of the Return Value
   * @throws RepositoryException Description of the Exception
   */
  public int countProcessInstances(String processDefinitionId, IFilter filter)
          throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      QueryBuilder builder = new QueryBuilder();

      if (filter != null) {
        filter.toBuilder(builder);
      }
      builder.compareExpr("processDefinitionId", CompareFilter.EQ, processDefinitionId);
      builder.andExpr();

      Query query = builder.createQuery(session, "select count(*) from o in class " + PProcessInstance.class.getName(), null);

      return (Integer) query.iterate().next();
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Retrieve a list of process instances.
   *
   * @param processDefinitionId The process definition Id, can be
   *                            <code>null>/code> to select from instances of any process definition.
   * @param filter              A Filter specification.
   * @return An array of matching process instances.
   * @throws RepositoryException Description of the Exception
   */
  public IProcessInstance[] findProcessInstances(String processDefinitionId,
                                                 IFilter filter)
          throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      QueryBuilder builder = new QueryBuilder();

      if (filter != null) {
        filter.toBuilder(builder);
      }

      if (processDefinitionId != null) {
        builder.compareExpr("processDefinitionId.id", CompareFilter.EQ, processDefinitionId);
        builder.andExpr();
      }

      Query query = builder.createQuery(session, "from o in class " + PProcessInstance.class.getName(), "o.entityOid asc");

      List result = query.list();
      IProcessInstance ret[] = new IProcessInstance[result.size()];
      Iterator it = result.iterator();
      int i;

      for (i = 0; it.hasNext(); i++) {
        PProcessInstance processInstance = (PProcessInstance) it.next();
        ret[i] = processInstance;
      }

      return ret;
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Permanently deletes the specified process instance.  Implementations
   * must cascade this delete to include all related ActivityInstance,
   * WorkItem and AttributeInstance entities.
   *
   * @param processInstanceId The ID of the process instance to delete.
   * @throws RepositoryException
   */
  public void deleteProcessInstance(String processInstanceId)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("deleteProcessInstance: " + processInstanceId);
    }
    try {
      Session session = (Session) TransactionLocal.get();

      PProcessInstance processInstance = (PProcessInstance) session.load(PProcessInstance.class, new Long(processInstanceId));

      Query query = session.createQuery("from o in class " + PActivityInstance.class.getName() + " where o.processInstance = :processInstance");

      query.setLong("processInstance", Long.parseLong(processInstance.getProcessInstanceId()));

      List activities = query.list();
      Iterator activitiesIt = activities.iterator();

      while (activitiesIt.hasNext()) {
        IActivityInstance activity = (IActivityInstance) activitiesIt.next();

        query = session.createQuery("from o in class " + PWorkItem.class.getName() + " where o.activityInfo = :activityInstance");

        query.setLong("activityInstance", Long.parseLong(activity.getActivityInstanceId()));

        List workItems = query.list();
        Iterator workItemIt = workItems.iterator();

        while (workItemIt.hasNext()) {
          IWorkItem workItem = (IWorkItem) workItemIt.next();

          session.delete(workItem);
        }
        session.flush();

        session.delete(activity);
      }
      session.flush();

      session.delete(processInstance);
      session.flush();
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(processInstanceId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(processInstanceId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Returns the specified activity instance.
   *
   * @param processDefinitionId     The process definition id.
   * @param processInstanceId       The process instance id.
   * @param activityDefinitionId    The activity definition id.
   * @param activityName            The name of the activity.
   * @param blockActivityInstanceId The block activity instance ID, or
   *                                <code>null</code> if the activity is not defined within an activity set.
   * @param priority                Activity priority.
   * @param state                   Activity state, one of the integer values defined in
   *                                {@link org.wfmc.wapi.WMActivityInstanceState}.
   * @param participants            The list of participants for this activity.
   * @param joinType                Description of the Parameter
   * @param transitionIds           Description of the Parameter
   * @param completionStrategy      Description of the Parameter
   * @return The new activity instance.
   * @throws RepositoryException Workflow client exception.
   */
  public IActivityInstance createActivityInstance(String processDefinitionId,
                                                  String processInstanceId, String activityDefinitionId,
                                                  String activityName, String blockActivityInstanceId,
                                                  int joinType, String[] transitionIds,
                                                  int priority, int state,
                                                  String[] participants, int completionStrategy)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("createActivityInstance: " + processDefinitionId + " " + processInstanceId + " " +
              activityDefinitionId + " " + activityName + " " + blockActivityInstanceId + " " + completionStrategy);
    }
    try {
      Session session = (Session) TransactionLocal.get();

      PProcessInstance procInst = (PProcessInstance) session.load(PProcessInstance.class, new Long(processInstanceId));

      PActivityInstance activityInstance = new PActivityInstance(procInst,
              activityDefinitionId, activityName, blockActivityInstanceId,
              joinType, transitionIds,
              priority, state, participants, completionStrategy);

      session.save(activityInstance);
      session.flush();

      return activityInstance;
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(processInstanceId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(processInstanceId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }

  /**
   * Reads the specified activity instance.
   *
   * @param activityInstanceId The activity instance id.
   * @return The activity instance.
   * @throws RepositoryException Workflow client exception.
   */
  public IActivityInstance findActivityInstance(String activityInstanceId)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("findActivityInstance: '" + activityInstanceId + "'");
    }
    try {
      Session session = (Session) TransactionLocal.get();

      return (PActivityInstance) session.load(PActivityInstance.class, new Long(activityInstanceId));
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(activityInstanceId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(activityInstanceId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Description of the Method
   *
   * @param processInstanceId Description of the Parameter
   * @return Description of the Return Value
   * @throws RepositoryException Description of the Exception
   */
  public IActivityInstance[] findActivityInstances(String processInstanceId)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("findActivityInstances: '" + processInstanceId + "'");
    }
    try {
      Session session = (Session) TransactionLocal.get();

      Query query = session.createQuery("from o in class " + PActivityInstance.class.getName() + " where o.processInstance = :processInstance");

      query.setLong("processInstance", Long.parseLong(processInstanceId));

      List result = query.list();
      IActivityInstance ret[] = new IActivityInstance[result.size()];
      Iterator it = result.iterator();
      int i;

      for (i = 0; it.hasNext(); i++) {
        PActivityInstance activityInstance = (PActivityInstance) it.next();
        ret[i] = activityInstance;
      }

      return ret;
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Reads the specified activity instance.  This method takes either the
   * activity definition ID or the activity instance ID.
   *
   * @param processInstanceId       The process instance id.
   * @param activityDefinitionId    The activity definition id.
   * @param blockActivityInstanceId The ID of the block activity instance to
   *                                which the acitivty instance belongs.
   * @return The activity instance.
   * @throws RepositoryException Workflow client exception.
   */
  public IActivityInstance findActivityInstance(String processInstanceId,
                                                String activityDefinitionId, String blockActivityInstanceId)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("findActivityInstance: '" + processInstanceId + "' '" + activityDefinitionId + "' '" + blockActivityInstanceId + "'");
    }
    try {
      Session session = (Session) TransactionLocal.get();
      List result = null;

      if (blockActivityInstanceId == null) {
        Query query = session.createQuery("from o in class " + PActivityInstance.class.getName() +
                " where o.processInstance = :processInstance and o.activityDefinitionId = :activityDefinitionId");

        query.setLong("processInstance", Long.parseLong(processInstanceId));
        query.setString("activityDefinitionId", activityDefinitionId);

        result = query.list();
      } else {
        Query query = session.createQuery("from o in class " + PActivityInstance.class.getName() +
                " where o.processInstance = :processInstance and o.activityDefinitionId = :activityDefinitionId and o.blockActivityInstanceId = :blockActivityInstanceId");

        query.setLong("processInstance", Long.parseLong(processInstanceId));
        query.setString("activityDefinitionId", activityDefinitionId);
        query.setString("blockActivityInstanceId", blockActivityInstanceId);

        result = query.list();
      }

      if (log.isDebugEnabled()) {
        log.debug("Found: " + result.size());
      }

      if (result.size() > 0) {
        return (IActivityInstance) result.get(0);
      } else {
        throw new ObjectNotFoundException(processInstanceId + " " + activityDefinitionId + " " + blockActivityInstanceId);
      }
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Description of the Method
   *
   * @param processDefinitionId  Description of the Parameter
   * @param activityDefinitionId Description of the Parameter
   * @param filter               Description of the Parameter
   * @return Description of the Return Value
   * @throws RepositoryException Description of the Exception
   */
  public int countActivityInstances(String processDefinitionId,
                                    String activityDefinitionId, IFilter filter)
          throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      QueryBuilder builder = new QueryBuilder();

      if (filter != null) {
        filter.toBuilder(builder);
      }
      builder.compareExpr("processDefinitionId", CompareFilter.EQ, processDefinitionId);
      builder.compareExpr("activityDefinitionId", CompareFilter.EQ, processDefinitionId);
      builder.andExpr();

      Query query = builder.createQuery(session, "select count(*) from o in class " + PActivityInstance.class.getName(), null);

      return ((Integer) query.iterate().next()).intValue();
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * Reads a list of activity instances.
   *
   * @param processDefinitionId  The process definition ID, can be
   *                             <code>null</code>.
   * @param filter               A Filter specification.
   * @param activityDefinitionId Description of the Parameter
   * @return An array of matching activity instances.
   * @throws RepositoryException Description of the Exception
   */
  public IActivityInstance[] findActivityInstances(String processDefinitionId,
                                                   String activityDefinitionId, IFilter filter)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("findActivityInstances: '" + processDefinitionId + "' '" + activityDefinitionId + "' " + filter);
    }
    try {
      Session session = (Session) TransactionLocal.get();

      QueryBuilder builder = new QueryBuilder();

      if (filter != null) {
        filter.toBuilder(builder);
      }
      if (processDefinitionId != null) {
        builder.compareExpr("processDefinitionId.id", CompareFilter.EQ, processDefinitionId);
      }
      if (activityDefinitionId != null) {
        builder.compareExpr("activityDefinitionId", CompareFilter.EQ, processDefinitionId);
      }
      builder.andExpr();

      Query query = builder.createQuery(session, "from o in class " + PActivityInstance.class.getName(), "o.entityOid asc");

      List result = query.list();
      IActivityInstance ret[] = new IActivityInstance[result.size()];
      Iterator it = result.iterator();
      int i;

      for (i = 0; it.hasNext(); i++) {
        PActivityInstance activityInstance = (PActivityInstance) it.next();
        ret[i] = activityInstance;
      }

      return ret;
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * (non-Javadoc)
   *
   * @see de.objectcode.canyon.spi.instance.IInstanceRepository#beginTransaction()
   */
  public void beginTransaction()
          throws RepositoryException {
    try {
      if (TransactionLocal.get() == null)
        TransactionLocal.set(m_factory.openSession());
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }


  /**
   * (non-Javadoc)
   *
   * @see de.objectcode.canyon.spi.instance.IInstanceRepository#endTransaction()
   */
  public void endTransaction(boolean flush)
          throws RepositoryException {
    try {
      Session session = (Session) TransactionLocal.get();

      if (session != null) {
        if (flush) {
          session.flush();
        }
        session.close();
        TransactionLocal.set(null);
      }
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    }
  }

}
