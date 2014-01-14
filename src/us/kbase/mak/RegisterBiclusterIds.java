package us.kbase.mak;

import us.kbase.idserverapi.IDServerAPIClient;

import java.net.URL;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/8/14
 * Time: 4:05 PM
 */
public class RegisterBiclusterIds {

    final public static void main(String argv[]) {

        try {
            IDServerAPIClient idClient = new IDServerAPIClient(new URL("http://kbase.us/services/idserver"));
            String id = "kb|" + typeName + "." + idClient.allocateIdRange(typeName, 1L).toString();

            idClient.registerAllocatedIds();
            idClient.registerIds();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
