package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UObject;
import us.kbase.idmap.IdMapClient;
import us.kbase.idmap.IdPair;
import util.MoreArray;
import util.StringUtil;
import util.TabFile;
import util.TextFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/22/14
 * Time: 10:33 PM
 */
public class MapIds {

    //SOMR1 dnaA
    String testid = "199205";
    String testgenome = "Shewanella oneidensis MR-1";
    String taxId = "211586";
    String kbGenomeId;

    final static String sourcedb = "microbes_online";
    String[] gene_labels;

    HashMap<String, String> idmapping = new HashMap<String, String>();

    /**
     * @param args
     */
    public MapIds(String[] args) {

        init(args);

        String prefix = args[0].substring(0, args[0].indexOf("_MAKResult"));

        if (idmapping.size() == 0)
            idMap(args);

        try {
            ObjectMapper mapper = new ObjectMapper();
            File f = new File(args[0]);
            MAKResult makResult = mapper.readValue(f, MAKResult.class);


            List<MAKBicluster> mbs = makResult.getSets().get(0).getBiclusters();

            for (int a = 0; a < mbs.size(); a++) {
                MAKBicluster makb = mbs.get(a);
                List<String> gids = makb.getGeneIds();
                List<String> kbgids = new ArrayList<String>();
                for (int z = 0; z < gids.size(); z++) {
                    String cur = gids.get(z);
                    String moid = gene_labels[Integer.parseInt(cur) - 1];
                    String e = idmapping.get(moid);
                    //System.out.println("mapped " + cur + "\t" + moid + "\t" + e);
                    if (e != null)
                        kbgids.add(e);
                }

                if (kbgids.size() != gids.size()) {
                    System.out.println("failed to map " + (gids.size() - kbgids.size()));
                }
                System.out.println("map " + kbgids.size());
                makb.setGeneIds(kbgids);
                mbs.set(a, makb);


                //SOMR1_expr_refine_top_0.25_1.0_c_reconstructed.txt

                //SOMR1_expr_refine_top_0.25_1.0_c_reconstructed.txt_bicluster.0_data.jsonp


                //tried
                //SOMR1_expr_refine_top_0.25_1.0_c_reconstructed.txt_MAKResult.jsonp_bicluster.0_data.jsonp

                ObjectMapper mapper2 = new ObjectMapper();
                String inpath = prefix + "_bicluster." + a + "_data.jsonp";
                File f2 = new File(inpath);
                FloatDataTable biclustertable = mapper2.readValue(f2, FloatDataTable.class);

                biclustertable.setRowIds(kbgids);

                TextFile.write(UObject.transformObjectToString(biclustertable), inpath + "_map.jsonp");
            }
            makResult.getSets().get(0).setBiclusters(mbs);
            MAKParameters makprm = makResult.getParameters();
            makprm.setGenomeId(this.kbGenomeId);
            makprm.setTaxon(this.taxId);
            makResult.setParameters(makprm);

            MAKInputData makid = makResult.get

            TextFile.write(UObject.transformObjectToString(makResult), args[0] + "_kbmap.jsonp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param args
     */
    private void idMap(String[] args) {
        try {
            IdMapClient idc = new IdMapClient(new URL("http://127.0.0.1:7111"));
            List kbasegenomes = new ArrayList();
            try {
                kbasegenomes = idc.lookupGenome(args[1], "NCBI_TAXID");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonClientException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < kbasegenomes.size(); i++) {
                System.out.println(i + "\t" + kbasegenomes.get(i));
            }

            for (int i = 0; i < kbasegenomes.size(); i++) {
                IdPair ip = (IdPair) kbasegenomes.get(i);

                System.out.println(i + "\t" + ip.getKbaseId());

                ArrayList unmapped = new ArrayList();

                try {
                    List syns = idc.lookupFeatureSynonyms(ip.getKbaseId(), "CDS");
                    System.out.println("synonyms " + ip.getKbaseId() + "\t" + syns.size());

                    if (syns.size() > 0) {
                        this.kbGenomeId = ip.getKbaseId();
                        //String[] kbaseids = new String[gene_labels.length];

                        Map map = idc.lookupFeatures(ip.getKbaseId(), Arrays.asList(gene_labels), "CDS", "microbes_online");
                        Set keys = map.keySet();
                        Set entries = map.entrySet();
                        Iterator kit = keys.iterator();
                        Iterator eit = entries.iterator();

                        int[] done = new int[gene_labels.length];
                        System.out.println("entries " + entries.size());
                        // for (int j = 0; j < gene_labels.length; j++) {
                        // System.out.println(j + "\t" + gene_labels[j]);
                        //for (int k = 0; k < keys.size(); k++) {
                        if (kit.hasNext()) {
                            while (kit.hasNext()) {
                                String query = (String) kit.next();
                                int index = StringUtil.getFirstEqualsIndex(gene_labels, query);
                                done[index] = 1;
                                ArrayList me = (ArrayList) map.get(query);
                                for (int z = 0; z < me.size(); z++) {
                                    IdPair cur = (IdPair) me.get(z);
                                    System.out.println("idmapping " + query + "\t" + cur.getAlias() + "\t" + cur.getKbaseId() + "\t" + cur.getSourceDb());
                                    idmapping.put(cur.getAlias(), cur.getKbaseId());
                                }
                            }
                        }


                        for (int j = 0; j < done.length; j++) {
                            if (done[j] == 0)
                                unmapped.add(gene_labels[j]);
                        }

                        //}
                        //testSynonyms(syns);

                        System.out.println("unmapped " + unmapped.size());
                        String outpath = this.taxId + "_unmapped.txt";
                        TextFile.write(unmapped, outpath);
                        System.out.println("wrote " + outpath);
                    }

                    ArrayList outmap = new ArrayList();
                    Set<String> setm = idmapping.keySet();

                    System.out.println("outputting map " + setm.size());
                    Iterator<String> mapit = setm.iterator();
                    while (mapit.hasNext()) {
                        String key = mapit.next();
                        outmap.add(key + "\t" + idmapping.get(key));
                    }
                    String outpath2 = this.taxId + "_map.txt";
                    TextFile.write(outmap, outpath2);
                    System.out.println("wrote " + outpath2);

                } catch (JsonClientException e) {
                    e.printStackTrace();
                }
            }

            //get list of kbase genome ids
            //client.lookupFeatureSynonyms(kbasegenomeid,"CDS");

            //Microbes_Online
            //idc.lookupFeatures( ,"CDS", "microbes_online");

        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (JsonClientException e) {
                    e.printStackTrace();
                }*/
    }

    private void testSynonyms(List syns) {
        int count = 0;
        for (int j = 0; j < syns.size(); j++) {
            IdPair ip2 = (IdPair) syns.get(j);
            if (ip2.getSourceDb().equals(sourcedb)) {
                System.out.println(j + "\t" + ip2.getAlias() + "\t" + ip2.getKbaseId() + "\t" + ip2.getSourceDb());
                count++;
            }
        }
        System.out.println("count " + count);
    }

    private void init(String[] args) {

        taxId = args[1];
        try {
            String[][] sarray = TabFile.readtoArray(args[2]);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            int col = 1;
            String[] n = MoreArray.extractColumnStr(sarray, col);
            gene_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting 1 cols");
            e.printStackTrace();
        }

        if (args.length == 4) {
            String[][] txt = TabFile.readtoArray(args[3]);

            for (int i = 0; i < txt.length; i++) {
                System.out.println("load map " + txt[i][0] + "\t" + txt[i][1]);
                idmapping.put(txt[i][0], txt[i][1]);
            }
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 3 || args.length == 4) {
            MapIds ce = new MapIds(args);
        } else {
            System.out.println("usage: java us.kbase.mak.MapIds <MAK result jsonp> " +
                    "<taxonomy id> " +
                    "<id file> " +
                    "<OPTIONAL precomputed map file>");
        }
    }

}
