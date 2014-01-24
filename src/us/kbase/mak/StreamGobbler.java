package us.kbase.mak;

import us.kbase.auth.AuthException;
import us.kbase.common.service.JsonClientException;

import java.io.*;
import java.util.regex.Pattern;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/22/14
 * Time: 3:03 PM
 */
public class StreamGobbler extends Thread {

        InputStream is;
        String type;
        String jobId = null;
        String logfileName = null;
        private static Pattern iterationPattern = Pattern.compile("INFO     Iteration # (\\d{1,2})(0{2,3})$");
        private static Pattern iterationNumberPattern = Pattern.compile("INFO     Iteration # ");
        private String token;

        StreamGobbler(InputStream is, String type, String logfileName)
        {
            this.is = is;
            this.type = type;
            this.logfileName = logfileName;
        }

        StreamGobbler(InputStream is, String type, String jobId, String token, String logfileName)
        {
            this.is = is;
            this.type = type;
            this.jobId = jobId;
            this.token = token;
            this.logfileName = logfileName;
        }

        public void run()
        {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            BufferedWriter writer = null;
           	try {
                if (logfileName != null) {
    					writer = new BufferedWriter (new FileWriter(logfileName));
                };
                String line=null;
    			while ( (line = br.readLine()) != null) {
    				//System.out.println(type + ">" + line);
    				if (logfileName != null) {
    					writer.write(line);
    					writer.newLine();
    				}
    				//Catch "INFO     Iteration # " pattern
    				if (iterationPattern.matcher(line).find()){
    					//call CmonkeyServerImpl
    					String[] lineParts = iterationNumberPattern.split(line);
    					System.out.println("ITERATION: " + lineParts[lineParts.length - 1]);
    			   		if (jobId != null) {
    			   			String status = "cMonkey iteration " + lineParts[lineParts.length - 1];
    			   			System.out.println(status);
    						MAKServerImpl.updateJobProgress(jobId, status, token);
    					}
    				}
    			};
           		if (logfileName != null) {
            		writer.write("STREAMGOBBLER RUN FINISHED");
            		writer.close();
           		};
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (JsonClientException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (AuthException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} finally {
    			if (writer != null) {
    				try {
    					writer.close();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    		}
        }
}
