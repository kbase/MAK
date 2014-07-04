package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.idserverapi.IDServerAPIClient;
import util.StringUtil;
import util.TextFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/8/14
 * Time: 4:05 PM
 */
public class RegisterBiclusterIds {

    String biclusterType = "bicluster";//"makbicluster";
    String setType = "biclusterset";//"makset";
    String resultType = "makresult";
    String MAKresource = "MAK";

    /**
     * @param args
     */
    public RegisterBiclusterIds(String args[]) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            File f = new File(args[0]);
            MAKResult makResult = mapper.readValue(f, MAKResult.class);

            String prefixInput = args[0].substring(0, args[0].indexOf("_MAKResult"));

            int slashind = args[0].indexOf("/");
            int max = Math.max(0, slashind);
            int dotind = args[0].indexOf(".", max);
            String fileprefix = args[0].substring(max, dotind);
            try {
                IDServerAPIClient idClient = new IDServerAPIClient(new URL("http://kbase.us/services/idserver"));


                String prefix = "kb|" + biclusterType;
                MAKBiclusterSet makBiclusterSet = makResult.getSets().get(0);
                List<MAKBicluster> biclusters = makBiclusterSet.getBiclusters();
                Long aLong = idClient.allocateIdRange(biclusterType, (long) biclusters.size());
                String biclusterid = prefix + "." + aLong.toString();
                System.out.println("biclusterid " + biclusterid);

                Map<String, Long> unmappedIds = new HashMap<String, Long>();
                Map<String, String> mappedIds = new HashMap<String, String>();
                for (int i = 0; i < biclusters.size(); i++) {
                    String key = fileprefix + "." + i;
                    if (i == 0)
                        System.out.println("bicluster external id " + key);

                    Map<String, String> isMapped = idClient.externalIdsToKbaseIds(MAKresource, Arrays.asList(key));

                    //System.exit(0);

                    if (isMapped.size() == 0) {
                        unmappedIds.put(key, aLong + i);
                    } else {
                        System.out.println(isMapped);
                        mappedIds.put(key, isMapped.get(key));
                    }
                }

                if (unmappedIds.size() > 0) {
                    System.out.println("registering biclusters " + unmappedIds.size());
                    idClient.registerAllocatedIds(prefix, MAKresource, unmappedIds);
                }

                System.out.println();

                String prefix2 = "kb|" + setType;
                Long bLong = idClient.allocateIdRange(setType, (long) 1);
                String setId = prefix2 + "." + bLong.toString();
                System.out.println("reserved new set id " + setId);

                Map<String, Long> map2 = new HashMap<String, Long>();
                String key2 = fileprefix + ".set." + 0;
                Map<String, String> mappedIds2 = idClient.externalIdsToKbaseIds(MAKresource, Arrays.asList(key2));
                System.out.println("set external id " + key2);
                String setKBId = "";
                if (mappedIds2.size() == 0) {
                    map2.put(key2, bLong);
                    idClient.registerAllocatedIds(prefix2, MAKresource, map2);
                    setKBId = prefix2 + "." + bLong;
                    System.out.println("registering set " + setKBId);
                } else {
                    setKBId = mappedIds2.get(key2);
                    System.out.println("existing set " + setKBId);
                }

                System.out.println();

                String prefix3 = "kb|" + resultType;
                Long cLong = idClient.allocateIdRange(resultType, (long) 1);
                String resultId = prefix3 + "." + cLong.toString();
                System.out.println("reserved new result id " + resultId);

                Map<String, Long> map3 = new HashMap<String, Long>();
                String key3 = fileprefix + ".result." + 0;
                Map<String, String> mappedIds3 = idClient.externalIdsToKbaseIds(MAKresource, Arrays.asList(key3));
                System.out.println("result external id " + key3);
                String resultKBId = "";
                if (mappedIds3.size() == 0) {
                    map3.put(key3, cLong);
                    idClient.registerAllocatedIds(prefix3, MAKresource, map3);
                    resultKBId = prefix2 + "." + cLong;
                    System.out.println("registering result " + resultKBId);
                } else {
                    resultKBId = mappedIds3.get(key3);
                    System.out.println("existing result " + resultKBId);
                }


                makBiclusterSet.setId(setKBId);
                makResult.setId(resultKBId);

                Map<String, String> indexMap = new HashMap<String, String>();
                for (int i = 0; i < biclusters.size(); i++) {
                    MAKBicluster mb = biclusters.get(i);
                    //String kbid = prefix + "." + unmappedIds.get(fileprefix + "." + i);

                    String curkey = fileprefix + "." + i;
                    String kbid = mappedIds.get(curkey);
                    String shortkbid = mappedIds.get(curkey);
                    if (kbid == null) {
                        kbid = prefix + "." + unmappedIds.get(curkey);
                        shortkbid = biclusterType + "." + unmappedIds.get(curkey);
                    }
                    else {
                        shortkbid = StringUtil.replace(shortkbid, "kb|", "");
                    }
                    mb.setBiclusterId(kbid);
                    biclusters.set(i, mb);

                    indexMap.put(kbid, "" + i);

                    //SOMR1_expr_refine_top_0.25_1.0_c_reconstructed.txt

                    //SOMR1_expr_refine_top_0.25_1.0_c_reconstructed.txt_bicluster.0_data.jsonp

                    //SOMR1_expr_refine_top_0.25_1.0_c_reconstructed.txt_MAKResult.jsonp_kbmap.jsonp_bicluster.0_data.jsonp

                    ObjectMapper mapper2 = new ObjectMapper();
                    String inpath = prefixInput + "_bicluster." + i + "_data.jsonp_map.jsonp";//"bicluster." + kbid;//
                    File f2 = new File(inpath);
                    FloatDataTable biclustertable = mapper2.readValue(f2, FloatDataTable.class);

                    biclustertable.setId(kbid);

                    TextFile.write(UObject.transformObjectToString(biclustertable), shortkbid + ".jsonp");
                }

                makBiclusterSet.setIdIndex(indexMap);
                makBiclusterSet.setBiclusters(biclusters);

                TextFile.write(UObject.transformObjectToString(makResult), args[0] + "_register.jsonp");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            RegisterBiclusterIds ce = new RegisterBiclusterIds(args);
        } else {
            System.out.println("usage: java us.kbase.mak.RegisterBiclusterIds <MAK result jsonp>");
        }
    }

}
