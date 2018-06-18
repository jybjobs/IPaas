package com.cloud.paas.monitor.util.configuration;

/**
 * @Author: srf
 * @desc: Unit对象
 * @Date: Created in 2018-04-11 16-48
 * @Modified By:
 */
public enum Unit {
    m("minute", "m"), h("hour", "h"), d("day", "d");
    private String name;
    private String shortName;
    private Unit(String name, String unitStr){
        this.name = name;
        this.shortName = unitStr;
    }
    public static Unit getName(String shortName){
        for(Unit unit: Unit.values()){
            if (unit.getShortName().equals(shortName)) {
                return unit;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
