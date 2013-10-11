<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>


<html>
<head>
	<title>Worklist</title>
	<style type="text/css">
  	@import url(../style.css);
    </style>
</head>
<body class="bodystyle">
	<nested:form action="/worklist/list" method="POST">
		<nested:hidden property="processData"/>
		<nested:hidden property="currentId"/>
		<nested:define id="theCurrentId" property="currentId"/>
		<table class="issuetable" cellpadding="0" cellspacing="0" width="100%" height="100%">
			<tr>
				<td>
					<nested:nest property="workItemTable">
    			       <%@ include file="/worklist/workItemTable.jinc" %>
					</nested:nest>
				</td>
			</tr>
			<tr>
				<td>
					<nested:nest property="workItemDetail">
						<%@ include file="/worklist/workItemDetail.jinc" %>
					</nested:nest>
				</td>
			</tr>
			<tr>
				<td>
					<nested:submit property="submit.save" value="&Uuml;bernehmen" styleClass="submitButton"/>
					<nested:submit property="submit.complete" value="Abschliessen" styleClass="submitButton"/>
				</td>
			</tr>
		</table>
	</nested:form>
</body>
</html>