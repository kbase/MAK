package us.kbase.mak;

import DataMining.ValueBlock;
import DataMining.ValueBlockList;
import DataMining.ValueBlockListMethods;
import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.idserverapi.IDServerAPIClient;
import util.MoreArray;
import util.TabFile;
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


    String[] geneids;
    double cutoff = 0.25;


    public SearchBiclusters(String[] args) {

        String kbgid = args[0].substring(3, args[0].length());

        ArrayList<String[]> labels = MAKServerImpl.loadLabels(kbgid);

        String[] gene_labels = labels.get(0);
        String[] exp_labels = labels.get(1);

        ArrayList geneIds = new ArrayList();
        int[] expall = mathy.stat.createNaturalNumbers(1, exp_labels.length + 1);
        /*for (int i = 0; i < genes.size(); i++) {
            geneIds.add(MoreArray.getArrayInd(gene_labels, genes.get(i)) + 1);
        }
*/
        //add starting point
        String g = MoreArray.toString(MoreArray.ArrayListtoInt(geneIds), ",");
        String e = MoreArray.toString(expall, ",");
        String searchblockS = g + "/" + e;
        ValueBlock searchblock = new ValueBlock(searchblockS);

        try {
            ObjectMapper mapper = new ObjectMapper();
            File f = new File(args[0]);
            MAKResult makResult = mapper.readValue(f, MAKResult.class);

            int slashind = args[0].indexOf("/");
            int max = Math.max(0, slashind);
            int dotind = args[0].indexOf(".", max);
            String fileprefix = args[0].substring(max, dotind);

            List<MAKBicluster> biclusters = makResult.getSets().get(0).getBiclusters();
            List<MAKBicluster> biclustersMatch = new ArrayList<MAKBicluster>();
            for (int i = 0; i < biclusters.size(); i++) {

                MAKBicluster mb = biclusters.get(i);

                ValueBlock vb = new ValueBlock(MoreArray.ArrayListtoInt((ArrayList) mb.getGeneIds()), MoreArray.ArrayListtoInt((ArrayList) mb.getConditionIds()));


                /*if (match > cutoff) {
                    biclustersMatch.add(vb);
                }*/

            }


            makResult.getSets().get(0).setBiclusters(biclusters);

            TextFile.write(UObject.transformObjectToString(makResult), args[0] + "_result.jsonp");


        } catch (IOException e2) {
            e2.printStackTrace();
        }


       /* try {
            ValueBlockList query = ValueBlockListMethods.readAny(args[0], false);

            ValueBlockList db = ValueBlockListMethods.readAny(args[1], false);

            try {
                String[][] sarray = TabFile.readtoArray(args[2]);
                System.out.println("geneids g " + sarray.length + "\t" + sarray[0].length);
                int col = 2;
                String[] n = MoreArray.extractColumnStr(sarray, col);
                geneids = MoreArray.replaceAll(n, "\"", "");
                System.out.println("geneids gene " + geneids.length + "\t" + geneids[0]);
            } catch (Exception e) {
                System.out.println("expecting 2 cols");
                e.printStackTrace();
            }

            System.out.println("db " + db.size() + "\tq " + query.size());
            int matches = 0;
            for (int i = 0; i < query.size(); i++) {
                ValueBlock cur = (ValueBlock) query.get(i);
                int[] genes = cur.genes;
                System.out.println(MoreArray.toString(genes, ","));
                for (int j = 0; j < db.size(); j++) {
                    ValueBlock curdb = (ValueBlock) db.get(j);
                    int[] dbgenes = curdb.genes;
                    double count = 0;
                    ArrayList ids = new ArrayList();
                    for (int k = 0; k < genes.length; k++) {
                        int index = MoreArray.getArrayInd(dbgenes, genes[k]);
                        //System.out.println(index);
                        if (index != -1) {
                            count++;
                            ids.add(geneids[genes[k] - 1]);
                        }
                    }
                    if (count > 0) {
                        System.out.println("count " + j + "\td " + (count / (double) dbgenes.length) + "\tq " +
                                (count / (double) genes.length) + "\t" + count);
                        System.out.println(MoreArray.arrayListtoString(ids, ","));
                        matches++;
                    }
                }

            }
            System.out.println("matches " + matches);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 3 || args.length == 4) {
            SearchBiclusters rm = new SearchBiclusters(args);
        } else {
            System.out.println("syntax: java us.kbase.mak.SearchBiclusters\n" +
                    "<query file (simple format)>\n" +
                    "<gene ids>\n" +
                    "<out dir>\n" +
                    "<optional cutoff>"
            );
        }
    }
}
