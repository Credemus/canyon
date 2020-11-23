/*--

 Copyright (C) 2002-2003 Aetrion LLC.
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.

 3. The names "OBE" and "Open Business Engine" must not be used to
 	endorse or promote products derived from this software without prior
 	written permission.  For written permission, please contact
 	obe@aetrion.com.

 4. Products derived from this software may not be called "OBE" or
 	"Open Business Engine", nor may "OBE" or "Open Business Engine"
 	appear in their name, without prior written permission from
 	Aetrion LLC (obe@aetrion.com).

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.

 For more information on OBE, please see
 <http://www.openbusinessengine.org/>.

 */

package org.obe.serializer.dom4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.obe.ConformanceClass;
import org.obe.ExternalPackage;
import org.obe.GraphConformance;
import org.obe.Package;
import org.obe.PackageHeader;
import org.obe.PublicationStatus;
import org.obe.RedefinableHeader;
import org.obe.Script;
import org.obe.activity.Activity;
import org.obe.activity.ActivitySet;
import org.obe.activity.AutomationMode;
import org.obe.activity.BlockActivity;
import org.obe.activity.CompletionStrategy;
import org.obe.activity.ExecutionType;
import org.obe.activity.ForEach;
import org.obe.activity.Implementation;
import org.obe.activity.Loop;
import org.obe.activity.LoopBody;
import org.obe.activity.NoImplementation;
import org.obe.activity.Route;
import org.obe.activity.SubFlow;
import org.obe.activity.Tool;
import org.obe.activity.ToolSet;
import org.obe.activity.Until;
import org.obe.activity.While;
import org.obe.application.Application;
import org.obe.condition.Condition;
import org.obe.condition.ConditionType;
import org.obe.condition.Xpression;
import org.obe.data.ActualParameter;
import org.obe.data.ArrayType;
import org.obe.data.BasicType;
import org.obe.data.DataField;
import org.obe.data.DataType;
import org.obe.data.DeclaredType;
import org.obe.data.EnumerationType;
import org.obe.data.EnumerationValue;
import org.obe.data.ExternalReference;
import org.obe.data.FormalParameter;
import org.obe.data.ListType;
import org.obe.data.RecordType;
import org.obe.data.Type;
import org.obe.data.TypeDeclaration;
import org.obe.data.UnionType;
import org.obe.participant.Participant;
import org.obe.serializer.ElementRequiredException;
import org.obe.serializer.XPDLSerializer;
import org.obe.serializer.XPDLSerializerException;
import org.obe.transition.Event;
import org.obe.transition.EventType;
import org.obe.transition.Join;
import org.obe.transition.JoinType;
import org.obe.transition.Split;
import org.obe.transition.SplitType;
import org.obe.transition.Transition;
import org.obe.transition.TransitionRestriction;
import org.obe.util.Deadline;
import org.obe.util.DurationUnit;
import org.obe.util.SimulationInformation;
import org.obe.util.TimeEstimation;
import org.obe.workflow.ProcessHeader;
import org.obe.workflow.WorkflowProcess;

/**
 * Implementation of the XPDLSerializer interface which uses the Dom4J
 * library to serialize a package to an XPDL document.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public class Dom4JXPDLSerializer implements XPDLSerializer {

    public static final String DEFAULT_XPDL_VERSION = "1.0";
    public static final String DEFAULT_VENDOR = "OpenBusinessEngine.org";
    private static final Log log = LogFactory.getLog(Dom4JXPDLSerializer.class);

    public void serialize(Package pkg, OutputStream out)
        throws IOException, XPDLSerializerException {

        serialize(pkg, new OutputStreamWriter(out));
    }

    /** Serialize the package to the given output stream.

     @param pkg The Package
     @param out The OutputStream
     @throws IOException Any I/O exception
     @throws XPDLSerializerException Any serializer Exception
     */

    public void serialize(Package pkg, Writer out)
        throws IOException, XPDLSerializerException {

        DocumentFactory df = new DocumentFactory();

        // serialize the Package
        Element packageElement = df.createElement(new QName(PACKAGE, XPDL_NS));
        packageElement.addNamespace(XPDL_NS_PREFIX, XPDL_URI);
        packageElement.addNamespace(XSD_NS_PREFIX, XSD_URI);
        packageElement.addNamespace(XSI_NS_PREFIX, XSI_URI);
        packageElement.addNamespace(OBE_NS_PREFIX, OBE_URI);
        packageElement.addAttribute(XSI_SCHEMA_LOCATION_QNAME,
            XPDL_SCHEMA_LOCATION);
        packageElement.addAttribute(ID, pkg.getId());
        packageElement.addAttribute(NAME, pkg.getName());

        writePackageHeader(pkg.getPackageHeader(), packageElement);
        writeRedefinableHeader(pkg.getRedefinableHeader(), packageElement);
        writeConformanceClass(pkg.getConformanceClass(), packageElement);
        writeScript(pkg.getScript(), packageElement);
        writeExternalPackages(pkg.getExternalPackages(), packageElement);
        writeTypeDeclarations(pkg.getTypeDeclarations(), packageElement);
        writeParticipants(pkg.getParticipants(), packageElement);
        writeApplications(pkg.getApplications(), packageElement);
        writeDataFields(pkg.getDataFields(), packageElement);
        writeWorkflowProcesses(pkg.getWorkflowProcesses(), packageElement);
        writeExtendedAttributes(pkg.getExtendedAttributes(), packageElement);
        writeEventTypes(pkg.getEventTypes(), packageElement);

        // create the Document object
        Document document = df.createDocument(packageElement);

        // write the document to the output stream
        OutputFormat format = new OutputFormat("    ", true);
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        out.flush();
    }

    protected void writePackageHeader(PackageHeader header, Element parent)
        throws XPDLSerializerException {

        if (header == null) {
            throw new ElementRequiredException("Package header required");
        }

        Element headerElement =
            Util.addElement(parent, PACKAGE_HEADER);

        Util.addElement(headerElement, XPDL_VERSION,
            header.getXPDLVersion(), DEFAULT_XPDL_VERSION);
        Util.addElement(headerElement, VENDOR, header.getVendor(),
            DEFAULT_VENDOR);
        Util.addElement(headerElement, CREATED, header.getCreated(),
            new Date());
        Util.addElement(headerElement, DESCRIPTION, header.getDescription());
        Util.addElement(headerElement, DOCUMENTATION,
            header.getDocumentation());
        Util.addElement(headerElement, PRIORITY_UNIT, header.getPriorityUnit());
        Util.addElement(headerElement, COST_UNIT, header.getCostUnit());
    }

    protected void writeRedefinableHeader(RedefinableHeader header,
        Element parent) {

        if (header == null) {
            return;
        }

        Element headerElement =
            Util.addElement(parent, REDEFINABLE_HEADER);

        Util.addElement(headerElement, AUTHOR, header.getAuthor());
        Util.addElement(headerElement, VERSION, header.getVersion());
        Util.addElement(headerElement, CODEPAGE, header.getCodepage());
        Util.addElement(headerElement, COUNTRYKEY, header.getCountrykey());

        writeResponsibles(header.getResponsibles(), headerElement);

        PublicationStatus publicationStatus = header.getPublicationStatus();
        if (publicationStatus != null) {
            headerElement.addAttribute(PUBLICATION_STATUS,
                publicationStatus.toString());
        }
    }

    protected void writeConformanceClass(ConformanceClass cc, Element parent) {
        if (cc == null) {
            log.debug("No conformance class specified");
            return;
        }

        log.debug("Writing conformance class: " + cc);
        Element ccElement = Util.addElement(parent, CONFORMANCE_CLASS);
        GraphConformance gc = cc.getGraphConformance();
        if (gc != null)
            ccElement.addAttribute(GRAPH_CONFORMANCE, gc.toString());
    }

    protected void writeScript(Script script, Element parent) {
        if (script == null) {
            log.debug("No script language specified");
            return;
        }

        Element scriptElement = Util.addElement(parent, SCRIPT);
        scriptElement.addAttribute(TYPE, script.getType());
        scriptElement.addAttribute(VERSION, script.getVersion());
        scriptElement.addAttribute(GRAMMAR, script.getGrammar());
    }

    protected void writeExternalPackages(List externalPackages,
        Element parent) {

        if (externalPackages == null || externalPackages.size() == 0) {
            return;
        }

        Element externalPackagesElement = Util.addElement(parent,
            EXTERNAL_PACKAGES);
        Iterator iter = externalPackages.iterator();
        while (iter.hasNext()) {
            ExternalPackage externalPackage = (ExternalPackage)iter.next();
            Element externalPackageElement = Util.addElement(
                externalPackagesElement, EXTERNAL_PACKAGE);

            externalPackageElement.addAttribute(HREF,
                externalPackage.getHref());

            writeExtendedAttributes(externalPackage.getExtendedAttributes(),
                externalPackageElement);
        }
    }

    protected void writeTypeDeclarations(List typeDeclarations, Element parent)
        throws XPDLSerializerException {
        if (typeDeclarations == null || typeDeclarations.size() == 0) {
            return;
        }

        Element typeDeclarationsElement = Util.addElement(parent,
            TYPE_DECLARATIONS);
        Iterator iter = typeDeclarations.iterator();
        while (iter.hasNext()) {
            TypeDeclaration typeDeclaration = (TypeDeclaration)iter.next();
            Element typeDeclarationElement = Util.addElement(
                typeDeclarationsElement, TYPE_DECLARATION);

            typeDeclarationElement.addAttribute(ID, typeDeclaration.getId());
            typeDeclarationElement.addAttribute(NAME, typeDeclaration.getName());

            Util.addElement(typeDeclarationElement, DESCRIPTION,
                typeDeclaration.getDescription());

            writeType(typeDeclaration.getType(), typeDeclarationElement);
            writeExtendedAttributes(typeDeclaration.getExtendedAttributes(),
                typeDeclarationElement);
        }
    }

    protected void writeParticipants(List participants, Element parent) {
        if (participants == null || participants.size() == 0) {
            return;
        }

        Element participantsElement = Util.addElement(parent, PARTICIPANTS);
        Iterator iter = participants.iterator();
        while (iter.hasNext()) {
            Participant participant = (Participant)iter.next();
            Element participantElement = Util.addElement(
                participantsElement, PARTICIPANT);

            participantElement.addAttribute(ID, participant.getId());
            if (participant.getName() != null)
                participantElement.addAttribute(NAME, participant.getName());

            Element participantTypeElement = Util.addElement(
                participantElement, PARTICIPANT_TYPE);
            participantTypeElement.addAttribute(TYPE,
                participant.getParticipantType().toString());

            Util.addElement(participantElement, DESCRIPTION,
                participant.getDescription());

            writeExtendedAttributes(participant.getExtendedAttributes(),
                participantElement);
        }
    }

    protected void writeApplications(List applications, Element parent)
        throws XPDLSerializerException {

        if (applications == null || applications.size() == 0) {
            return;
        }

        Element applicationsElement = Util.addElement(parent, APPLICATIONS);
        Iterator iter = applications.iterator();
        while (iter.hasNext()) {
            Application application = (Application)iter.next();
            Element applicationElement = Util.addElement(
                applicationsElement, APPLICATION);

            applicationElement.addAttribute(ID, application.getId());
            if (application.getName() != null)
                applicationElement.addAttribute(NAME, application.getName());

            if (application.getDescription() != null) {
                Util.addElement(applicationElement, DESCRIPTION,
                    application.getDescription());
            }

            writeFormalParameters(application.getFormalParameters(),
                applicationElement);
            writeExternalReference(application.getExternalReference(),
                applicationElement);
            writeExtendedAttributes(application.getExtendedAttributes(),
                applicationElement);
        }
    }

    protected void writeEventTypes(List eventTypes, Element parent)
        throws XPDLSerializerException {

        if (eventTypes == null || eventTypes.size() == 0) {
            return;
        }

        // For now, our parent element must be an ExtendedAttribute.
        // Check whether the ExtendedAttributes element is already present.
        // If not, add it now.
        Element extendedAttributesElement = parent.element(
            EXTENDED_ATTRIBUTES_QNAME);
        if (extendedAttributesElement == null) {
            extendedAttributesElement = Util.addElement(parent,
                EXTENDED_ATTRIBUTES);
        }
        parent = Util.addElement(extendedAttributesElement, EXTENDED_ATTRIBUTE);
        parent.addAttribute(NAME, OBE_EVENT_TYPES);

        Element eventTypesElement = parent.addElement(EVENT_TYPES_QNAME);
        Iterator iter = eventTypes.iterator();
        while (iter.hasNext()) {
            EventType eventType = (EventType)iter.next();
            Element eventTypeElement = eventTypesElement.addElement(
                EVENT_QNAME);
            eventTypeElement.addAttribute(ID, eventType.getId());
            if (eventType.getName() != null) {
                eventTypeElement.addAttribute(NAME, eventType.getName());
            }
            if (eventType.getDescription() != null) {
                Element descElement = eventTypeElement.addElement(
                    DESCRIPTION_QNAME);
                descElement.addText(eventType.getDescription());
            }

            writeFormalParameters(eventType.getFormalParameters(),
                eventTypeElement);
            writeExternalReference(eventType.getExternalReference(),
                eventTypeElement);
        }
    }

    protected void writeDataFields(List dataFields, Element parent)
        throws XPDLSerializerException {

        if (dataFields == null || dataFields.size() == 0) {
            return;
        }

        Element dataFieldsElement = Util.addElement(parent, DATA_FIELDS);
        Iterator iter = dataFields.iterator();
        while (iter.hasNext()) {
            DataField dataField = (DataField)iter.next();
            Element dataFieldElement = Util.addElement(
                dataFieldsElement, DATA_FIELD);

            dataFieldElement.addAttribute(ID, dataField.getId());
            if (dataField.getName() != null)
                dataFieldElement.addAttribute(NAME, dataField.getName());
            if (dataField.isArray()) {
                dataFieldElement.addAttribute(IS_ARRAY,
                    String.valueOf(dataField.isArray()));
            }

            writeDataType(dataField.getDataType(), dataFieldElement);

            Util.addElement(dataFieldElement, INITIAL_VALUE,
                dataField.getInitialValue());

            if (dataField.isArray()) {
                Util.addElement(dataFieldElement, LENGTH,
                    Integer.toString(dataField.getLength()));
            }

            Util.addElement(dataFieldElement, DESCRIPTION,
                dataField.getDescription());

            writeExtendedAttributes(dataField.getExtendedAttributes(),
                dataFieldElement);
        }
    }

    protected void writeDataType(DataType dataType, Element parent)
        throws XPDLSerializerException {

        if (dataType == null) {
            return;
        }

        Element dataTypeElement = Util.addElement(parent, DATA_TYPE);
        writeType(dataType.getType(), dataTypeElement);
    }

    protected void writeResponsibles(List responsibles, Element parent) {
        if (responsibles == null || responsibles.size() == 0) {
            return;
        }

        Element responsiblesElement = Util.addElement(parent, RESPONSIBLES);
        Iterator iter = responsibles.iterator();
        while (iter.hasNext()) {
            String responsibleId = (String)iter.next();
            Util.addElement(responsiblesElement, RESPONSIBLE, responsibleId);
        }
    }

    protected void writeType(Type type, Element parent)
        throws XPDLSerializerException {

        Element typeElement;
        if (type instanceof BasicType) {
            typeElement = Util.addElement(parent, BASIC_TYPE);
            typeElement.addAttribute(TYPE, type.toString());
        } else if (type instanceof RecordType) {
            typeElement = Util.addElement(parent, RECORD_TYPE);
            Element memberElement = Util.addElement(typeElement, MEMBER);
            Iterator iter = ((RecordType)type).getMembers().iterator();
            while (iter.hasNext()) {
                writeType((Type)iter.next(), memberElement);
            }
        } else if (type instanceof UnionType) {
            typeElement = Util.addElement(parent, UNION_TYPE);
            Iterator iter = ((UnionType)type).getMembers().iterator();
            while (iter.hasNext()) {
                writeType((Type)iter.next(), typeElement);
            }
        } else if (type instanceof ListType) {
            typeElement = Util.addElement(parent, LIST_TYPE);
            writeType(((ListType)type).getType(), typeElement);
        } else if (type instanceof EnumerationType) {
            typeElement = Util.addElement(parent, ENUMERATION_TYPE);
            Iterator iter = ((EnumerationType)type).getValues().iterator();
            while (iter.hasNext()) {
                EnumerationValue value = (EnumerationValue)iter.next();
                Util.addElement(typeElement, ENUMERATION_VALUE,
                    value.getName());
            }
        } else if (type instanceof ArrayType) {
            ArrayType arrayType = (ArrayType)type;
            typeElement = Util.addElement(parent, ARRAY_TYPE);
            writeType(arrayType.getType(), typeElement);
            typeElement.addAttribute(LOWER_INDEX,
                Integer.toString(arrayType.getLowerIndex()));
            typeElement.addAttribute(UPPER_INDEX,
                Integer.toString(arrayType.getUpperIndex()));
        } else if (type instanceof DeclaredType) {
            typeElement = Util.addElement(parent, DECLARED_TYPE);
            typeElement.addAttribute(ID, ((DeclaredType)type).getId());
        } else if (type instanceof ExternalReference) {
            writeExternalReference((ExternalReference)type, parent);
        } else {
            throw new XPDLSerializerException("Unsupported data type: " +
                type.getClass().getName());
        }
    }

    protected void writeWorkflowProcesses(List workflowProcesses,
        Element parent) throws XPDLSerializerException {

        if (workflowProcesses == null || workflowProcesses.size() == 0) {
            return;
        }

        Element wpsElement = Util.addElement(parent, WORKFLOW_PROCESSES);

        Iterator iter = workflowProcesses.iterator();
        while (iter.hasNext()) {
            writeWorkflowProcess((WorkflowProcess)iter.next(), wpsElement);
        }
    }

    protected void writeWorkflowProcess(WorkflowProcess wp, Element parent)
        throws XPDLSerializerException {

        Element wpElement = Util.addElement(parent, WORKFLOW_PROCESS);

        wpElement.addAttribute(ID, wp.getId());
        wpElement.addAttribute(NAME, wp.getName());
        wpElement.addAttribute(ACCESS_LEVEL, wp.getAccessLevel().toString());

        ProcessHeader processHeader = wp.getProcessHeader();
        if (processHeader == null) {
            throw new ElementRequiredException("Process header required");
        }
        writeProcessHeader(processHeader, wpElement);
        writeRedefinableHeader(wp.getRedefinableHeader(), wpElement);
        writeFormalParameters(wp.getFormalParameters(), wpElement);
        writeDataFields(wp.getDataFields(), wpElement);
        writeParticipants(wp.getParticipants(), wpElement);
        writeApplications(wp.getApplications(), wpElement);
        writeActivitySets(wp.getActivitySets(), wpElement);
        writeActivities(wp.getActivities(), wpElement);
        writeTransitions(wp.getTransitions(), wpElement);
        writeExtendedAttributes(wp.getExtendedAttributes(), wpElement);
        writeEventTypes(wp.getEventTypes(), wpElement);
    }

    protected void writeActivitySets(List activitySets, Element parent)
        throws XPDLSerializerException {

        if (activitySets == null || activitySets.size() == 0) {
            return;
        }

        Element activitySetsElement = Util.addElement(parent, ACTIVITY_SETS);

        Iterator iter = activitySets.iterator();
        while (iter.hasNext()) {
            writeActivitySet((ActivitySet)iter.next(), activitySetsElement);
        }
    }

    protected void writeActivitySet(ActivitySet activitySet, Element parent)
        throws XPDLSerializerException {

        Element activitySetElement = Util.addElement(parent, ACTIVITY_SET);

        activitySetElement.addAttribute(ID, activitySet.getId());

        writeActivities(activitySet.getActivities(), activitySetElement);
        writeTransitions(activitySet.getTransitions(), activitySetElement);
    }

    protected void writeActivities(List activities, Element parent)
        throws XPDLSerializerException {

        if (activities == null || activities.size() == 0) {
            return;
        }

        Element activitiesElement = Util.addElement(parent, ACTIVITIES);

        Iterator iter = activities.iterator();
        while (iter.hasNext()) {
            writeActivity((Activity)iter.next(), activitiesElement);
        }
    }

    protected void writeActivity(Activity activity, Element parent)
        throws XPDLSerializerException {

        Element activityElement = Util.addElement(parent, ACTIVITY);

        activityElement.addAttribute(ID, activity.getId());
        activityElement.addAttribute(NAME, activity.getName());

        Util.addElement(activityElement, DESCRIPTION,
            activity.getDescription());
        Util.addElement(activityElement, LIMIT, activity.getLimit());

        Implementation implementation = activity.getImplementation();
        if (implementation != null) {
            Element implementationElement = Util.addElement(activityElement,
                IMPLEMENTATION);
            if (implementation instanceof ToolSet) {
                Iterator tools = ((ToolSet)implementation)
                    .getTools().iterator();
                while (tools.hasNext()) {
                    Tool tool = (Tool)tools.next();
                    Element toolElement = Util.addElement(implementationElement,
                        TOOL);
                    toolElement.addAttribute(ID, tool.getId());
                    toolElement.addAttribute(TYPE,
                        tool.getToolType().toString());
                    Util.addElement(toolElement, DESCRIPTION,
                        tool.getDescription());
                    writeActualParameters(tool.getActualParameters(),
                        toolElement);
                }
            } else if (implementation instanceof SubFlow) {
                SubFlow subFlow = ((SubFlow)implementation);
                Element subFlowElement = Util.addElement(implementationElement,
                    SUBFLOW);
                subFlowElement.addAttribute(ID, subFlow.getId());
                subFlowElement.addAttribute(EXECUTION,
                    subFlow.getExecution().toString());
                writeActualParameters(subFlow.getActualParameters(),
                    subFlowElement);
            } else if (implementation instanceof NoImplementation) {
                Util.addElement(implementationElement, NO);
            } else {
                throw new XPDLSerializerException(
                    "Unknown implementation type: " +
                    implementation.getClass());
            }
        }

        Route route = activity.getRoute();
        if (route != null) {
            if (implementation != null) {
                throw new XPDLSerializerException(
                    "Activity cannot contain both a route and an implementation");
            }

            Util.addElement(activityElement, ROUTE);
        }

        BlockActivity blockActivity = activity.getBlockActivity();
        if (blockActivity != null) {
            if (implementation != null || route != null) {
                throw new XPDLSerializerException(
                    "Activity cannot contain a route or an implementation as well as a BlockActivity");
            }

            Element baElement = activityElement.addElement(BLOCKACTIVITY_QNAME);
            baElement.addAttribute(BLOCKID, blockActivity.getBlockId());
        }

        Util.addElement(activityElement, PERFORMER, activity.getPerformer());

        AutomationMode startMode = activity.getStartMode();
        Element startModeElement = Util.addElement(activityElement, START_MODE);
        if (startMode == AutomationMode.MANUAL) {
            Util.addElement(startModeElement, MANUAL);
        } else {
            Util.addElement(startModeElement, AUTOMATIC);
        }

        AutomationMode finishMode = activity.getFinishMode();
        Element finishModeElement = Util.addElement(activityElement,
            FINISH_MODE);
        if (finishMode == AutomationMode.MANUAL) {
            Util.addElement(finishModeElement, MANUAL);
        } else {
            Util.addElement(finishModeElement, AUTOMATIC);
        }

        Util.addElement(activityElement, PRIORITY, activity.getPriority());

        writeDeadlines(activity.getDeadlines(), activityElement);
        writeSimulationInformation(activity.getSimulationInformation(),
            activityElement);

        Util.addElement(activityElement, ICON, activity.getIcon());
        Util.addElement(activityElement, DOCUMENTATION,
            activity.getDocumentation());

        writeTransitionRestrictions(activity.getTransitionRestrictions(),
            activityElement);
        writeExtendedAttributes(activity.getExtendedAttributes(),
            activityElement);
        if (blockActivity != null)
            writeLoop(blockActivity.getLoop(), activityElement);

        CompletionStrategy strat = activity.getCompletionStrategy();
        if (strat != null)
            writeCompletionStrategy(strat, activityElement);
    }

    protected void writeCompletionStrategy(CompletionStrategy strat,
        Element parent) {

        if (strat == null) {
            return;
        }

        // For now, our parent element must be an ExtendedAttribute.
        // Check whether the ExtendedAttributes element is already present.
        // If not, add it now.
        Element extendedAttributesElement = parent.element(
            EXTENDED_ATTRIBUTES_QNAME);
        if (extendedAttributesElement == null) {
            extendedAttributesElement = Util.addElement(parent,
                EXTENDED_ATTRIBUTES);
        }
        parent = Util.addElement(extendedAttributesElement, EXTENDED_ATTRIBUTE);
        parent.addAttribute(NAME, OBE_COMPLETION_STRATEGY);
        parent.addAttribute(VALUE, strat.toString());
    }

    protected void writeLoop(Loop loop, Element parent) {
        if (loop == null) {
            return;
        }

        // For now, our parent element must be an ExtendedAttribute.
        // Check whether the ExtendedAttributes element is already present.
        // If not, add it now.
        Element extendedAttributesElement = parent.element(
            EXTENDED_ATTRIBUTES_QNAME);
        if (extendedAttributesElement == null) {
            extendedAttributesElement = Util.addElement(parent,
                EXTENDED_ATTRIBUTES);
        }
        parent = Util.addElement(extendedAttributesElement, EXTENDED_ATTRIBUTE);
        parent.addAttribute(NAME, OBE_LOOP);

        Element loopElement = parent.addElement(LOOP_QNAME);
        LoopBody body = loop.getBody();
        if (body instanceof ForEach) {
            ForEach forEach = (ForEach)body;
            Element forEachElement = loopElement.addElement(FOREACH_QNAME);
            forEachElement.addAttribute(DATA_FIELD, forEach.getDataField());
            Element inElement = forEachElement.addElement(IN_QNAME);
            inElement.addText(forEach.getExpr());
        } else if (body instanceof Until) {
            Element untilElement = loopElement.addElement(UNTIL_QNAME);
            writeCondition(((Until)body).getCondition(), untilElement);
        } else if (body instanceof While) {
            Element whileElement = loopElement.addElement(WHILE_QNAME);
            writeCondition(((While)body).getCondition(), whileElement);
        }
    }

    protected void writeTransitions(List transitions, Element parent)
        throws XPDLSerializerException {

        if (transitions == null || transitions.size() == 0) {
            return;
        }

        Element transitionsElement = Util.addElement(parent, TRANSITIONS);

        Iterator iter = transitions.iterator();
        while (iter.hasNext()) {
            writeTransition((Transition)iter.next(), transitionsElement);
        }
    }

    protected void writeTransition(Transition transition, Element parent)
        throws XPDLSerializerException {

        Element transitionElement = Util.addElement(parent, TRANSITION);

        transitionElement.addAttribute(ID, transition.getId());
        transitionElement.addAttribute(FROM, transition.getFrom());
        transitionElement.addAttribute(TO, transition.getTo());

        if (transition.getName() != null)
            transitionElement.addAttribute(NAME, transition.getName());

        writeCondition(transition.getCondition(), transitionElement);

        if (transition.getDescription() != null) {
            Util.addElement(transitionElement, DESCRIPTION,
                transition.getDescription());
        }

        writeExtendedAttributes(transition.getExtendedAttributes(),
            transitionElement);

        writeEvent(transition.getEvent(), transitionElement);
        writeExecutionType(transition.getExecution(), transitionElement);
    }

    protected void writeEvent(Event event, Element parent)
        throws XPDLSerializerException {

        if (event == null)
            return;

        // For now, our parent element must be an ExtendedAttribute.
        // Check whether the ExtendedAttributes element is already present.
        // If not, add it now.
        Element extendedAttributesElement = parent.element(
            EXTENDED_ATTRIBUTES_QNAME);
        if (extendedAttributesElement == null) {
            extendedAttributesElement = Util.addElement(parent,
                EXTENDED_ATTRIBUTES);
        }
        parent = Util.addElement(extendedAttributesElement, EXTENDED_ATTRIBUTE);
        parent.addAttribute(NAME, OBE_EVENT);

        Element eventElement = parent.addElement(EVENT_QNAME);
        eventElement.addAttribute(ID, event.getId());
        if (event.getDataField() != null) {
            eventElement.addAttribute(DATA_FIELD, event.getDataField());
        }

        List actualParameters = event.getActualParameters();
        if (actualParameters != null) {
            writeActualParameters(actualParameters, eventElement);
        }
    }

    protected void writeExecutionType(ExecutionType execType, Element parent)
        throws XPDLSerializerException {

        if (execType == null)
            return;

        // For now, our parent element must be an ExtendedAttribute.
        // Check whether the ExtendedAttributes element is already present.
        // If not, add it now.
        Element extendedAttributesElement = parent.element(
            EXTENDED_ATTRIBUTES_QNAME);
        if (extendedAttributesElement == null) {
            extendedAttributesElement = Util.addElement(parent,
                EXTENDED_ATTRIBUTES);
        }
        parent = Util.addElement(extendedAttributesElement, EXTENDED_ATTRIBUTE);
        parent.addAttribute(NAME, EXECUTION);
        parent.addAttribute(VALUE, execType.toString());
    }

    protected void writeTransitionRestrictions(List transitionRestrictions,
        Element parent) {

        if (transitionRestrictions == null ||
            transitionRestrictions.size() == 0) {

            return;
        }

        Element transitionRestrictionsElement = Util.addElement(
            parent, TRANSITION_RESTRICTIONS);

        Iterator iter = transitionRestrictions.iterator();
        while (iter.hasNext()) {
            writeTransitionRestriction((TransitionRestriction)iter.next(),
                transitionRestrictionsElement);
        }
    }

    protected void writeTransitionRestriction(TransitionRestriction
        transitionRestriction, Element parent) {
        Element transitionRestrictionElement = Util.addElement(
            parent, TRANSITION_RESTRICTION);
        writeJoin(transitionRestriction.getJoin(),
            transitionRestrictionElement);
        writeSplit(transitionRestriction.getSplit(),
            transitionRestrictionElement);
    }

    protected void writeJoin(Join join, Element parent) {
        if (join == null) {
            return;
        }

        Element joinElement = Util.addElement(parent, JOIN);

        JoinType joinType = join.getType();
        if (joinType != null) {
            joinElement.addAttribute(TYPE, joinType.toString());
        }
    }

    protected void writeSplit(Split split, Element parent) {
        if (split == null) {
            return;
        }

        Element splitElement = Util.addElement(parent, SPLIT);

        SplitType splitType = split.getType();
        if (splitType != null) {
            splitElement.addAttribute(TYPE, splitType.toString());
        }

        writeTransitionReferences(split.getTransitionReferences(),
            splitElement);
    }

    protected void writeTransitionReferences(List transitionReferences,
        Element parent) {

        if (transitionReferences == null || transitionReferences.size() == 0) {
            return;
        }

        Element transitionReferencesElement = Util.addElement(parent,
            TRANSITION_REFERENCES);

        Iterator iter = transitionReferences.iterator();
        while (iter.hasNext()) {
            String transitionId = (String)iter.next();

            Element transitionRefElement = Util.addElement(
                transitionReferencesElement, TRANSITION_REFERENCE);

            transitionRefElement.addAttribute(ID, transitionId);
        }
    }

    protected void writeProcessHeader(ProcessHeader header, Element parent) {
        Element headerElement = Util.addElement(parent, PROCESS_HEADER);

        DurationUnit durationUnit = header.getDurationUnit();
        if (durationUnit != null) {
            headerElement.addAttribute(DURATION_UNIT, durationUnit.toString());
        }

        Util.addElement(headerElement, CREATED, header.getCreated());
        Util.addElement(headerElement, DESCRIPTION, header.getDescription());
        Util.addElement(headerElement, PRIORITY, header.getPriority());
        Util.addElement(headerElement, LIMIT, header.getLimit());
        Util.addElement(headerElement, VALID_FROM, header.getValidFrom());
        Util.addElement(headerElement, VALID_TO, header.getValidTo());
        writeTimeEstimation(header.getTimeEstimation(), headerElement);
    }

    protected void writeDeadlines(List deadlines, Element parent) {
        if (deadlines == null) {
            log.debug("No Deadlines found");
            return;
        }

        for (Iterator i = deadlines.iterator(); i.hasNext();)
            writeDeadline((Deadline)i.next(), parent);
    }

    protected void writeDeadline(Deadline deadline, Element parent) {
        if (deadline == null) {
            log.debug("No Deadline found");
            return;
        }

        log.debug("Writing Deadline element");
        Element deadlineElement = Util.addElement(parent, DEADLINE);
        deadlineElement.addAttribute(EXECUTION,
            deadline.getExecutionType().toString());

        Util.addElement(deadlineElement, DEADLINE_CONDITION,
            deadline.getDeadlineCondition());
        Util.addElement(deadlineElement, EXCEPTION_NAME,
            deadline.getExceptionName());
    }

    protected void writeTimeEstimation(TimeEstimation timeEstimation,
        Element parent) {
        if (timeEstimation == null) {
            return;
        }

        Element timeEstimationElement = Util.addElement(parent,
            TIME_ESTIMATION);
        Util.addElement(timeEstimationElement, WAITING_TIME,
            timeEstimation.getWaitingTime());
        Util.addElement(timeEstimationElement, WORKING_TIME,
            timeEstimation.getWorkingTime());
        Util.addElement(timeEstimationElement, DURATION,
            timeEstimation.getDuration());
    }

    protected void writeSimulationInformation(
        SimulationInformation simulationInformation, Element parent) {

        if (simulationInformation == null) {
            return;
        }

        Element simulationInformationElement = Util.addElement(parent,
            SIMULATION_INFORMATION);

        Util.addElement(simulationInformationElement, COST,
            simulationInformation.getCost());

        writeTimeEstimation(simulationInformation.getTimeEstimation(),
            simulationInformationElement);
    }

    protected void writeCondition(Condition condition, Element parent) {
        if (condition == null) {
            return;
        }

        Element conditionElement = parent.addElement(CONDITION_QNAME);
        ConditionType type = condition.getType();
        if (type != null)
            conditionElement.addAttribute(TYPE, type.toString());
        if (condition.getValue() != null)
            conditionElement.addText(condition.getValue());
        Iterator xpressions = condition.getXpressions().iterator();
        while (xpressions.hasNext()) {
            Xpression xpression = (Xpression)xpressions.next();
            Util.addElement(conditionElement, XPRESSION, xpression.getValue());
        }
    }

    protected void writeExtendedAttributes(Map extendedAttributes,
        Element parent) {

        if (extendedAttributes == null || extendedAttributes.size() == 0) {
            return;
        }

        Element extendedAttributesElement = parent.addElement(
            EXTENDED_ATTRIBUTES_QNAME);

        Iterator keys = extendedAttributes.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = extendedAttributes.get(key);

            Element extendedAttributeElement =
                Util.addElement(extendedAttributesElement, EXTENDED_ATTRIBUTE);
            extendedAttributeElement.addAttribute(NAME, key.toString());
            if (value != null)
                extendedAttributeElement.addAttribute(VALUE, value.toString());
        }
    }

    protected void writeFormalParameters(List formalParameters, Element parent)
        throws XPDLSerializerException {

        if (formalParameters == null || formalParameters.size() == 0) {
            return;
        }

        Element formalParametersElement = parent.addElement(
            FORMAL_PARAMETERS_QNAME);
        Iterator iter = formalParameters.iterator();
        while (iter.hasNext()) {
            FormalParameter formalParameter = (FormalParameter)iter.next();
            Element formalParameterElement = Util.addElement(
                formalParametersElement, FORMAL_PARAMETER);

            formalParameterElement.addAttribute(ID, formalParameter.getId());
            if (formalParameter.getIndex() != 0) {
                formalParameterElement.addAttribute(INDEX, Integer.toString(
                    formalParameter.getIndex()));
            }
            formalParameterElement.addAttribute(MODE,
                formalParameter.getMode().toString());

            writeType(formalParameter.getDataType().getType(),
                Util.addElement(formalParameterElement, DATA_TYPE));

            Util.addElement(formalParameterElement, DESCRIPTION,
                formalParameter.getDescription());
        }
    }

    protected void writeActualParameters(List actualParameters, Element parent)
        throws XPDLSerializerException {

        if (actualParameters == null || actualParameters.size() == 0) {
            return;
        }

        Element actualParametersElement = Util.addElement(parent,
            ACTUAL_PARAMETERS);
        Iterator iter = actualParameters.iterator();
        while (iter.hasNext()) {
            ActualParameter actualParameter = (ActualParameter)iter.next();
            Util.addElement(actualParametersElement, ACTUAL_PARAMETER,
                actualParameter.getText());
        }
    }

    protected void writeExternalReference(ExternalReference externalReference,
        Element parent) throws XPDLSerializerException {

        if (externalReference == null) {
            return;
        }

        Element element = parent.addElement(EXTERNAL_REFERENCE_QNAME);
        element.addAttribute(LOCATION, externalReference.getLocation());
        String xref = externalReference.getXref();
        if (xref != null) {
            element.addAttribute(XREF, xref);
        }
        String namespace = externalReference.getNamespace();
        if (namespace != null) {
            element.addAttribute(NAMESPACE, namespace);
        }
    }
}