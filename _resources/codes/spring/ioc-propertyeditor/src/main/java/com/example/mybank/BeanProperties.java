package com.example.mybank;

import java.text.DateFormat;
import java.util.*;

public class BeanProperties {

    private int[] propIntArray;
    private String[] propStrArray;
    private Object[] propObjArray;
    private Properties propProperties;
    private List<Integer> propList;
    private Set<String> propSet;
    private Map<String, Object> propMap;
    private Date propDate;

    public void setPropIntArray(int[] propIntArray) {
        this.propIntArray = propIntArray;
    }

    public void setPropStrArray(String[] propStrArray) {
        this.propStrArray = propStrArray;
    }

    public void setPropObjArray(Object[] propObjArray) {
        this.propObjArray = propObjArray;
    }

    public void setPropProperties(Properties propProperties) {
        this.propProperties = propProperties;
    }

    public void setPropList(List<Integer> propList) {
        this.propList = propList;
    }

    public void setPropSet(Set<String> propSet) {
        this.propSet = propSet;
    }

    public void setPropMap(Map<String, Object> propMap) {
        this.propMap = propMap;
    }

    public void setPropDate(Date propDate) {
        this.propDate = propDate;
    }

    @Override
    public String toString() {
        return "BeanProperties " +
                "{\n    propIntArray=" + Arrays.toString(propIntArray) +
                ",\n    propStrArray=" + Arrays.toString(propStrArray) +
                ",\n    propObjArray=" + Arrays.toString(propObjArray) +
                ",\n    propProperties=" + propProperties +
                ",\n    propList=" + propList +
                ",\n    propSet=" + propSet +
                ",\n    propMap=" + propMap +
                ",\n    propDate=" + DateFormat.getDateInstance().format(propDate) +
                "\n}" +
                '\n' + propStrArray[0].getClass() +
                '\n' + propObjArray[0].getClass() +
                '\n' + propList.getClass() +
                '\n' + propSet.getClass() +
                '\n' + propMap.getClass();
    }
}
