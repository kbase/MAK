package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UObject;
import us.kbase.idmap.IdMapClient;
import us.kbase.idmap.IdPair;
import util.MoreArray;
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
    String testgenomeId = "211586";

    final static String sourcedb = "microbes_online";
    String[] gene_labels;

    HashMap<String, String> idmapping = new HashMap<String, String>();

    /**
     * @param args
     */
    public MapIds(String[] args) {

        init(args);

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
                    System.out.println("mapped " + cur + "\t" + moid + "\t" + e);
                    if (e != null)
                        kbgids.add(e);
                }

                if (kbgids.size() != gids.size()) {
                    System.out.println("failed to map " + (gids.size() - kbgids.size()));
                }
                System.out.println("map " + kbgids.size());
                makb.setGeneIds(kbgids);
                mbs.set(a, makb);
            }
            makResult.getSets().get(0).setBiclusters(mbs);

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
                    System.out.println("synonyms " + syns.size());

                    if (syns.size() > 0) {
                        //String[] kbaseids = new String[gene_labels.length];
                        for (int j = 0; j < gene_labels.length; j++) {
                            System.out.println(j + "\t" + gene_labels[j]);
                            List<String> a = new ArrayList();
                            a.add(gene_labels[j]);
                            Map map = idc.lookupFeatures(ip.getKbaseId(), Arrays.asList(gene_labels[j]), "CDS", "microbes_online");
                            Set keys = map.keySet();
                            Set entries = map.entrySet();
                            Iterator kit = keys.iterator();
                            Iterator eit = entries.iterator();

                            System.out.println("entries " + entries.size());
                            //for (int k = 0; k < keys.size(); k++) {
                            if (kit.hasNext()) {
                                while (kit.hasNext()) {
                                    ArrayList me = (ArrayList) map.get(kit.next());
                                    for (int z = 0; z < me.size(); z++) {
                                        IdPair cur = (IdPair) me.get(z);
                                        System.out.println(gene_labels[j] + "\t" + cur.getAlias() + "\t" + cur.getKbaseId() + "\t" + cur.getSourceDb());
                                        idmapping.put(cur.getAlias(), cur.getKbaseId());
                                    }
                                }
                            } else {
                                unmapped.add(gene_labels[j]);
                            }

                        }
                        //testSynonyms(syns);

                        System.out.println("unmapped " + unmapped.size());
                        String outpath = this.testgenomeId + "_unmapped.txt";
                        TextFile.write(unmapped, outpath);
                        System.out.println("wrote " + outpath);
                    }

                    ArrayList outmap = new ArrayList();
                    Set<String> setm = idmapping.keySet();
                    Iterator<String> mapit = setm.iterator();
                    while (mapit.hasNext()) {
                        String key = mapit.next();
                        outmap.add(key + "\t" + idmapping.get(key));
                    }
                    String outpath = this.testgenomeId + "_map.txt";
                    TextFile.write(outmap, outpath);
                    System.out.println("wrote " + outpath);

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

        testgenomeId = args[1];
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
            System.out.println("usage: java us.kbase.mak.MapIds <MAK result jsonp> <taxonomy id> <id file> <OPTIONAL precomputed map file>");
        }
    }

}
