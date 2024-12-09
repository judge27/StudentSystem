package com.example.project1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class APIResponse {
  String status;
  List<APIModel[]> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<APIModel[]> getData() {
        return data;
    }

    public void setData(List<APIModel[]> data) {
        this.data = data;
    }
}
