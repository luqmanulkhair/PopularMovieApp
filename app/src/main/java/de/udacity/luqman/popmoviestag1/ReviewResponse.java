package de.udacity.luqman.popmoviestag1;

/**
 * Created by luqman on 13.04.2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ReviewResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Review> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

}
