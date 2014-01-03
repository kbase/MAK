package us.kbase.mak;

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

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: <program> <server_port>");
            return;
        }
        new MAKServer().startupServer(Integer.parseInt(args[0]));
    }
}
