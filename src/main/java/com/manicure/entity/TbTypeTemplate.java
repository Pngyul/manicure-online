package com.manicure.entity;

public class TbTypeTemplate {
    private Long id;

    private String name;

    private String specIds;

    private String efficiencyIds;

    private String customAttributeItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSpecIds() {
        return specIds;
    }

    public void setSpecIds(String specIds) {
        this.specIds = specIds == null ? null : specIds.trim();
    }

    public String getEfficiencyIds() {
        return efficiencyIds;
    }

    public void setEfficiencyIds(String efficiencyIds) {
        this.efficiencyIds = efficiencyIds == null ? null : efficiencyIds.trim();
    }

    public String getCustomAttributeItems() {
        return customAttributeItems;
    }

    public void setCustomAttributeItems(String customAttributeItems) {
        this.customAttributeItems = customAttributeItems == null ? null : customAttributeItems.trim();
    }
}