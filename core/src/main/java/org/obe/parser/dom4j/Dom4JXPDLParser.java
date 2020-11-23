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

package org.obe.parser.dom4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.obe.AccessLevel;
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
import org.obe.activity.ImplementationType;
import org.obe.activity.Loop;
import org.obe.activity.NoImplementation;
import org.obe.activity.Route;
import org.obe.activity.SubFlow;
import org.obe.activity.Tool;
import org.obe.activity.ToolSet;
import org.obe.activity.ToolType;
import org.obe.activity.Until;
import org.obe.activity.While;
import org.obe.application.Application;
import org.obe.application.BasicApplication;
import org.obe.condition.BasicCondition;
import org.obe.condition.BasicXpression;
import org.obe.condition.Condition;
import org.obe.condition.ConditionType;
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
import org.obe.parser.ElementRequiredException;
import org.obe.parser.XPDLParser;
import org.obe.parser.XPDLParserException;
import org.obe.participant.BasicParticipant;
import org.obe.participant.Participant;
import org.obe.participant.ParticipantType;
import org.obe.transition.BasicTransition;
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
import org.obe.util.InstantiationType;
import org.obe.util.SimulationInformation;
import org.obe.util.TimeEstimation;
import org.obe.workflow.BasicWorkflowProcess;
import org.obe.workflow.ProcessHeader;
import org.obe.workflow.WorkflowProcess;

/**
 * An implementation of the XPDLParser interface, using Dom4J.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public class Dom4JXPDLParser implements XPDLParser {
    private static Log log = LogFactory.getLog(Dom4JXPDLParser.class);

    private DurationUnit defaultDurationUnit = DurationUnit.MINUTE;
    private InstantiationType defaultInstantiationType = InstantiationType.ONCE;

    /** Construct a new Dom4JXPDLParser. */

    public Dom4JXPDLParser() {
    }

    public Package parse(InputStream in) throws IOException, XPDLParserException {
        return parse(new InputStreamReader(in));
    }

    /** Parse the XPDL document which is provided by the given InputStream.
     @param in The XPDL character stream
     @return The Workflow Package
     @throws IOException Any I/O Exception
     @throws XPDLParserException Any parser exception
     */
    public Package parse(Reader in) throws IOException, XPDLParserException {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);

            Element packageElement = document.getRootElement();

            // load the package header
            PackageHeader packageHeader = createPackageHeader(
                Util.child(packageElement, PACKAGE_HEADER));

            // construct the Package
            Package pkg = new Package(
                packageElement.attributeValue(ID),
                packageElement.attributeValue(NAME),
                packageHeader
            );

            // load the redefinable header
            pkg.setRedefinableHeader(createRedefinableHeader(
                Util.child(packageElement, REDEFINABLE_HEADER)));

            // load the conformance class
            pkg.setConformanceClass(createConformanceClass(
                Util.child(packageElement, CONFORMANCE_CLASS)));

            pkg.setScript(createScript(
                Util.child(packageElement, SCRIPT)));

            loadExternalPackages(pkg.getExternalPackages(),
                Util.child(packageElement, EXTERNAL_PACKAGES));

            loadTypeDeclarations(pkg.getTypeDeclarations(),
                Util.child(packageElement, TYPE_DECLARATIONS));

            loadParticipants(pkg.getParticipants(),
                Util.child(packageElement, PARTICIPANTS));

            log.debug("Loading applications in package " + pkg.getId() + "...");
            loadApplications(pkg.getApplications(),
                Util.child(packageElement, APPLICATIONS));
            log.debug(pkg.getApplications().size() + " applications loaded");

            loadDataFields(pkg.getDataFields(),
                Util.child(packageElement, DATA_FIELDS));

            loadWorkflowProcesses(pkg, pkg.getWorkflowProcesses(),
                Util.child(packageElement, WORKFLOW_PROCESSES));

            Map extAttrs = pkg.getExtendedAttributes();
            loadExtendedAttributes(extAttrs,
                Util.child(packageElement, EXTENDED_ATTRIBUTES));

            // For now, event types are defined using the obe:EventTypes
            // extended attribute.
            List eventTypes = (List)extAttrs.remove(OBE_EVENT_TYPES);
            if (eventTypes != null)
                pkg.getEventTypes().addAll(eventTypes);

            return pkg;
        } catch (DocumentException e) {
            throw new XPDLParserException("Error parsing document.", e);
        }
    }

    /** Get the default duration unit.  If no value is set then the duration
     will default to "minute".

     @return The default DurationUnit
     */

    public DurationUnit getDefaultDurationUnit() {
        return defaultDurationUnit;
    }

    /** Set the default duration unit.

     @param defaultDurationUnit The new default duration unit
     */

    public void setDefaultDurationUnit(DurationUnit defaultDurationUnit) {
        this.defaultDurationUnit = defaultDurationUnit;
    }

    // Header-related configuration

    /** Create the package header from the given element.  This method will
     throw an XPDLParserException if the Package header element is
     null.

     @param element The package header element
     @throws XPDLParserException Thrown if the element is null
     */

    protected PackageHeader createPackageHeader(Element element) throws
        XPDLParserException {
        if (element == null) {
            throw new ElementRequiredException(PACKAGE_HEADER,
                "Package header required but not found");
        }

        PackageHeader packageHeader = new PackageHeader();
        packageHeader.setXPDLVersion(Util.elementAsString(element, XPDL_VERSION));
        packageHeader.setVendor(Util.elementAsString(element, VENDOR));
        packageHeader.setCreated(Util.elementAsDate(element, CREATED));
        packageHeader.setDescription(Util.elementAsString(element, DESCRIPTION));
        packageHeader.setDocumentation(Util.elementAsURL(element, DOCUMENTATION));
        packageHeader.setPriorityUnit(Util.elementAsString(element, PRIORITY_UNIT));
        packageHeader.setCostUnit(Util.elementAsString(element, COST_UNIT));
        return packageHeader;
    }

    protected RedefinableHeader createRedefinableHeader(Element element) throws
        XPDLParserException {
        if (element == null) {
            log.debug("Redefinable header not found");
            return null;
        }

        RedefinableHeader redefinableHeader = new RedefinableHeader();
        redefinableHeader.setAuthor(Util.elementAsString(element, AUTHOR));
        redefinableHeader.setVersion(Util.elementAsString(element, VERSION));
        redefinableHeader.setCodepage(Util.elementAsString(element, CODEPAGE));
        redefinableHeader.setCountrykey(Util.elementAsString(element, COUNTRYKEY));

        String pubStatusString = element.attributeValue(PUBLICATION_STATUS);
        if (pubStatusString != null) {
            PublicationStatus publicationStatus = PublicationStatus.fromString(
                pubStatusString);
            redefinableHeader.setPublicationStatus(publicationStatus);
        }

        loadResponsibles(redefinableHeader.getResponsibles(),
            Util.child(element, RESPONSIBLES));

        return redefinableHeader;
    }

    /** Create a ConformanceClass object from the given element.

     @param element The ConformanceClass element
     @throws XPDLParserException Any parser exception
     */

    protected ConformanceClass createConformanceClass(Element element) throws
        XPDLParserException {
        if (element == null) {
            log.debug("Conformance class header not found");
            return null;
        }

        log.debug("ConformanceClass element: " + element);
        ConformanceClass cc = new ConformanceClass();
        GraphConformance gc = GraphConformance.fromString(
            element.attributeValue(GRAPH_CONFORMANCE));
        cc.setGraphConformance(gc);
        return cc;
    }

    /** Create a Script object from the given Element.

     @param element The Element
     @return the Script object
     @throws XPDLParserException
     */

    protected Script createScript(Element element) throws XPDLParserException {
        if (element == null) {
            log.debug("Script not found");
            return null;
        }

        Script script = new Script(element.attributeValue(TYPE));

        script.setVersion(element.attributeValue(VERSION));
        script.setGrammar(element.attributeValue(GRAMMAR));

        return script;
    }

    // External Packages

    protected void loadExternalPackages(List externalPackages, Element element)
        throws XPDLParserException {
        if (element == null) {
            return;
        }

        loadExternalPackages(externalPackages, Util.children(element, EXTERNAL_PACKAGE));
    }

    protected void loadExternalPackages(List externalPackages, List elements)
        throws XPDLParserException {
        externalPackages.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            try {
                Element xpackElement = (Element)iter.next();
                externalPackages.add(createExternalPackage(xpackElement));
            } catch (Throwable t) {
                log.error("Error loading external package");
            }
        }

        if (externalPackages.size() == 0) {
            log.debug("External packages element found but no external packages found");
        }
    }

    protected ExternalPackage createExternalPackage(Element element) throws
        XPDLParserException {
        String href = element.attributeValue(HREF);
        log.debug("Loading external package from " + href);

        Package p = null;

        try {
            p = parse(new URL(href).openStream());
            log.debug("Loaded external package " + p.getId());
        } catch (Exception e) {
            log.error("Error loading external package: " + e.getMessage());
            throw new XPDLParserException(
                "Error loading external package " + href, e);
        }

        ExternalPackage xpackage = new ExternalPackage(href, p);
        loadExtendedAttributes(xpackage.getExtendedAttributes(),
            Util.child(element, EXTENDED_ATTRIBUTES));

        return xpackage;
    }

    // Responsibles

    protected void loadResponsibles(List responsibles, Element element) {
        if (element == null) {
            return;
        }

        loadResponsibles(responsibles, Util.children(element, RESPONSIBLE));
    }

    protected void loadResponsibles(List responsibles, List elements) {
        responsibles.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element responsibleElement = (Element)iter.next();
            responsibles.add(responsibleElement.getTextTrim());
        }

        if (responsibles.size() == 0) {
            log.debug("Responsibles element found but no responsibles found");
        }
    }

    // Participant-related configuration

    /** Load participants from the given Element.  The element should represent
     a collection of participants (which is identified by the "Participants"
     tag).

     @param participants The list of participants
     @param element The "Participants" element
     @throws XPDLParserException
     */

    protected void loadParticipants(List participants, Element element) throws
        XPDLParserException {
        if (element == null) {
            return;
        }

        loadParticipants(participants, Util.children(element, PARTICIPANT));
    }

    /** Load the participant objects from the given list of "Participant" elements.

     @param participants The list of participants
     @param elements The list of "Participant" elements
     @throws XPDLParserException
     */

    protected void loadParticipants(List participants, List elements) throws
        XPDLParserException {
        participants.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element participantElement = (Element)iter.next();
            participants.add(createParticipant(participantElement));
        }
    }

    protected Participant createParticipant(Element element) throws
        XPDLParserException {
        Element typeElement = Util.child(element, PARTICIPANT_TYPE);
        if (typeElement == null) {
            throw new ElementRequiredException(PARTICIPANT_TYPE,
                "Participant type required");
        }

        String typeString = typeElement.attributeValue(TYPE);
        if (typeString == null) {
            throw new ElementRequiredException(TYPE,
                "Participant type identifier required");
        }

        ParticipantType participantType = ParticipantType.fromString(typeString);
        if (participantType == null) {
            throw new XPDLParserException("Unknown participant type: " + typeString);
        }

        Participant participant = new BasicParticipant(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            participantType
        );

        participant.setDescription(Util.elementAsString(element, DESCRIPTION));

        loadExtendedAttributes(participant.getExtendedAttributes(),
            Util.child(element, EXTENDED_ATTRIBUTES));

        return participant;
    }

    // Application-related configuration

    /** Load applications from the given Element.  The element should represent
     a collection of applications (which is identified by the "Applications"
     tag).

     @param applications The list of applications
     @param element The "Applications" element
     @throws XPDLParserException
     */

    protected void loadApplications(List applications, Element element) throws
        XPDLParserException {
        if (element == null) {
            return;
        }

        loadApplications(applications, Util.children(element, APPLICATION));
    }

    /** Load the applications objects from the given list of "Application"
     elements.

     @param applications The list of applications
     @param elements The list of "Application" elements
     @throws XPDLParserException
     */

    protected void loadApplications(List applications, List elements) throws
        XPDLParserException {
        applications.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element applicationElement = (Element)iter.next();
            applications.add(createApplication(applicationElement));
        }
    }

    protected Application createApplication(Element element)
        throws XPDLParserException {

        BasicApplication application = new BasicApplication(
            element.attributeValue(ID), element.attributeValue(NAME));

        log.debug("Created application " + application.getId());

        application.setDescription(Util.elementAsString(element, DESCRIPTION));

        if (Util.child(element, FORMAL_PARAMETERS) != null) {
            loadFormalParameters(application.getFormalParameters(),
                Util.child(element, FORMAL_PARAMETERS));
        } else if (Util.child(element, EXTERNAL_REFERENCE) != null) {
            application.setExternalReference(createExternalReference(
                Util.child(element, EXTERNAL_REFERENCE)));
        }

        loadExtendedAttributes(application.getExtendedAttributes(),
            Util.child(element, EXTENDED_ATTRIBUTES));

        return application;
    }

    protected void loadEventTypes(List eventTypes, Element element)
        throws XPDLParserException {

        if (element == null)
            throw new XPDLParserException("obe:EventTypes element missing");

        loadEventTypes(eventTypes, element.elements(EVENT_TYPE_QNAME));
    }

    protected void loadEventTypes(List eventTypes, List elements)
        throws XPDLParserException {

        eventTypes.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element eventElem = (Element)iter.next();
            eventTypes.add(createEventType(eventElem));
        }
    }

    protected EventType createEventType(Element element)
        throws XPDLParserException {

        EventType eventType = new EventType(element.attributeValue(ID),
            element.attributeValue(NAME));

        log.debug("Created event type " + eventType.getId());

        eventType.setDescription(element.elementTextTrim(DESCRIPTION_QNAME));

        Element formalParametersElem = element.element(FORMAL_PARAMETERS_QNAME);
        if (formalParametersElem != null) {
            loadFormalParameters(eventType.getFormalParameters(),
                formalParametersElem);
        } else {
            eventType.setExternalReference(createExternalReference(
                element.element(EXTERNAL_REFERENCE_QNAME)));
        }

        return eventType;
    }

    // WorkflowProcess related

    /** Load workflow processes from the given Element.  The element should
     represent a collection of workflow process (which is identified by the
     "WorkflowProcesses" tag).

     @param workflowProcesses The list of workflow processes
     @param element The "WorkflowProcesses" element
     @throws XPDLParserException
     */

    protected void loadWorkflowProcesses(Package pkg, List workflowProcesses,
        Element element) throws XPDLParserException {

        if (element == null)
            return;

        loadWorkflowProcesses(pkg, workflowProcesses,
            Util.children(element, WORKFLOW_PROCESS));
    }

    /** Load the workflow process objects from the given list of
     "WorkflowProcess" elements.

     @param workflowProcesses The list of workflow processes
     @param elements The list of "WorkflowProcess" elements
     @throws XPDLParserException
     */

    protected void loadWorkflowProcesses(Package pkg, List workflowProcesses,
        List elements) throws XPDLParserException {

        workflowProcesses.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element workflowProcessElement = (Element)iter.next();
            workflowProcesses.add(createWorkflowProcess(pkg,
                workflowProcessElement));
        }
    }

    protected WorkflowProcess createWorkflowProcess(Package pkg,
        Element element) throws XPDLParserException {

        ProcessHeader processHeader = createProcessHeader(
            Util.child(element, PROCESS_HEADER));

        BasicWorkflowProcess wp = new BasicWorkflowProcess(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            pkg, processHeader
        );

        log.debug("Created workflow process [id=" + wp.getId() + ",name=" +
            wp.getName() + "]");

        wp.setRedefinableHeader(createRedefinableHeader(
            Util.child(element, REDEFINABLE_HEADER)));

        wp.setAccessLevel(AccessLevel.fromString(
            element.attributeValue(ACCESS_LEVEL)));

        loadFormalParameters(wp.getFormalParameters(),
            Util.child(element, FORMAL_PARAMETERS));

        loadDataFields(wp.getDataFields(), Util.child(element, DATA_FIELDS));
        loadParticipants(wp.getParticipants(),
            Util.child(element, PARTICIPANTS));

        log.debug("Loading applications in process " + wp.getId() + "...");
        loadApplications(wp.getApplications(),
            Util.child(element, APPLICATIONS));
        log.debug(wp.getApplications().size() + " applications loaded");

        log.debug("Loading activity sets in process " + wp.getId() + "...");
        loadActivitySets(wp, wp.getActivitySets(),
            Util.child(element, ACTIVITY_SETS));
        log.debug(wp.getActivitySets().size() + " activity sets loaded");

        log.debug("Loading activities in process " + wp.getId() + "...");
        loadActivities(wp, wp.getActivities(), Util.child(element, ACTIVITIES));
        log.debug(wp.getActivities().size() + " activities loaded");

        log.debug("Loading transitions in process " + wp.getId() + "...");
        loadTransitions(wp.getTransitions(), Util.child(element, TRANSITIONS));
        log.debug(wp.getTransitions().size() + " transitions loaded");

        Map extAttrs = wp.getExtendedAttributes();
        loadExtendedAttributes(extAttrs,
            Util.child(element, EXTENDED_ATTRIBUTES));

        // For now, event types are defined using the obe:EventTypes
        // extended attribute.
        List eventTypes = (List)extAttrs.remove(OBE_EVENT_TYPES);
        if (eventTypes != null)
            wp.getEventTypes().addAll(eventTypes);

        log.debug("Resolving references in process " + wp.getId() + "...");
        wp.resolveReferences();

        return wp;
    }

    protected ProcessHeader createProcessHeader(Element element)
        throws XPDLParserException {

        if (element == null) {
            throw new ElementRequiredException(PROCESS_HEADER,
                "Process header required");
        }

        ProcessHeader processHeader = new ProcessHeader();

        // set the duration unit to use if no duration unit is specified
        DurationUnit durationUnit = defaultDurationUnit;
        String durationUnitString = element.attributeValue(DURATION_UNIT);
        if (durationUnitString != null) {
            durationUnit = DurationUnit.fromString(durationUnitString);
        }
        processHeader.setDurationUnit(durationUnit);

        // set header properties
        processHeader.setCreated(Util.elementAsDate(element, CREATED));
        processHeader.setPriority(Util.elementAsString(element, PRIORITY));
        processHeader.setLimit(Util.elementAsDuration(element, LIMIT));
        processHeader.setValidFrom(
            Util.elementAsDuration(element, VALID_FROM));
        processHeader.setValidTo(Util.elementAsDuration(element, VALID_TO));
        processHeader.setDescription(
            Util.elementAsString(element, DESCRIPTION));

        processHeader.setTimeEstimation(createTimeEstimation(
            Util.child(element, TIME_ESTIMATION)));

        return processHeader;
    }

    // ActivitySet

    /** Load activity sets from the given Element.  The element should represent
     a collection of activity sets (which is identified by the "ActivitySets"
     tag).

     @param activitySets The list of activity sets
     @param element The "ActivitySets" element
     @throws XPDLParserException
     */

    protected void loadActivitySets(WorkflowProcess wp, List activitySets,
        Element element) throws XPDLParserException {

        if (element == null) {
            log.debug("ActivitySets element was null");
            return;
        }

        loadActivitySets(wp, activitySets,
            Util.children(element, ACTIVITY_SET));
    }

    /** Load the activity set objects from the given list of "ActivitySet"
     elements.

     @param activitySets The list of activity sets
     @param elements The list of "Activity" elements
     @throws XPDLParserException
     */

    protected void loadActivitySets(WorkflowProcess wp, List activitySets,
        List elements) throws XPDLParserException {

        activitySets.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element activitySetElement = (Element)iter.next();
            activitySets.add(createActivitySet(wp, activitySetElement));
        }
    }

    protected ActivitySet createActivitySet(WorkflowProcess wp, Element element)
        throws XPDLParserException {

        ActivitySet activitySet = new ActivitySet(
            element.attributeValue(ID));

        loadActivities(wp, activitySet.getActivities(),
            Util.child(element, ACTIVITIES));
        loadTransitions(activitySet.getTransitions(),
            Util.child(element, TRANSITIONS));

        return activitySet;
    }

    // Activity Related

    /** Load actvities from the given Element.  The element should represent
     a collection of activities (which is identified by the "Activities"
     tag).

     @param activities The list of activities
     @param element The "Activities" element
     @throws XPDLParserException
     */

    protected void loadActivities(WorkflowProcess wp, List activities,
        Element element) throws XPDLParserException {

        if (element == null) {
            log.debug("Activites element was null");
            return;
        }

        loadActivities(wp, activities, Util.children(element, ACTIVITY));
    }

    /** Load the activity objects from the given list of "Activity"
     elements.

     @param activities The list of activites
     @param elements The list of "Activity" elements
     @throws XPDLParserException
     */

    protected void loadActivities(WorkflowProcess wp, List activities,
        List elements) throws XPDLParserException {

        activities.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element activityElement = (Element)iter.next();
            activities.add(createActivity(wp, activityElement));
        }
    }

    protected Activity createActivity(WorkflowProcess wp, Element element)
        throws XPDLParserException {

        Activity activity = new Activity(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            wp
        );

        log.debug("Created activity [id=" + activity.getId() + ",name=" +
            activity.getName() + "]");

        activity.setDescription(Util.elementAsString(element, DESCRIPTION));
        activity.setLimit(Util.elementAsDuration(element, LIMIT));
        activity.setPerformer(Util.elementAsString(element, PERFORMER));
        activity.setPriority(Util.elementAsString(element, PRIORITY));
        activity.setDocumentation(Util.elementAsURL(element, DOCUMENTATION));
        activity.setIcon(Util.elementAsURL(element, ICON));

        loadDeadlines(activity.getDeadlines(),
            Util.children(element, DEADLINE));
        activity.setSimulationInformation(createSimulationInformation(
            Util.child(element, SIMULATION_INFORMATION)));

        // implementation
        Element implementationElement = Util.child(element, IMPLEMENTATION);
        if (implementationElement != null) {
            activity.setImplementation(
                createImplementation(implementationElement));
        }

        // route (only if implementation not specified)
        Element routeElement = Util.child(element, ROUTE);
        if (routeElement != null) {
            if (implementationElement != null) {
                // TODO: perform validating parse to catch this error earlier.
                log.warn("Implementation and route element cannot be " +
                    "defined together.  Ignoring route.");
            } else {
                activity.setRoute(createRoute(routeElement));
            }
        }

        Element blockActivityElement = Util.child(element, BLOCKACTIVITY);
        if (blockActivityElement != null) {
            if (implementationElement != null || routeElement != null) {
                // TODO: perform validating parse to catch this error earlier.
                log.warn("Implementation and route element cannot be " +
                    "defined together.  Ignoring BlockActivity.");
            } else {
                activity.setBlockActivity(createBlockActivity(
                    blockActivityElement));
            }
        }

        // TODO: perform validating parse to catch this error earlier.
        if (implementationElement == null && routeElement == null &&
            blockActivityElement == null) {

            throw new XPDLParserException("Activity '" + activity.getId() +
                "' requires Implementation, Route or BlockActivity");
        }

        // Set the start mode if specified.
        Element startModeElement = Util.child(element, START_MODE);
        if (startModeElement != null &&
            Util.child(startModeElement, MANUAL) != null) {

            activity.setStartMode(AutomationMode.MANUAL);
        } else {
            activity.setStartMode(AutomationMode.AUTOMATIC);
        }

        // Set the finish mode if specified.
        Element finishModeElement = Util.child(element, FINISH_MODE);
        if (finishModeElement != null &&
            Util.child(finishModeElement, MANUAL) != null) {

            activity.setFinishMode(AutomationMode.MANUAL);
        } else {
            activity.setFinishMode(AutomationMode.AUTOMATIC);
        }

        loadTransitionRestrictions(activity.getTransitionRestrictions(),
            Util.child(element, TRANSITION_RESTRICTIONS));

        Map extAttrs = activity.getExtendedAttributes();
        loadExtendedAttributes(extAttrs,
            Util.child(element, EXTENDED_ATTRIBUTES));

        BlockActivity ba = activity.getBlockActivity();
        if (ba != null)
            ba.setLoop((Loop)extAttrs.remove(OBE_LOOP));

        String completionStrategy = (String)extAttrs.remove(
            OBE_COMPLETION_STRATEGY);
        if (completionStrategy != null) {
            activity.setCompletionStrategy(
                CompletionStrategy.fromString(completionStrategy));
        }

        return activity;
    }

    protected void loadDeadlines(List deadlines, List elements)
        throws XPDLParserException {

        if (elements != null) {
            for (Iterator i = elements.iterator(); i.hasNext();) {
                Deadline deadline = createDeadline((Element)i.next());
                if (deadline != null)
                    deadlines.add(deadline);
            }
        }
    }

    /** Create a Deadline object from the given Element.

     @param element The Element
     @return the Deadline object
     @throws XPDLParserException
     */

    protected Deadline createDeadline(Element element)
        throws XPDLParserException {

        if (element == null) {
            log.debug("Deadline not found");
            return null;
        }

        log.debug("Creating Deadline object");
        Deadline deadline = new Deadline(
            Util.elementAsString(element, DEADLINE_CONDITION),
            Util.elementAsString(element, EXCEPTION_NAME)
        );
        deadline.setExecutionType(ExecutionType.fromString(
            element.attributeValue(EXECUTION)));

        return deadline;
    }

    protected BlockActivity createBlockActivity(Element element) {
        BlockActivity blockActivity = new BlockActivity();
        blockActivity.setBlockId(element.attributeValue(BLOCKID));

        log.debug("Created BlockActivity[blockId=" +
            blockActivity.getBlockId() + ']');

        return blockActivity;
    }

    protected Implementation createImplementation(Element element)
        throws XPDLParserException {

        ToolSet toolSet = new ToolSet();

        Iterator elements = element.elements().iterator();
        while (elements.hasNext()) {
            Element implElement = (Element)elements.next();
            ImplementationType implType = ImplementationType.fromString(
                implElement.getName());

            if (implType == null) {
                throw new XPDLParserException(
                    "Illegal implementation type: " + implElement.getName());
            }

            switch (implType.getValue()) {
                case ImplementationType.NO_INT:
                    return new NoImplementation();
                case ImplementationType.SUBFLOW_INT:
                    return createSubFlow(implElement);
                case ImplementationType.TOOL_INT:
                    toolSet.getTools().add(createTool(implElement));
                    break;
                default:
                    throw new XPDLParserException(
                        "Unsupported implementation type: " + implType);
            }

        }

        return toolSet;
    }

    protected Route createRoute(Element element) {
        return new Route();
    }

    protected Loop createLoop(Element element) throws XPDLParserException {
        if (element == null)
            throw new XPDLParserException("obe:Loop element missing");
        Loop loop = new Loop();
        Element whileElement = element.element(WHILE_QNAME);
        if (whileElement != null) {
            loop.setBody(createWhile(whileElement));
        } else {
            Element untilElement = element.element(UNTIL_QNAME);
            if (untilElement != null) {
                loop.setBody(createUntil(untilElement));
            } else {
                Element foreachElement = element.element(FOREACH_QNAME);
                if (foreachElement != null) {
                    loop.setBody(createForEach(foreachElement));
                } else {

                }
            }
        }
        return loop;
    }

    private While createWhile(Element element) throws XPDLParserException {
        Element condElement = element.element(CONDITION_QNAME);
        // TODO: validating parse will remove the need for this.
        if (condElement == null)
            throw new XPDLParserException("Missing Condition element in While");
        return new While(createCondition(condElement));
    }

    private Until createUntil(Element element) throws XPDLParserException {
        Element condElement = element.element(CONDITION_QNAME);
        // TODO: validating parse will remove the need for this.
        if (condElement == null)
            throw new XPDLParserException("Missing Condition element in Until");
        return new Until(createCondition(condElement));
    }

    private ForEach createForEach(Element element) throws XPDLParserException {
        String dataField = element.attributeValue(DATA_FIELD);
        Element exprElement = element.element(IN_QNAME);
        // TODO: validating parse will remove the need for this.
        if (exprElement == null)
            throw new XPDLParserException("Missing In element in ForEach");
        return new ForEach(dataField, exprElement.getTextTrim());
    }

    protected SubFlow createSubFlow(Element element)
        throws XPDLParserException {

        SubFlow subFlow = new SubFlow(element.attributeValue(ID));

        String executionString = element.attributeValue(EXECUTION);
        if (executionString != null) {
            subFlow.setExecution(ExecutionType.fromString(executionString));
        }

        loadActualParameters(subFlow.getActualParameters(),
            Util.child(element, ACTUAL_PARAMETERS));

        return subFlow;
    }

    protected Tool createTool(Element element) throws XPDLParserException {
        Tool tool = new Tool(element.attributeValue(ID));

        tool.setDescription(Util.elementAsString(element, DESCRIPTION));

        String typeString = element.attributeValue(TYPE);
        if (typeString != null) {
            tool.setToolType(ToolType.fromString(typeString));
        }

        loadExtendedAttributes(tool.getExtendedAttributes(),
            Util.child(element, EXTENDED_ATTRIBUTES));

        loadActualParameters(tool.getActualParameters(),
            Util.child(element, ACTUAL_PARAMETERS));

        return tool;
    }

    // Deadline

    protected SimulationInformation createSimulationInformation(Element element)
        throws XPDLParserException {

        if (element == null) {
            return null;
        }

        SimulationInformation simInfo = new SimulationInformation();

        InstantiationType instantiationType = defaultInstantiationType;
        String instantiationTypeString = element.attributeValue(INSTANTIATION_TYPE);
        if (instantiationTypeString != null) {
            instantiationType = InstantiationType.fromString(
                instantiationTypeString);
        }

        simInfo.setInstantiationType(instantiationType);
        simInfo.setCost(Util.elementAsString(element, COST));
        simInfo.setTimeEstimation(createTimeEstimation(
            Util.child(element, TIME_ESTIMATION)));

        return simInfo;
    }

    /** Create a TimeEstimation object.

     @param element The TimeEstimation Element
     @return The TimeEstimation object
     @throws XPDLParserException Parse error
     */

    protected TimeEstimation createTimeEstimation(Element element)
        throws XPDLParserException {

        if (element == null) {
            return null;
        }

        TimeEstimation timeEstimation = new TimeEstimation();
        timeEstimation.setWaitingTime(Util.elementAsDuration(element, WAITING_TIME));
        timeEstimation.setWorkingTime(Util.elementAsDuration(element, WORKING_TIME));
        timeEstimation.setDuration(Util.elementAsDuration(element, DURATION));
        return timeEstimation;
    }

    public Condition createCondition(Element element) {
        if (element == null)
            return null;

        Condition condition = new BasicCondition();

        List xpressions = condition.getXpressions();
        Iterator xpressionElements = Util.children(element, XPRESSION).iterator();
        while (xpressionElements.hasNext()) {
            Element xpressionElement = (Element)xpressionElements.next();
            xpressions.add(new BasicXpression(xpressionElement.getText()));
        }

        String typeString = element.attributeValue(TYPE);
        if (typeString != null) {
            condition.setType(ConditionType.fromString(typeString));
        }

        condition.setValue(element.getText());

        return condition;
    }

    // Transitions

    /** Load transitions from the given Element.  The element should represent a
     collection of transitions (which is identified by the "Transitions" tag).

     @param transitions The list of transitions
     @param element The "Transitions" element
     @throws XPDLParserException
     */

    protected void loadTransitions(List transitions, Element element)
        throws XPDLParserException {

        if (element == null)
            return;

        loadTransitions(transitions, Util.children(element, TRANSITION));
    }

    /** Load the transition objects from the given list of "Transition" elements.

     @param transitions The list of transitions
     @param elements The list of "Transition" elements
     @throws XPDLParserException
     */

    protected void loadTransitions(List transitions, List elements)
        throws XPDLParserException {

        transitions.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element transitionElement = (Element)iter.next();
            transitions.add(createTransition(transitionElement));
        }
    }

    protected Transition createTransition(Element element)
        throws XPDLParserException {

        Transition transition = new BasicTransition(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            element.attributeValue(FROM),
            element.attributeValue(TO)
        );

        transition.setDescription(Util.elementAsString(element, DESCRIPTION));
        transition.setCondition(createCondition(Util.child(element, CONDITION)));

        // load extended attributes
        Map extAttrs = transition.getExtendedAttributes();
        loadExtendedAttributes(extAttrs,
            Util.child(element, EXTENDED_ATTRIBUTES));

        // For now, event transitions and execution type are described by
        // extended attributes.
        transition.setEvent((Event)extAttrs.remove(OBE_EVENT));
        transition.setExecutionType((ExecutionType)extAttrs.remove(EXECUTION));

        return transition;
    }

    protected Event createEvent(Element eventElement)
        throws XPDLParserException {

        if (eventElement == null)
            throw new XPDLParserException("obe:Event element missing");

        Event event = new Event(eventElement.attributeValue(ID),
            eventElement.attributeValue(DATA_FIELD));

        Element actualParametersElem = eventElement.element(
            ACTUAL_PARAMETERS_QNAME);
        if (actualParametersElem != null) {
            loadActualParameters(event.getActualParameters(),
                actualParametersElem);
        }

        log.debug("Created event " + event.getId());

        return event;
    }

    // Transition Restrictions

    /** Load transition restrictions from the given Element.  The element should
     represent a collection of transition restrictions (which is identified
     by the "TransitionRestrictions" tag).

     @param transitionRestrictions The list of transition restrictions
     @param element The "TransitionRestrictions" element
     @throws XPDLParserException
     */

    protected void loadTransitionRestrictions(List transitionRestrictions,
        Element element) throws XPDLParserException {

        if (element == null)
            return;

        loadTransitionRestrictions(transitionRestrictions,
            Util.children(element, TRANSITION_RESTRICTION));
    }

    /** Load the transition restriction objects from the given list of
     "TransitionRestriction"  elements.

     @param transitionRestrictions The list of transition restriction
     @param elements The list of "TransitionRestriction" elements
     @throws XPDLParserException
     */

    protected void loadTransitionRestrictions(List transitionRestrictions,
        List elements) throws XPDLParserException {

        transitionRestrictions.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element transitionRestrictionElement = (Element)iter.next();
            transitionRestrictions.add(
                createTransitionRestriction(transitionRestrictionElement));
        }
    }

    protected TransitionRestriction createTransitionRestriction(Element element)
        throws XPDLParserException {

        log.debug("Loading transition restriction");

        TransitionRestriction transitionRestriction = new TransitionRestriction();

        transitionRestriction.setJoin(createJoin(Util.child(element, JOIN)));
        transitionRestriction.setSplit(createSplit(Util.child(element, SPLIT)));

        return transitionRestriction;
    }

    public Join createJoin(Element element) {
        if (element == null)
            return null;

        Join join = new Join();

        String typeString = element.attributeValue(TYPE);
        if (typeString != null) {
            join.setType(JoinType.fromString(typeString));
        }

        return join;
    }

    public Split createSplit(Element element) throws XPDLParserException {
        if (element == null)
            return null;

        Split split = new Split();

        String typeString = element.attributeValue(TYPE);
        if (typeString == null) {
            throw new XPDLParserException("Unsupported split type: " + typeString);
        }
        split.setType(SplitType.fromString(typeString));

        loadTransitionReferences(split.getTransitionReferences(),
            Util.child(element, TRANSITION_REFERENCES));

        return split;
    }

    // Transition References

    /** Load transition references from the given Element.  The element should
     represent a collection of transition references (which is identified by
     the "TransitionRefs" tag).

     @param transitionReferences The list of transition references
     @param element The "TransitionRefs" element
     @throws XPDLParserException
     */

    protected void loadTransitionReferences(List transitionReferences,
        Element element) throws XPDLParserException {
        if (element == null)
            return;

        loadTransitionReferences(transitionReferences,
            Util.children(element, TRANSITION_REFERENCE));
    }

    /** Load the transition references objects from the given list of
     "TransitionRef" elements.

     @param transitionReferences The list of transition references
     @param elements The list of "TransitionRef" elements
     @throws XPDLParserException
     */

    protected void loadTransitionReferences(List transitionReferences,
        List elements) throws XPDLParserException {

        transitionReferences.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element transitionRefElement = (Element)iter.next();
            transitionReferences.add(transitionRefElement.attributeValue(ID));
        }
    }

    // Formal Parameters

    /** Load formal parameters from the given Element.  The element should
     represent a collection of formal parameters (which is identified by
     the "FormalParameters" tag).

     @param formalParameters The list of formal parameters
     @param element The "FormalParameters" element
     @throws XPDLParserException
     */

    protected void loadFormalParameters(List formalParameters, Element element)
        throws XPDLParserException {

        if (element == null)
            return;

        loadFormalParameters(formalParameters, Util.children(element,
            FORMAL_PARAMETER));
    }

    /** Load the formal parameters objects from the given list of
     "FormalParameter" elements.

     @param formalParameters The list of formal parameters
     @param elements The list of "FormalParameter" elements
     @throws XPDLParserException
     */

    protected void loadFormalParameters(List formalParameters, List elements)
        throws XPDLParserException {

        formalParameters.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element formalParameterElement = (Element)iter.next();
            formalParameters.add(createFormalParameter(formalParameterElement));
        }
    }

    protected FormalParameter createFormalParameter(Element element)
        throws XPDLParserException {

        DataType dataType = createDataType(Util.child(element, DATA_TYPE));
        if (dataType == null) {
            throw new ElementRequiredException(DATA_TYPE, "Data type required");
        }

        FormalParameter param = new FormalParameter(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            element.attributeValue(INDEX),
            element.attributeValue(MODE),
            dataType
        );

        param.setDescription(Util.elementAsString(element, DESCRIPTION));

        return param;
    }

    // Actual Parameters

    /** Load actual parameters from the given Element.  The element should
     represent a collection of actual parameters (which is identified by
     the "ActualParameters" tag).

     @param actualParameters The list of actual parameters
     @param element The "ActualParameters" element
     @throws XPDLParserException
     */

    protected void loadActualParameters(List actualParameters, Element element)
        throws XPDLParserException {

        if (element == null)
            return;

        loadActualParameters(actualParameters, Util.children(element,
            ACTUAL_PARAMETER));
    }

    /** Load the actual parameters objects from the given list of "ActualParameter"
     elements.

     @param actualParameters The list of actual parameters
     @param elements The list of "ActualParameter" elements
     @throws XPDLParserException
     */

    protected void loadActualParameters(List actualParameters, List elements)
        throws XPDLParserException {

        actualParameters.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element actualParameterElement = (Element)iter.next();
            actualParameters.add(createActualParameter(actualParameterElement));
        }
    }

    protected ActualParameter createActualParameter(Element element)
        throws XPDLParserException {

        return new ActualParameter(element.getText());
    }

    // DataType

    protected DataType createDataType(Element element)
        throws XPDLParserException {

        Type type = getFirstType(element);
        if (type == null) {
            throw new ElementRequiredException(TYPE, "Type required");
        }

        return new DataType(type);
    }

    protected Type getFirstType(Element element) throws XPDLParserException {
        Iterator iter = element.elements().iterator();
        while (iter.hasNext()) {
            Element child = (Element)iter.next();
            String name = child.getName();
            if (name.equals(PLAIN_TYPE) ||
                name.equals(BASIC_TYPE) ||
                name.equals(RECORD_TYPE) ||
                name.equals(UNION_TYPE) ||
                name.equals(LIST_TYPE) ||
                name.equals(ENUMERATION_TYPE) ||
                name.equals(ARRAY_TYPE) ||
                name.equals(DECLARED_TYPE)
            ) {
                return createType(child);
            } else if (name.equals(EXTERNAL_REFERENCE)) {
                return createExternalReference(child);
            }
        }
        return null;
    }

    protected Type createType(Element element) throws XPDLParserException {
        String typeName = element.getName();
        if (typeName.equals(BASIC_TYPE)) {
            return BasicType.fromString(element.attributeValue(TYPE));
        } else if (typeName.equals(RECORD_TYPE)) {
            RecordType type = new RecordType();
            loadMembers(Util.children(element, MEMBER), type.getMembers());
            return type;
        } else if (typeName.equals(UNION_TYPE)) {
            UnionType type = new UnionType();
            loadMembers(Util.children(element, MEMBER), type.getMembers());
            return type;
        } else if (typeName.equals(LIST_TYPE)) {
            return new ListType(getFirstType(element));
        } else if (typeName.equals(ENUMERATION_TYPE)) {
            EnumerationType type = new EnumerationType();
            loadEnumerationValues(Util.children(element, ENUMERATION_VALUE),
                type.getValues());
            return type;
        } else if (typeName.equals(ARRAY_TYPE)) {
            return new ArrayType(
                getFirstType(element),
                element.attributeValue(LOWER_INDEX),
                element.attributeValue(UPPER_INDEX)
            );
        } else if (typeName.equals(DECLARED_TYPE)) {
            return new DeclaredType(element.attributeValue(ID));
        } else {
            throw new XPDLParserException("Unsupported type: " + typeName);
        }
    }

    protected void loadMembers(List elements, List members)
        throws XPDLParserException {

        Iterator memberElements = elements.iterator();
        while (memberElements.hasNext()) {
            Element memberElement = (Element)memberElements.next();
            Type type = getFirstType(memberElement);
            if (type == null) {
                throw new ElementRequiredException(TYPE, "Member missing type");
            }
            members.add(type);
        }
    }

    protected void loadEnumerationValues(List elements, List enumerationValues)
        throws XPDLParserException {

        Iterator valueElements = elements.iterator();
        while (valueElements.hasNext()) {
            Element valueElement = (Element)valueElements.next();
            enumerationValues.add(new EnumerationValue(
                valueElement.attributeValue(NAME)));
        }
    }

    // Type Declarations

    /** Load type declarations from the given Element.  The element should
     represent a collection of type declarations (which is identified by
     the "TypeDeclarations" tag).

     @param typeDeclarations The list of type declarations
     @param element The "TypeDeclarations" element
     @throws XPDLParserException
     */

    protected void loadTypeDeclarations(List typeDeclarations, Element element)
        throws XPDLParserException {

        if (element == null)
            return;

        loadTypeDeclarations(typeDeclarations, Util.children(element,
            TYPE_DECLARATION));
    }

    /** Load the type declaration objects from the given list of
     "TypeDeclaration" elements.

     @param typeDeclarations The list of type declarations
     @param elements The list of "TypeDeclaration" elements
     @throws XPDLParserException
     */

    protected void loadTypeDeclarations(List typeDeclarations, List elements)
        throws XPDLParserException {

        typeDeclarations.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element typeDeclarationElement = (Element)iter.next();
            typeDeclarations.add(createTypeDeclaration(typeDeclarationElement));
        }
    }

    protected TypeDeclaration createTypeDeclaration(Element element)
        throws XPDLParserException {

        Type type = getFirstType(element);
        if (type == null) {
            throw new ElementRequiredException(TYPE, "Type required");
        }

        TypeDeclaration typeDeclaration = new TypeDeclaration(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            type
        );

        typeDeclaration.setDescription(Util.elementAsString(element, DESCRIPTION));

        loadExtendedAttributes(typeDeclaration.getExtendedAttributes(),
            Util.child(element, EXTENDED_ATTRIBUTES));

        return typeDeclaration;
    }

    // Data Fields

    /** Load data fields from the given Element.  The element should
     represent a collection of data fields (which is identified by
     the "DataFields" tag).

     @param dataFields The list of data fields
     @param element The "DataFields" element
     @throws XPDLParserException
     */

    protected void loadDataFields(List dataFields, Element element)
        throws XPDLParserException {

        if (element == null)
            return;

        loadDataFields(dataFields, Util.children(element, DATA_FIELD));
    }

    /** Load the data field objects from the given list of "DataField"
     elements.

     @param dataFields The list of data fields
     @param elements The list of "DataField" elements
     @throws XPDLParserException
     */

    protected void loadDataFields(List dataFields, List elements)
        throws XPDLParserException {

        dataFields.clear();
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element dataFieldElement = (Element)iter.next();
            dataFields.add(createDataField(dataFieldElement));
        }

        log.debug("Data fields size after load: " + dataFields.size());
    }

    protected DataField createDataField(Element element)
        throws XPDLParserException {

        DataType dataType = createDataType(Util.child(element, DATA_TYPE));
        if (dataType == null) {
            throw new ElementRequiredException(DATA_TYPE, "Data type required");
        }

        DataField dataField = new DataField(
            element.attributeValue(ID),
            element.attributeValue(NAME),
            dataType
        );

        String isArrayString = element.attributeValue(IS_ARRAY);
        if (isArrayString == null) {
            isArrayString = "false";
        }
        dataField.setArray(new Boolean(isArrayString).booleanValue());

        dataField.setDescription(Util.elementAsString(element, DESCRIPTION));
        dataField.setInitialValue(Util.elementAsString(element, INITIAL_VALUE));
        dataField.setLength(Util.elementAsInteger(element, LENGTH));

        loadExtendedAttributes(dataField.getExtendedAttributes(),
            Util.child(element, EXTENDED_ATTRIBUTES));

        return dataField;
    }

    // External Reference

    /** Create an ExternalReference object from the given Element.

     @param element The "ExternalReference" element
     @return The ExternalReference object
     @throws XPDLParserException
     */

    protected ExternalReference createExternalReference(Element element)
        throws XPDLParserException {

        log.debug("ExternalReference Element: " + element);

        ExternalReference externalReference = new ExternalReference(
            element.attributeValue(LOCATION));

        externalReference.setNamespace(element.attributeValue(NAMESPACE));
        externalReference.setXref(element.attributeValue(XREF));

        return externalReference;
    }

    // External Attributes

    /** Load extended attributes from the given Element.  The element should
     represent a collection of extended attributes (which is identified
     by the "ExtendedAttributes" tag).

     @param extendedAttributes The map of extended attributes
     @param element The "ExtendedAttributes" element
     @throws XPDLParserException
     */

    protected void loadExtendedAttributes(Map extendedAttributes,
        Element element) throws XPDLParserException {

        if (element == null)
            return;

        loadExtendedAttributes(extendedAttributes,
            Util.children(element, EXTENDED_ATTRIBUTE));
    }

    /** Load the extended attribute objects from the given list of
     "ExtendedAttribute" elements.

     @param extendedAttributes The map of extended attribute
     @param elements The list of "ExtendedAttribute" elements
     @throws XPDLParserException
     */

    protected void loadExtendedAttributes(Map extendedAttributes, List elements)
        throws XPDLParserException {

        log.debug("Loading extended attributes [size=" + elements.size() + "]");
        extendedAttributes.clear();

        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element extAttrElement = (Element)iter.next();
            String name = extAttrElement.attributeValue(NAME);
            Object value;
            if (name.equals(OBE_LOOP)) {
                Element loopElement = extAttrElement.element(LOOP_QNAME);
                value = createLoop(loopElement);
            } else if (name.equals(OBE_EVENT)) {
                Element eventElement = extAttrElement.element(EVENT_QNAME);
                value = createEvent(eventElement);
            } else if (name.equals(OBE_EVENT_TYPES)) {
                Element eventTypesElem = extAttrElement.element(EVENT_TYPES_QNAME);
                value = new ArrayList();
                loadEventTypes((List)value, eventTypesElem);
            } else if (name.equals(EXECUTION)) {
                String tag = extAttrElement.attributeValue(VALUE);
                value = ExecutionType.fromString(tag);
            } else {
                value = extAttrElement.attributeValue(VALUE);
            }
            extendedAttributes.put(name, value);
            log.debug("Added extended attribute [" + name + "=" + value + "]");
        }
    }
}