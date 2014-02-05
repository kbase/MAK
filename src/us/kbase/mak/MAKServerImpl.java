package us.kbase.mak;

import DataMining.DiscoveryParameters;
import DataMining.Parameters;
import us.kbase.auth.AuthException;
import us.kbase.auth.AuthToken;
import us.kbase.auth.TokenFormatException;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UObject;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.expressionservices.ExpressionSample;
import us.kbase.idserverapi.IDServerAPIClient;
import us.kbase.userandjobstate.InitProgress;
import us.kbase.userandjobstate.Results;
import us.kbase.userandjobstate.UserAndJobStateClient;
import us.kbase.util.WsDeluxeUtil;
import us.kbase.workspace.ObjectData;
import util.MoreArray;
import util.TabFile;

import java.io.*;
import java.net.URL;
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

    private static final String DATA_PATH = "$(TARGET)/services/$(SERVICE_DIR)/";

    private static final String JOB_PATH = MAKServerConfig.JOB_DIRECTORY;
    private static final String MAK_DIR = MAKServerConfig.MAK_DIRECTORY;
    private static final String MAK_COMMAND = MAKServerConfig.MAK_RUN_PATH;

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

    public static void allMAKJobFromWs(String wsName, String kbgid,
                                       String jobId, String token,
                                       String currentDir) throws UnauthorizedException, IOException,
            JsonClientException, AuthException, InterruptedException,
            ClassNotFoundException {
        if (jobId != null)
            updateJobProgress(jobId,
                    "AWE task started. Preparing input...", token);
        // get expression data
            /*ExpressionSeries series = WsDeluxeUtil
                    .getObjectFromWsByRef(params.getSeriesRef(), token).getData()
    				.asClassInstance(ExpressionSeries.class);*/

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

    		/*// prepare input file
            createInputTable(jobPath, series.getExpressionSampleIds(), token);
    		writer.write("Input file created\n");
    		writer.flush();
    		series = null;*/


        /*TODO finish MAK command line*/
        // generate command line
        String readparam = DATA_PATH + "/" + kbgid + "_trajectory_parameters.txt";
        String commandLine = generateMAKCommandLine(jobPath, readparam);//wsName + "_" +
        writer.write(commandLine + "\n");
        writer.flush();
        gc();

        Parameters prm = new Parameters();
        prm.read(readparam);

        String readparamdisc = DATA_PATH + "/" + kbgid + "_discovery_parameters.txt";
        DiscoveryParameters dp = new DiscoveryParameters();
        dp.read(readparamdisc);

        MAKInputData mid = new MAKInputData();
        mid.withDataPath(prm.EXPR_DATA_PATH)
                .withDataType("expression")
                .withDescription("")
                .withId("")
                .withNumCols(-1L)
                .withNumRows(-1L)
                .withTaxon("")
                .withGenomeId(kbgid);

        MAKParameters makp = new MAKParameters();
        makp.withLinkage(dp.linkage)
                .withMaxBiclusterOverlap(dp.max_bicluster_overlap)
                .withMaxEnrichPvalue(dp.max_enrich_pvalue)
                .withMinRawBiclusterScore(dp.min_raw_bicluster_score)
                .withNullDataPath(dp.null_data_path)
                .withRcodepath(prm.R_METHODS_PATH)
                .withRdatapath(prm.R_DATA_PATH)
                .withRefine(dp.refine ? 1L : 0L)
                .withRounds(new Long(dp.rounds))
                .withRoundsMoveSequences(MoreArray.convtoArrayList(dp.move_sequences))
                .withInputs(Arrays.asList(mid));


        if (jobId != null)
            updateJobProgress(jobId,
                    "Input prepared. Starting MAK ...", token);

        Integer exitVal = executeCommand(commandLine, jobPath, jobId, token);
        //Integer exitVal = executeCommand("", jobPath, jobId, token);
        if (exitVal != null) {
            writer.write("ExitValue: " + exitVal.toString() + "\n");
            writer.flush();
        } else {
            writer.write("ExitValue: null\n");
            writer.flush();
        }
        // parse results
        if (jobId != null)
            updateJobProgress(jobId, "MAK finished. Processing output...",
                    token);

        String status = "";
        if (status != null) {
            writer.write("Error: " + status);
            if (jobId != null)
                finishJob(jobId, wsName, null, "Error: " + status,
                        token.toString());
            // close log file
            writer.close();
        } else {
            String resultId = getKbaseId("MAKResult");
            writer.write(resultId + "\n");
            makResult.setId(resultId);
            makResult.setParameters(makp);
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
                deleteFilesByPattern(MAK_DIR, "mak-checkpoint.*");
            }
            if (jobId != null)
                finishJob(jobId, wsName, makResult.getId(), "Finished",
                        token.toString());
        }
    }


    public static void singleMAKJobFromWs(String wsName, String kbgid, List<String> genes,
                                          String jobId, String token,
                                          String currentDir) throws UnauthorizedException, IOException,
            JsonClientException, AuthException, InterruptedException,
            ClassNotFoundException {
        if (jobId != null)
            updateJobProgress(jobId,
                    "AWE task started. Preparing input...", token);
        // get expression data
                   /*ExpressionSeries series = WsDeluxeUtil
                           .getObjectFromWsByRef(params.getSeriesRef(), token).getData()
           				.asClassInstance(ExpressionSeries.class);*/

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

           		/*// prepare input file
                   createInputTable(jobPath, series.getExpressionSampleIds(), token);
           		writer.write("Input file created\n");
           		writer.flush();
           		series = null;*/


        Parameters prm = new Parameters();
        prm.read(DATA_PATH + "/" + kbgid + "_trajectory_parameters.txt");

        String outputDirectory = jobPath + "out";
        prm.OUTDIR = outputDirectory;
        ArrayList<String[]> labels = loadLabels(kbgid);

        String[] gene_labels = labels.get(0);
        String[] exp_labels = labels.get(1);

        ArrayList geneIds = new ArrayList();
        int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels.length + 1);
        for (int i = 0; i < genes.size(); i++) {
            geneIds.add(MoreArray.getArrayInd(gene_labels, genes.get(i)) + 1);
        }

        //add starting point
        String g = MoreArray.toString(MoreArray.ArrayListtoInt(geneIds), ",");
        String e = MoreArray.toString(expall, ",");
        prm.init_block_list = (ArrayList) Arrays.asList(g + "/" + e);

        Random rand = new Random();

        //write out new paramter file
        String readparam = DATA_PATH + "/" + wsName + "_" + jobId + "_" + rand.nextLong() + "_trajectory_parameters.txt";
        prm.write(readparam);

        String readparamdisc = DATA_PATH + "/" + kbgid + "_discovery_parameters.txt";
        DiscoveryParameters dp = new DiscoveryParameters();
        dp.read(readparamdisc);


        MAKInputData mid = new MAKInputData();
        mid.withDataPath(prm.EXPR_DATA_PATH)
                .withDataType("expression")
                .withDescription("")
                .withId("")
                .withNumCols(-1L)
                .withNumRows(-1L)
                .withTaxon("")
                .withGenomeId(kbgid);

        MAKParameters makp = new MAKParameters();
        makp.withLinkage(dp.linkage)
                .withMaxBiclusterOverlap(dp.max_bicluster_overlap)
                .withMaxEnrichPvalue(dp.max_enrich_pvalue)
                .withMinRawBiclusterScore(dp.min_raw_bicluster_score)
                .withNullDataPath(dp.null_data_path)
                .withRcodepath(prm.R_METHODS_PATH)
                .withRdatapath(prm.R_DATA_PATH)
                .withRefine(dp.refine ? 1L : 0L)
                .withRounds(new Long(dp.rounds))
                .withRoundsMoveSequences(MoreArray.convtoArrayList(dp.move_sequences))
                .withInputs(Arrays.asList(mid));


        // generate command line
        //String readparam = DATA_PATH + "/" + kbgid + "_trajectory_parameters.txt";
        String commandLine = generateMAKCommandLine(jobPath, readparam);//wsName + "_" +
        writer.write(commandLine + "\n");
        writer.flush();
        gc();


        if (jobId != null)
            updateJobProgress(jobId,
                    "Input prepared. Starting MAK ...", token);

        Integer exitVal = executeCommand(commandLine, jobPath, jobId, token);
        //Integer exitVal = executeCommand("", jobPath, jobId, token);
        if (exitVal != null) {
            writer.write("ExitValue: " + exitVal.toString() + "\n");
            writer.flush();
        } else {
            writer.write("ExitValue: null\n");
            writer.flush();
        }
        // parse results
        if (jobId != null)
            updateJobProgress(jobId, "MAK finished. Processing output...",
                    token);

        String status = "";
        if (status != null) {
            writer.write("Error: " + status);
            if (jobId != null)
                finishJob(jobId, wsName, null, "Error: " + status,
                        token.toString());
            // close log file
            writer.close();
        } else {
            String resultId = getKbaseId("MAKResult");
            writer.write(resultId + "\n");
            makResult.setId(resultId);
            makResult.setParameters(makp);
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
                deleteFilesByPattern(MAK_DIR, "mak-checkpoint.*");
            }
            if (jobId != null)
                finishJob(jobId, wsName, makResult.getId(), "Finished",
                        token.toString());
        }
    }


    public static void searchMAKFromWs(String wsName, String kbgid, String data_type, List<String> genes,
                                       String jobId, String token,
                                       String currentDir) throws UnauthorizedException, IOException,
            JsonClientException, AuthException, InterruptedException,
            ClassNotFoundException {
        if (jobId != null)
            updateJobProgress(jobId,
                    "AWE task started. Preparing input...", token);

        // create working directory
        String jobPath = createDirs(jobId, currentDir);
        // start log file
        System.setErr(new PrintStream(new File(jobPath + "servererror.txt")));
        FileWriter writer = new FileWriter(jobPath + "serveroutput.txt");
        Date date = new Date();
        writer.write("log file created " + dateFormat.format(date) + "\n");
        writer.flush();

       /* String commandLine = generateSearchCommandLine(jobPath, kbgid, data_type, genes);//wsName + "_" +
        writer.write(commandLine + "\n");
        writer.flush();
        gc();*/


        if (jobId != null)
            updateJobProgress(jobId,
                    "Input prepared. Starting MAK ...", token);

        Integer exitVal = -1;//executeCommand(commandLine, jobPath, jobId, token);
        //Integer exitVal = executeCommand("", jobPath, jobId, token);
        if (exitVal != null) {
            writer.write("ExitValue: " + exitVal.toString() + "\n");
            writer.flush();
        } else {
            writer.write("ExitValue: null\n");
            writer.flush();
        }
        // parse results
        if (jobId != null)
            updateJobProgress(jobId, "MAK finished. Processing output...",
                    token);

        String status = "";
        if (status != null) {
            writer.write("Error: " + status);
            if (jobId != null)
                finishJob(jobId, wsName, null, "Error: " + status,
                        token.toString());
            // close log file
            writer.close();
        } else {
           /* String resultId = getKbaseId("MAKResult");
            writer.write(resultId + "\n");
            makResult.setId(resultId);
            makResult.setParameters(makp);
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
                deleteFilesByPattern(MAK_DIR, "mak-checkpoint.*");
            }
            if (jobId != null)
                finishJob(jobId, wsName, makResult.getId(), "Finished",
                        token.toString());*/
        }
    }

    public final static ArrayList loadLabels(String kbgid) {
        String[] gene_labels = null;
        String gread = DATA_PATH + "/" + kbgid + "_geneids.txt";
        try {
            String[][] sarray = TabFile.readtoArray(gread);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            int col = 2;
            String[] n = MoreArray.extractColumnStr(sarray, col);
            gene_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
        } catch (Exception e) {
            //System.out.println("expecting 2 cols");
            //e.printStackTrace();
            try {
                String[][] sarray = TabFile.readtoArray(gread);
                System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
                int col = 1;
                String[] n = MoreArray.extractColumnStr(sarray, col);
                gene_labels = MoreArray.replaceAll(n, "\"", "");
                System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        String[] exp_labels = null;
        String eread = DATA_PATH + "/" + kbgid + "_expids.txt";
        try {
            String[][] sarray = TabFile.readtoArray(eread);
            System.out.println("setLabels e " + sarray.length + "\t" + sarray[0].length);
            int col = 2;
            exp_labels = MoreArray.replaceAll(MoreArray.extractColumnStr(sarray, col), "\"", "");
            System.out.println("setLabels e " + exp_labels.length + "\t" + exp_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting 2 cols");
            //e.printStackTrace();
            try {
                String[][] sarray = TabFile.readtoArray(eread);
                System.out.println("setLabels e " + sarray.length + "\t" + sarray[0].length);
                int col = 1;
                exp_labels = MoreArray.replaceAll(MoreArray.extractColumnStr(sarray, col), "\"", "");
                System.out.println("setLabels e " + exp_labels.length + "\t" + exp_labels[0]);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }


        try {
            boolean bad = false;
            if (gene_labels == null) {
                System.out.println("failed to read gene labels " + gread);
                bad = true;
            } else if (exp_labels == null) {
                System.out.println("failed to read exp labels " + eread);
                bad = true;
            }
            if (bad)
                System.exit(0);
        } finally {

        }

        ArrayList out = new ArrayList();

        out.add(gene_labels);
        out.add(exp_labels);

        return out;
    }

    /**
     * @param jobId
     * @param currentDir
     * @return
     */
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
        new File(jobPath + "out_1/").mkdir();
        new File(jobPath + "cache/").mkdir();
        new File(jobPath + "tmp/").mkdir();

        return jobPath;
    }

    protected static String generateMAKCommandLine(String jobPath, String parampath) {

        String commandLine = null;
        try {
            /*Parameters now = new Parameters(defparampath);
            now.OUTDIR = outputDirectory;

            String outfile = currentDir + "/" + jobId + "_parameters.txt";
            now.write(outfile);
*/
            commandLine = "java -Xmx2G DataMining.RunMiner -param " + parampath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commandLine;
    }

    /**
     * @param jobId
     * @param desc
     * @param tasks
     * @param token
     * @throws UnauthorizedException
     * @throws IOException
     * @throws JsonClientException
     * @throws AuthException
     */
    protected static void startJob(String jobId, String desc, Long tasks,
                                   String token) throws UnauthorizedException, IOException,
            JsonClientException, AuthException {

        String status = "MAK service job started. Preparing input...";
        InitProgress initProgress = new InitProgress();
        initProgress.setPtype("task");
        initProgress.setMax(tasks);
        Date date = new Date();
        date.setTime(date.getTime() + 108000000L);
        jobClient(token).startJob(jobId, token, status, desc, initProgress,
                dateFormat.format(date));
    }

    /**
     * @param jobId
     * @param status
     * @param token
     * @throws UnauthorizedException
     * @throws IOException
     * @throws JsonClientException
     * @throws AuthException
     */
    protected static void updateJobProgress(String jobId, String status,
                                            String token) throws UnauthorizedException, IOException,
            JsonClientException, AuthException {
        Date date = new Date();
        date.setTime(date.getTime() + 1000000L);
        jobClient(token).updateJobProgress(jobId, token, status, 1L,
                dateFormat.format(date));
    }

    /**
     * @param jobId
     * @param wsId
     * @param objectId
     * @param status
     * @param token
     * @throws UnauthorizedException
     * @throws IOException
     * @throws JsonClientException
     * @throws AuthException
     */
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

    /**
     * @param entityType
     * @return
     * @throws TokenFormatException
     * @throws UnauthorizedException
     * @throws IOException
     * @throws JsonClientException
     */
    protected static String getKbaseId(String entityType)
            throws TokenFormatException, UnauthorizedException, IOException,
            JsonClientException {
        String returnVal = null;
        if (entityType.equals("MAKResult")) {
            returnVal = "kb|makrunresult."
                    + idClient().allocateIdRange("kb|makrunresultmakrunresult", 1L)
                    .toString();
        } else if (entityType.equals("MAKBiclusterSet")) {
            returnVal = "kb|biclusterset."
                    + idClient().allocateIdRange("kb|biclusterset", 1L)
                    .toString();
        } else if (entityType.equals("MAKBicluster")) {
            returnVal = "kb|bicluster."
                    + idClient().allocateIdRange("kb|bicluster", 1L)
                    .toString();
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
                new File(MAK_DIR));

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
