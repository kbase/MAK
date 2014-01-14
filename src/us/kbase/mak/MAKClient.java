package us.kbase.mak;

import com.fasterxml.jackson.core.type.TypeReference;
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
 * Module MAKBiclusterCollection version 1.0
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

	public void setConnectionReadTimeOut(Integer milliseconds) {
		this.caller.setConnectionReadTimeOut(milliseconds);
	}

    public boolean isAuthAllowedForHttp() {
        return caller.isAuthAllowedForHttp();
    }

    public void setAuthAllowedForHttp(boolean isAuthAllowedForHttp) {
        caller.setAuthAllowedForHttp(isAuthAllowedForHttp);
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
     * @param   params   instance of type {@link us.kbase.mak.MAKParameters MAKParameters}
     * @return   parameter "MAK_job_id" of String
     * @throws IOException if an IO exception occurs
     * @throws JsonClientException if a JSON RPC exception occurs
     */
    public String runMAKJobFromWs(String wsId, MAKParameters params) throws IOException, JsonClientException {
        List<Object> args = new ArrayList<Object>();
        args.add(wsId);
        args.add(params);
        TypeReference<List<String>> retType = new TypeReference<List<String>>() {};
        List<String> res = caller.jsonrpcCall("MAK.run_MAK_job_from_ws", args, retType, true, true);
        return res.get(0);
    }
}
