<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:fuse="http://www.objectcode.com/fuse" version="1.0">
  <xsl:output method="html" indent="yes" encoding="ISO-8859-1"/>

  <xsl:template match="process">
    <html>
	  <h1>Process <xsl:value-of select="@id"/> <xsl:value-of select="@processInstanceId"/></h1>
      <xsl:apply-templates/>
    </html>
  </xsl:template>
  
  <xsl:template match="definitions">
	<h2>Type definitions</h2>
	<table border="1">
		<xsl:apply-templates/>	
	</table>
  </xsl:template>
  
  <xsl:template match="complex-type">
	<tr><td colspan="2"><xsl:value-of select="@name"/></td></tr>
	<tr><td witdh="50"><br/></td>
		<td><table width="100%">
			<tr><th>Name</th><th>Type</th><th>default</th></tr>
			<xsl:apply-templates/>
		</table></td></tr>
  </xsl:template>
  
  <xsl:template match="property">
	<tr>
		<td><xsl:value-of select="@name"/></td>
		<td><xsl:value-of select="@type"/></td>
		<td><xsl:value-of select="@default"/></td>
	</tr>
  </xsl:template>
  
  <xsl:template match="variables">
	<h2>Variables</h2>
	<table border="1" width="100%">
		<tr><th>Name</th><th>Type</th><th>Value</th></tr>
		<xsl:apply-templates/>
	</table>
  </xsl:template>
  
  <xsl:template match="basic-variable">
	<tr>
		<td><xsl:value-of select="@name"/></td>
		<td><xsl:value-of select="@type"/></td>
		<td><xsl:value-of select="@value"/></td>
	</tr>
  </xsl:template>
  
  <xsl:template match="correlations">
	<h2>Correlations</h2>
	<table>
		<xsl:apply-templates/>
	</table>
  </xsl:template>
  
  <xsl:template match="correlation">
	<tr><td><xsl:value-of select="@name"/></td></tr>
  </xsl:template>
  
  <xsl:template name="activity">
	<xsl:param name="activity-type">Activity</xsl:param>
	<table border="1" width="100%">
		<tr><td colspan="2">
			<table width="100%" border="0">
				<xsl:choose>
					<xsl:when test="@state = 'OPEN'">
						<xsl:attribute name="bgcolor">#00ff00</xsl:attribute>
					</xsl:when>
					<xsl:when test="@state = 'RUNNING'">
						<xsl:attribute name="bgcolor">#ffff00</xsl:attribute>
					</xsl:when>
					<xsl:when test="@state = 'ABORTED'">
						<xsl:attribute name="bgcolor">#ff0000</xsl:attribute>
					</xsl:when>
					<xsl:when test="@state = 'COMPLETED'">
						<xsl:attribute name="bgcolor">#808080</xsl:attribute>
					</xsl:when>
					<xsl:when test="@state = 'SKIPED'">
						<xsl:attribute name="bgcolor">#0000ff</xsl:attribute>
					</xsl:when>
				</xsl:choose>
				<tr><td colspan="2"><b><xsl:value-of select="$activity-type"/></b><xsl:text> </xsl:text><b><xsl:value-of select="@id"/></b></td></tr>
				<tr><td width="100">Id</td><td><xsl:value-of select="@id"/></td></tr>
				<tr><td width="100">Name</td><td><xsl:value-of select="@name"/></td></tr>
				<tr><td width="100">State</td><td><xsl:value-of select="@state"/></td></tr>
				<tr><td width="100">Incoming Links</td><td>
					<table bgcolor="'#ffffff'" width="100%">
						<tr><th>Name</th><th>From</th><th>To</th><th>State</th></tr>
						<xsl:apply-templates select="incoming-links" mode="links"/></table></td></tr>
				<tr><td width="100">Outgoing Links</td><td>
					<table bgcolor="'#ffffff'" width="100%">
						<tr><th>Name</th><th>Source</th><th>Target</th><th>State</th></tr>
						<xsl:apply-templates select="outgoing-links" mode="links"/></table></td></tr>
			</table></td></tr>
		<tr><td witdh="5%"><br/></td><td width="95%"><xsl:apply-templates/></td></tr>
	</table>
  </xsl:template>
  
  <xsl:template match="link" mode="links">
	<xsl:variable name="color"/>
	<tr>
		<xsl:choose>
			<xsl:when test="@state = 'UNKNOWN'">
				<xsl:attribute name="bgcolor">#808080</xsl:attribute>
			</xsl:when>
			<xsl:when test="@state = 'TRUE'">
				<xsl:attribute name="bgcolor">#00ff00</xsl:attribute>
			</xsl:when>
			<xsl:when test="@state = 'FALSE'">
				<xsl:attribute name="bgcolor">#ff0000</xsl:attribute>
			</xsl:when>
		</xsl:choose>
		<td><xsl:value-of select="@name"/></td>
		<td><xsl:value-of select="@source"/></td>
		<td><xsl:value-of select="@target"/></td>
		<td><xsl:value-of select="@state"/></td>
	</tr>
  </xsl:template>
  
  <xsl:template match="empty">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">Empty</xsl:with-param>
	</xsl:call-template>
  </xsl:template>
  
  <xsl:template match="sequence">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">Sequence</xsl:with-param>
	</xsl:call-template>
  </xsl:template>

  <xsl:template match="flow">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">Flow</xsl:with-param>
	</xsl:call-template>
  </xsl:template>
  
  <xsl:template match="invoke">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">Invoke</xsl:with-param>
	</xsl:call-template>
  </xsl:template>

  <xsl:template match="receive">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">Receive</xsl:with-param>
	</xsl:call-template>
  </xsl:template>

  <xsl:template match="pick">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">Pick</xsl:with-param>
	</xsl:call-template>
  </xsl:template>
  
  <xsl:template match="xpdl-worklist-activity">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">XPDL Worklist (<xsl:value-of select="@xpdl-id"/>)</xsl:with-param>
	</xsl:call-template>
  </xsl:template>

  <xsl:template match="xpdl-tool-activity">
	<xsl:call-template name="activity">
		<xsl:with-param name="activity-type">XPDL Tool (<xsl:value-of select="@xpdl-id"/>)</xsl:with-param>
	</xsl:call-template>
  </xsl:template>
  
  <xsl:template match="onMessage">
	<table border="1" width="100%">
		<tr><td colspan="2">
			<table width="100%" border="0">
				<tr><td>MessageType</td><td><xsl:value-of select="@messageType"/></td></tr>
			</table></td></tr>
		<tr><td witdh="5%"><br/></td><td width="95%"><xsl:apply-templates/></td></tr>
	</table>
  </xsl:template>
  
<!--  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>   -->
</xsl:stylesheet>