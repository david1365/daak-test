package com.caspco.unittest.model;

import java.util.List;
import java.util.Objects;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class SampleModelWithList {
    private Integer id;

    private List<SampleModel> sampleModelList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SampleModel> getSampleModelList() {
        return sampleModelList;
    }

    public void setSampleModelList(List<SampleModel> sampleModelList) {
        this.sampleModelList = sampleModelList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleModelWithList that = (SampleModelWithList) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sampleModelList, that.sampleModelList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sampleModelList);
    }
}
