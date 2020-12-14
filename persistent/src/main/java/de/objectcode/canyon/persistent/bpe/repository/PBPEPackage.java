package de.objectcode.canyon.persistent.bpe.repository;

import java.util.Date;

/**
 * @hibernate.class table="PBPEPACKAGE"
 * 
 * @author xylander
 */
public class PBPEPackage {
	protected long m_entityOid;

	protected String m_packageId;

	protected String m_packageVersion;
	
	
	protected Date m_creationDate;

	/**
	 * Constructor for the PBPEPackage object
	 */
	public PBPEPackage() {
	}

	public PBPEPackage(String packageId, String packageVersions) {
		m_packageId = packageId;
		m_packageVersion = packageVersions;
		m_creationDate = new Date();
	}

	/**
	 * @param entityOid
	 *          The entityOid to set.
	 */
	public void setEntityOid(long entityOid) {
		m_entityOid = entityOid;
	}

	public void setPackageId(String packageId) {
		m_packageId = packageId;
	}

	public void setPackageVersion(String packageVersion) {
		m_packageVersion = packageVersion;
	}

	/**
	 * @hibernate.id generator-class="native" column="ENTITYID" type="long"
	 *               unsaved-value="0"
	 * 
	 * @return
	 */
	public long getEntityOid() {
		return m_entityOid;
	}

	/**
	 * @hibernate.property type="string" length="64" column="PACKAGEID"
	 *                     not-null="true"
	 * 
	 * @return
	 */
	public String getPackageId() {
		return m_packageId;
	}

	/**
	 * @hibernate.property type="string" length="64" column="PACKAGEVERSION"
	 *                     not-null="true"
	 * 
	 * @return
	 */
	public String getPackageVersion() {
		return m_packageVersion;
	}

  /**
   * @hibernate.property column="CREATIONDATE" type="timestamp" not-null="false"
   *
   * @return
   */	
	public Date getCreationDate() {
		return m_creationDate;
	}

	public void setCreationDate(Date creationDate) {
		m_creationDate = creationDate;
	}

}
