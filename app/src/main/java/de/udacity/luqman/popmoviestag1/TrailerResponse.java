package de.udacity.luqman.popmoviestag1;

/**
 * Created by luqman on 13.04.2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TrailerResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Trailer> results;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public List<Trailer> getResults() { return results; }

    public void setResults(List<Trailer> results) { this.results = results; }
}
