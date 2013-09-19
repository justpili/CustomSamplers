package postgresql;

import java.util.HashMap;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import binaryconfig.BinaryConfigElement;

import utils.BinaryFileInfo;
import utils.CustomSamplerUtils;

public class PostgreSampler extends AbstractSampler implements TestBean {

	private static final long serialVersionUID = 5294863538969681929L;
	private static final Logger log = LoggingManager.getLoggerForClass();

	public final static String DATABASE = "PostgreSampler.database";
	public final static String BINARYINFO = "PostgreSampler.binaryInfo";
	public final static String USECHUNKS = "PostgreSampler.useChunks";
	public final static String LARGEOBJECTMETHOD = "PostgreSampler.largeObjectMethod";
	public final static String DOREAD = "PostgreSampler.doRead";
	public final static String USERANDOMACCESS = "PostgreSampler.useRandomAccess";
	public final static String CHECKREAD = "PostgreSampler.checkRead";
	public final static String DOWRITE = "PostgreSampler.doWrite";
	public final static String ASSIGNED_WRITE = "PostgreSampler.assignedWrite";

	public static BinaryFileInfo binaryInfo;

	public PostgreSampler() {
		binaryInfo = null;
		trace("PostgreSampler()" + this.toString());
	}

	@Override
	public SampleResult sample(Entry arg0) {
		int threadID = CustomSamplerUtils.getThreadID(Thread.currentThread().getName());
		trace("sample() ThreadID: " + threadID);

		// Get BinaryInfo and QueryHandler instances.
		BinaryFileInfo binaryInfo = null;
		PostgreQueryHandler queryHandler = null;
		try {
			binaryInfo = BinaryConfigElement.getBinaryFileInfo(getBinaryInfo());
			queryHandler = new PostgreQueryHandler(getDatabase());
		} catch (Exception e) {
			log.error("Failed to create a PostgreQueryHandler instance for the " + 
					Thread.currentThread().getName() + " sampler. Details:" + e.toString());
		}

		// Get an initial SampleResult and parse options.
		SampleResult res = CustomSamplerUtils.getInitialSampleResult(getTitle());
		HashMap<String, Boolean> options = prepareOptions();

		if (options.get("doRead")) { // DO THE READ
			CustomSamplerUtils.readWith(queryHandler, binaryInfo, res, options);
		} else if (options.get("doWrite")) { // DO THE WRITE
			CustomSamplerUtils.writeWith(queryHandler, binaryInfo, res, options);
		}

		return res;
	}

	private HashMap<String, Boolean> prepareOptions() {
		HashMap<String, Boolean> options = new HashMap<String, Boolean>();
		options.put("doRead", Boolean.parseBoolean(getDoRead()));
		options.put("doWrite", Boolean.parseBoolean(getDoWrite()));
		options.put("useChunks", Boolean.parseBoolean(getUseChunks()));
		options.put("isRandom", Boolean.parseBoolean(getUseRandomAccess()));
		options.put("isCheckRead", Boolean.parseBoolean(getCheckRead()));
		options.put("isSpecial", Boolean.parseBoolean(getLargeObjectMethod()));
		options.put("isAssigned", Boolean.parseBoolean(getAssignedWrite()));
		return options;
	}

	private void trace(String s) {
		if(log.isDebugEnabled()) {
			log.debug(Thread.currentThread().getName() + " (" + getTitle() + " " + s + " " + this.toString());
		}
	}

	public String getTitle() {
		return this.getName();
	}
	public String getDatabase() {
		return getPropertyAsString(DATABASE);
	}
	public void setDatabase(String database) {
		setProperty(DATABASE, database);
	}
	public String getBinaryInfo() {
		return getPropertyAsString(BINARYINFO);
	}
	public void setBinaryInfo(String binaryInfo) {
		setProperty(BINARYINFO, binaryInfo);
	}
	public String getUseChunks() {
		return getPropertyAsString(USECHUNKS);
	}
	public void setUseChunks(String useChunks) {
		setProperty(USECHUNKS, useChunks);
	}
	public String getLargeObjectMethod() {
		return getPropertyAsString(LARGEOBJECTMETHOD);
	}
	public void setLargeObjectMethod(String largeObjectMethod) {
		setProperty(LARGEOBJECTMETHOD, largeObjectMethod);
	}
	public String getUseRandomAccess() {
		return getPropertyAsString(USERANDOMACCESS);
	}
	public void setUseRandomAccess(String useRandomAccess) {
		setProperty(USERANDOMACCESS, useRandomAccess);
	}
	public String getCheckRead() {
		return getPropertyAsString(CHECKREAD);
	}
	public void setCheckRead(String checkRead) {
		setProperty(CHECKREAD, checkRead);
	}
	public String getDoRead() {
		return getPropertyAsString(DOREAD);
	}
	public void setDoRead(String doRead) {
		setProperty(DOREAD, doRead);
	}
	public String getDoWrite() {
		return getPropertyAsString(DOWRITE);
	}
	public void setDoWrite(String doWrite) {
		setProperty(DOWRITE, doWrite);
	}
	public String getAssignedWrite() {
		return getPropertyAsString(ASSIGNED_WRITE);
	}
	public void setAssignedWrite(String assignedWrite) {
		setProperty(ASSIGNED_WRITE, assignedWrite);
	}
}
