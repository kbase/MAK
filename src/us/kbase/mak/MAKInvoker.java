package us.kbase.mak;

import util.MapArgOptions;
import util.MoreArray;
import util.ParsePath;

import java.util.HashMap;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/15/14
 * Time: 2:15 PM
 */
public class MAKInvoker {

    String[] valid_args = {
            "-method", "-wsid", "-kbgid", "-geneids", "-jobid", "-token"
    };
    HashMap options;


    @SuppressWarnings("static-access")
    public MAKInvoker(String[] args) {
        init(args);
        //run();

    }

    private void runMAKAll() throws Exception {

        String currentDir = System.getProperty("user.dir");
        System.out.println("Run MAK from " + currentDir);

        String wsId = (String) options.get("-wsid");
        System.out.println(wsId);

        String kbgid = (String) options.get("-kbgid");
        System.out.println(kbgid);

        String token = (String) options.get("-token");
        System.out.println(token);

        String jobid = (String) options.get("-jobid");
        System.out.println(jobid);

        MAKServerImpl.allMAKJobFromWs(wsId, kbgid, jobid, token, currentDir);

    }

    private void runMAKSingle() throws Exception {

        String currentDir = System.getProperty("user.dir");
        System.out.println("Run MAK from " + currentDir);

        String wsId = (String) options.get("-wsid");
        System.out.println(wsId);

        String kbgid = (String) options.get("-kbgid");
        System.out.println(kbgid);

        String token = (String) options.get("-token");
        System.out.println(token);

        String jobid = (String) options.get("-jobid");
        System.out.println(jobid);

        String[] genes = ((String) options.get("-jobid")).split(",");
        System.out.println(jobid);

        MAKServerImpl.singleMAKJobFromWs(wsId, kbgid, MoreArray.convtoArrayList(genes), jobid, token, currentDir);
    }

    private void searchMAK() throws Exception {

            String currentDir = System.getProperty("user.dir");
            System.out.println("Run MAK from " + currentDir);

            String wsId = (String) options.get("-wsid");
            System.out.println(wsId);

            String kbgid = (String) options.get("-kbgid");
            System.out.println(kbgid);

            String token = (String) options.get("-token");
            System.out.println(token);

            String jobid = (String) options.get("-jobid");
            System.out.println(jobid);

            MAKServerImpl.searchMAKJobFromWs(wsId, kbgid, jobid, token, currentDir);
        }

    public void run(String[] args) throws Exception {

        String serverMethod = "";

        try {
            // parse the command line arguments

            if (serverMethod.equalsIgnoreCase("runall_MAK_job_from_ws")) {
                runMAKAll();
            } else if (serverMethod.equalsIgnoreCase("runsingle_MAK_job_from_ws")) {
                runMAKSingle();
            } else if (serverMethod.equalsIgnoreCase("search_MAK_results_from_ws")) {
                searchMAK();
            } else {
                System.err.println("Unknown method: " + serverMethod + "\n");
                System.exit(1);
            }

        } catch (Exception exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }


    private static boolean validateInput(CommandLine line) {
        boolean returnVal = false;
        if (line.hasOption("method")) {

            if (line.hasOption("wsid")) {

                if (line.hasOption("token")) {

                    //if (line.hasOption("series")) {

                    if (line.hasOption("job")) {

                        if (line.hasOption("taxon")) {

                            returnVal = true;
                        } else {
                            System.err.println("taxon required");
                        }
                    } else {
                        System.err.println("Job ID required");
                    }

                    //} else {
                    //    System.err.println("Expression data series ID required");
                    //}

                } else {
                    System.err.println("Authorization required");
                }

            } else {
                System.err.println("Workspace ID required");
            }

        } else {
            System.err.println("Method required");
        }
        return returnVal;
    }

    /**
     * @param args
     */
    private void init(String[] args) {
        MoreArray.printArray(args);

        ParsePath pp = null;

        options = MapArgOptions.maptoMap(args, valid_args);

    }


    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        MAKInvoker invoker = new MAKInvoker(args);
        invoker.run(args);
    }


}
