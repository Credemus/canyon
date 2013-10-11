THIS IS A COLLECTION OF NOTES. NO NEED TO READ


CANYON uses a patched version of xdoclet and hibernate.

Howto perform a Hibernate upgrade:

jboss.ejb-local-ref-jndi->jboss.ejb-local-ref

Multiple
   * @hibernate.collection-key-column
must be preceeded by a
   * @hibernate.collection-key


- Merge changes in HibernateService.java 
-- The only patch is that canyon uses an own version of HibernateService.
This modified Servcice provides automatic dialect detection and automatic schema evolution

- Merge changes in JBossServiceSubTask.java
-- Add new property
    private String  _serviceClassName = null;
    
    /**
     * @param serviceClassName
     * @ant.not-required
     */
    public void setServiceClassName(String serviceClassName)
    {
        _serviceClassName = serviceClassName;
    }

    public String getServiceClassName()
    {
        return _serviceClassName;
    }

- Merge changes in HibernateTagsHandler.java

-- Patch method serviceClassName(
    public String serviceClassName(Properties attributes)
    {
      
      if (getJBossServiceSubTask().getServiceClassName() == null) {
        if ("1.1".equals(getHibernateSubTask().getVersion()))
            return "cirrus.hibernate.jmx.HibernateService";
        else
            return "net.sf.hibernate.jmx.HibernateService";
    }

    return getJBossServiceSubTask().getServiceClassName();
    }



- xdoclet-hibernate: hibernate.properties

Check, that this feature has not been added to xdoclet by the xdoclet team
otherwise add 


     <XDtMethod:ifHasMethodTag tagName="hibernate.dynabean">
       <dynabean
           name="<XDtMethod:propertyName />"
           dynaclass="<XDtMethod:methodTagValue tagName="hibernate.dynabean" paramName="dynaclass"/>"
       >
       </dynabean>
     </XDtMethod:ifHasMethodTag>

     
- xdoclet-hibernate: hibernate-subclass.properties     

Check, that this feature has not been added to xdoclet by the xdoclet team
otherwise add it as FIRST element in subclass element.  

            <XDtClass:forAllClassTags tagName="hibernate.meta">
              <meta attribute="<XDtMethod:methodTagValue tagName="hibernate.meta" paramName="attribute"/>"><XDtMethod:methodTagValue tagName="hibernate.meta" paramName="value"/></meta>
            </XDtClass:forAllClassTags>

- xdoclet-hibernate: hibernate.properties
Check, that this feature has not been added to xdoclet by the xdoclet team
otherwise add it as FIRST element in class element.  
    <XDtClass:forAllClassTags tagName="hibernate.meta">
      <meta attribute="<XDtMethod:methodTagValue tagName="hibernate.meta" paramName="attribute"/>"><XDtMethod:methodTagValue tagName="hibernate.meta" paramName="value"/></meta>
    </XDtClass:forAllClassTags>

and as as FIRST element in id element.              

          <XDtMethod:forAllMethodTags tagName="hibernate.meta">
            <meta attribute="<XDtMethod:methodTagValue tagName="hibernate.meta" paramName="attribute"/>"><XDtMethod:methodTagValue tagName="hibernate.meta" paramName="value"/></meta>
          </XDtMethod:forAllMethodTags>
 
- xdoclet-hibernate: hibernate-column.properties
Check, that this feature has not been added to xdoclet by the xdoclet team
otherwise add it before column element:

        <XDtMethod:forAllMethodTags tagName="hibernate.meta">
            <meta attribute="<XDtMethod:methodTagValue tagName="hibernate.meta" paramName="attribute"/>"><XDtMethod:methodTagValue tagName="hibernate.meta" paramName="value"/></meta>
        </XDtMethod:forAllMethodTags>
           
           
- xdoclet-jboss: jboss-xml.xdt