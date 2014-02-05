package us.kbase.mak;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/15/14
 * Time: 2:46 PM
 */
public class MAKServerThread {

    	String wsId;
    	MAKParameters params;
    	String jobId;
    	String token;

    	public MAKServerThread (String wsId, MAKParameters params, String jobId, String token ){
    		this.wsId = wsId;
    		this.params = params;
    		this.jobId = jobId;
    		this.token = token;
    	}

    	public void run (){
    		try {
    			//MAKServerImpl.MAKJobFromWs(wsId, params, jobId, token, null);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}

}
