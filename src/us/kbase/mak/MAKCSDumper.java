package us.kbase.mak;


import com.fasterxml.jackson.databind.ObjectMapper;
import us.kbase.centralstore.Dumper;
import us.kbase.centralstore.DumperBundle;
import us.kbase.centralstore.dumpers.*;
import us.kbase.common.service.JsonClientException;
import us.kbase.idmap.IdMapClient;
import us.kbase.idmap.IdPair;
import us.kbase.mak.util.KBidforTaxId;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 1/29/14
 * Time: 11:40 AM
 */
public class MAKCSDumper {

    String MAKresource = "MAKDB";
    String MAKdatasource = "MAK";

    MAKResult makResult;
    String fileprefix;

    String kbgid;

    String MAKurl = "http://genomics.lbl.gov/MAK";

    MAKBundle bundle;

    /**
     * @param args
     */
    public MAKCSDumper(String[] args) {

        try {

            try {

                try {
                    init(args);

                    MAKBiclusterSet makBiclusterSet = makResult.getSets().get(0);
                    List<MAKBicluster> lmkb = makBiclusterSet.getBiclusters();

                    // Set target directory
                    Dumper.setTargetDirectory(args[1]);
                    try {
                        Dumper.init(bundle);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    try {
                        bundle.so.withId(MAKresource)
                                .withDescription("Massive Associative K-biclustering database")
                                .withName("MAKDB")
                                .withUrl(MAKurl)
                                .push();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String type = setType();

                    String makeBiclusterSetId = makBiclusterSet.getId();
                    try {
                        bundle.ad.withId(makeBiclusterSetId)
                                .withAssociation_type(type)
                                .withData_source(MAKdatasource)
                                .withDescription(makResult.getParameters().getInputs().get(0).getDescription())
                                .withUrl(MAKurl)
                                .push();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        bundle.dsToGenomeDumper
                                .withFrom_link(makeBiclusterSetId)
                                .withTo_link(kbgid)
                                .push();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < lmkb.size(); i++) {
                        MAKBicluster mb = lmkb.get(i);
                        // Dump a record
                        String biclusterId = mb.getBiclusterId();
                        try {
                            bundle.association.withId(biclusterId)
                                    .withConfidence(new Float(mb.getFullCrit()))
                                    .withDescription("MAK bicluster from " + fileprefix)//super description" + i)
                                    .withDirectional((byte) 0)
                                    .withName(fileprefix + "." + i)
                                    .withUrl("")//"url" + i)
                                    .push();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            bundle.dsToAssocDumper
                                    .withFrom_link(makeBiclusterSetId)
                                    .withTo_link(biclusterId)
                                    .push();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < lmkb.size(); i++) {
                        MAKBicluster mb = lmkb.get(i);
                        for (int a = 0; a < mb.getGeneIds().size(); a++) {
                            try {
                                bundle.associationFeat.withStrength(new Float(mb.getFullCrit()))
                                        .withFrom_link(mb.getBiclusterId())
                                        .withTo_link(mb.getGeneIds().get(a))
                                        .withRank(1)
                                        .withStoichiometry(1)
                                        .push();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            } finally {


                try {
                    Dumper.close(bundle);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                // It should create a ready to use shell script to load data into MySQL
                // for ALL(!) tables (dumpers) in your bundle
                Dumper.createImportScript(bundle, "load_MAK");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String setType() {


        String type = "";
        String dataType = makResult.getParameters().getInputs().get(0).getDataType();
        System.out.println("dataType " + dataType);
        if (dataType.equals("expression"))
            type = "CO-EXPRESSION";
        else if (dataType.equals("fitness"))
            type = "CO-FITNESS";
        return type;
    }

    /**
     * @param args
     * @throws IOException
     */
    private void init(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File f = new File(args[0]);

        int slashind = args[0].indexOf("/");
        int max = Math.max(0, slashind);
        int dotind = args[0].indexOf(".", max);
        fileprefix = args[0].substring(max, dotind);

        makResult = mapper.readValue(f, MAKResult.class);

        kbgid = KBidforTaxId.getKBidforTaxId(makResult.getParameters().getInputs().get(0).getTaxon());

        bundle = new MAKBundle();
    }

    /**
     * @param args
     */

    public static void main(String[] args) {
        if (args.length == 2) {
            MAKCSDumper ce = new MAKCSDumper(args);
        } else {
            System.out.println("usage: java us.kbase.mak.MAKCSDumper <MAK result jsonp> <out dir>");
        }
    }

    public class MAKBundle implements DumperBundle {
        Source so;
        AssociationDataset ad;
        IsDatasetFor dsToGenomeDumper;
        Association association;
        IsGroupingOf dsToAssocDumper;
        AssociationFeature associationFeat;
    }
}



