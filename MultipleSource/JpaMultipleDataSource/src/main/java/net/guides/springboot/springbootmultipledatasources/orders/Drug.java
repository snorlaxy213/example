package net.guides.springboot.springbootmultipledatasources.orders;

public class Drug {

    // 药物ID
    private Long drugId;
    // 药物名
    private String drugName;
    // 商品用名
    private String productName;
    // 结构
    private String structure;
    // 红外光谱
    private String infrared;
    // 紫外光谱
    private String UVSpectrum;
    // 规格
    private String specification;
    // 药物准文号
    private String drugApprovalNo;
    // 生产厂商
    private String manufacturer;

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getInfrared() {
        return infrared;
    }

    public void setInfrared(String infrared) {
        this.infrared = infrared;
    }

    public String getUVSpectrum() {
        return UVSpectrum;
    }

    public void setUVSpectrum(String UVSpectrum) {
        this.UVSpectrum = UVSpectrum;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDrugApprovalNo() {
        return drugApprovalNo;
    }

    public void setDrugApprovalNo(String drugApprovalNo) {
        this.drugApprovalNo = drugApprovalNo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
