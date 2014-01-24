package us.kbase.mak;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UObject;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.expressionservices.ExpressionSample;
import us.kbase.expressionservices.ExpressionSeries;
import us.kbase.idserverapi.IDServerAPIClient;
import us.kbase.userandjobstate.InitProgress;
import us.kbase.userandjobstate.Results;
import us.kbase.userandjobstate.UserAndJobStateClient;
import us.kbase.util.GenomeExporter;
import us.kbase.util.NetworkExporter;
import us.kbase.util.WsDeluxeUtil;
import us.kbase.workspace.ObjectData;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/15/14
 * Time: 2:47 PM
 */
public class MAKServerImpl {

    	private static boolean awe = MAKServerConfig.DEPLOY_AWE;

    	private static final String JOB_PATH = MAKServerConfig.JOB_DIRECTORY;
    	private static final String CMONKEY_DIR = MAKServerConfig.MAK_DIRECTORY;
    	private static final String CMONKEY_COMMAND = MAKServerConfig.MAK_RUN_PATH;

    	private static final String ID_SERVICE_URL = MAKServerConfig.ID_SERVICE_URL;
    	private static final String JOB_SERVICE_URL = MAKServerConfig.JOB_SERVICE_URL;
    	private static IDServerAPIClient _idClient = null;
    	private static UserAndJobStateClient _jobClient = null; // this class will
    															// run separately
    															// for each task,
    															// thus UJS client
    															// can be static
    															// singleton

    //	private static Date date = new Date();
    	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
    			"yyyy-MM-dd'T'HH:mm:ssZ");

    	protected static IDServerAPIClient idClient() throws TokenFormatException,
                UnauthorizedException, IOException {
    		if (_idClient == null) {
    			URL idServerUrl = new URL(ID_SERVICE_URL);
    			_idClient = new IDServerAPIClient(idServerUrl);
    		}
    		return _idClient;
    	}

    	protected static UserAndJobStateClient jobClient(String token)
    			throws UnauthorizedException, IOException, AuthException {
    		if (_jobClient == null) {
    			URL jobServiceUrl = new URL(JOB_SERVICE_URL);
    			AuthToken authToken = new AuthToken(token);
    			_jobClient = new UserAndJobStateClient(jobServiceUrl, authToken);
    			_jobClient.setAuthAllowedForHttp(true);
    		}
    		return _jobClient;
    	}

    	public static void MAKJobFromWs(String wsName,
    			MAKParameters params, String jobId, String token,
    			String currentDir) throws UnauthorizedException, IOException,
                JsonClientException, AuthException, InterruptedException,
    			ClassNotFoundException, SQLException {
    		// Let's start!
    		if (jobId != null)
    			updateJobProgress(jobId,
    					"AWE task started. Preparing input...", token);
    		// get expression data
    		ExpressionSeries series = WsDeluxeUtil
    				.getObjectFromWsByRef(params.getSeriesRef(), token).getData()
    				.asClassInstance(ExpressionSeries.class);
    		// create result object
    		MAKResult makResult = new MAKResult()
    				.withId(getKbaseId("MAKResult"));
    		// create working directory
    		String jobPath = createDirs(jobId, currentDir);
    		// start log file
    		System.setErr(new PrintStream(new File(jobPath + "servererror.txt")));
    		FileWriter writer = new FileWriter(jobPath + "serveroutput.txt");
    		Date date = new Date();
    		writer.write("log file created " + dateFormat.format(date) + "\n");
    		writer.flush();
    		// prepare input file
    		createInputTable(jobPath, series.getExpressionSampleIds(), token);
    		writer.write("Input file created\n");
    		writer.flush();
    		series = null;
    		// prepare cache files
    		String genomeName = prepareCacheFiles(jobPath + "cache/", params,
    				token, writer);
    		writer.write("Cache files created in " + jobPath + "cache/\n");
    		writer.flush();
    		// generate command line
    		String commandLine = generateMAKCommandLine(jobPath, params);
    		writer.write(commandLine + "\n");
    		writer.flush();
    		gc();
    		// cross fingers and run cmonkey-python
    		if (jobId != null)
    			updateJobProgress(jobId,
    					"Input prepared. Starting cMonkey program...", token);
    		Integer exitVal = executeCommand(commandLine, jobPath, jobId, token);
    		if (exitVal != null) {
    			writer.write("ExitValue: " + exitVal.toString() + "\n");
    			writer.flush();
    		} else {
    			writer.write("ExitValue: null\n");
    			writer.flush();
    		}
    		// parse results
    		if (jobId != null)
    			updateJobProgress(jobId, "cMonkey finished. Processing output...",
    					token);
    		String sqlFile = jobPath + "out/cmonkey_run.db";
    		writer.write(sqlFile + "\n");
    		writer.flush();
    		//
    		// String status = parseCmonkeySql(sqlFile, makResult, genomeName);
            String status = "";
    		if (status != null) {
    			writer.write("Error: " + status);
    			if (jobId != null)
    				finishJob(jobId, wsName, null, "Error: " + status,
    						token.toString());
    			// close log file
    			writer.close();
    		} else {
    			String resultId = getKbaseId("CmonkeyRunResult");
    			writer.write(resultId + "\n");
    			// get ID for the result
                makResult.setId(resultId);
                makResult.setParameters(params);
    			// save result
    			WsDeluxeUtil.saveObjectToWorkspace(UObject.transformObjectToObject(
                        makResult, UObject.class),
    					"MAK.MAKResult", wsName, makResult
    							.getId(), token.toString());
    			// close log file
    			writer.close();
    			// clean up (if not on AWE)
    			if (awe == false) {
    				File fileDelete = new File(jobPath);
    				deleteDirectoryRecursively(fileDelete);
    				deleteFilesByPattern(CMONKEY_DIR, "mak-checkpoint.*");
    			}
    			if (jobId != null)
    				finishJob(jobId, wsName, makResult.getId(), "Finished",
    						token.toString());
    		}
    	}

    	protected static String prepareCacheFiles(String cachePath,
    			MAKParameters params, String token, FileWriter writer)
    			throws TokenFormatException, IOException, JsonClientException {
    		// get genome, contigset and export
    		/*String genomeName = GenomeExporter.writeGenome(params.getGenomeRef(),
                    "my_favorite_pet", cachePath, token);
    		writer.write("Genome files created\n");
    		writer.flush();
    		// get operons and export
    		gc();
    		if (!((params.getOperomeRef() == null) || (params.getOperomeRef()
    				.equals("")))) {
    			NetworkExporter.exportOperons(params.getOperomeRef(), "1",
                        cachePath, token);
    			writer.write("Operons file created\n");
    			writer.flush();
    		}
    		// get string and export
    		gc();
    		if (!((params.getNetworkRef() == null) || (params.getNetworkRef()
    				.equals("")))) {
    			NetworkExporter.exportString(params.getNetworkRef(), "1",
    					cachePath, token);
    			writer.write("String file created\n");
    			writer.flush();
    		}
    		return genomeName;*/
            return null;
    	}

    	protected static String createDirs(String jobId, String currentDir) {
    		String jobPath = null;

    		if (currentDir == null) {
    			jobPath = JOB_PATH + jobId + "/";
    			new File(jobPath).mkdir();
    		} else {
    			jobPath = currentDir + "/" + jobId + "/";
    			new File(jobPath).mkdir();
    			awe = true;
    		}
    		new File(jobPath + "out/").mkdir();
    		new File(jobPath + "cache/").mkdir();
    		new File(jobPath + "tmp/").mkdir();

    		return jobPath;
    	}

    	protected static String generateMAKCommandLine(String jobPath,
    			MAKParameters params) {

    		String outputDirectory = jobPath + "out";
    		String cacheDirectory = jobPath + "cache";
    		String inputFile = jobPath + "input.txt";

    		/*String commandLine = CMONKEY_COMMAND
    				+ " --rsat_organism my_favorite_pet --rsat_dir "
    				+ cacheDirectory + " --ratios " + inputFile + " --cache "
    				+ cacheDirectory + " --out " + outputDirectory
    				+ " --config /etc/cmonkey-python/config.ini ";
    		// Set options
    		if (params.getMotifsScoring() == 0L) {
    			commandLine += " --nomotifs";
    		}
    		if (!((params.getOperomeRef() == null) || (params.getOperomeRef()
    				.equals("")))) {
    			commandLine += " --operons " + cacheDirectory + "/gnc1.named";
    		} else {
    			commandLine += " --nooperons";
    		}
    		if (!((params.getNetworkRef() == null) || (params.getNetworkRef()
    				.equals("")))) {
    			commandLine += " --string " + cacheDirectory + "/1.gz";
    			if (params.getNetworksScoring() == 0L) {
    				commandLine += " --nonetworks";
    			}
    		} else {
    			commandLine += " --nostring --nonetworks";
    		}
    		return commandLine;*/

            return null;
    	}

    	protected static void startJob(String jobId, String desc, Long tasks,
    			String token) throws UnauthorizedException, IOException,
    			JsonClientException, AuthException {

    		String status = "cmonkey service job started. Preparing input...";
    		InitProgress initProgress = new InitProgress();
    		initProgress.setPtype("task");
    		initProgress.setMax(tasks);
    		Date date = new Date();
    		date.setTime(date.getTime() + 108000000L);
    		jobClient(token).startJob(jobId, token, status, desc, initProgress,
    				dateFormat.format(date));
    	}

    	protected static void updateJobProgress(String jobId, String status,
    			String token) throws UnauthorizedException, IOException,
    			JsonClientException, AuthException {
    		Date date = new Date();
    		date.setTime(date.getTime() + 1000000L);
    		jobClient(token).updateJobProgress(jobId, token, status, 1L,
    				dateFormat.format(date));
    	}

    	protected static void finishJob(String jobId, String wsId, String objectId,
    			String status, String token) throws UnauthorizedException,
    			IOException, JsonClientException, AuthException {
    		String error = null;
    		Results res = new Results();
    		List<String> workspaceIds = new ArrayList<String>();
    		workspaceIds.add(wsId + "/" + objectId);
    		res.setWorkspaceids(workspaceIds);
    		jobClient(token).completeJob(jobId, token, status, error, res);
    	}

    	protected static String getKbaseId(String entityType)
    			throws TokenFormatException, UnauthorizedException, IOException,
    			JsonClientException {
    		String returnVal = null;
    		if (entityType.equals("CmonkeyRunResult")) {
    			returnVal = "kb|cmonkeyrunresult."
    					+ idClient().allocateIdRange("kb|cmonkeyrunresult", 1L)
    							.toString();
    		} else if (entityType.equals("CmonkeyNetwork")) {
    			returnVal = "kb|cmonkeynetwork."
    					+ idClient().allocateIdRange("kb|cmonkeynetwork", 1L)
    							.toString();
    		} else if (entityType.equals("CmonkeyCluster")) {
    			returnVal = "kb|cmonkeycluster."
    					+ idClient().allocateIdRange("kb|cmonkeycluster", 1L)
    							.toString();
    		} else if (entityType.equals("CmonkeyMotif")) {
    			returnVal = "kb|cmonkeymotif."
    					+ idClient().allocateIdRange("kb|cmonkeymotif", 1L)
    							.toString();
    		} else if (entityType.equals("MastHit")) {
    			returnVal = "kb|masthit."
    					+ idClient().allocateIdRange("kb|masthit", 1L).toString();
    		} else if (entityType.equals("ExpressionSeries")) {
    			returnVal = "kb|series."
    					+ idClient().allocateIdRange("kb|series", 1L).toString();
    		} else if (entityType.equals("ExpressionSample")) {
    			returnVal = "kb|sample."
    					+ idClient().allocateIdRange("kb|sample", 1L).toString();
    		} else {
    			System.out.println("ID requested for unknown type " + entityType);
    		}
    		return returnVal;
    	}

    	protected static void createInputTable(String jobPath,
    			List<String> sampleRefs, String token) throws TokenFormatException,
    			IOException, JsonClientException {

    		List<ObjectData> objects = WsDeluxeUtil.getObjectsFromWsByRef(sampleRefs, token);
    		List<ExpressionSample> samples = new ArrayList<ExpressionSample>();
    		for (ObjectData o : objects) {
    			ExpressionSample s = o.getData().asClassInstance(
    					ExpressionSample.class);
    			samples.add(s);
    		}

    		BufferedWriter writer = null;
    		writer = new BufferedWriter(new FileWriter(jobPath + "input.txt"));
    		writer.write("GENE");

    		List<Map<String, Double>> dataCollection = new ArrayList<Map<String, Double>>();
    		// make list of conditions
    		for (ExpressionSample sample : samples) {
    			writer.write("\t" + sample.getKbId());
    			Map<String, Double> dataSet = sample.getExpressionLevels();
    			dataCollection.add(dataSet);
    		}
    		// make list of genes
    		List<String> geneNames = new ArrayList<String>();
    		for (ExpressionSample sample : samples) {
    			geneNames.addAll(sample.getExpressionLevels().keySet());
    		}
    		List<String> uniqueGeneNames = new ArrayList<String>(
    				new HashSet<String>(geneNames));
    		for (String geneName : uniqueGeneNames) {
    			writer.write("\n" + geneName);
    			DecimalFormat df = new DecimalFormat("0.000");
    			for (Map<String, Double> dataSetMap : dataCollection) {
    				if (dataSetMap.containsKey(geneName)) {
    					if (dataSetMap.get(geneName).toString().matches("-.*")) {
    						writer.write("\t" + df.format(dataSetMap.get(geneName)));
    					} else {
    						writer.write("\t "
    								+ df.format(dataSetMap.get(geneName)));
    					}
    				} else {
    					writer.write("\tNA");
    				}
    			}
    		}
    		writer.write("\n");
    		writer.close();
    	}

    	protected static Integer executeCommand(String commandLine, String jobPath,
    			String jobId, String token) throws InterruptedException,
    			IOException {
    		Integer exitVal = null;
    		Process p = Runtime.getRuntime().exec(commandLine, null,
    				new File(CMONKEY_DIR));

    		StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
    				"ERROR", jobId, token, jobPath + "errorlog.txt");

    		// any output?
    		StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(),
    				"OUTPUT", jobId, token, jobPath + "out.txt");

    		// kick them off
    		errorGobbler.start();
    		outputGobbler.start();

    		// any error???
    		exitVal = p.waitFor();
    		System.out.println("ExitValue: " + exitVal);
    		return exitVal;

    	}

    	/*protected static String parseCmonkeySql(String sqlFile,
    			CmonkeyRunResult cmonkeyRunResult, String genomeName)
    			throws ClassNotFoundException, SQLException, IOException,
    			JsonClientException {
    		CmonkeySqlite database = new CmonkeySqlite(sqlFile);
    		String status = database.buildCmonkeyRunResult(cmonkeyRunResult,
    				genomeName);
    		database.disconnect();
    		return status;
    	}
*/
    	protected static void deleteFilesByPattern(String folder,
    			final String pattern) {
    		File dir = new File(folder);
    		File fileDelete;

    		for (String file : dir.list(new FilenameFilter() {
    			public boolean accept(File dir, String name) {
    				return name.matches(pattern);
    			}
    		})) {
    			String temp = new StringBuffer(folder).append(File.separator)
    					.append(file).toString();
    			fileDelete = new File(temp);
    			boolean isdeleted = fileDelete.delete();
    			System.out.println("file : " + temp + " is deleted : " + isdeleted);
    		}
    	}

    	public static void deleteDirectoryRecursively(File startFile) {
    		if (startFile.isDirectory()) {
    			for (File f : startFile.listFiles()) {
    				deleteDirectoryRecursively(f);
    			}
    			startFile.delete();
    		} else {
    			startFile.delete();
    		}
    	}

    	public static void gc() {
    		Object obj = new Object();
    		@SuppressWarnings("rawtypes")
    		java.lang.ref.WeakReference ref = new java.lang.ref.WeakReference<Object>(
    				obj);
    		obj = null;
    		while (ref.get() != null) {
    			System.out.println("garbage collector");
    			System.gc();
    		}
    	}

}