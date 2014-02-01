package us.kbase.mak;

import com.fasterxml.jackson.databind.ObjectMapper;
import mathy.SimpleMatrix;
import us.kbase.common.service.JsonClientException;
import us.kbase.idmap.IdMapClient;
import us.kbase.idmap.IdPair;
import us.kbase.mak.util.KBidforTaxId;
import util.MoreArray;
import util.TabFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin Joachimiak
 * User: marcin
 * Date: 2/1/14
 * Time: 3:36 PM
 */
public class ExpressionDatatoJSON {


    String[] gene_labels, exp_labels;

    SimpleMatrix expMatrix;

    String taxId;

    String kbgid;


    /**
     *
     */
    public ExpressionDatatoJSON(String[] args) {

        try {
            init(args);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     * @throws java.io.IOException
     */
    private void init(String[] args) throws IOException {

        expMatrix = new SimpleMatrix(args[0]);

        try {
            String[][] sarray = TabFile.readtoArray(args[1]);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            int col = 2;
            String[] n = MoreArray.extractColumnStr(sarray, col);
            gene_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + gene_labels.length + "\t" + gene_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting 2 cols");
            //e.printStackTrace();
            try {
                String[][] sarray = TabFile.readtoArray(args[1]);
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
            String[][] sarray = TabFile.readtoArray(args[2]);
            System.out.println("setLabels g " + sarray.length + "\t" + sarray[0].length);
            int col = 1;
            String[] n = MoreArray.extractColumnStr(sarray, col);
            exp_labels = MoreArray.replaceAll(n, "\"", "");
            System.out.println("setLabels gene " + exp_labels.length + "\t" + exp_labels[0]);
        } catch (Exception e) {
            System.out.println("expecting 1 cols");
            e.printStackTrace();
        }

        taxId = args[3];

        kbgid = KBidforTaxId.getKBidforTaxId(taxId);
    }

    /**
     * @paramfile args
     */

    public final static void main(String[] args) {
        if (args.length == 4) {
            BiclusterstoJSON rm = new BiclusterstoJSON(args);
        } else {
            System.out.println("syntax: java us.kbase.mak.BiclusterstoJSON\n" +
                    "< expression data matrix >\n" +
                    "< gene labels >\n" +
                    "< exp labels >\n" +
                    "< tax id >"
            );
        }
    }
}
