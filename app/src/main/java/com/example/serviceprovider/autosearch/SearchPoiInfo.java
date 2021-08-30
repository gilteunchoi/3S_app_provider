package com.example.serviceprovider.autosearch;



public class SearchPoiInfo {
    private String totalCount;
    private String count;
    private String page;
    private Pois pois;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Pois getPois() {
        return pois;
    }

    public void setPois(Pois pois) {
        this.pois = pois;
    }
}
