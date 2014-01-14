package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.ppi.InteractionDataset;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;

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

    final public static void main(String argv[]) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File f = new File(argv[0]);
            MAKBiclusterSet ds = mapper.readValue(f, MAKBiclusterSet.class);
            WorkspaceClient wc = new WorkspaceClient(new URL(argv[1]),
                    argv[2],
                    argv[3]);
            String wsName = argv[4];
            String objType = "MAK.BiclusterSet-6                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               .0";
            wc.setAuthAllowedForHttp(true);
            wc.saveObjects(new SaveObjectsParams().withWorkspace(wsName).withObjects(Arrays.asList(new ObjectSaveData().withType(objType).withData(new UObject(ds)).withName(argv[5]))));
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
