package us.kbase.mak;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/15/14
 * Time: 2:44 PM
 */
public class MAKServerConfig {

    //Deployment options
    	protected static boolean DEPLOY_AWE = true;

    	//Service URLs
    	public static final String JOB_SERVICE_URL = " https://kbase.us/services/userandjobstate";//dev:"http://140.221.84.180:7083";
    	public static final String AWE_SERVICE_URL = "http://140.221.85.171:7080/job";
    	public static final String ID_SERVICE_URL = "http://kbase.us/services/idserver";
    	public static final String WS_SERVICE_URL = "http://140.221.84.209:7058";//"https://kbase.us/services/workspace/";//
    	public static final String SHOCK_URL = "http://140.221.84.236:8000";

    	//Paths
    	protected static final String JOB_DIRECTORY = "/var/tmp/mak/";
    	protected static final String MAK_DIRECTORY = "/kb/runtime/mak/";
    	protected static final String DATA_PATH = "/etc/mak/";
    	protected static final String MAK_RUN_PATH = "/kb/runtime/mak/MAK.jar";
    	protected static final String AWF_CONFIG_FILE = "/kb/deployment/services/mak/mak.awf";
    	protected static final String CACHE_PATH = "/mnt/MAK";


    	//Logging options

    	//With LOG_AWE_CALLS = true, MAK will write all JSON calls to AWE client and all AWE responses to /var/tmp/MAK/MAK-awe.log
    	//This is a serious security threat because log will contain all auth tokens
    	//SET IT TO FALSE ON PRODUCTION
    	public static final boolean LOG_AWE_CALLS = true;

}
