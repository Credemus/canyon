package de.objectcode.canyon.bpe.engine.evaluator;

import java.sql.Timestamp;
import java.util.Date;

import de.objectcode.canyon.bpe.engine.activities.Activity;
import de.objectcode.canyon.model.process.Duration;
import de.objectcode.canyon.model.process.DurationUnit;
import de.objectcode.canyon.spi.calendar.IBusinessCalendar;

public class BSFContext {

	private Activity fActivity;
	
	private String   fClientID;
	
	private BusinessCalendarHelper fBusinessCalendarHelper;
	
	public BSFContext(Activity activity) {
		fActivity = activity;
		fBusinessCalendarHelper = new BusinessCalendarHelper();
		
	}

	public BusinessCalendarHelper getBusinessCalendarHelper() {
		return fBusinessCalendarHelper;
	}
	
	public Activity getActivity() {
		return fActivity;
	}

	public String getClientID() {
		return fActivity.getProcess().getClientId();
	}
	
	public DurationUnit getDefaultDurationUnit() {
		return fActivity.getProcess().getDefaultDurationUnit();
	}
	
	public class BusinessCalendarHelper {
		private IBusinessCalendar fBusinessCalendar;
		public BusinessCalendarHelper() {
			fBusinessCalendar = getActivity().getProcess().getBPEEngine().getBusinessCalendar();
		}
	
		// TODO Add more methods
		public Timestamp addDays(int days) {
			return new Timestamp(fBusinessCalendar.add(getClientID(),System.currentTimeMillis(),new Duration(days,DurationUnit.DAY)));
		}
		
		public Timestamp addWithDefaultDurationUnit(int amount) {
			return new Timestamp(fBusinessCalendar.add(getClientID(),System.currentTimeMillis(),new Duration(amount,getDefaultDurationUnit())));
		}
		public Timestamp addWithDefaultDurationUnit(Date date, int amount) {
			return new Timestamp(fBusinessCalendar.add(getClientID(),date.getTime(),new Duration(amount,getDefaultDurationUnit())));
		}
		public Timestamp addWithDefaultDurationUnit(long base, int amount) {
			return new Timestamp(fBusinessCalendar.add(getClientID(),base,new Duration(amount,getDefaultDurationUnit())));
		}
	}
	
}
