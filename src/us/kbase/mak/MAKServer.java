package us.kbase.mak;

import DataMining.RunMiner;
import DataMining.ValueBlock;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonServerMethod;
import us.kbase.common.service.JsonServerServlet;
import util.MoreArray;

import java.util.ArrayList;

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

    static final String default_paramfile = "parameters.txt";

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
     * MAKParameters params - parameters of MAK job
     * string job_id - identifier of MAK job
     * </pre>
     * @param   wsId   instance of String
     * @param   makb   instance of MAKBicluster
     * @param   params   instance of type {@link us.kbase.mak.MAKParameters MAKParameters}
     * @return   parameter "MAK_job_id" of String
     */
    @JsonServerMethod(rpc = "MAK.run_MAK_job_from_ws")
    public String runMAKJobFromWs(String wsId, MAKBicluster makb, MAKParameters params, AuthToken authPart) throws Exception {
        String returnVal = null;
        //BEGIN run_MAK_job_from_ws

        /*TODO allow use of string ids from geneids file in ValueBlock*/

        DataMining.ValueBlock vb = new ValueBlock(MoreArray.ArrayListtoString((ArrayList) makb.getGeneIds()), MoreArray.ArrayListtoString((ArrayList)makb.getConditionIds()));

        /*TODO choose parameter file based on tax id*/

        //DataMining.RunMiner rm = new DataMining.RunMiner(vb, default_paramfile);

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
