package us.kbase.mak.util;

import us.kbase.common.service.JsonClientException;
import us.kbase.idmap.IdMapClient;
import us.kbase.idmap.IdPair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 2/1/14
 * Time: 3:44 PM
 */
public class KBidforTaxId {


    public final static String getKBidforTaxId(String taxId) {
        try {
            IdMapClient idc = new IdMapClient(new URL("http://127.0.0.1:7111"));
            List kbasegenomes = new ArrayList();
            try {
                kbasegenomes = idc.lookupGenome(taxId, "NCBI_TAXID");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonClientException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < kbasegenomes.size(); i++) {
                IdPair ip = (IdPair) kbasegenomes.get(i);
                //System.out.println(i + "\t" + ip.getKbaseId());

                try {
                    List syns = null;
                    try {
                        syns = idc.lookupFeatureSynonyms(ip.getKbaseId(), "CDS");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("synonyms " + syns.size());

                    if (syns.size() > 0) {
                        System.out.println(i + "\t" + kbasegenomes.get(i));
                        return ip.getKbaseId();
                    }
                } catch (JsonClientException e) {
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
