package us.kbase.mak;


import DataMining.BlockMethods;
import DataMining.ValueBlock;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.log.MLevel;
import us.kbase.common.service.UObject;
import util.MoreArray;
import util.TabFile;
import util.TextFile;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*import us.kbase.kbasenetworks.KBaseNetworksClient;
import us.kbase.kbasenetworks.Dataset;*/

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 4/22/14
 * Time: 7:56 PM
 */
public class SearchBiclustersCDMI {

    //boolean server = false;
    double cutoff = 0.25;

    ArrayList querygenes;
    String[] gene_labels;
    String[] exp_labels;

    String outpath;

    //String url = "140.221.85.172:7064/KBaseNetworksRPC/networks";
    //String url = "http://127.0.0.1:1111";
    //String url = "http://140.221.85.172:7064";
    //String url = "http://127.0.0.1:7064/KBaseNetworksService";

    String server = "jdbc:mysql://db3.chicago.kbase.us/";
    //String server = "jdbc:mysql://localhost:49997/";

    String db = "kbase_sapling_v3";
    String user;
    String pwd;

    //KBaseNetworksClient knc;
    String data_type;
    int[] expall = {1};
    ValueBlock searchblock;

    String kbgid = "g.371";

    //static String PATH_TO_CFG = "/kb/dev_container/modules/MAK/deploy.cfg";


    /**
     */
    public SearchBiclustersCDMI() {

    }

    /**
     * @param args
     */
    public SearchBiclustersCDMI(String[] args) {

        init(args);

        start();

    }


    public MAKBiclusterSet start() {

        createQueryBicluster();

        //until condition data is incorporated
        //int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels.length + 1);
        MAKBiclusterSet biclustersMatch = null;
        ComboPooledDataSource cpds = null;
        Statement stmt = null;
        Statement stmt2 = null;
        Connection con = null;

        try {

            com.mchange.v2.log.MLog.getLogger().setLevel(MLevel.INFO);

            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setJdbcUrl(server + db);
            cpds.setUser(user);
            cpds.setPassword(pwd);
            cpds.setMaxPoolSize(10);
            cpds.setMinPoolSize(1);
            cpds.setAcquireIncrement(1);
            cpds.setDebugUnreturnedConnectionStackTraces(true);
            cpds.setBreakAfterAcquireFailure(false);
            cpds.setAcquireRetryAttempts(10);
            cpds.setAcquireIncrement(10);
            cpds.setAcquireRetryDelay(10);
            cpds.setCheckoutTimeout(2000);
            con = cpds.getConnection();

            //JDBC
            //Connection con = createConnection(server, db, user, pwd);//
            System.out.println("made connection");
            stmt = con.createStatement();
            stmt2 = con.createStatement();
            System.out.println("made statements");

            biclustersMatch = queryandCompare(stmt, stmt2);

        } catch (Exception e) {
            e.printStackTrace();

            try {
                stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                stmt2.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            try {
                cpds.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();

            }
        }

        return biclustersMatch;
    }

    private MAKBiclusterSet queryandCompare(Statement stmt, Statement stmt2) throws SQLException {
        String select = "select id,name from Association where description like 'MAK%' and description like '%" + data_type + "%'";
        ResultSet rss = stmt.executeQuery(select);
        List<MAKBicluster> biclustersMatch = new ArrayList<MAKBicluster>();
        while (rss.next()) {
            String bicid = rss.getString(1);
            String bicname = rss.getString(2);
            //System.out.println(bicid);
            String select2 = "select to_link from AssociationFeature where from_link='" + bicid + "'";
            ResultSet rsss = stmt2.executeQuery(select2);

            ArrayList curbic = new ArrayList();
            while (rsss.next()) {
                String gid = rsss.getString(1);
                //System.out.println(gid);
                curbic.add(gid);
            }

            ArrayList query_geneIds = new ArrayList();

            for (int i = 0; i < curbic.size(); i++) {
                String q = (String) curbic.get(i);
                //System.out.println(q + "\t" + gene_labels[0]);
                //look up index in reference labels
                query_geneIds.add(MoreArray.getArrayInd(gene_labels, q) + 1);
            }
            ValueBlock vb = new ValueBlock(MoreArray.ArrayListtoInt(query_geneIds), expall);

            double match = BlockMethods.computeBlockOverlapGeneSum(searchblock, vb);
            if (match > cutoff) {
                MAKBicluster mb = new MAKBicluster();
                mb.setGeneIds(curbic);
                mb.setConditionIds(MoreArray.convtoArrayList(MoreArray.toStringArray(expall)));

                System.out.println("match " + match + "\t" + bicname + "\t" + biclustersMatch.size());
                biclustersMatch.add(mb);
            }

        }

        MAKBiclusterSet mbs = new MAKBiclusterSet();
        mbs.setBiclusters(biclustersMatch);
        mbs.setId("0");
        mbs.setTaxon(kbgid);
        mbs.setBiclusterType(data_type);
        return mbs;
    }

    /**
     *
     */
    private void createQueryBicluster() {
        ArrayList query_geneIds = new ArrayList();
        int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels != null ? exp_labels.length + 1 : 2);
        for (int i = 0; i < querygenes.size(); i++) {
            String q = (String) querygenes.get(i);
            //System.out.println(q + "\t" + gene_labels[0]);
            //look up index in reference labels
            int arrayInd = MoreArray.getArrayInd(gene_labels, q);
            // System.out.println(q + "\t" + gene_labels[0]+"\t"+arrayInd);
            query_geneIds.add(arrayInd + 1);
        }

        searchblock = new ValueBlock(MoreArray.ArrayListtoInt(query_geneIds), expall);
    }


    /**
     * @param custom_db_url
     * @return
     * @throws java.sql.SQLException
     */
    public static Connection createConnection(String custom_db_url, String custom_db, String user, String pwd) throws SQLException {
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
            System.out.println("got mysql driver");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                System.out.println("got mysql driver 2");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        String url = custom_db_url + custom_db + "?user=" + user + "&password=" + pwd;
        System.out.println("createConnection SQL createStatement " + url);
        Connection con = DriverManager.getConnection(url);
        System.out.println("createConnection success");
        return con;
    }


    public void old(String[] args) {

        //query_geneIds stores integer indices based on master gene id list
        ArrayList query_geneIds = new ArrayList();
        int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels.length + 1);
        for (int i = 0; i < querygenes.size(); i++) {
            String q = (String) querygenes.get(i);
            //System.out.println(q + "\t" + gene_labels[0]);
            //look up index in reference labels
            query_geneIds.add(MoreArray.getArrayInd(gene_labels, q) + 1);
        }

        //add starting point
        String g = MoreArray.toString(MoreArray.ArrayListtoInt(query_geneIds), ",");
        System.out.println("query " + g);

        String e = MoreArray.toString(expall, ",");
        String searchblockS = g + "/" + e;
        ValueBlock searchblock = new ValueBlock(searchblockS);

        try {
            ObjectMapper mapper = new ObjectMapper();
            File f = new File(args[0]);
            MAKResult makResult = mapper.readValue(f, MAKResult.class);

            if (args.length == 4)
                cutoff = Double.parseDouble(args[3]);

            int slashind = args[0].indexOf("/");
            int max = Math.max(0, slashind);

            List<MAKBicluster> biclusters = makResult.getSets().get(0).getBiclusters();
            List<MAKBicluster> biclustersMatch = new ArrayList<MAKBicluster>();
            for (int i = 0; i < biclusters.size(); i++) {

                MAKBicluster mb = biclusters.get(i);

                ArrayList geneIds = (ArrayList) mb.getGeneIds();
                ArrayList geneIdsInt = new ArrayList();
                ArrayList conditionIds = (ArrayList) mb.getConditionIds();
                //System.out.println("geneIds1 " + (String) geneIds.get(0));
                //System.out.println("getConditionIds " + (String) conditionIds.get(0));

                for (int k = 0; k < geneIds.size(); k++) {
                    String q = (String) geneIds.get(k);
                    //System.out.println(q + "\t" + gene_labels[0]);
                    //look up index in reference labels
                    geneIdsInt.add(MoreArray.getArrayInd(gene_labels, q) + 1);
                }


                ValueBlock vb = new ValueBlock(MoreArray.ArrayListtoInt(geneIdsInt), MoreArray.ArrayListtoInt(conditionIds));

                //System.out.println("db " + MoreArray.toString(vb.genes, ","));

                double match = BlockMethods.computeBlockOverlapGeneSum(searchblock, vb);
                if (match > cutoff) {
                    System.out.println("match " + match + "\t" + i);
                    biclustersMatch.add(mb);
                }
            }

            /*for (int i = 0; i < biclustersMatch.size(); i++) {
                MAKBicluster mb = biclustersMatch.get(i);
                ArrayList geneIds1 = (ArrayList) mb.getGeneIds();
            }*/


            System.out.println("total matched " + biclustersMatch.size());

            makResult.getSets().get(0).setBiclusters(biclustersMatch);

            TextFile.write(UObject.transformObjectToString(makResult), outpath);

        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    /**
     * @param args
     */
    private void init(String[] args) {
        if (args.length == 4)
            doInit(args[0], args[1], args[2], args[3], null, null, null);
        else if (args.length == 5)
            doInit(args[0], args[1], args[2], args[3], args[4], null, null);
        else if (args.length == 5)
            doInit(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
    }

    /**
     * @param queryStr
     * @param out
     * @param gids
     * @param type
     * @param cut
     */
    public void doInit(String queryStr, String out, String gids, String type, String cut, String u, String p) {

        user = u;
        pwd = p;
        System.out.println("SearchBiclusterCDMI doInit " + user + "\t" + pwd);

        /*System.out.println("MAKServer doInit start " + PATH_TO_CFG);

        if (u != null && p != null) {
            user = u;
            pwd = p;
            System.out.println("MAKServer doInit 0 " + user + "\t" + pwd);
        } else {
            try {
                user = System.getProperty("test.user");
                pwd = System.getProperty("test.pwd");
                System.out.println("MAKServer doInit 1 " + user + "\t" + pwd);
            } catch (Exception e) {
                System.out.println(e.getCause());
                System.out.println(e.getMessage());
                String[] cfgdata = TextFile.readtoArray(PATH_TO_CFG);
                System.out.println(MoreArray.toString(cfgdata, ","));
                for (int i = 0; i < cfgdata.length; i++) {
                    if (cfgdata[i].indexOf("dbUser=") == 0) {
                        user = cfgdata[i].substring("dbUser=".length(), cfgdata[i].length());
                    }
                    if (cfgdata[i].indexOf("dbPwd=") == 0) {
                        pwd = cfgdata[i].substring("dbPwd=".length(), cfgdata[i].length());
                    }
                }
                System.out.println("MAKServer doInit 2 " + user + "\t" + pwd);
            }
        }

        System.out.println("MAKServer doInit final " + user + "\t" + pwd)*/
        ;


        System.out.println(queryStr);
        querygenes = MoreArray.convtoArrayList(queryStr.split(","));

        outpath = out;

        int gcol = 2;
        int ecol = 1;

        System.out.println("reading " + gids);

        try {
            String[][] sarray = TabFile.readtoArray(gids);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            String[] n = MoreArray.extractColumnStr(sarray, gcol);
            gene_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting " + gcol + " cols");
            e.printStackTrace();
        }

        /*try {
            String[][] sarray = TabFile.readtoArray(args[3]);
            System.out.println("setLabels e " + sarray.length + "\t" + sarray[0].length);
            exp_labels = MoreArray.replaceAll(MoreArray.extractColumnStr(sarray, ecol), "\"", "");
            System.out.println("setLabels e " + exp_labels.length + "\t" + exp_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting " + ecol + " cols");
            e.printStackTrace();
        }*/
        if (type.equals("expr") || type.equals("fit"))
            data_type = type;


        if (cut != null)
            cutoff = Double.parseDouble(cut);
    }

  /*  private ShockNode writeFileToNode(final Map<String, Object> attribs,
                     final String string, final long writes, final String last,
                     final String filename, final String format, final int id)
                     throws Exception {
             final int readlen = string.getBytes(StandardCharsets.UTF_8).length;
             final int finallen = last.getBytes(StandardCharsets.UTF_8).length;
             final long filesize = readlen * writes + finallen;

             InputStreamFromOutputStream<String> isos =
                             new InputStreamFromOutputStream<String>() {

                     @Override
                     public String produce(final OutputStream dataSink)
                                     throws Exception {
                             Writer writer = new OutputStreamWriter(dataSink,
                                             StandardCharsets.UTF_8);
                             for (int i = 0; i < writes - 1; i++) {
                                     writer.write(string);
                             }
                             writer.write(string + last);
//                              System.out.println("ID " + id + " finished writes.");
                             writer.flush();
//                              writer.close();
//                              dataSink.flush();
//                              dataSink.close();
//                              System.out.println("ID " + id + " closed the output stream.");
                             return null;
                     }
             };
             System.out.println("ID " + id + " Streaming " + filesize + "b file... ");
             ShockNode sn;
             if (attribs == null) {
                     sn = bsc1.addNode(isos, filename, format);
             } else {
                     sn = bsc1.addNode(attribs, isos, filename, format);
             }
             isos.close();
             System.out.println("\tID " + id + " Streaming done.");
             return sn;
     }*/

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 4 || args.length == 5) {
            SearchBiclustersCDMI rm = new SearchBiclustersCDMI(args);
        } else {
            System.out.println("syntax: java us.kbase.mak.SearchBiclustersCDMI\n" +
                    "<query kb gene ids>\n" +
                    "<out path>\n" +
                    "<gene id map>\n" +
                    //"<exp ids>\n" +
                    "<data type 'expr', 'fit'>\n" +
                    "<OPTIONAL cutoff>");

        }
    }
}


/*
public class BorrowerSummaryContainer {
  @JsonProperty("BorrowerSummary")
  public List<MAKBiclusterSet> makbicset;
}
*/


