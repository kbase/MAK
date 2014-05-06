package us.kbase.mak;


import DataMining.BlockMethods;
import DataMining.ValueBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
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

    String user = "kbase_sapselect";
    //String server = "jdbc:mysql://db3.chicago.kbase.us/";
    String server = "jdbc:mysql://localhost:49997/";
    //for jdbc url encoded
    //String pass = "oiwn22%26dmwWEe";
    String pass = "oiwn22&dmwWEe";
    String db = "kbase_sapling_v3";

    //KBaseNetworksClient knc;
    String data_type;
    int[] expall = {1};
    ValueBlock searchblock;


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


    public List<MAKBicluster> start() {
        createQueryBicluster();

        //until condition data is incorporated
        //int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels.length + 1);
        List<MAKBicluster> biclustersMatch;
        try {

            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setJdbcUrl(server + db);
            cpds.setUser(user);
            cpds.setPassword(pass);
            cpds.setMaxPoolSize(50);
            cpds.setMinPoolSize(1);
            cpds.setAcquireIncrement(1);
            Connection con = cpds.getConnection();

            //JDBC
            //Connection con = createConnection(server, db, user, pass);//
            System.out.println("made connection");
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            System.out.println("made statements");

            biclustersMatch = queryandCompare(stmt, stmt2);

            stmt.close();
            stmt2.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return biclustersMatch;
    }

    private List<MAKBicluster> queryandCompare(Statement stmt, Statement stmt2) throws SQLException {
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
                mb.setConditionIds(MoreArray.convtoArrayList(expall));

                System.out.println("match " + match + "\t" + bicname + "\t" + biclustersMatch.size());
                biclustersMatch.add(mb);
            }

        }

        return biclustersMatch;
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

    private void init(String[] args) {

        if (args.length == 4)
            doInit(args[0], args[1], args[2], args[3], null);
        else if (args.length == 4)
            doInit(args[0], args[1], args[2], args[3], args[4]);
    }

    public void doInit(String queryStr, String out, String gids, String type, String cut) {

        System.out.println(queryStr);
        querygenes = MoreArray.convtoArrayList(queryStr.split(","));


        outpath = out;

        int gcol = 2;
        int ecol = 1;

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


        if (cut == 5)
            cutoff = Double.parseDouble(cut);
    }

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
