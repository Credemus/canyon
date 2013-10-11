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

package org.obe.parser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import org.obe.ConformanceClass;
import org.obe.GraphConformance;
import org.obe.Package;
import org.obe.PackageHeader;
import org.obe.RedefinableHeader;
import org.obe.activity.Activity;
import org.obe.activity.ActivitySet;
import org.obe.activity.BlockActivity;
import org.obe.activity.ExecutionType;
import org.obe.activity.Implementation;
import org.obe.activity.NoImplementation;
import org.obe.activity.SubFlow;
import org.obe.activity.Tool;
import org.obe.activity.ToolSet;
import org.obe.application.Application;
import org.obe.condition.Condition;
import org.obe.condition.ConditionType;
import org.obe.data.ActualParameter;
import org.obe.data.ArrayType;
import org.obe.data.BasicType;
import org.obe.data.DataField;
import org.obe.data.DataType;
import org.obe.data.DeclaredType;
import org.obe.data.EnumerationType;
import org.obe.data.ExternalReference;
import org.obe.data.FormalParameter;
import org.obe.data.ListType;
import org.obe.data.ParameterMode;
import org.obe.data.RecordType;
import org.obe.data.SchemaType;
import org.obe.data.Type;
import org.obe.data.TypeDeclaration;
import org.obe.data.UnionType;
import org.obe.parser.dom4j.Dom4JXPDLParser;
import org.obe.participant.Participant;
import org.obe.transition.Join;
import org.obe.transition.Split;
import org.obe.transition.SplitType;
import org.obe.transition.Transition;
import org.obe.transition.TransitionRestriction;
import org.obe.util.Deadline;
import org.obe.workflow.BasicWorkflowProcess;
import org.obe.workflow.ProcessHeader;
import org.obe.workflow.WorkflowProcess;

/**
 * Validates an XPDL package bean.
 *
 * @author Adrian Price
 */
public class PackageValidator implements PackageValidatorMessages {
    public static final String GRAPH_CONFORMANCE_MIN_PROP =
        "graph.conformance.minimum";
    private static final String ACTIVITY = "Activity";
    private static final String ACTIVITY_SET = "ActivitySet";
    private static final String PKG_APPLICATION = "Application(Package)";
    private static final String APPLICATION = "Application";
    private static final String PKG_DATA_FIELD = "DataField(Package)";
    private static final String DATA_FIELD = "DataField";
    private static final String FORMAL_PARAMETER = "FormalParameter";
    private static final String PKG_PARTICIPANT = "Participant(Package)";
    private static final String PARTICIPANT = "Participant";
    private static final String TRANSITION = "Transition";
    private static final String TYPE_DECLARATION = "TypeDeclaration";
    private static final String WORKFLOW_PROCESS = "WorkflowProcess";
    private static final String WORKFLOW_FORMAL_PARAMETER = "FormalParameter(Workflow)";
    private static final String[][] DEFAULTS = {
        {GRAPH_CONFORMANCE_MIN_PROP, GraphConformance.NON_BLOCKED.toString()},
    };
    private static final Properties _defaultProps = new Properties();
    private Properties _props = new Properties(_defaultProps);

    private static class ValidationContext {
        Package pkg;
        private Stack _prefixes = new Stack();
        private Map _uniqueIds = new HashMap();
        private String _prefix;
        private List _errors;
        private List _warnings;

        ValidationContext(Package pkg) {
            this.pkg = pkg;
        }

        void popPrefix() {
            _prefixes.pop();
            generatePrefix();
        }

        String peekPrefix() {
            return (String)_prefixes.peek();
        }

        void pushPrefix(String prefix) {
            _prefixes.push(prefix);
            generatePrefix();
        }

        private void generatePrefix() {
            StringBuffer sb = new StringBuffer();
            for (Iterator i = _prefixes.iterator(); i.hasNext();) {
                sb.append('/');
                sb.append(i.next());
            }
            _prefix = sb.toString();
        }

        void checkUniqueId(Object src, String prefix, String primaryKey,
            String id, Object obj) {

            checkUniqueId(src, prefix, primaryKey, null, null, id, obj);
        }

        void checkUniqueId(Object src, String prefix, String primaryKey,
            String secondaryKey, String id, Object obj) {

            checkUniqueId(src, prefix, primaryKey, secondaryKey, null, id, obj);
        }

        void checkUniqueId(Object src, String prefix, String primaryKey,
            String secondaryKey, String tertiaryKey, String id, Object obj) {

            String owner = prefix != null ? prefix : peekPrefix();
            if (prefix == null)
                prefix = "";
            if (id == null || id.trim().length() == 0) {
                addError(src, ID_MUST_BE_SPECIFIED, new Object[]{owner},
                    prefix + "/Id must be specified");
            } else {
                // TODO: validate that this is a valid NMTOKEN string.
                Map uniqueIds = getUniqueIds(primaryKey);
                boolean exists = uniqueIds.containsKey(id);
                if (!exists && secondaryKey != null)
                    exists = getUniqueIds(secondaryKey).containsKey(id);
                if (!exists && tertiaryKey != null)
                    exists = getUniqueIds(tertiaryKey).containsKey(id);
                if (!exists) {
                    uniqueIds.put(id, obj);
                } else {
                    addError(src, ID_ALREADY_DEFINED, new Object[]{owner},
                        prefix +
                        " is already defined. Remove the duplicate entity or assign it a unique ID.");
                }
            }
        }

        Object checkValidIdRef(Object src, String prefix, String primaryKey,
            String refId) {

            return checkValidIdRef(src, prefix, primaryKey, null, null, refId);
        }

        Object checkValidIdRef(Object src, String prefix, String primaryKey,
            String secondaryKey, String refId) {

            return checkValidIdRef(src, prefix, primaryKey, secondaryKey, null,
                refId);
        }

        Object checkValidIdRef(Object src, String prefix, String primaryKey,
            String secondaryKey, String tertiaryKey, String refId) {

            String owner = prefix != null ? prefix : peekPrefix();
            if (prefix == null)
                prefix = "";
            Object obj = getUniqueIds(primaryKey).get(refId);
            if (obj == null && secondaryKey != null)
                obj = getUniqueIds(secondaryKey).get(refId);
            if (obj == null && tertiaryKey != null)
                obj = getUniqueIds(tertiaryKey).get(refId);
            if (obj == null) {
                addError(src, UNDEFINED_REFERENCE,
                    new Object[]{owner, primaryKey, refId}, prefix +
                    " references an undefined " + primaryKey + '[' + refId +
                    ']');
            }
            return obj;
        }

        void resetUniqueId(String key) {
            getUniqueIds(key).clear();
        }

        boolean checkNotNull(Object src, String prefix, Object obj) {
            if (prefix == null)
                prefix = "";
            if (obj instanceof String) {
                String s = ((String)obj).trim();
                if (s.length() == 0)
                    obj = null;
            }
            boolean b = obj != null;
            if (!b) {
                addError(src, PROPERTY_MISSING,
                    new Object[]{peekPrefix(), prefix},
                    prefix + " must be specified");
            }
            return b;
        }

        boolean checkEQ(Object src, String prefix, String property,
            int expected, int actual) {

            if (prefix == null)
                prefix = "";
            boolean b = actual == expected;
            if (!b) {
                addError(src, COUNT_INCORRECT,
                    new Object[]{peekPrefix(), property,
                                 new Integer(expected),
                                 new Integer(actual)},
                    prefix + ": expected <" + expected + ">, actual <" +
                    actual + ">");
            }
            return b;
        }

        boolean checkGE(Object src, String prefix, String property,
            int expected, int actual) {

            if (prefix == null)
                prefix = "";
            boolean b = actual >= expected;
            if (!b) {
                addError(src, PROPERTY_VALUE_TOO_LOW,
                    new Object[]{peekPrefix(), property,
                                 new Integer(expected), new Integer(actual)},
                    prefix + ": expected <" + expected + ">, actual <" +
                    actual + ">");
            }
            return b;
        }

        void addError(Object src, int msgCode, Object[] args, String msg) {
            if (_errors == null)
                _errors = new ArrayList();
            _errors.add(new ValidationError(ValidationError.TYPE_ERROR, msgCode,
                args, src, _prefix + msg));
        }

        void addWarning(Object src, int msgCode, Object[] args, String msg) {
            if (_warnings == null)
                _warnings = new ArrayList();
            _warnings.add(new ValidationError(ValidationError.TYPE_WARNING,
                msgCode, args, src, _prefix + msg));
        }

        ValidationError[] getErrors() {
            return _errors == null
                ? null
                : (ValidationError[])_errors.toArray(
                    new ValidationError[_errors.size()]);
        }

        ValidationError[] getWarnings() {
            return _warnings == null
                ? null
                : (ValidationError[])_warnings.toArray(
                    new ValidationError[_warnings.size()]);
        }

        private Map getUniqueIds(String key) {
            Map ids = (Map)_uniqueIds.get(key);
            if (ids == null) {
                ids = new HashMap();
                _uniqueIds.put(key, ids);
            }
            return ids;
        }
    }

    static {
        for (int i = 0; i < DEFAULTS.length; i++) {
            String[] entry = DEFAULTS[i];
            _defaultProps.setProperty(entry[0], entry[1]);
        }
    }

    public static void main(String[] args) {
        try {
            XPDLParser parser = new Dom4JXPDLParser();
            if (args.length == 0)
                usage();
            int i = 0;
            Properties props = null;
            if (args[i].startsWith("-props")) {
                String propfile = args[i++];
                if (i == args.length)
                    usage();
                props = new Properties();
                InputStream in = new FileInputStream(propfile);
                props.load(in);
                in.close();
            }
            PackageValidator validator = new PackageValidator(props);
            for (; i < args.length; i++) {
                String src = args[i];
                Reader rdr = new FileReader(src);
                Package pkg = parser.parse(rdr);
                rdr.close();
                ValidationError[] errors = validator.validate(pkg, false);
                if (errors == null)
                    System.out.println(src + " is a valid XPDL package");
                else {
                    System.err.println(src + " contains errors:\n");
                    for (int j = 0; j < errors.length; j++)
                        System.err.println(errors[j].getMessage());
                    System.err.println("\n" + errors.length + " errors\n");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void usage() {
        System.out.println("usage: java org.obe.parser.PackageValidator [-props propfile] file...");
        System.exit(1);
    }

    public PackageValidator() {
    }

    public PackageValidator(Properties props) {
        if (props != null)
            _props.putAll(props);
    }

    public String getProperty(String key) {
        return _props.getProperty(key);
    }

    public Object setProperty(String key, String value) {
        return _props.setProperty(key, value);
    }

    public ValidationError[] validate(Package pkg, boolean throwException)
        throws InvalidPackageException {

        // Make sure all transitions connect to their associated activities.
        for (Iterator i = pkg.getWorkflowProcesses().iterator(); i.hasNext();) {
            BasicWorkflowProcess workflow = (BasicWorkflowProcess)i.next();
            workflow.resolveReferences();
        }

        ValidationContext ctx = new ValidationContext(pkg);
        checkPackage(ctx);

        ValidationError[] errors = ctx.getErrors();
        if (errors != null && throwException)
            throw new InvalidPackageException(pkg.getId(), errors);
        return errors;
    }

    private void checkPackage(ValidationContext ctx) {
        ctx.pushPrefix("Package[" + ctx.pkg.getId() + ']');
        checkPackageHeader(ctx);
        checkRedefinableHeader(ctx.pkg.getRedefinableHeader(), ctx);
        checkConformanceClass(ctx);
        checkScript(ctx);
        checkExternalPackages(ctx);
        checkTypeDeclarations(ctx);
        checkParticipants(ctx.pkg.getParticipants(), true, ctx);
        checkApplications(ctx.pkg.getApplications(), true, ctx);
        checkDataFields(ctx.pkg.getDataFields(), true, ctx);
        checkWorkflows(ctx);
        ctx.popPrefix();
    }

    private void checkPackageHeader(ValidationContext ctx) {
        ctx.pushPrefix("PackageHeader");
        PackageHeader hdr = ctx.pkg.getPackageHeader();
        ctx.checkNotNull(hdr, "/XPDLVersion", hdr.getXPDLVersion());
        ctx.checkNotNull(hdr, "/Vendor", hdr.getVendor());
        ctx.checkNotNull(hdr, "/Created", hdr.getCreated());
        ctx.popPrefix();
    }

    private void checkRedefinableHeader(RedefinableHeader hdr,
        ValidationContext ctx) {

        // TODO: implement checkRedefinableHeader.
        ctx.pushPrefix("RedefinableHeader");
        ctx.popPrefix();
    }

    // Ensure a minimum graph conformance level, and that the activity net
    // does actually conform to that level.
    private void checkConformanceClass(ValidationContext ctx) {
        ConformanceClass cc = ctx.pkg.getConformanceClass();
        GraphConformance gc = cc == null ? null : cc.getGraphConformance();
        if (gc == null)
            gc = GraphConformance.NON_BLOCKED;
        String gcl = getProperty(GRAPH_CONFORMANCE_MIN_PROP);
        ctx.checkGE(ctx.pkg, " Graph conformance level must be at least " + gcl,
            XPDLNames.GRAPH_CONFORMANCE,
            GraphConformance.fromString(gcl).getValue(), gc.getValue());
        // TODO: verify activity nets conform to declared graph conformance class.
    }

    private void checkScript(ValidationContext ctx) {
        // TODO: implement checkScript.
    }

    private void checkExternalPackages(ValidationContext ctx) {
        // TODO: implement checkExternalPackages.
    }

    private void checkTypeDeclarations(ValidationContext ctx) {
        ctx.resetUniqueId(TYPE_DECLARATION);
        List typeDeclarations = ctx.pkg.getTypeDeclarations();
        if (typeDeclarations != null) {
            for (Iterator i = typeDeclarations.iterator(); i.hasNext();) {
                TypeDeclaration typeDecl = (TypeDeclaration)i.next();
                checkTypeDeclaration(typeDecl, ctx);
            }
        }
    }

    private void checkTypeDeclaration(TypeDeclaration typeDecl,
        ValidationContext ctx) {

        ctx.pushPrefix("TypeDeclaration[" + typeDecl.getId() + ']');
        ctx.checkUniqueId(typeDecl, null, TYPE_DECLARATION, typeDecl.getId(),
            typeDecl);
        // TODO: complete implementation of checkTypeDeclaration.
        ctx.popPrefix();
    }

    private void checkParticipants(List participants,
        boolean pkgLevel, ValidationContext ctx) {

        String primary = pkgLevel ? PKG_PARTICIPANT : PARTICIPANT;
        String secondary = pkgLevel ? null : PKG_PARTICIPANT;
        ctx.resetUniqueId(primary);
        if (participants != null) {
            for (Iterator i = participants.iterator(); i.hasNext();) {
                Participant particip = (Participant)i.next();
                ctx.checkUniqueId(particip, "/Participant[" +
                    particip.getId() + ']', primary, secondary,
                    particip.getId(), particip);
            }
        }
    }

    private void checkApplications(List applications, boolean pkgLevel,
        ValidationContext ctx) {

        String primary = pkgLevel ? PKG_APPLICATION : APPLICATION;
        String secondary = pkgLevel ? null : PKG_APPLICATION;
        ctx.resetUniqueId(primary);
        if (applications != null) {
            for (Iterator i = applications.iterator(); i.hasNext();) {
                Application app = (Application)i.next();
                ctx.pushPrefix("Application[" + app.getId() + ']');
                ctx.checkUniqueId(app, null, primary, secondary, app.getId(),
                    app);
                checkFormalParameters(app.getFormalParameters(), false, ctx);
                ctx.popPrefix();
            }
        }
    }

    private void checkFormalParameters(List parameters, boolean wfLevel,
        ValidationContext ctx) {

        String key = wfLevel ? WORKFLOW_FORMAL_PARAMETER : FORMAL_PARAMETER;
        ctx.resetUniqueId(key);
        if (parameters != null) {
            for (Iterator i = parameters.iterator(); i.hasNext();) {
                FormalParameter parameter = (FormalParameter)i.next();
                ctx.checkUniqueId(parameter, "/FormalParameter[" +
                    parameter.getId() + ']', key, parameter.getId(), parameter);
            }
        }
    }

    private void checkDataFields(List dataFields,
        boolean pkgLevel, ValidationContext ctx) {

        ctx.resetUniqueId(pkgLevel ? PKG_DATA_FIELD : DATA_FIELD);
        if (dataFields != null) {
            for (Iterator i = dataFields.iterator(); i.hasNext();) {
                DataField dataField = (DataField)i.next();
                checkDataField(dataField, pkgLevel, ctx);
            }
        }
    }

    private void checkDataField(DataField dataField,
        boolean pkgLevel, ValidationContext ctx) {

        String primary = pkgLevel ? PKG_DATA_FIELD : DATA_FIELD;
        String secondary = pkgLevel ? null : WORKFLOW_FORMAL_PARAMETER;
        String tertiary = pkgLevel ? null : PKG_DATA_FIELD;
        String dataFieldId = dataField.getId();
        ctx.pushPrefix("DataField[" + dataFieldId + ']');
        ctx.checkUniqueId(dataField, null, primary, secondary, tertiary,
            dataFieldId, dataField);
        DataType dataType = dataField.getDataType();
        Type type = dataType == null ? null : dataType.getType();
        ctx.checkNotNull(dataField, "/Type", type);
        if (type instanceof BasicType) {
        } else if (type instanceof DeclaredType) {
            ctx.checkValidIdRef(dataField, "/Id", TYPE_DECLARATION,
                ((DeclaredType)type).getId());
        } else if (type instanceof SchemaType) {
        } else if (type instanceof ExternalReference) {
        } else if (type instanceof RecordType) {
        } else if (type instanceof UnionType) {
        } else if (type instanceof EnumerationType) {
        } else if (type instanceof ArrayType) {
        } else if (type instanceof ListType) {
        }
        ctx.popPrefix();
    }

    private void checkWorkflows(ValidationContext ctx) {
        ctx.resetUniqueId(WORKFLOW_PROCESS);
        List workflowProcesses = ctx.pkg.getWorkflowProcesses();
        if (workflowProcesses != null) {
            for (Iterator i = workflowProcesses.iterator(); i.hasNext();) {
                WorkflowProcess workflow = (WorkflowProcess)i.next();
                checkWorkflow(workflow, ctx);
            }
        }
    }

    private void checkWorkflow(WorkflowProcess workflow,
        ValidationContext ctx) {

        ctx.resetUniqueId(DATA_FIELD);
        ctx.resetUniqueId(WORKFLOW_FORMAL_PARAMETER);
        ctx.resetUniqueId(ACTIVITY);
        ctx.resetUniqueId(TRANSITION);
        String workflowId = workflow.getId();
        ctx.pushPrefix("Workflow[" + workflowId + ']');
        ctx.checkUniqueId(workflow, null, WORKFLOW_PROCESS, workflowId,
            workflow);
        checkProcessHeader(workflow.getProcessHeader(), ctx);
        checkRedefinableHeader(workflow.getRedefinableHeader(), ctx);
        checkFormalParameters(workflow.getFormalParameters(), true, ctx);
        checkDataFields(workflow.getDataFields(), false, ctx);
        checkParticipants(workflow.getParticipants(), false, ctx);
        checkApplications(workflow.getApplications(), false, ctx);
        checkActivitySets(workflow.getActivitySets(), ctx);
        checkActivities(workflow, workflow.getActivities(), ctx);
        checkTransitions(workflow.getTransitions(), ctx);
        ctx.popPrefix();
    }

    private void checkProcessHeader(ProcessHeader hdr, ValidationContext ctx) {
        // TODO: implement checkProcessHeader.
    }

    private void checkActivitySets(List activitySets, ValidationContext ctx) {
        ctx.resetUniqueId(ACTIVITY_SET);
        if (activitySets != null) {
            for (Iterator j = activitySets.iterator(); j.hasNext();) {
                ActivitySet activitySet = (ActivitySet)j.next();
                checkActivitySet(activitySet, ctx);
            }
        }
    }

    private void checkActivitySet(ActivitySet activitySet,
        ValidationContext ctx) {

        String id = activitySet.getId();
        ctx.pushPrefix("ActivitySet[" + id + ']');
        ctx.checkUniqueId(activitySet, null, ACTIVITY_SET, id, activitySet);
        checkActivities(activitySet, activitySet.getActivities(), ctx);
        checkTransitions(activitySet.getTransitions(), ctx);
        ctx.popPrefix();
    }

    private void checkActivities(Object src, List activities,
        ValidationContext ctx) {

        if (activities != null) {
            boolean foundStart = false;
            boolean foundExit = false;
            for (Iterator i = activities.iterator(); i.hasNext();) {
                Activity activity = (Activity)i.next();
                checkActivity(activity, ctx);
                if (activity.isStartActivity())
                    foundStart = true;
                if (activity.isExitActivity())
                    foundExit = true;
            }
            if (!foundStart) {
                ctx.addError(src, START_ACTIVITY_REQUIRED,
                    new Object[]{ctx.peekPrefix()},
                    " must have at least one start activity (one with no afferent (inbound) transitions)");
            }
            if (!foundExit) {
                ctx.addError(src, EXIT_ACTIVITY_REQUIRED,
                    new Object[]{ctx.peekPrefix()},
                    " must have at least one exit activity (one with no efferent (outbound) transitions)");
            }
        }
    }

    private void checkActivity(Activity activity, ValidationContext ctx) {
        String activityId = activity.getId();
        ctx.pushPrefix("Activity[" + activityId + ']');
        ctx.checkUniqueId(activity, null, ACTIVITY, activityId, activity);
        String performer = activity.getPerformer();
        if (performer != null) {
            StringTokenizer strTok = new StringTokenizer(performer, ",");
            while (strTok.hasMoreTokens()) {
                ctx.checkValidIdRef(activity, "/Performer", PARTICIPANT,
                    PKG_PARTICIPANT, strTok.nextToken());
            }
        }
        int bodyCount = 0;
        BlockActivity blk = activity.getBlockActivity();
        if (blk != null) {
            String blockId = blk.getBlockId();
            if (ctx.checkNotNull(activity, "/BlockActivity/Id", blockId) &&
                blk.getActivitySet() == null) {

                ctx.addError(activity, UNDEFINED_ACTIVITY_SET,
                    new Object[]{ctx.peekPrefix(), blockId},
                    "/BlockActivity references an undefined ActivitySet[" +
                    blockId + ']');
            }
            bodyCount++;
        }
        Implementation impl = activity.getImplementation();
        if (impl != null) {
            ctx.pushPrefix("Implementation");
            if (impl instanceof NoImplementation) {
                // Nothing to check.
            } else if (impl instanceof SubFlow) {
                SubFlow subFlow = (SubFlow)impl;
                String subFlowId = subFlow.getId();
                ctx.pushPrefix("SubFlow[" + subFlowId + ']');
                if (ctx.checkNotNull(subFlow, null, subFlowId)) {
                    boolean found = false;
                    for (Iterator i = ctx.pkg.getWorkflowProcesses().iterator();
                         i.hasNext();) {

                        WorkflowProcess workflow = (WorkflowProcess)i.next();
                        if (workflow.getId().equals(subFlowId)) {
                            checkActualParameters(subFlow,
                                workflow.getFormalParameters(),
                                subFlow.getActualParameters(), ctx);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        ctx.addError(activity, UNDEFINED_SUBPROCESS,
                            new Object[]{activityId, subFlowId},
                            " references an undefined WorkflowProcess[" +
                            subFlowId + ']');
                    }
                }
                ctx.popPrefix();
            } else if (impl instanceof ToolSet) {
                ToolSet toolSet = (ToolSet)impl;
                for (Iterator j = toolSet.getTools().iterator(); j.hasNext();) {
                    Tool tool = (Tool)j.next();
                    String toolId = tool.getId();
                    if (ctx.checkNotNull(tool, "/Tool/Id", toolId)) {
                        ctx.pushPrefix("Tool[" + toolId + ']');
                        Application app = (Application)ctx.checkValidIdRef(
                            tool, null, APPLICATION, PKG_APPLICATION, toolId);
                        ctx.checkNotNull(tool, "/Type", tool.getToolType());
                        if (app != null) {
                            checkActualParameters(tool,
                                app.getFormalParameters(),
                                tool.getActualParameters(), ctx);
                        }
                        ctx.popPrefix();
                    }
                }
            }
            bodyCount++;
            ctx.popPrefix();
        }
        if (activity.getRoute() != null) {
            if (performer != null)
                ctx.addError(activity, ROUTE_CANNOT_HAVE_PERFORMER,
                    new Object[]{activityId}, "/Route cannot have a Performer");
            bodyCount++;
        }
        if (bodyCount != 1) {
            ctx.addError(activity, ACTIVITY_BODY_MISSING,
                new Object[]{activityId},
                " must include only one of: Route, Implementation, or BlockActivity");
        }

        List deadlines = activity.getDeadlines();
        if (deadlines != null) {
            Set deadlineExceptions = new HashSet();
            ctx.pushPrefix("Deadline");
            int syncCount = 0;
            for (Iterator i = deadlines.iterator(); i.hasNext();) {
                Deadline deadline = (Deadline)i.next();
                // TODO: validate condition.
                String condition = deadline.getDeadlineCondition();
                ctx.checkNotNull(deadline, "/Condition", condition);
                String exceptionName = deadline.getExceptionName();
                ctx.checkNotNull(deadline, "/Exception", exceptionName);
                deadlineExceptions.add(exceptionName);
                if (deadline.getExecutionType() == ExecutionType.SYNCHRONOUS &&
                    ++syncCount == 2) {

                    ctx.addError(activity, MAX_ONE_SYNC_DEADLINE,
                        new Object[]{activityId},
                        " can only have one synchronous deadline");
                }
            }
            // Check that all deadline exceptions are handled.
            Map transitions = activity.getEfferentTransitions();
            if (transitions!= null) {
                for (Iterator i = transitions.values().iterator();
                     i.hasNext();) {

                    Transition transition = (Transition)i.next();
                    Condition condition = transition.getCondition();
                    if (condition != null) {
                        ConditionType type = condition.getType();
                        if (type == ConditionType.DEFAULTEXCEPTION) {
                            deadlineExceptions.clear();
                            break;
                        } else if (type == ConditionType.EXCEPTION) {
                            deadlineExceptions.remove(condition.getValue());
                        }
                    }
                    if (transition.getExecution() != null &&
                        deadlines.size() > 0) {

                        ctx.addError(activity, INCOMPATIBLE_TRANSITION,
                            new Object[]{activityId, transition.getId()},
                            " is incompatible with Transition[" +
                            transition.getId() + "]/ExtendedAttribute[" +
                            XPDLNames.EXECUTION + ']');
                    }
                }
            }
            for (Iterator i = deadlineExceptions.iterator(); i.hasNext();) {
                String exception = (String)i.next();
                ctx.addError(activity, EXCEPTION_NOT_HANDLED,
                    new Object[]{activityId, exception},
                    "/ExceptionName[" + exception +
                    "] is not handled by any efferent (outbound) transition");
            }
            ctx.popPrefix();
        }

        List restrictions = activity.getTransitionRestrictions();
        boolean joinFound = false;
        boolean splitFound = false;
        if (restrictions != null) {
            ctx.pushPrefix("TransitionRestriction");
            for (Iterator i = restrictions.iterator(); i.hasNext();) {
                TransitionRestriction restriction = (TransitionRestriction)
                    i.next();
                Join join = restriction.getJoin();
                if (join != null) {
                    // Only one join is permitted.
                    if (joinFound) {
                        ctx.addError(activity,
                            TRANSITION_RESTRICTIONS_ONLY_ONE_JOIN,
                            new Object[]{activityId},
                            " TransitionRestrictions can only contain one join.");
                    }
                    joinFound = true;
                }
                Split split = restriction.getSplit();
                if (split != null) {
                    // Only one split is permitted.
                    if (splitFound) {
                        ctx.addError(activity,
                            TRANSITION_RESTRICTIONS_ONLY_ONE_SPLIT,
                            new Object[]{activityId},
                            " OBE TransitionRestrictions can only contain one split (XPDL permits more, but does not define the semantics).");
                    }
                    splitFound = true;
                    SplitType type = split.getType();
                    ctx.checkNotNull(split, "/Split/Type", type);
                    if (type == SplitType.XOR) {
                        ctx.pushPrefix("Split[XOR]");
                        List transitionRefs = split.getTransitionReferences();
                        if (transitionRefs != null) {
                            // TransitionRefs must refer to valid transitions.
                            for (Iterator j = transitionRefs.iterator();
                                 j.hasNext();) {

                                String transitionId = (String)j.next();
                                if (!activity.getEfferentTransitions()
                                    .containsKey(transitionId)) {

                                    ctx.addError(activity, INVALID_TRANSITION_REF,
                                        new Object[]{activityId, transitionId},
                                        "/TransitionRef[" + transitionId +
                                        "] references non-efferent (outbound) Transition");
                                }
                            }
                            // Efferent transitions must have TransitionRefs
                            // (other than exception and event transitions).
                            boolean foundOtherwise = false;
                            for (Iterator j = activity.getEfferentTransitions()
                                .values().iterator(); j.hasNext();) {

                                Transition transition = (Transition)j.next();
                                Condition condition = transition.getCondition();
                                ConditionType transitionType =
                                    condition == null
                                    ? null
                                    : condition.getType();
                                if (transition.getEvent() == null) {
                                    if (transitionType == null ||
                                        transitionType == ConditionType.CONDITION ||
                                        transitionType == ConditionType.OTHERWISE) {

                                        String transitionId = transition.getId();
                                        if (!transitionRefs.contains(transitionId)) {
                                            ctx.addError(activity,
                                                TRANSITION_REF_MISSING,
                                                new Object[]{activityId, transitionId},
                                                " does not reference Transition[" +
                                                transitionId + ']');
                                        }
                                    }
                                } else if (transitionType == ConditionType.DEFAULTEXCEPTION ||
                                    transitionType == ConditionType.EXCEPTION ||
                                    transitionType == ConditionType.OTHERWISE) {

                                    // TODO: check that the event is declared.
                                    ctx.addError(transition,
                                        EVENT_TRANSITION_MISMATCH,
                                        new Object[]{transition.getId(), transitionType},
                                        " OBE event transitions cannot be used in conjunction with an XPDL transition type of " +
                                        transitionType);
                                }
                                if (transitionType == ConditionType.OTHERWISE) {
                                    if (foundOtherwise) {
                                        ctx.addError(transition,
                                            OTHERWISE_ALREADY_DEFINED,
                                            new Object[]{activityId},
                                            " an OTHERWISE transition has already been defined");
                                    }
                                    foundOtherwise = true;
                                }
                            }
                        } else {
                            ctx.addError(activity, SPLIT_MISSING_REFERENCES,
                                new Object[]{activityId},
                                " does not reference any transitions");
                        }
                        ctx.popPrefix();
                    } else {
                        boolean foundOtherwise = false;
                        for (Iterator j = activity.getEfferentTransitions()
                            .values().iterator(); j.hasNext();) {

                            Transition transition = (Transition)j.next();
                            Condition condition = transition.getCondition();
                            ConditionType transitionType =
                                condition == null
                                ? null
                                : condition.getType();
                            if (transitionType == ConditionType.OTHERWISE) {
                                if (foundOtherwise) {
                                    ctx.addError(transition,
                                        OTHERWISE_ALREADY_DEFINED,
                                        new Object[]{activityId},
                                        " an OTHERWISE transition has already been defined");
                                }
                                foundOtherwise = true;
                            }
                        }
                    }
                }
            }
            ctx.popPrefix();
        }
        if (!joinFound) {
            int count = activity.getAfferentTransitions().size();
            if (count > 1) {
                ctx.addError(activity, JOIN_REQUIRED,
                    new Object[]{activityId, new Integer(count)},
                    " has " + count +
                    " afferent (inbound) transitions, and therefore requires a Join");
            }
        }
        if (!splitFound) {
            int count = 0;
            for (Iterator i = activity.getEfferentTransitions().values()
                .iterator(); i.hasNext();) {

                Transition transition = (Transition)i.next();
                Condition condition = transition.getCondition();
                ConditionType transitionType =
                    condition == null ? null : condition.getType();
                if (transition.getEvent() == null &&
                    (transitionType == null ||
                    transitionType == ConditionType.CONDITION ||
                    transitionType == ConditionType.OTHERWISE)) {

                    count++;
                }
            }
            if (count > 1) {
                ctx.addError(activity, SPLIT_REQUIRED,
                    new Object[]{activityId, new Integer(count)},
                    " has " + count +
                    " regular efferent (outbound) transitions, and therefore requires a Split");
            }
        }
        ctx.popPrefix();
    }

    private void checkActualParameters(Object src, List formalParms,
        List actualParms, ValidationContext ctx) {

        int fpCount = formalParms == null ? 0 : formalParms.size();
        int apCount = actualParms == null ? 0 : actualParms.size();
        boolean ok = ctx.checkEQ(src,
            " formal and actual parameter counts differ", "FormalParameters",
            fpCount, apCount);
        if (ok) {
            for (int i = 0; i < fpCount; i++) {
                FormalParameter fp = (FormalParameter)formalParms.get(i);
                ActualParameter ap = (ActualParameter)actualParms.get(i);
                switch (fp.getMode().getValue()) {
                    case ParameterMode.IN_INT:
                        // TODO: validate script expression.
                        break;
                    case ParameterMode.INOUT_INT:
                    case ParameterMode.OUT_INT:
                        ctx.checkValidIdRef(src, "/ActualParameter[" + fp.getId() +
                            ']', DATA_FIELD, WORKFLOW_FORMAL_PARAMETER,
                            PKG_DATA_FIELD, ap.getText());
                        break;
                }
            }
        }
    }

    private void checkTransitions(List transitions, ValidationContext ctx) {
        if (transitions != null) {
            for (Iterator i = transitions.iterator(); i.hasNext();) {
                Transition t = (Transition)i.next();
                checkTransition(t, ctx);
            }
        }
    }

    private void checkTransition(Transition transition,
        ValidationContext ctx) {

        String transitionId = transition.getId();
        ctx.pushPrefix("Transition[" + transitionId + ']');
        ctx.checkUniqueId(transition, null, TRANSITION, transitionId, transition);
        if (transition.getFromActivity() == null) {
            String fromId = transition.getFrom();
            ctx.addError(transition, TRANSITION_FROM_ACTIVITY_UNDEFINED,
                new Object[]{transitionId, fromId},
                "/From references an undefined Activity[" +
                fromId + ']');
        }
        if (transition.getToActivity() == null) {
            String toId = transition.getTo();
            ctx.addError(transition, TRANSITION_TO_ACTIVITY_UNDEFINED,
                new Object[]{transitionId, toId},
                "/To references an undefined Activity[" +
                toId + ']');
        }
//        transition.getEvent();
//        transition.getExecution();
        ctx.popPrefix();
    }
}