package com.doubledeltas.minecollector.data;

public enum Department {
    OCEAN("해양과"),
    AGRICULTURE("농업과"),
    FASHION("패션과"),
    ADVENTURE("모험과"),
    ARCHITECTURE("건축과");

    private final String korName;

    Department(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }

    public static Department fromPrefix(String prefix) {
        if (prefix == null) return null;
        
        // 색상 코드를 제거하고 실제 텍스트만 추출
        String cleanPrefix = prefix.replaceAll("§[0-9a-fk-or]|&[0-9a-fk-or]", "");
        
        if (cleanPrefix.contains("해양과")) return OCEAN;
        if (cleanPrefix.contains("농업과")) return AGRICULTURE;
        if (cleanPrefix.contains("패션과")) return FASHION;
        if (cleanPrefix.contains("모험과")) return ADVENTURE;
        if (cleanPrefix.contains("건축과")) return ARCHITECTURE;
        
        return null;
    }
} 