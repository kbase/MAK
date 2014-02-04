package us.kbase.mak;

import java.util.List;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonServerMethod;
import us.kbase.common.service.JsonServerServlet;

//BEGIN_HEADER
//END_HEADER

/**
 * <p>Original spec-file module name: MAK</p>
 * <pre>
 * Module MAKBiclusterCollection version 1.0
 * This module provides access to MAK biclustering results.
 * @author marcin
 * </pre>
 */
public class MAKServer extends JsonServerServlet {
    private static final long serialVersionUID = 1L;

    //BEGIN_CLASS_HEADER
    //END_CLASS_HEADER

    public MAKServer() throws Exception {
        super("MAK");
        //BEGIN_CONSTRUCTOR
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
     * @param   data_type   instance of String
     * @return   parameter "MAK_job_id" of String
     */
    @JsonServerMethod(rpc = "MAK.runall_MAK_job_from_ws")
    public String runallMAKJobFromWs(String wsId, String kbgid, String data_type, AuthToken authPart) throws Exception {
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
     * @param   data_type   instance of String
     * @param   geneids   instance of list of String
     * @return   parameter "MAK_job_id" of String
     */
    @JsonServerMethod(rpc = "MAK.runsingle_MAK_job_from_ws")
    public String runsingleMAKJobFromWs(String wsId, String kbgid, String data_type, List<String> geneids, AuthToken authPart) throws Exception {
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
     * @param   data_type   instance of String
     * @param   geneids   instance of list of String
     * @return   parameter "mbs" of type {@link us.kbase.mak.MAKBiclusterSet MAKBiclusterSet}
     */
    @JsonServerMethod(rpc = "MAK.search_MAK_results_from_ws")
    public MAKBiclusterSet searchMAKResultsFromWs(String wsId, String kbgid, String data_type, List<String> geneids, AuthToken authPart) throws Exception {
        MAKBiclusterSet returnVal = null;
        //BEGIN search_MAK_results_from_ws
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
