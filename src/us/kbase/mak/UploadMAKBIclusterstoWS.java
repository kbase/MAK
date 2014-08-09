package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.workspace.ObjectSaveData;
import us.kbase.workspace.SaveObjectsParams;
import us.kbase.workspace.WorkspaceClient;

import java.io.Console;
import java.io.File;
import java.io.IOException;
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
     * @param args
     */
    public UploadMAKBIclusterstoWS(String[] args) {
        try {

            String user = System.getProperty("test.user");
            String pwd = System.getProperty("test.pwd");
            System.out.println(args[1]);
            WorkspaceClient wc = new WorkspaceClient(new URL(args[1]),
                    user,
                    pwd);
            wc.setAuthAllowedForHttp(true);

            ObjectMapper mapper = new ObjectMapper();
            File f = new File(args[0]);

            String[] filelist = f.list();

            String prefix = "";
            for (int i = 0; i < filelist.length; i++) {
                if (filelist[i].indexOf("MAKResult") != -1) {
                    int index = filelist[i].indexOf(".txt");
                    prefix = filelist[i].substring(0, index);
                    break;
                }
            }


            for (int i = 0; i < filelist.length; i++) {
                File objfile = new File(args[0] + "/" + filelist[i]);

                if (filelist[i].indexOf("MAKResult") != -1) {
                    try {
                        MAKResult makResult = mapper.readValue(objfile, MAKResult.class);
                        String objType = "MAK.MAKResult-5.0";
                        final String name = prefix + "__" + makResult.getId().substring(3);
                        System.out.println("saving object MAKResult " + name);
                        wc.saveObjects(new SaveObjectsParams().withWorkspace(args[2]).withObjects(Arrays.asList(new ObjectSaveData().withType(objType).withData(new UObject(makResult)).withName(makResult.getId()))));
                    } catch (IOException e) {
                        System.out.println("Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FloatDataTable fdt = mapper.readValue(objfile, FloatDataTable.class);
                        String objType = "MAK.FloatDataTable-2.1";
                        final String name = prefix + "__" + fdt.getId().substring(3);
                        System.out.println("saving object FloatDataTable " + name);
                        wc.saveObjects(new SaveObjectsParams().withWorkspace(args[2]).withObjects(Arrays.asList(new ObjectSaveData().withType(objType).withData(new UObject(fdt)).withName(fdt.getId()))));

                    } catch (Exception e) {
                        System.out.println("Exception: " + e.getMessage());
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * read in a password without screen echoing
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

    final public static void main(String args[]) {
        if (args.length == 3) {
            UploadMAKBIclusterstoWS ce = new UploadMAKBIclusterstoWS(args);
        } else {
            System.out.println("usage: java us.kbase.mak.UploadMAKBIclusterstoWS <dir of files to upload> <workspace server> <workspace name>");
        }
    }

}
