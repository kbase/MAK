
package us.kbase.mak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: MAKBicluster</p>
 * <pre>
 * Bicluster 
 *         string bicluster_type - type of bicluster (determined by source data, e.g. expression, fitness, metagenomic, metabolite, integrated)
 *         string bicluster_id - id
 *         int num_genes - number of genes
 *         int num_conditions - number of conditions
 *         list<string> condition_ids - condition ids
 *         list<string> condition_labels - condition labels
 *         list<string> gene_ids - gene ids
 *         list<string> gene_labels - gene labels
 *         float exp_mean - expression mean
 *         float exp_mean_crit - expression mean criterion value
 *         float exp_crit - expression criterion value
 *         float ppi_crit - PPI criterion value
 *         float TF_crit - TF criterion value
 *         float ortho_crit - orthology criterion value
 *         float full_crit - full criterion value
 *         float miss_frxn - fraction of missing data
 *         mapping<string, string> enriched_terms - enriched terms
 *         @searchable ws_subset bicluster_id gene_ids gene_labels condition_ids condition_labels enriched_terms
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "bicluster_id",
    "num_genes",
    "num_conditions",
    "condition_ids",
    "condition_labels",
    "gene_ids",
    "gene_labels",
    "exp_mean",
    "exp_mean_crit",
    "exp_crit",
    "ppi_crit",
    "TF_crit",
    "ortho_crit",
    "full_crit",
    "miss_frxn",
    "enriched_terms"
})
public class MAKBicluster {

    @JsonProperty("bicluster_id")
    private java.lang.String biclusterId;
    @JsonProperty("num_genes")
    private Integer numGenes;
    @JsonProperty("num_conditions")
    private Integer numConditions;
    @JsonProperty("condition_ids")
    private List<java.lang.String> conditionIds = new ArrayList<java.lang.String>();
    @JsonProperty("condition_labels")
    private List<java.lang.String> conditionLabels = new ArrayList<java.lang.String>();
    @JsonProperty("gene_ids")
    private List<java.lang.String> geneIds = new ArrayList<java.lang.String>();
    @JsonProperty("gene_labels")
    private List<java.lang.String> geneLabels = new ArrayList<java.lang.String>();
    @JsonProperty("exp_mean")
    private Double expMean;
    @JsonProperty("exp_mean_crit")
    private Double expMeanCrit;
    @JsonProperty("exp_crit")
    private Double expCrit;
    @JsonProperty("ppi_crit")
    private Double ppiCrit;
    @JsonProperty("TF_crit")
    private Double TFCrit;
    @JsonProperty("ortho_crit")
    private Double orthoCrit;
    @JsonProperty("full_crit")
    private Double fullCrit;
    @JsonProperty("miss_frxn")
    private Double missFrxn;
    @JsonProperty("enriched_terms")
    private Map<String, String> enrichedTerms;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("bicluster_id")
    public java.lang.String getBiclusterId() {
        return biclusterId;
    }

    @JsonProperty("bicluster_id")
    public void setBiclusterId(java.lang.String biclusterId) {
        this.biclusterId = biclusterId;
    }

    public MAKBicluster withBiclusterId(java.lang.String biclusterId) {
        this.biclusterId = biclusterId;
        return this;
    }

    @JsonProperty("num_genes")
    public Integer getNumGenes() {
        return numGenes;
    }

    @JsonProperty("num_genes")
    public void setNumGenes(Integer numGenes) {
        this.numGenes = numGenes;
    }

    public MAKBicluster withNumGenes(Integer numGenes) {
        this.numGenes = numGenes;
        return this;
    }

    @JsonProperty("num_conditions")
    public Integer getNumConditions() {
        return numConditions;
    }

    @JsonProperty("num_conditions")
    public void setNumConditions(Integer numConditions) {
        this.numConditions = numConditions;
    }

    public MAKBicluster withNumConditions(Integer numConditions) {
        this.numConditions = numConditions;
        return this;
    }

    @JsonProperty("condition_ids")
    public List<java.lang.String> getConditionIds() {
        return conditionIds;
    }

    @JsonProperty("condition_ids")
    public void setConditionIds(List<java.lang.String> conditionIds) {
        this.conditionIds = conditionIds;
    }

    public MAKBicluster withConditionIds(List<java.lang.String> conditionIds) {
        this.conditionIds = conditionIds;
        return this;
    }

    @JsonProperty("condition_labels")
    public List<java.lang.String> getConditionLabels() {
        return conditionLabels;
    }

    @JsonProperty("condition_labels")
    public void setConditionLabels(List<java.lang.String> conditionLabels) {
        this.conditionLabels = conditionLabels;
    }

    public MAKBicluster withConditionLabels(List<java.lang.String> conditionLabels) {
        this.conditionLabels = conditionLabels;
        return this;
    }

    @JsonProperty("gene_ids")
    public List<java.lang.String> getGeneIds() {
        return geneIds;
    }

    @JsonProperty("gene_ids")
    public void setGeneIds(List<java.lang.String> geneIds) {
        this.geneIds = geneIds;
    }

    public MAKBicluster withGeneIds(List<java.lang.String> geneIds) {
        this.geneIds = geneIds;
        return this;
    }

    @JsonProperty("gene_labels")
    public List<java.lang.String> getGeneLabels() {
        return geneLabels;
    }

    @JsonProperty("gene_labels")
    public void setGeneLabels(List<java.lang.String> geneLabels) {
        this.geneLabels = geneLabels;
    }

    public MAKBicluster withGeneLabels(List<java.lang.String> geneLabels) {
        this.geneLabels = geneLabels;
        return this;
    }

    @JsonProperty("exp_mean")
    public Double getExpMean() {
        return expMean;
    }

    @JsonProperty("exp_mean")
    public void setExpMean(Double expMean) {
        this.expMean = expMean;
    }

    public MAKBicluster withExpMean(Double expMean) {
        this.expMean = expMean;
        return this;
    }

    @JsonProperty("exp_mean_crit")
    public Double getExpMeanCrit() {
        return expMeanCrit;
    }

    @JsonProperty("exp_mean_crit")
    public void setExpMeanCrit(Double expMeanCrit) {
        this.expMeanCrit = expMeanCrit;
    }

    public MAKBicluster withExpMeanCrit(Double expMeanCrit) {
        this.expMeanCrit = expMeanCrit;
        return this;
    }

    @JsonProperty("exp_crit")
    public Double getExpCrit() {
        return expCrit;
    }

    @JsonProperty("exp_crit")
    public void setExpCrit(Double expCrit) {
        this.expCrit = expCrit;
    }

    public MAKBicluster withExpCrit(Double expCrit) {
        this.expCrit = expCrit;
        return this;
    }

    @JsonProperty("ppi_crit")
    public Double getPpiCrit() {
        return ppiCrit;
    }

    @JsonProperty("ppi_crit")
    public void setPpiCrit(Double ppiCrit) {
        this.ppiCrit = ppiCrit;
    }

    public MAKBicluster withPpiCrit(Double ppiCrit) {
        this.ppiCrit = ppiCrit;
        return this;
    }

    @JsonProperty("TF_crit")
    public Double getTFCrit() {
        return TFCrit;
    }

    @JsonProperty("TF_crit")
    public void setTFCrit(Double TFCrit) {
        this.TFCrit = TFCrit;
    }

    public MAKBicluster withTFCrit(Double TFCrit) {
        this.TFCrit = TFCrit;
        return this;
    }

    @JsonProperty("ortho_crit")
    public Double getOrthoCrit() {
        return orthoCrit;
    }

    @JsonProperty("ortho_crit")
    public void setOrthoCrit(Double orthoCrit) {
        this.orthoCrit = orthoCrit;
    }

    public MAKBicluster withOrthoCrit(Double orthoCrit) {
        this.orthoCrit = orthoCrit;
        return this;
    }

    @JsonProperty("full_crit")
    public Double getFullCrit() {
        return fullCrit;
    }

    @JsonProperty("full_crit")
    public void setFullCrit(Double fullCrit) {
        this.fullCrit = fullCrit;
    }

    public MAKBicluster withFullCrit(Double fullCrit) {
        this.fullCrit = fullCrit;
        return this;
    }

    @JsonProperty("miss_frxn")
    public Double getMissFrxn() {
        return missFrxn;
    }

    @JsonProperty("miss_frxn")
    public void setMissFrxn(Double missFrxn) {
        this.missFrxn = missFrxn;
    }

    public MAKBicluster withMissFrxn(Double missFrxn) {
        this.missFrxn = missFrxn;
        return this;
    }

    @JsonProperty("enriched_terms")
    public Map<String, String> getEnrichedTerms() {
        return enrichedTerms;
    }

    @JsonProperty("enriched_terms")
    public void setEnrichedTerms(Map<String, String> enrichedTerms) {
        this.enrichedTerms = enrichedTerms;
    }

    public MAKBicluster withEnrichedTerms(Map<String, String> enrichedTerms) {
        this.enrichedTerms = enrichedTerms;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
