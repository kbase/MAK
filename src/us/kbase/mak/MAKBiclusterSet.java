
package us.kbase.mak;

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
 * <p>Original spec-file type: MAKBiclusterSet</p>
 * <pre>
 * Bicluster set 
 *         string id - id
 *         string time_stamp - time stamp for the results
 *         string version - MAK version
 *         int number - number of biclusters in set
 *         int min_genes - min genes for bicluster in set
 *         int max_genes - max genes for bicluster in set
 *         int min_conditions - max genes for bicluster in set
 *         int max_conditions - max conditions for bicluster in set
 *         string taxon - NCBI taxonomy id
 *         string bicluster_type - type of bicluster (determined by source data, e.g. expression, fitness, metagenomic, metabolite, integrated)                
 *         list<MAKBicluster> biclusters - biclusters
 *         
 * @searchable ws_subset id taxon
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "time_stamp",
    "version",
    "number",
    "min_genes",
    "max_genes",
    "min_conditions",
    "max_conditions",
    "taxon",
    "bicluster_type",
    "biclusters"
})
public class MAKBiclusterSet {

    @JsonProperty("id")
    private String id;
    @JsonProperty("time_stamp")
    private String timeStamp;
    @JsonProperty("version")
    private String version;
    @JsonProperty("number")
    private Long number;
    @JsonProperty("min_genes")
    private Long minGenes;
    @JsonProperty("max_genes")
    private Long maxGenes;
    @JsonProperty("min_conditions")
    private Long minConditions;
    @JsonProperty("max_conditions")
    private Long maxConditions;
    @JsonProperty("taxon")
    private String taxon;
    @JsonProperty("bicluster_type")
    private String biclusterType;
    @JsonProperty("biclusters")
    private List<MAKBicluster> biclusters;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public MAKBiclusterSet withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("time_stamp")
    public String getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("time_stamp")
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public MAKBiclusterSet withTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public MAKBiclusterSet withVersion(String version) {
        this.version = version;
        return this;
    }

    @JsonProperty("number")
    public Long getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(Long number) {
        this.number = number;
    }

    public MAKBiclusterSet withNumber(Long number) {
        this.number = number;
        return this;
    }

    @JsonProperty("min_genes")
    public Long getMinGenes() {
        return minGenes;
    }

    @JsonProperty("min_genes")
    public void setMinGenes(Long minGenes) {
        this.minGenes = minGenes;
    }

    public MAKBiclusterSet withMinGenes(Long minGenes) {
        this.minGenes = minGenes;
        return this;
    }

    @JsonProperty("max_genes")
    public Long getMaxGenes() {
        return maxGenes;
    }

    @JsonProperty("max_genes")
    public void setMaxGenes(Long maxGenes) {
        this.maxGenes = maxGenes;
    }

    public MAKBiclusterSet withMaxGenes(Long maxGenes) {
        this.maxGenes = maxGenes;
        return this;
    }

    @JsonProperty("min_conditions")
    public Long getMinConditions() {
        return minConditions;
    }

    @JsonProperty("min_conditions")
    public void setMinConditions(Long minConditions) {
        this.minConditions = minConditions;
    }

    public MAKBiclusterSet withMinConditions(Long minConditions) {
        this.minConditions = minConditions;
        return this;
    }

    @JsonProperty("max_conditions")
    public Long getMaxConditions() {
        return maxConditions;
    }

    @JsonProperty("max_conditions")
    public void setMaxConditions(Long maxConditions) {
        this.maxConditions = maxConditions;
    }

    public MAKBiclusterSet withMaxConditions(Long maxConditions) {
        this.maxConditions = maxConditions;
        return this;
    }

    @JsonProperty("taxon")
    public String getTaxon() {
        return taxon;
    }

    @JsonProperty("taxon")
    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }

    public MAKBiclusterSet withTaxon(String taxon) {
        this.taxon = taxon;
        return this;
    }

    @JsonProperty("bicluster_type")
    public String getBiclusterType() {
        return biclusterType;
    }

    @JsonProperty("bicluster_type")
    public void setBiclusterType(String biclusterType) {
        this.biclusterType = biclusterType;
    }

    public MAKBiclusterSet withBiclusterType(String biclusterType) {
        this.biclusterType = biclusterType;
        return this;
    }

    @JsonProperty("biclusters")
    public List<MAKBicluster> getBiclusters() {
        return biclusters;
    }

    @JsonProperty("biclusters")
    public void setBiclusters(List<MAKBicluster> biclusters) {
        this.biclusters = biclusters;
    }

    public MAKBiclusterSet withBiclusters(List<MAKBicluster> biclusters) {
        this.biclusters = biclusters;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ((((((((((((((((((((((((("MAKBiclusterSet"+" [id=")+ id)+", timeStamp=")+ timeStamp)+", version=")+ version)+", number=")+ number)+", minGenes=")+ minGenes)+", maxGenes=")+ maxGenes)+", minConditions=")+ minConditions)+", maxConditions=")+ maxConditions)+", taxon=")+ taxon)+", biclusterType=")+ biclusterType)+", biclusters=")+ biclusters)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
