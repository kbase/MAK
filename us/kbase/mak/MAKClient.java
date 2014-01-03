package us.kbase.mak;

import java.net.URL;
import us.kbase.common.service.JsonClientCaller;

/**
 * <p>Original spec-file module name: MAK</p>
 * <pre>
 * Module MAKBiclusterCollection version 1.0
 * This module provides access to MAK biclustering results.
 * @author marcin
 * </pre>
 */
public class MAKClient {
    private JsonClientCaller caller;

    public MAKClient(URL url) {
        caller = new JsonClientCaller(url);
    }
}
