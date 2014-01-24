package us.kbase.mak;

import us.kbase.common.service.JsonClientException;
import us.kbase.idmap.IdMapClient;
import us.kbase.idmap.IdPair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/22/14
 * Time: 3:26 PM
 */
public class TestIdMap {

    //SOMR1 dnaA
    String SOMR1testid = "199205";
    String testgenomeId = "882";//"211586";           //

    final static String sourcedb = "microbes_online";

    /**
     * @param args
     */
    public TestIdMap(String[] args) {

        try {
            IdMapClient idc = new IdMapClient(new URL("http://127.0.0.1:7111"));
            List kbasegenomes = new ArrayList();
            try {
                kbasegenomes = idc.lookupGenome(testgenomeId, "NCBI_TAXID");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonClientException e) {
                e.printStackTrace();
            }

            /*for (int i = 0; i < kbasegenomes.size(); i++) {
                IdPair ip = (IdPair) kbasegenomes.get(i);
                System.out.println(i + "\t" + ip.getKbaseId());
            }*/

            for (int i = 0; i < kbasegenomes.size(); i++) {
                IdPair ip = (IdPair) kbasegenomes.get(i);
                System.out.println("genome " + i + "\t" + ip.getKbaseId());
                try {
                    List syns = idc.lookupFeatureSynonyms(ip.getKbaseId(), "CDS");
                    System.out.println("synonyms " + syns.size() + "\t" + (syns.size() > 0 ? ((IdPair) syns.get(0)).getKbaseId() : ""));
                    if (syns.size() > 0) {
                        int count = 0;
                        ArrayList sources = new ArrayList();
                        HashMap<String, Integer> sourcecount = new HashMap<String, Integer>();
                        for (int j = 0; j < syns.size(); j++) {
                            IdPair ip2 = (IdPair) syns.get(j);
                            String sourceDB = ip2.getSourceDb();
                            if (sources.indexOf(sourceDB) == -1) {
                                sources.add(sourceDB);
                            }

                            Object o = sourcecount.get(sourceDB);
                            if (o == null) {
                                sourcecount.put(sourceDB, 1);
                            } else {
                                int cur = (Integer) o;
                                sourcecount.put(sourceDB, cur + 1);
                            }

                            if (sourceDB.equals(sourcedb)) {
                                System.out.println("synonym count "+j + "\t" + ip2.getAlias() + "\t" + ip2.getKbaseId() + "\t" + sourceDB);
                                count++;
                            }
                        }
                        System.out.println("count MO " + count);
                        Set keys = sourcecount.keySet();
                        Iterator kit = keys.iterator();
                        while(kit.hasNext()) {

                        }
                    }


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


    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            TestIdMap ce = new TestIdMap(args);
        } else {
            System.out.println("usage: java us.kbase.mak.TestIdMap");
        }
    }


}
