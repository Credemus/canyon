package de.objectcode.canyon.spiImpl.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.canyon.api.worklist.BPEEvent;
import de.objectcode.canyon.api.worklist.ProcessInstanceData;
import de.objectcode.canyon.spi.event.IBPEEventListener;

public class LoggingBPEEventListener implements IBPEEventListener {
	private final static Log log = LogFactory
			.getLog(LoggingBPEEventListener.class);

	public void handleBPEEvent(BPEEvent bpeEvent) {
		StringBuilder b = new StringBuilder();
		b.append("Event:");
		b.append("type=").append(bpeEvent.getType());
		b.append(",packageId=").append(bpeEvent.getPackageId());
		b.append(",packageVersion=").append(bpeEvent.getPackageVersion());
		b.append(",processId=").append(bpeEvent.getProcessId());
		b.append(",processVersion=").append(bpeEvent.getProcessVersion());
		b.append(",activityId=").append(bpeEvent.getActivityId());
		b.append(",processInstanceIdPath=").append(bpeEvent.getProcessInstanceIdPath());
		b.append(",parameters=[");
		boolean first = true;
		for (ProcessInstanceData.Attribute attribute : bpeEvent
				.getProcessInstanceData().getAttributes()) {
			if (!first) {
				b.append(",");
			} else {
				first = false;
			}
			b.append(attribute.getName()).append("=").append(attribute.getValue());
		}
		b.append("]");
		first = true;
		b.append(",performers=[");
		if (bpeEvent.getPerformers() != null) {
			for (String performer : bpeEvent.getPerformers()) {
				if (!first) {
					b.append(",");
				} else {
					first = false;
				}
				b.append(performer);
			}
		}
		b.append("]");
		log.error(b.toString());
	}

}
