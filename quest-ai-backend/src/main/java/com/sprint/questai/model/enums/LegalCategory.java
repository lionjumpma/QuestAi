package com.sprint.questai.model.enums;

public enum LegalCategory {
    CIVIL_LAW("民事法"),
    COMMERCIAL_LAW("商业法"),
    CRIMINAL_LAW("刑事法"),
    PUBLIC_LAW("公共法"),
    INTERNATIONAL_LAW("国际法");

    private final String displayName;
    LegalCategory(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public LegalCategory fromString(String text) {
        System.out.println("fromString: " + text);
        for (LegalCategory b : LegalCategory.values()) {
            if (b.displayName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String toString() {
        return displayName;
    }
}
