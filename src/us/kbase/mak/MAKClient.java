package us.kbase.mak;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import us.kbase.auth.AuthToken;
import us.kbase.common.service.JsonClientCaller;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UnauthorizedException;

/**
 * <p>Original spec-file module name: MAK</p>
 * <pre>
 * Module MAK version 1.0
 * This module provides access to MAK biclustering results.
 * @author marcin
 * </pre>
 */
public class MAKClient {
    private JsonClientCaller caller;

    public MAKClient(URL url) {
        caller = new JsonClientCaller(url);
    }

    public MAKClient(URL url, AuthToken token) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, token);
    }

    public MAKClient(URL url, String user, String password) throws UnauthorizedException, IOException {
        caller = new JsonClientCaller(url, user, password);
    }

    public URL getURL() {
        return caller.getURL();
    }

    public void setConnectionReadTimeOut(Integer milliseconds) {
        this.caller.setConnectionReadTimeOut(milliseconds);
    }

    public boolean isAuthAllowedForHttp() {
        return caller.isAuthAllowedForHttp();
    }

    public void setAuthAllowedForHttp(boolean isAuthAllowedForHttp) {
        caller.setAuthAllowedForHttp(isAuthAllowedForHttp);
    }

    public AuthToken getToken() {
        return caller.getToken();
    }

    public void _setFileForNextRpcResponse(File f) {
        caller.setFileForNextRpcResponse(f);
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
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public String runallMAKJobFromWs(String wsId, String kbgid, String dataType) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(wsId);
        args.add(kbgid);
        args.add(dataType);
        TypeReference<List<String>> retType = new TypeReference<List<String>>() {};
        List<String> res = caller.jsonrpcCall("MAK.runall_MAK_job_from_ws", args, retType, true, true);
        return res.get(0);
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
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public String runsingleMAKJobFromWs(String wsId, String kbgid, String dataType, List<String> geneids) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(wsId);
        args.add(kbgid);
        args.add(dataType);
        args.add(geneids);
        TypeReference<List<String>> retType = new TypeReference<List<String>>() {};
        List<String> res = caller.jsonrpcCall("MAK.runsingle_MAK_job_from_ws", args, retType, true, true);
        return res.get(0);
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
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public MAKBiclusterSet searchMAKResultsFromWs(String wsId, String kbgid, String dataType, List<String> geneids) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(wsId);
        args.add(kbgid);
        args.add(dataType);
        args.add(geneids);
        TypeReference<List<MAKBiclusterSet>> retType = new TypeReference<List<MAKBiclusterSet>>() {};
        List<MAKBiclusterSet> res = caller.jsonrpcCall("MAK.search_MAK_results_from_ws", args, retType, true, true);
        return res.get(0);
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
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public MAKBiclusterSet searchMAKResultsFromCds(String kbgid, String dataType, List<String> geneids) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(kbgid);
        args.add(dataType);
        args.add(geneids);
        TypeReference<List<MAKBiclusterSet>> retType = new TypeReference<List<MAKBiclusterSet>>() {};
        List<MAKBiclusterSet> res = caller.jsonrpcCall("MAK.search_MAK_results_from_cds", args, retType, true, true);
        return res.get(0);
    }
}
