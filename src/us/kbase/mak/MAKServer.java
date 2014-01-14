package us.kbase.mak;

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
     * <p>Original spec-file function name: run_MAK_job_from_ws</p>
     * <pre>
     * Starts MAK server job for a series of expression data stored in workspace and returns job ID of the run
     * string ws_id - workspace id
     * string series_id - kbase id of expression data series for MAK job
     * MAKParameters params - parameters of MAK job
     * string job_id - identifier of MAK job
     * </pre>
     * @param   wsId   instance of String
     * @param   seriesId   instance of String
     * @param   params   instance of type {@link us.kbase.mak.MAKParameters MAKParameters}
     * @return   parameter "MAK_job_id" of String
     */
    @JsonServerMethod(rpc = "MAK.run_MAK_job_from_ws")
    public String runMAKJobFromWs(String wsId, String seriesId, MAKParameters params, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN run_MAK_job_from_ws
        //END run_MAK_job_from_ws
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
