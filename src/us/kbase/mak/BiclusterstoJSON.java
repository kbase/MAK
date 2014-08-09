package us.kbase.mak;

import DataMining.*;
import mathy.SimpleMatrix;
import us.kbase.common.service.UObject;
import util.MoreArray;
import util.ParsePath;
import util.TabFile;
import util.TextFile;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 12/17/13
 * Time: 11:37 PM
 */
public class BiclusterstoJSON {

    ValueBlockList vbl;
    Parameters prm;
    DiscoveryParameters dprm;
    String[][] summary_data;
    SimpleMatrix sm;
    String[] gene_labels;
    String[] exp_labels;

    MAKBiclusterSet mbs;
    MAKResult makr;

    java.util.Date date;

    //String default_series_ref = "AKtest/D_vulgaris_series_series";

    /*TODO user variable for primary data type*/
    String default_bicluster_type;// = "expression";
    String default_biclusterset_id = "" + 0;
    String default_biclusterset_desc = " biclusters";//gene expression

    String title;


    /**
     * @param args
     */
    public BiclusterstoJSON(String[] args) {

        try {
            loadInputs(args);

            long max_conditions = -1;
            long max_genes = -1;
            long num = vbl.size();

            mbs = new MAKBiclusterSet();
            mbs.setId("" + 0);
            mbs.setMaxConditions(max_conditions);
            mbs.setMaxGenes(max_genes);
            mbs.setMinConditions(max_conditions);
            mbs.setMinGenes(max_genes);
            mbs.setNumber(num);
            mbs.setBiclusterType(default_bicluster_type);
            mbs.setTaxon(dprm.taxon);
            mbs.setTimeStamp((new Timestamp(date.getTime())).toString());
            mbs.setVersion(MINER_STATIC.version);

            doBiclusters();


            //FloatDataTableContainer fdtc = new FloatDataTableContainer();

            //List<FloatDataTable> lfdt = new ArrayList<FloatDataTable>();
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < vbl.size(); i++) {
                System.out.println("i " + i);
                List<List<Double>> lld = new ArrayList<List<Double>>();
                ValueBlock vb = (ValueBlock) vbl.get(i);
                for (int a = 0; a < vb.genes.length; a++) {
                    List<Double> ld = new ArrayList<Double>();
                    for (int b = 0; b < vb.exps.length; b++) {
                        Double e = null;
                        try {
                            e = sm.data[vb.genes[a] - 1][vb.exps[b] - 1];
                        } catch (Exception e1) {
                            System.out.println("sm " + sm.data.length + "\t" + sm.data[0].length);
                            System.out.println("vb " + vb.genes.length + "\t" + vb.exps.length);
                            System.out.println("g " + vb.genes[a] + "\te " + vb.exps[b]);
                            System.out.println("g " + (vb.genes[a] - 1) + "\te " + (vb.exps[b] - 1));
                            System.out.println("val " + sm.data[vb.genes[a] - 1][vb.exps[b] - 1]);
                            e1.printStackTrace();
                        }
                        Double store = null;
                        if (Double.isNaN(e)) {
                            //System.out.println("e null " + e);
                            store = null;
                        } else store = new Double(e);
                        ld.add(store);
                    }
                    lld.add(ld);
                }

                ArrayList rowS = new ArrayList();
                for (int a = 0; a < vb.genes.length; a++) {
                    rowS.add(this.gene_labels[vb.genes[a] - 1]);
                }
                ArrayList rowkb = new ArrayList();
                for (int d = 0; d < vb.genes.length; d++) {
                    rowkb.add(this.gene_labels[vb.genes[d] - 1]);
                }
                ArrayList colS = new ArrayList();
                for (int b = 0; b < vb.exps.length; b++) {
                    colS.add(this.exp_labels[vb.exps[b] - 1]);
                }

                FloatDataTable fdt = new FloatDataTable();
                fdt.setRowIds(rowkb);
                fdt.setRowLabels(rowS);
                //fdt.setRowGroups();
                fdt.setColumnIds(colS);
                fdt.setColumnLabels(colS);
                //fdt.setColumnGroups();
                String name = title + "_" + i;
                map.put(name, "" + i);
                fdt.setName(name);
                fdt.setData(lld);
                TextFile.write(UObject.transformObjectToString(fdt), args[0] + "_bicluster." + i + "_data.jsonp");
                //lfdt.add(fdt);
            }
           /* fdtc.setSetdata(lfdt);
            fdtc.setIdIndex(map);
            fdtc.setName(title);
            TextFile.write(UObject.transformObjectToString(fdtc), args[0] + "_data.jsonp");
            */
            List<MAKInputData> listinput = doInputs();

            makr = new MAKResult();
            makr.setStartTime("" + 0);
            makr.setFinishTime("" + 10000);
            makr.setId("" + 0);

            List<MAKBiclusterSet> makbl = Arrays.asList(mbs);
            makr.setSets(makbl);
            doParams(listinput);

            TextFile.write(UObject.transformObjectToString(makr), args[0] + "_MAKResult.jsonp");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private void doBiclusters() {
        List<MAKBicluster> biclust = new ArrayList<MAKBicluster>();//vbl.size()
        for (int i = 0; i < vbl.size(); i++) {
            ValueBlock vb = (ValueBlock) vbl.get(i);
            if (vb == null)
                System.out.println("vb is null");
            MAKBicluster mb = new MAKBicluster();
            mb.setBiclusterId("" + i);
            mb.setConditionIds(MoreArray.convtoArrayList(MoreArray.toStringArray(vb.exps)));
            if (exp_labels != null) {
                List<String> clabels = new ArrayList();
                for (int g = 0; g < vb.exps.length; g++) {
                    String s = exp_labels[vb.exps[g] - 1];
                    clabels.add(s);
                }
                mb.setConditionLabels(clabels);
            } else {
                mb.setConditionLabels(MoreArray.convtoArrayList(MoreArray.toStringArray(vb.exps)));
            }

            mb.setGeneIds(MoreArray.convtoArrayList(MoreArray.toStringArray(vb.genes)));
            if (gene_labels != null) {
                List<String> glabels = new ArrayList();
                for (int g = 0; g < vb.genes.length; g++) {
                    String s = gene_labels[vb.genes[g] - 1];
                    glabels.add(s);
                }
                mb.setGeneLabels(glabels);
            } else {
                mb.setGeneLabels(MoreArray.convtoArrayList(MoreArray.toStringArray(vb.genes)));
            }

           /* vb.setDataAndMean(sm.data);
            List<List<String>> listdata = new ArrayList();// = MoreArray.initArrayList((vb.exp_data.length));
            for (int g = 0; g < vb.exp_data.length; g++) {
                ArrayList cur = MoreArray.convtoArrayList(MoreArray.toStringArray(vb.exp_data[g]));//new ArrayList();
                *//*for (int c = 0; c < cur.size(); c++) {
                    Double d = (Double) cur.get(c);
                    if (d.isNaN())
                        d = null;
                    cur.set(c, d.toString());
                }*//*
                //if (g == 0)
                //    MoreArray.printArray(vb.exp_data[g]);
                listdata.add(cur);
            }

            mb.setData(listdata);*/

            mb.setNumConditions((long) vb.exps.length);
            mb.setNumGenes((long) vb.genes.length);
            mb.setMissFrxn(vb.frxnNaN());

            Map terms = new HashMap();
            //GO terms
            String allterms = summary_data[i][12];
            String[] termlist = new String[0];
            try {
                System.out.println("GO " + allterms);
                termlist = allterms.substring(4).split("_");
                for (int t = 0; t < termlist.length; t++) {
                    terms.put("GO", termlist[t]);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("allterms GO " + allterms);
            }

            //TIGRFam roles
            allterms = summary_data[i][10];
            try {
                System.out.println("TIGRFam " + allterms);
                termlist = allterms.substring(10).split("_");
                for (int t = 0; t < termlist.length; t++) {
                    terms.put("TIGRFam", termlist[t]);
                }
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("allterms TIGRFam " + allterms);
            }

            mb.setEnrichedTerms(terms);
            mb.setExpCrit((vb.all_criteria[ValueBlock_STATIC.expr_mse_IND] + vb.all_criteria[ValueBlock_STATIC.expr_reg_IND] +
                    vb.all_criteria[ValueBlock_STATIC.expr_kend_IND]) / 3.0);
            mb.setExpMean(vb.exp_mean);
            mb.setExpMeanCrit(vb.all_criteria[ValueBlock_STATIC.expr_mean_IND]);
            mb.setFullCrit(vb.full_criterion);
            mb.setGeneIds(MoreArray.convtoArrayList(MoreArray.toStringArray(vb.genes)));
            //mb.setGeneLabels();
            mb.setOrthoCrit(vb.all_criteria[ValueBlock_STATIC.feat_IND]);
            mb.setPpiCrit(vb.all_criteria[ValueBlock_STATIC.INTERACT_IND]);
            mb.setTFCrit(vb.all_criteria[ValueBlock_STATIC.TF_IND]);

            biclust.add(mb);
        }

        mbs.setBiclusters(biclust);
    }

    /**
     * @param listinput
     */
    private void doParams(List<MAKInputData> listinput) {
        MAKParameters makp = new MAKParameters();
        makp.setInputs(listinput);
        makp.setLinkage(dprm.linkage);
        makp.setMaxBiclusterOverlap(dprm.max_bicluster_overlap);
        makp.setMaxEnrichPvalue(dprm.max_enrich_pvalue);
        makp.setMinRawBiclusterScore(dprm.min_raw_bicluster_score);
        makp.setNullDataPath(dprm.null_data_path);
        makp.setRcodepath(prm.R_METHODS_PATH);
        makp.setRdatapath(prm.R_DATA_PATH);
        makp.setRefine((long) (dprm.refine ? 1 : 0));
        makp.setRounds((long) dprm.rounds);
        makp.setRoundsMoveSequences(MoreArray.convtoArrayList(dprm.move_sequences));

        //makp.setGenomeId();
        //makp.setTaxon();
        //makp.setSeriesRef(default_series_ref);
        makr.setParameters(makp);
    }

    /**
     * @return
     */
    private List<MAKInputData> doInputs() {
        /* TODO support multiple inputs */
        MAKInputData maki = new MAKInputData();
        maki.setDataPath(prm.EXPR_DATA_PATH);
        maki.setDataType(default_bicluster_type);
        maki.setDescription(default_biclusterset_desc);
        maki.setId(default_biclusterset_id);
        maki.setNumCols((long) sm.data[0].length);
        maki.setNumRows((long) sm.data.length);
        maki.setTaxon(dprm.taxon);
        List<MAKInputData> listinput = new ArrayList();
        listinput.add(maki);
        return listinput;
    }

    /**
     * @param args
     * @throws Exception
     */
    private void loadInputs(String[] args) throws Exception {


        ParsePath pp = new ParsePath(args[0]);
        title = pp.getName();
        vbl = ValueBlockListMethods.readAny(args[0]);

        System.out.println("Loaded " + vbl.size());

        summary_data = TabFile.readtoArray(args[1]);

        sm = new SimpleMatrix(args[2]);

        prm = new Parameters();
        prm.read(args[3]);

        dprm = new DiscoveryParameters();
        dprm.read(args[4]);

        try {
            String[][] sarray = TabFile.readtoArray(args[5]);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            int col = 2;
            String[] n = MoreArray.extractColumnStr(sarray, col);
            gene_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting 2 cols");
            //e.printStackTrace();
            try {
                String[][] sarray = TabFile.readtoArray(args[5]);
                System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
                int col = 1;
                String[] n = MoreArray.extractColumnStr(sarray, col);
                gene_labels = MoreArray.replaceAll(n, "\"", "");
                System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
        try {
            String[][] sarray = TabFile.readtoArray(args[6]);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            int col = 1;
            String[] n = MoreArray.extractColumnStr(sarray, col);
            exp_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + exp_labels.length + "\t" + exp_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting 1 cols");
            e.printStackTrace();
        }


        default_bicluster_type = args[7];
        default_biclusterset_desc = default_bicluster_type + default_biclusterset_desc;

        date = new java.util.Date();
    }


    /**
     * @paramfile args
     */

    public final static void main(String[] args) {
        if (args.length == 8) {
            BiclusterstoJSON rm = new BiclusterstoJSON(args);
        } else {
            System.out.println("syntax: java us.kbase.mak.BiclusterstoJSON\n" +
                    "< MAK merged .vbl file >\n" +
                    "< MAK summary file >\n" +
                    "< primary input data>\n" +
                    "< MAK trajectory parameter file >\n" +
                    "< MAK discovery strategy file >\n" +
                    "< primary input data gene labels >\n" +
                    "< primary input data condition labels >\n" +
                    "< bicluster type >"
                    /*TODO add dash options, add option to name primary data type*/
            );
        }
    }
}
