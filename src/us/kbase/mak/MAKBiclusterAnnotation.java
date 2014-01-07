
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
 * <p>Original spec-file type: MAKBiclusterAnnotation</p>
 * <pre>
 * Bicluster annotations -- for future upgrade, currently rely on map<string, string> in MAKBicluster 
 *         WARNING -- placeholder for future implementation
 *         list<list<string>> GO - list of GO terms enriched in this set of genes
 *         list<list<string>> KEGG - list of KEGG pathways enriched in this set of genes
 *         list<list<string>> TIGRFAM_role - list of TIGRFAM roles enriched in this set of genes
 *         list<list<string>> TF_binding_sites - list of TFs with binding sites in this set of genes
 *         @searchable ws_subset GO KEGG TIGRFAM_role TF_binding_sites
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "GO",
    "KEGG",
    "TIGRFAM_role",
    "TF_binding_sites"
})
public class MAKBiclusterAnnotation {

    @JsonProperty("GO")
    private List<List<String>> GO = new ArrayList<List<String>>();
    @JsonProperty("KEGG")
    private List<List<String>> KEGG = new ArrayList<List<String>>();
    @JsonProperty("TIGRFAM_role")
    private List<List<String>> TIGRFAMRole = new ArrayList<List<String>>();
    @JsonProperty("TF_binding_sites")
    private List<List<String>> TFBindingSites = new ArrayList<List<String>>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("GO")
    public List<List<String>> getGO() {
        return GO;
    }

    @JsonProperty("GO")
    public void setGO(List<List<String>> GO) {
        this.GO = GO;
    }

    public MAKBiclusterAnnotation withGO(List<List<String>> GO) {
        this.GO = GO;
        return this;
    }

    @JsonProperty("KEGG")
    public List<List<String>> getKEGG() {
        return KEGG;
    }

    @JsonProperty("KEGG")
    public void setKEGG(List<List<String>> KEGG) {
        this.KEGG = KEGG;
    }

    public MAKBiclusterAnnotation withKEGG(List<List<String>> KEGG) {
        this.KEGG = KEGG;
        return this;
    }

    @JsonProperty("TIGRFAM_role")
    public List<List<String>> getTIGRFAMRole() {
        return TIGRFAMRole;
    }

    @JsonProperty("TIGRFAM_role")
    public void setTIGRFAMRole(List<List<String>> TIGRFAMRole) {
        this.TIGRFAMRole = TIGRFAMRole;
    }

    public MAKBiclusterAnnotation withTIGRFAMRole(List<List<String>> TIGRFAMRole) {
        this.TIGRFAMRole = TIGRFAMRole;
        return this;
    }

    @JsonProperty("TF_binding_sites")
    public List<List<String>> getTFBindingSites() {
        return TFBindingSites;
    }

    @JsonProperty("TF_binding_sites")
    public void setTFBindingSites(List<List<String>> TFBindingSites) {
        this.TFBindingSites = TFBindingSites;
    }

    public MAKBiclusterAnnotation withTFBindingSites(List<List<String>> TFBindingSites) {
        this.TFBindingSites = TFBindingSites;
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

}
