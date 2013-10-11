package de.objectcode.canyon.bpe.engine.activities;

import de.objectcode.canyon.bpe.engine.EngineException;
import de.objectcode.canyon.bpe.engine.evaluator.ICondition;

import java.util.Iterator;

import org.dom4j.Element;

/**
 * @author junglas
 * @created 17. Juni 2004
 */
public class While extends CompositeActivity {
	final static long serialVersionUID = 5124517202719720842L;

	protected ICondition m_condition;

	/**
	 * Constructor for the While object
	 * 
	 * @param name
	 *          Description of the Parameter
	 * @param scope
	 *          Description of the Parameter
	 */
	public While(String name, Scope scope) {
		super(name, scope);
	}

	/**
	 * Gets the elementName attribute of the While object
	 * 
	 * @return The elementName value
	 */
	public String getElementName() {
		return "while";
	}

	/**
	 * @see de.objectcode.canyon.bpe.engine.activities.IActivityContainer#isNonBlocked()
	 */
	public boolean isNonBlocked() {
		return false;
	}

	/**
	 * @exception EngineException
	 *              Description of the Exception
	 * @see de.objectcode.canyon.bpe.engine.activities.Activity#complete()
	 */
	public void complete() throws EngineException {
		if (m_condition.eval(this)) {
			Iterator it = m_childActivities.iterator();

			while (it.hasNext()) {
				Activity activity = (Activity) it.next();

				activity.reopen();
				activity.activate();
			}
		} else
			super.complete();
	}

	/**
	 * @exception EngineException
	 *              Description of the Exception
	 * @see de.objectcode.canyon.bpe.engine.activities.Activity#start()
	 */
	public void start() throws EngineException {
		super.start();

		if (m_condition.eval(this)) {
			Iterator it = m_childActivities.iterator();

			while (it.hasNext()) {
				Activity activity = (Activity) it.next();

				activity.activate();
			}
		}
	}

	/**
	 * @param element
	 *          Description of the Parameter
	 * @see de.objectcode.canyon.bpe.util.IDomSerializable#toDom(org.dom4j.Element)
	 */
	public void toDom(Element element) {
		super.toDom(element);
		
    if ( m_condition != null ) {
      m_condition.toDom( element.addElement( m_condition.getElementName() ) );
    }

		Iterator it = m_childActivities.iterator();

		while (it.hasNext()) {
			Activity activity = (Activity) it.next();

			activity.toDom(element.addElement(activity.getElementName()));
		}
	}

	public void setCondition(ICondition condition) {
		m_condition = condition;
	}
}
