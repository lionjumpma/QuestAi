package com.sprint.questai.model.enums;

import lombok.Getter;

import java.util.*;

@Getter
public enum LegalCaseType {
    SALE_CONTRACT("买卖合同", LegalCategory.CIVIL_LAW),
    LABOR_CONTRACT("劳动合同", LegalCategory.COMMERCIAL_LAW),
    CONTRACT_AFFAIRS("合同事务", LegalCategory.CIVIL_LAW),
    TRANSPORT_CONTRACT("运输合同", LegalCategory.CIVIL_LAW),
    MORTGAGE_GUARANTEE("抵押担保", LegalCategory.CIVIL_LAW),
    LABOR_DISPUTE("劳动争议", LegalCategory.COMMERCIAL_LAW),
    TRAFFIC_LIABILITY("交通责任", LegalCategory.CRIMINAL_LAW),
    PROPERTY_RIGHT_DISPUTE("物权纠纷", LegalCategory.CIVIL_LAW),
    LEASE_CONTRACT("租赁合同", LegalCategory.CIVIL_LAW),
    CORPORATE_AFFAIRS("公司事务", LegalCategory.COMMERCIAL_LAW),
    TORT_DISPUTE("侵权纠纷", LegalCategory.CIVIL_LAW),
    LOAN_CONTRACT("借款合同", LegalCategory.CIVIL_LAW),
    COPYRIGHT_SOFTWARE("版权软著", LegalCategory.COMMERCIAL_LAW),
    PERSONALITY_PRIVACY("人格隐私", LegalCategory.PUBLIC_LAW),
    INSURANCE_DISPUTE("保险纠纷", LegalCategory.CIVIL_LAW),
    CHILD_CUSTODY("孩子抚养", LegalCategory.CIVIL_LAW),
    COMMISSION_CONTRACT("委托合同", LegalCategory.CIVIL_LAW),
    FINANCE_SECURITIES_INSURANCE("金融证券保险", LegalCategory.COMMERCIAL_LAW),
    CONSTRUCTION_ENGINEERING("建筑工程", LegalCategory.CIVIL_LAW),
    REAL_ESTATE_DISPUTE("房地产纠纷", LegalCategory.CIVIL_LAW),
    HOUSE_SALE("房屋买卖", LegalCategory.CIVIL_LAW),
    PROPERTY_DISPUTE("物业纠纷", LegalCategory.CIVIL_LAW),
    SERVICE_CONTRACT("服务合同", LegalCategory.CIVIL_LAW),
    PRODUCT_LIABILITY("产品责任", LegalCategory.CIVIL_LAW),
    CONTRACT_DISPUTE("合同纠纷", LegalCategory.CIVIL_LAW),
    CONTRACTING_CONTRACT("承揽合同", LegalCategory.CIVIL_LAW),
    FINANCING_LEASE("融资租赁", LegalCategory.COMMERCIAL_LAW),
    CIVIL_DISPUTE("民事纠纷", LegalCategory.CIVIL_LAW),
    PROPERTY_DIVISION("财产分割", LegalCategory.CIVIL_LAW),
    INTELLECTUAL_PROPERTY("知识产权", LegalCategory.COMMERCIAL_LAW),
    MARRIAGE_DIVORCE("结婚离婚", LegalCategory.CIVIL_LAW),
    PARTNERSHIP_ENTERPRISE("合伙企业", LegalCategory.COMMERCIAL_LAW),
    LABOR_PERSONNEL("劳动人事", LegalCategory.COMMERCIAL_LAW),
    CONSTRUCTION_ENGINEERING_CONSTRUCTION("建设工程施工", LegalCategory.CIVIL_LAW),
    BUSINESS_MANAGEMENT("企业经营", LegalCategory.COMMERCIAL_LAW),
    PERSONAL_INJURY("人身侵权", LegalCategory.CIVIL_LAW),
    SALARY_BENEFITS("工资福利", LegalCategory.COMMERCIAL_LAW),
    TRAFFIC_ACCIDENT("交通事故", LegalCategory.CRIMINAL_LAW),
    MARRIAGE_FAMILY("婚姻家庭", LegalCategory.CIVIL_LAW),
    LAND_DISPUTE("土地纠纷", LegalCategory.CIVIL_LAW),
    INHERITANCE("遗产继承", LegalCategory.CIVIL_LAW),
    ELDERLY_SUPPORT("老人赡养", LegalCategory.CIVIL_LAW),
    CORPORATE_GOVERNANCE("公司治理", LegalCategory.COMMERCIAL_LAW),
    HOUSE_DISPUTE("房产纠纷", LegalCategory.CIVIL_LAW);

    private final String displayName;
    private final LegalCategory category;
    public static final String types = getTypes();
    public LegalCategory LegalCategory() {
        return category;
    }
    LegalCaseType(String displayName, LegalCategory category) {
        this.displayName = displayName;
        this.category = category;
    }
    public LegalCategory findCategory(String type) {
        for (LegalCaseType legalCaseType : LegalCaseType.values()) {
            if (legalCaseType.displayName.equals(type)) {
                return legalCaseType.category;
            }
        }
        return null;
    }
    public static LegalCaseType findByKeyword(String keyword) {
        for (LegalCaseType type : LegalCaseType.values()) {
            if (type.displayName.equals(keyword)) {
                return type;
            }
        }
        return null;
    }
    public static List<LegalCaseType> findByCategory(LegalCategory category) {
        List<LegalCaseType> types = new ArrayList<>();
        for (LegalCaseType type : LegalCaseType.values()) {
            if (type.category.equals(category)) {
                types.add(type);
            }
        }
        return types;
    }
    public static List<LegalCaseType> getTypeList() {
        List<LegalCaseType> types = new ArrayList<>();
        types.addAll(Arrays.asList(LegalCaseType.values()));
        return types;
    }
    public static Map<String, List<String>> mapByCategory() {
        Map<String, List<String>> categoryMap = new HashMap<>();
        for (LegalCaseType type : LegalCaseType.values()) {
            String category = type.getCategory().name();
            categoryMap.putIfAbsent(category, new ArrayList<>());
            categoryMap.get(category).add(type.getDisplayName());
        }
        return categoryMap;
    }
    public String toString() {
        return displayName;
    }
    public static String ListToString(List<LegalCaseType> types) {
        StringBuilder sb = new StringBuilder();
        for (LegalCaseType type : types) {
            sb.append(type.getDisplayName()).append(", ");
        }
        return sb.toString();
    }
    public  static String getTypes(){
        return ListToString(getTypeList());
    }

}


