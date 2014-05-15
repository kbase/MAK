package us.kbase.mak.util;

import us.kbase.auth.AuthException;
import us.kbase.auth.AuthService;
import us.kbase.auth.AuthUser;
import us.kbase.common.service.JsonClientException;
import us.kbase.common.service.UnauthorizedException;
import us.kbase.mak.MAKBiclusterSet;
import us.kbase.mak.MAKClient;
import util.MoreArray;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 5/7/14
 * Time: 3:37 PM
 */
public class TestMAKServer {


    String urlStr = "http://140.221.84.156:8000";//"http://127.0.0.1:49997/kbase_sapling_v3";
    String testQuery = "kb|g.371.peg.362,kb|g.371.peg.180,kb|g.371.peg.1427,kb|g.371.peg.1854,kb|g.371.peg.1241,kb|g.371.peg.1588,kb|g.371.peg.1986,kb|g.371.peg.1593,kb|g.371.peg.1522,kb|g.371.peg.1325,kb|g.371.peg.1512,kb|g.371.peg.1087,kb|g.371.peg.1022,kb|g.371.peg.1338,kb|g.371.peg.1435,kb|g.371.peg.1128,kb|g.371.peg.1346,kb|g.371.peg.1999,kb|g.371.peg.1849,kb|g.371.peg.1507,kb|g.371.peg.1119,kb|g.371.peg.1072,kb|g.371.peg.1189,kb|g.371.peg.1880,kb|g.371.peg.1524,kb|g.371.peg.1165,kb|g.371.peg.1033,kb|g.371.peg.1426,kb|g.371.peg.1168,kb|g.371.peg.1970,kb|g.371.peg.1615,kb|g.371.peg.1716,kb|g.371.peg.1734,kb|g.371.peg.1303,kb|g.371.peg.1425,kb|g.371.peg.1195,kb|g.371.peg.1976,kb|g.371.peg.1748,kb|g.371.peg.1711,kb|g.371.peg.1345,kb|g.371.peg.1458,kb|g.371.peg.1066,kb|g.371.peg.1065,kb|g.371.peg.1672,kb|g.371.peg.3199,kb|g.371.peg.3175,kb|g.371.peg.3947,kb|g.371.peg.3515,kb|g.371.peg.3727,kb|g.371.peg.3963,kb|g.371.peg.3296,kb|g.371.peg.3478,kb|g.371.peg.4487,kb|g.371.peg.4606,kb|g.371.peg.4411,kb|g.371.peg.4002,kb|g.371.peg.4313,kb|g.371.peg.4475,kb|g.371.peg.4665,kb|g.371.peg.4164,kb|g.371.peg.439,kb|g.371.peg.674,kb|g.371.peg.926,kb|g.371.peg.550,kb|g.371.peg.924,kb|g.371.peg.965,kb|g.371.peg.339,kb|g.371.peg.152,kb|g.371.peg.7,kb|g.371.peg.398,kb|g.371.peg.463,kb|g.371.peg.69,kb|g.371.peg.848,kb|g.371.peg.293,kb|g.371.peg.535,kb|g.371.peg.601";
    String kbgid = "g.371";
    String dataType = "expr";

    public TestMAKServer(String[] args) {

        String user = System.getProperty("test.user");
        String pwd = System.getProperty("test.pwd");

        System.out.println(user + "\t" + pwd);

        try {
            URL url = new URL(urlStr);
            try {
                AuthUser au = AuthService.login(user, pwd);

                MAKClient mc = new MAKClient(url, au.getToken());
                mc.setAuthAllowedForHttp(true);
                String[] ar = testQuery.split(",");

                List<String> qids = new ArrayList<String>();
                for(int i=0;i<ar.length;i++) {
                    qids.add(ar[i]);
                }

                try {
                    System.out.println("start");
                    MAKBiclusterSet mbs = mc.searchMAKResultsFromCDS(kbgid, dataType, qids,"211586_allmapunmap.txt");
                    System.out.println(mbs.getBiclusters().size());
                    System.out.println("end");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JsonClientException e) {
                    e.printStackTrace();
                }
            } catch (AuthException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnauthorizedException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            TestMAKServer rm = new TestMAKServer(args);
        }
    }

}
