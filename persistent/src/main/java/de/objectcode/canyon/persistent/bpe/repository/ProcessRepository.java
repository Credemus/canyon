package de.objectcode.canyon.persistent.bpe.repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import de.objectcode.canyon.bpe.repository.IProcessNoSourceVisitor;
import de.objectcode.canyon.persistent.instance.PImmutableObjectValue;
import net.sf.hibernate.*;

import org.apache.commons.collections.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.bpe.engine.activities.BPECompactProcess;
import de.objectcode.canyon.bpe.engine.activities.BPEProcess;
import de.objectcode.canyon.bpe.repository.IProcessRepository;
import de.objectcode.canyon.bpe.repository.IProcessVisitor;
import de.objectcode.canyon.spi.ObjectNotFoundException;
import de.objectcode.canyon.spi.RepositoryException;

/**
 * @author junglas
 * @created 13. Juli 2004
 */
public class ProcessRepository implements IProcessRepository {
  private final static Log log = LogFactory.getLog(ProcessRepository.class);

  private SessionFactory m_sessionFactory;

  private static Map<Long, Serializable> g_processSourceCache = Collections.synchronizedMap(new LRUMap(100));


  /**
   * Constructor for the ProcessRepository object
   *
   * @param sessionFactory Description of the Parameter
   */
  public ProcessRepository(SessionFactory sessionFactory) {
    m_sessionFactory = sessionFactory;
  }

  /**
   * Gets the process attribute of the ProcessRepository object
   *
   * @param processEntityOid Description of the Parameter
   * @return The process value
   * @throws RepositoryException Description of the Exception
   */
  public BPEProcess getProcess(long processEntityOid)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("getProcess: " + processEntityOid);
    }
    long start = 0L;
    if (log.isInfoEnabled()) {
      start = System.currentTimeMillis();
    }
    Session session = null;
    try {

      session = m_sessionFactory.openSession();

      PBPEProcess process = (PBPEProcess) session.load(PBPEProcess.class, processEntityOid);

      return process.getProcess();
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(String.valueOf(processEntityOid));
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(String.valueOf(processEntityOid));
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
      if (log.isInfoEnabled()) {
        long stop = System.currentTimeMillis();
        log.info("Split ProcessRepository.getProcess [" + processEntityOid + "]:" + (stop - start) + " ms");
      }
    }
  }

  public Long getPackageRevisionOid(long processEntityOid)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("getPackageRevisionOid: " + processEntityOid);
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PBPEProcess process = (PBPEProcess) session.load(PBPEProcess.class, processEntityOid);

      return process.getPackageRevisionOid();
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(String.valueOf(processEntityOid));
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(String.valueOf(processEntityOid));
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  /**
   * @param processId Description of the Parameter
   * @return The process value
   * @throws RepositoryException Description of the Exception
   * @see de.objectcode.canyon.bpe.repository.IProcessRepository#getProcess(java.lang.String)
   */
  public BPEProcess getProcess(String processId) throws RepositoryException {
    return getProcess(processId, (String) null);
  }

  /**
   * @param processId Description of the Parameter
   * @return The process value
   * @throws RepositoryException Description of the Exception
   * @see de.objectcode.canyon.bpe.repository.IProcessRepository#getProcess(java.lang.String)
   */
  public BPEProcess getProcess(String processId, String processVersion)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("getProcess: " + processId + "/" + processVersion);
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = null;
      if (processVersion != null) {
        query = session
                .createQuery("select o from o in class "
                        + PBPEProcess.class.getName()
                        + " where o.processId = :processId and o.processVersion = :processVersion order by o.entityOid desc");
        query.setString("processId", processId);
        query.setString("processVersion", processVersion);
      } else {
        query = session.createQuery("select o from o in class "
                + PBPEProcess.class.getName()
                + " where o.processId = :processId order by o.entityOid desc");
        query.setString("processId", processId);
      }

      Iterator<PBPEProcess> it = query.iterate();

      if (it.hasNext()) {
        PBPEProcess process = it.next();

        return process.getProcess();
      }
      return null;
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(processId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(processId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  public BPEProcess getProcess(String processId, Long packageRevisionOid)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("getProcess: " + processId + "/" + packageRevisionOid);
    }

    if (packageRevisionOid == null) {
      log.warn("NO PACKAGEREVISION PROVIDED. USING ACTIVE VERSION FOR PROCESS '"
              + processId + "'");
      return getProcess(processId);
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = null;
      query = session
              .createQuery("select o from o in class "
                      + PBPEProcess.class.getName()
                      + " where o.processId = :processId and o.packageRevisionOid = :packageRevisionOid order by o.entityOid desc");
      query.setString("processId", processId);
      query.setLong("packageRevisionOid", packageRevisionOid);

      Iterator<PBPEProcess> it = query.iterate();

      if (it.hasNext()) {
        PBPEProcess process = it.next();

        return process.getProcess();
      }
      return null;
    } catch (NumberFormatException e) {
      throw new ObjectNotFoundException(processId);
    } catch (net.sf.hibernate.ObjectNotFoundException e) {
      throw new ObjectNotFoundException(processId);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  abstract class SourceFinder {
    protected String m_processId;

    SourceFinder(String processId) {
      m_processId = processId;
    }

    abstract Query createQuery(Session session) throws HibernateException;

    Serializable find() throws RepositoryException {
      if (log.isDebugEnabled()) {
        log.debug("getProcessSource: " + m_processId);
      }
      Session session = null;
      try {
        session = m_sessionFactory.openSession();

        Query query = createQuery(session);

        Iterator<PBPEProcess> it = query.iterate();

        if (it.hasNext()) {
          PBPEProcess process = it.next();

          if (g_processSourceCache.containsKey(process.getEntityOid())) {
            return (Serializable) g_processSourceCache.get(process.getEntityOid());
          }

          Serializable source = (Serializable) process.getProcessSourceBlob()
                  .getValue();

          g_processSourceCache.put(process.getEntityOid(), source);

          return source;
        }
        return null;
      } catch (NumberFormatException e) {
        throw new ObjectNotFoundException(m_processId);
      } catch (net.sf.hibernate.ObjectNotFoundException e) {
        throw new ObjectNotFoundException(m_processId);
      } catch (HibernateException e) {
        log.error("Exception", e);
        throw new RepositoryException(e);
      } finally {
        try {
          session.close();
        } catch (Exception ignored) {
        }
      }
    }
  }

  /**
   * Gets the processSource attribute of the ProcessRepository object
   *
   * @param processId Description of the Parameter
   * @return The processSource value
   * @throws RepositoryException Description of the Exception
   */
  public Serializable getProcessSource(final String processId)
          throws RepositoryException {
    SourceFinder finder = new SourceFinder(processId) {
      Query createQuery(Session session) throws HibernateException {
        Query query = session.createQuery("select o from o in class "
                + PBPEProcess.class.getName()
                + " where o.processId = :processId order by o.entityOid desc");

        query.setString("processId", processId);
        return query;
      }
    };
    return finder.find();
  }

  /**
   * Gets the processSource attribute of the ProcessRepository object
   *
   * @param processId Description of the Parameter
   * @return The processSource value
   * @throws RepositoryException Description of the Exception
   */
  public Serializable getProcessSource(final String processId, final String version)
          throws RepositoryException {
    SourceFinder finder = new SourceFinder(processId) {
      Query createQuery(Session session) throws HibernateException {
        Query query = session.createQuery("select o from o in class " + PBPEProcess.class.getName()
                + " where o.processId = :processId and o.processVersion= :version order by o.entityOid desc");

        query.setString("processId", processId);
        query.setString("version", version);
        return query;
      }
    };
    return finder.find();
  }

  /**
   * Gets the processSource attribute of the ProcessRepository object
   *
   * @param processId Description of the Parameter
   * @return The processSource value
   * @throws RepositoryException Description of the Exception
   */
  public Serializable getProcessSource(final String processId, final long packageRevisionOid)
          throws RepositoryException {
    SourceFinder finder = new SourceFinder(processId) {
      Query createQuery(Session session) throws HibernateException {
        Query query = session.createQuery("select o from o in class " +
                PBPEProcess.class.getName() +
                " where o.processId = :processId " +
                "and o.packageRevisionOid = :packageRevisionOid " +
                "order by o.entityOid desc");

        query.setString("processId", processId);
        query.setLong("packageRevisionOid", packageRevisionOid);
        return query;
      }
    };
    return finder.find();
  }

  /**
   * @param process       Description of the Parameter
   * @param processSource Description of the Parameter
   * @throws RepositoryException Description of the Exception
   * @see de.objectcode.canyon.bpe.repository.IProcessRepository#saveProcess(de.objectcode.canyon.bpe.engine.activities.BPEProcess)
   */
  public void createProcess(long packageRevisionOid, BPEProcess process,
                            Serializable processSource) throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("saveProcess: " + process.getId());
    }

    BPEProcess bpeProcess = getProcess(process.getId(), process.getVersion());
    boolean enforceUniqueness = System
            .getProperty("de.objectcode.canyon.persistent.bpe.repository.ProcessRepository.enforceUniqueness") != null;
    if (enforceUniqueness && bpeProcess != null)
      throw new RepositoryException("PROCESS WITH ID='" + process.getId()
              + "' ALREADY EXISTS IN VERSION '" + process.getVersion() + "'");

    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PBPEProcess pprocess = new PBPEProcess(packageRevisionOid, process,
              processSource);

      session.save(pprocess);
      session.flush();

      process.setProcessEntityOid(pprocess.getEntityOid());

      g_processSourceCache.put(pprocess.getEntityOid(), processSource);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  /**
   * @param process       Description of the Parameter
   * @param processSource Description of the Parameter
   * @throws RepositoryException Description of the Exception
   */
  public void updateProcess(BPEProcess process, Serializable processSource)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("updateProcess: " + process.getId());
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PBPEProcess pprocess = (PBPEProcess) session.load(PBPEProcess.class, process.getProcessEntityOid());

      if (1 == 1)
        throw new RuntimeException("NOT IMPLEMENTED");
      // pprocess.setProcess( process, processSource );

      session.update(pprocess);
      session.flush();

      Long key = process.getProcessEntityOid();
      g_processSourceCache.put(key,
              processSource);
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  /**
   * Description of the Method
   *
   * @return Description of the Return Value
   * @throws RepositoryException Description of the Exception
   */
  public int countProcesses() throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("countProcesses: ");
    }

    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = session.createQuery("select count(*) from o in class "
              + PBPEProcess.class);

      return (Integer) query.iterate().next();
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  /**
   * @param visitor Description of the Parameter
   * @throws RepositoryException Description of the Exception
   */
  public void iterateProcesses(IProcessVisitor visitor, boolean onlyActive)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("iterateProcess: ");
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = null;
      if (onlyActive) {
        query = session.createQuery("select o from o in class " + PBPEProcess.class
                + ", p in class " + PBPEPackage.class
                + " where o.packageRevisionOid=p.entityOid and p.entityOid="
                + " (select max(pp.entityOid) from pp in class "
                + PBPEPackage.class + " where p.packageId=pp.packageId)");
//            select p.processid,pp.packageID from PBPEProcesses p, PBPEPACKAGE pp where
//			p.PACKAGE_REV_OID=pp.entityId and pp.entityId=(select max(ppp.entityId) from PBP
//			EPACKAGE ppp where ppp.packageID=pp.packageId);
      } else {
        log.info("Ignore processes (process definitions) with state >=4 (terminated, completed)  [MHE, 27.01.2010]");
        // MHE, 27.01.2010: ignore processes with state >=4 (terminated, completed)
        // Normally, processes have NO state at all! (all processes have state=0 in DB). But we use this
        // column now to manually exclude old processes from being loaded. Used in flow (01/2010)!
        // HINT: Anyway, the process will be loaded, if an active process instance will be loaded later!!!
        query = session.createQuery("select o from o in class " + PBPEProcess.class + " where o.state <=3 order by o.entityOid asc");
        // query = session.createQuery("from o in class " + PBPEProcess.class + " order by o.entityOid asc");
      }

      Iterator<PBPEProcess> it = query.iterate();

      while (it.hasNext()) {
        long start = System.currentTimeMillis();
        PBPEProcess process = it.next();
        if (log.isDebugEnabled()) {
          log.debug("iterateProcess: " + process.getEntityOid());
        }

        Serializable source = null;

        if (g_processSourceCache.containsKey(process.getEntityOid())) {
          source = g_processSourceCache.get(process.getEntityOid());
        } else {
          source = (Serializable) process.getProcessSourceBlob().getValue();
          g_processSourceCache.put(process.getEntityOid(), source);
        }
        visitor.visit(process.getProcess(), source);

        session.evict(process.getProcessBlob());
        session.evict(process);

        if (log.isDebugEnabled()) {
          log.debug("iterateProcess: " + process.getEntityOid() + "="
                  + (System.currentTimeMillis() - start) + " ms");
        }

      }
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  /**
   * @param visitor Description of the Parameter
   * @throws RepositoryException Description of the Exception
   */
  public void iterateProcessesNoSource(IProcessNoSourceVisitor visitor)
          throws RepositoryException {
    log.info("iterateProcessesNoSource: start");
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = null;
      log.info("Ignore processes (process definitions) with state >=4 (terminated, completed)  [MHE, 27.01.2010]");
      // MHE, 27.01.2010: ignore processes with state >=4 (terminated, completed)
      // Normally, processes have NO state at all! (all processes have state=0 in DB). But we use this
      // column now to manually exclude old processes from being loaded. Used in flow (01/2010)!
      // HINT: Anyway, the process will be loaded, if an active process instance will be loaded later!!!
      query = session.createQuery("select o.entityOid, o.processBlob from o in class " + PBPEProcess.class + " where o.state <=3");
      // query = session.createQuery("from o in class " + PBPEProcess.class + " order by o.entityOid asc");

      ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);

      while (results.next()) {
        long start = System.currentTimeMillis();
        Long entityOid = (Long) results.get(0);
        PImmutableObjectValue processBlob = (PImmutableObjectValue) results.get(1);
        BPEProcess process = (BPEProcess) processBlob.getValue();
        process.setProcessEntityOid(entityOid);
        if (log.isDebugEnabled()) {
          log.debug("iterateProcess: " + process.getProcessEntityOid());
        }

        visitor.visit(process);

        session.evict(processBlob);

        if (log.isDebugEnabled()) {
          log.debug("iterateProcess: " + process.getProcessEntityOid() + "="
                  + (System.currentTimeMillis() - start) + " ms");
        }
      }
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
      log.info("iterateProcessesNoSource: done");
    }
  }

  /**
   * @param visitor Description of the Parameter
   * @throws RepositoryException Description of the Exception
   * @see de.objectcode.canyon.bpe.repository.IProcessRepository#iterateProcesses(de.objectcode.canyon.bpe.engine.activities.ActivityState,
   * de.objectcode.canyon.bpe.repository.IProcessVisitor)
   */
  public void iterateCompactProcesses(IProcessVisitor visitor, boolean onlyActive)
          throws RepositoryException {
    log.info("MHE: iterateCompactProcesses()");

    if (log.isDebugEnabled()) {
      log.debug("iterateCompactProcesses: ");
    }
    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      Query query = null;
      if (onlyActive) {
        query = session.createQuery("select o from o in class " + PBPEProcess.class
                + ", p in class " + PBPEPackage.class
                + " where o.packageRevisionOid=p.entityOid and p.entityOid="
                + " (select max(pp.entityOid) from pp in class "
                + PBPEPackage.class + " where p.packageId=pp.packageId)");
        //          select p.processid,pp.packageID from PBPEProcesses p, PBPEPACKAGE pp where
        //          p.PACKAGE_REV_OID=pp.entityId and pp.entityId=(select max(ppp.entityId) from PBP
        //          EPACKAGE ppp where ppp.packageID=pp.packageId);
      } else {
        log.info("new: MHE, 27.01.2010: ignore processes (process definitions) with state >=4 (terminated, completed)");
        // MHE, 27.01.2010: ignore processes with state >=4 (terminated, completed)
        // Normally, processes have NO state at all! (all processes have state=0 in DB). But we use this
        // column now to manually exclude old processes from being loaded. Used in flow (01/2010)!
        // HINT: Anyway, the process will be loaded, if an active process instance will be loaded later!!!
        query = session.createQuery("select o from o in class " + PBPEProcess.class + " where o.state <=3 order by o.entityOid asc");

        // query = session.createQuery("from o in class " + PBPEProcess.class + " order by o.entityOid asc");
      }

      Iterator it = query.iterate();

      while (it.hasNext()) {
        long start = System.currentTimeMillis();
        PBPEProcess process = (PBPEProcess) it.next();
        if (log.isDebugEnabled()) {
          log.debug("iterateCompactProcesses: " + process.getEntityOid());
        }

        Serializable source = (Serializable) process.getProcessSourceBlob().getValue();
        visitor.visit(process.getProcess(), source);

//                BPECompactProcess compactProcess = new BPECompactProcess(process);

        if (log.isDebugEnabled()) {
          log.debug("iterateCompactProcesses: " + process.getEntityOid() + "="
                  + (System.currentTimeMillis() - start) + " ms");
        }

      }
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }

  public long createPackageRevision(String packageId, String packageVersion)
          throws RepositoryException {
    if (log.isDebugEnabled()) {
      log.debug("createPackageRevision: " + packageId + "/" + packageVersion);
    }

    Session session = null;
    try {
      session = m_sessionFactory.openSession();

      PBPEPackage ppackage = new PBPEPackage(packageId, packageVersion);

      session.save(ppackage);
      session.flush();

      return ppackage.getEntityOid();
    } catch (HibernateException e) {
      log.error("Exception", e);
      throw new RepositoryException(e);
    } finally {
      try {
        session.close();
      } catch (Exception ignored) {
      }
    }
  }
}
