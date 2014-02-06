package us.kbase.mak;

import DataMining.BlockMethods;
import DataMining.ValueBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.idserverapi.IDServerAPIClient;
import util.MoreArray;
import util.ParsePath;
import util.TextFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 2/4/14
 * Time: 12:10 AM
 */
public class SearchBiclusters {

    double cutoff = 0.25;


    /**
     * @param args
     */
    public SearchBiclusters(String[] args) {

        ParsePath pp = new ParsePath(args[0]);
        int endIndex = pp.getName().indexOf("_");
        String kbgid = pp.getName().substring(0, endIndex);
        int endIndex2 = pp.getName().indexOf("_", endIndex + 1);
        String data_type = pp.getName().substring(endIndex + 1, endIndex2);

        /*TODO enable for server deploy */
        //String gfile = MAKServerConfig.CACHE_PATH +"/"+kbgid+"_"+data_type+"_geneids.txt";
        //String efile = MAKServerConfig.CACHE_PATH +"/"+kbgid+"_"+data_type+"_expids.txt";

        String gfile = "./" + kbgid + "_" + data_type + "_geneids.txt";
        String efile = "./" + kbgid + "_" + data_type + "_expids.txt";

        ArrayList<String[]> labels = MAKServerImpl.loadLabels(kbgid, gfile, efile);

        String[] gene_labels = labels.get(0);
        String[] exp_labels = labels.get(1);

        System.out.println(args[1]);
        ArrayList genes = MoreArray.convtoArrayList(args[1].split(","));

        ArrayList geneIds = new ArrayList();
        int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels.length + 1);
        for (int i = 0; i < genes.size(); i++) {
            String q = (String) genes.get(i);
            System.out.println(q + "\t" + gene_labels[0]);
            geneIds.add(MoreArray.getArrayInd(gene_labels, q) + 1);
        }

        //add starting point
        String g = MoreArray.toString(MoreArray.ArrayListtoInt(geneIds), ",");
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

                ValueBlock vb = new ValueBlock(MoreArray.ArrayListtoInt((ArrayList) mb.getGeneIds()), MoreArray.ArrayListtoInt((ArrayList) mb.getConditionIds()));

                System.out.println("db " + MoreArray.toString(vb.genes, ","));

                double match = BlockMethods.computeBlockOverlapGeneSum(searchblock, vb);
                if (match > cutoff) {
                    System.out.println("match " + match + "\t" + i);
                    biclustersMatch.add(mb);
                }
            }

            makResult.getSets().get(0).setBiclusters(biclusters);

            TextFile.write(UObject.transformObjectToString(makResult), args[2]);


        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 3 || args.length == 4) {
            SearchBiclusters rm = new SearchBiclusters(args);
        } else {
            System.out.println("syntax: java us.kbase.mak.SearchBiclusters\n" +
                    "<db file path (jsonp)>\n" +
                    "<query kb gene ids>\n" +
                    //"<ref gene ids>\n" +
                    //"<ref exp ids>\n" +
                    "<out path>\n" +
                    "<OPTIONAL cutoff>"
            );
        }
    }
}
