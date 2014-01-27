package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.common.service.UObject;
import us.kbase.idserverapi.IDServerAPIClient;
import util.TextFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    /**
     * @param args
     */
    public RegisterBiclusterIds(String args[]) {

        System.out.println("1L " + 1L);

        try {
            ObjectMapper mapper = new ObjectMapper();
            File f = new File(args[0]);
            MAKResult makResult = mapper.readValue(f, MAKResult.class);

            try {
                IDServerAPIClient idClient = new IDServerAPIClient(new URL("http://kbase.us/services/idserver"));


                String prefix = "kb|" + biclusterType;
                List<MAKBicluster> biclusters = makResult.getSets().get(0).getBiclusters();
                Long aLong = idClient.allocateIdRange(biclusterType, (long) biclusters.size());
                String biclusterid = prefix + "." + aLong.toString();
                System.out.println("biclusterid " + biclusterid);

                Map<String, Long> map = new HashMap<String, Long>();
                for (int i = 0; i < biclusters.size(); i++) {
                    map.put(args[0] + "." + i, aLong + i);
                }

                idClient.registerAllocatedIds(prefix, "MAK", map);


                String prefix2 = "kb|" + setType;
                Long bLong = idClient.allocateIdRange(setType, (long) 1);
                String setId = prefix2 + "." + bLong.toString();
                System.out.println("biclusterid " + setId);

                Map<String, Long> map2 = new HashMap<String, Long>();
                map2.put(args[0] + ".set" + 0, bLong);

                idClient.registerAllocatedIds(prefix2, "MAK", map);


                String prefix3 = "kb|" + resultType;
                Long cLong = idClient.allocateIdRange(resultType, (long) 1);
                String resultId = prefix3 + "." + cLong.toString();
                System.out.println("biclusterid " + resultId);

                Map<String, Long> map3 = new HashMap<String, Long>();
                map3.put(args[0] + ".result" + 0, cLong);

                idClient.registerAllocatedIds(prefix3, "MAK", map);


                makResult.getSets().get(0).setId(setId);
                makResult.setId(resultId);

                for (int i = 0; i < biclusters.size(); i++) {
                    MAKBicluster mb = biclusters.get(i);
                    mb.setBiclusterId(prefix + "." + (aLong + i));
                    biclusters.set(i, mb);
                }

                makResult.getSets().get(0).setBiclusters(biclusters);

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
