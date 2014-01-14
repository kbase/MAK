package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;

import java.io.Console;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/7/14
 * Time: 12:22 PM
 */
public class UploadMAKBIclusterstoWS {

    /**
          read in a password without screen echoing
       */
       final public static String readPassword() {
           Console cons;
           char[] rv;
           if ((cons = System.console()) != null &&
               (rv = cons.readPassword("%s", "Password:")) != null) {
               return new String(rv);
           }
           return null;
       }

       final public static void main(String argv[]) {
           try {
               ObjectMapper mapper = new ObjectMapper();
               File f = new File(argv[0]);
               MAKResult ds = mapper.readValue(f, MAKResult.class);
               WorkspaceClient wc = new WorkspaceClient(new URL(argv[1]),
                                                        argv[2],
                                                        readPassword());
               String objType = "MAK.MAKResult-1";
               wc.setAuthAllowedForHttp(true);
               wc.saveObjects(new SaveObjectsParams().withWorkspace(argv[3]).withObjects(Arrays.asList(new ObjectSaveData().withType(objType).withData(new UObject(ds)).withName(argv[4]))));
           }
           catch (Exception e) {
               System.out.println("Exception: "+e.getMessage());
               e.printStackTrace();
           }
       }

}
