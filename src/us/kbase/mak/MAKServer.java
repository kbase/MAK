package us.kbase.mak;

//BEGIN_HEADER
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import DataMining.Parameters;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonServerMethod;
import us.kbase.common.service.JsonServerServlet;
import util.MoreArray;
import util.TabFile;
//END_HEADER

/**
 * <p>Original spec-file module name: MAK</p>
 * <pre>
 * Module MAK version 1.0
 * This module provides access to MAK biclustering results.
 * @author marcin
 * </pre>
 */
public class MAKServer extends JsonServerServlet {
    private static final long serialVersionUID = 1L;

    //BEGIN_CLASS_HEADER

    public static String PATH_TO_GENE_IDS = "/kb/dev_container/modules/MAK/data";
    String user;
    String pwd;
    //END_CLASS_HEADER

    public MAKServer() throws Exception {
        super("MAK");
        //BEGIN_CONSTRUCTOR

        user = super.config.get("dbUser");
        pwd = super.config.get("dbPwd");
        //END_CONSTRUCTOR
    }

    /**
     * <p>Original spec-file function name: runall_MAK_job_from_ws</p>
     * <pre>
     * Starts MAK server job and returns job ID of the run
     * string ws_id - workspace id
     * string kbgid - kbase genome id kbgid
     * string job_id - identifier of MAK job
     * </pre>
     * @param   wsId   instance of String
     * @param   kbgid   instance of String
     * @param   dataType   instance of String
     * @return   parameter "MAK_job_id" of String
     */
    @JsonServerMethod(rpc = "MAK.runall_MAK_job_from_ws")
    public String runallMAKJobFromWs(String wsId, String kbgid, String dataType, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN runall_MAK_job_from_ws
        //END runall_MAK_job_from_ws
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: runsingle_MAK_job_from_ws</p>
     * <pre>
     * Starts MAK server job and returns job ID of the run
     * string ws_id - workspace id
     * tring kbgid - kbase genome id kbgid
     * MAKBicluster makb - starting point bicluster
     * string job_id - identifier of MAK job
     * </pre>
     * @param   wsId   instance of String
     * @param   kbgid   instance of String
     * @param   dataType   instance of String
     * @param   geneids   instance of list of String
     * @return   parameter "MAK_job_id" of String
     */
    @JsonServerMethod(rpc = "MAK.runsingle_MAK_job_from_ws")
    public String runsingleMAKJobFromWs(String wsId, String kbgid, String dataType, List<String> geneids, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN runsingle_MAK_job_from_ws

        Parameters prm = new Parameters();
        final String defaultprm = kbgid + "_" + dataType + "_parameters.txt";
        prm.read(defaultprm);

        String[] gene_labels;
        String geneidfile = kbgid + "_" + dataType + "geneids.txt";

        File testg = new File(geneidfile);
        if (testg.exists()) {
            try {
                String[][] sarray = TabFile.readtoArray(geneidfile);
                System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
                int col = 2;
                String[] n = MoreArray.extractColumnStr(sarray, col);
                gene_labels = MoreArray.replaceAll(n, "\"", "");
                System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
            } catch (Exception e) {
                System.out.println("expecting 2 cols");
                //e.printStackTrace();
                try {
                    String[][] sarray = TabFile.readtoArray(geneidfile);
                    System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
                    int col = 1;
                    String[] n = MoreArray.extractColumnStr(sarray, col);
                    gene_labels = MoreArray.replaceAll(n, "\"", "");
                    System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }


        prm.init_block_list = new ArrayList();
        prm.INIT_BLOCKS = new ArrayList[2];
        //prm.INIT_BLOCKS[0] = MoreArray.convtoArrayList(genesInt);
        //prm.INIT_BLOCKS[1] = MoreArray.convtoArrayList(expsInt);
        prm.init_block_list.add(prm.INIT_BLOCKS);

        Date da = new Date();
        Random rand = new Random();

        final String newprm = defaultprm + "_" + da.getTime() + "_" + rand.nextLong() + ".txt";
        prm.write(newprm);

        //RunMiner rm = new RunMiner(newprm, 0);

        /*TODO check if job is done*/
        /*TODO if done create JSONP objects*/
        /*TODO upload JSONP to workspace*/
        //END runsingle_MAK_job_from_ws
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: search_MAK_results_from_ws</p>
     * <pre>
     * Starts MAK server job for searching precomputed biclusters and returns job ID of the run
     * string ws_id - workspace id
     * string kbgid - kbase genome id kbgid
     * list<string> geneids - list of kb gene ids
     * MAKBiclusterSet set - MAKBiclusterSet
     * </pre>
     * @param   wsId   instance of String
     * @param   kbgid   instance of String
     * @param   dataType   instance of String
     * @param   geneids   instance of list of String
     * @return   parameter "mbs" of type {@link us.kbase.mak.MAKBiclusterSet MAKBiclusterSet}
     */
    @JsonServerMethod(rpc = "MAK.search_MAK_results_from_ws")
    public MAKBiclusterSet searchMAKResultsFromWs(String wsId, String kbgid, String dataType, List<String> geneids, AuthToken authPart) throws Exception {
        MAKBiclusterSet returnVal = null;
        //BEGIN search_MAK_results_from_ws
        //END search_MAK_results_from_ws
        return returnVal;
    }

    /**
     * <p>Original spec-file function name: search_MAK_results_from_cds</p>
     * <pre>
     * Starts MAK server job for searching precomputed biclusters from the CDS and returns job ID of the run
     * string ws_id - workspace id
     * string kbgid - kbase genome id kbgid
     * list<string> geneids - list of kb gene ids
     * MAKBiclusterSet set - MAKBiclusterSet
     * </pre>
     * @param   kbgid   instance of String
     * @param   dataType   instance of String
     * @param   geneids   instance of list of String
     * @return   parameter "mbs" of type {@link us.kbase.mak.MAKBiclusterSet MAKBiclusterSet}
     */
    @JsonServerMethod(rpc = "MAK.search_MAK_results_from_cds")
    public MAKBiclusterSet searchMAKResultsFromCds(String kbgid, String dataType, List<String> geneids, AuthToken authPart) throws Exception {
        MAKBiclusterSet returnVal = null;
        //BEGIN search_MAK_results_from_cds

        String[] args = {};
        SearchBiclustersCDMI sc = new SearchBiclustersCDMI();

        ArrayList conv = new ArrayList(geneids);
        try {
            System.out.println("searchMAKResultsFromCDS");
            System.out.println("java.vm.version " + System.getProperty("java.vm.version"));
            System.out.println("JAVA_HOME " + System.getenv("JAVA_HOME"));
            System.out.println("KB_DEPLOYMENT_CONFIG " + System.getenv("KB_DEPLOYMENT_CONFIG"));
            System.out.println("KB_DEPLOYMENT_CONFIG p " + System.getProperty("KB_DEPLOYMENT_CONFIG"));
            System.out.println("KB_SERVICE_NAME " + System.getenv("KB_SERVICE_NAME"));
            System.out.println("KB_SERVICE_NAME p " + System.getProperty("KB_SERVICE_NAME"));
            System.out.println(MoreArray.arrayListtoString(conv, ","));
            String genemapfilename = kbgid + "_allmapunmap.txt";
            System.out.println(MAKServer.PATH_TO_GENE_IDS + "/" + genemapfilename);
            System.out.println(dataType);

            sc.doInit(MoreArray.arrayListtoString(conv, ","), null, MAKServer.PATH_TO_GENE_IDS + "/" + genemapfilename,
                    dataType, null, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            returnVal = sc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //END search_MAK_results_from_cds
        return returnVal;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: <program> <server_port>");
            return;
        }
        new MAKServer().startupServer(Integer.parseInt(args[0]));
    }
}
