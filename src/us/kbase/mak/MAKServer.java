package us.kbase.mak;

import java.util.ArrayList;
import java.util.List;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonServerMethod;
import us.kbase.common.service.JsonServerServlet;
import util.MoreArray;

//BEGIN_HEADER
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
     * string job_id - identifier of MAK job
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

        String[] args = {};
        SearchBiclustersCDMI sc = new SearchBiclustersCDMI();

        ArrayList conv = new ArrayList(geneids);
        try {
            System.out.println("searchMAKResultsFromCDS");
            System.out.println("java.vm.version "+System.getProperty("java.vm.version"));
            System.out.println("JAVA_HOME "+System.getenv("JAVA_HOME"));
            System.out.println("KB_DEPLOYMENT_CONFIG "+System.getenv("KB_DEPLOYMENT_CONFIG"));
            System.out.println("KB_DEPLOYMENT_CONFIG p "+System.getProperty("KB_DEPLOYMENT_CONFIG"));
            System.out.println("KB_SERVICE_NAME "+System.getenv("KB_SERVICE_NAME"));
            System.out.println("KB_SERVICE_NAME p "+System.getProperty("KB_SERVICE_NAME"));
            System.out.println(MoreArray.arrayListtoString(conv, ","));
            String genemapfilename = kbgid + "_allmapunmap.txt";
            System.out.println(MAKServer.PATH_TO_GENE_IDS + "/" + genemapfilename);
            System.out.println(dataType);

            sc.doInit(MoreArray.arrayListtoString(conv, ","), null, MAKServer.PATH_TO_GENE_IDS + "/" + genemapfilename,
                    dataType, null, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MAKBiclusterSet mbs = null;
        try {
            mbs = sc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //returnVal = CmonkeyServerCaller.buildCmonkeyNetworkJobFromWs(wsId, params, authPart);
        //END search_MAK_results_from_ws
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
