package com.yandex.money.api.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Complete list of currencies as in ISO 4217.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum Currency {
    AED("AED", 784),
    AFN("AFN", 971),
    ALL("ALL", 8),
    AMD("AMD", 51),
    ANG("ANG", 532),
    AOA("AOA", 973),
    ARS("ARS", 32),
    AUD("AUD", 36),
    AWG("AWG", 533),
    AZN("AZN", 944),
    BAM("BAM", 977),
    BBD("BBD", 52),
    BDT("BDT", 50),
    BGN("BGN", 975),
    BHD("BHD", 48),
    BIF("BIF", 108),
    BMD("BMD", 60),
    BND("BND", 96),
    BOB("BOB", 68),
    BOV("BOV", 984),
    BRL("BRL", 986),
    BSD("BSD", 44),
    BTN("BTN", 64),
    BWP("BWP", 72),
    BYR("BYR", 974),
    BZD("BZD", 84),
    CAD("CAD", 124),
    CDF("CDF", 976),
    CHE("CHE", 947),
    CHF("CHF", 756),
    CHW("CHW", 948),
    CLF("CLF", 990),
    CLP("CLP", 152),
    CNY("CNY", 156),
    COP("COP", 170),
    COU("COU", 970),
    CRC("CRC", 188),
    CUC("CUC", 931),
    CUP("CUP", 192),
    CVE("CVE", 132),
    CZK("CZK", 203),
    DJF("DJF", 262),
    DKK("DKK", 208),
    DOP("DOP", 214),
    DZD("DZD", 12),
    EGP("EGP", 818),
    ERN("ERN", 232),
    ETB("ETB", 230),
    EUR("EUR", 978),
    FJD("FJD", 242),
    FKP("FKP", 238),
    GBP("GBP", 826),
    GEL("GEL", 981),
    GHS("GHS", 936),
    GIP("GIP", 292),
    GMD("GMD", 270),
    GNF("GNF", 324),
    GTQ("GTQ", 320),
    GYD("GYD", 328),
    HKD("HKD", 344),
    HNL("HNL", 340),
    HRK("HRK", 191),
    HTG("HTG", 332),
    HUF("HUF", 348),
    IDR("IDR", 360),
    ILS("ILS", 376),
    INR("INR", 356),
    IQD("IQD", 368),
    IRR("IRR", 364),
    ISK("ISK", 352),
    JMD("JMD", 388),
    JOD("JOD", 400),
    JPY("JPY", 392),
    KES("KES", 404),
    KGS("KGS", 417),
    KHR("KHR", 116),
    KMF("KMF", 174),
    KPW("KPW", 408),
    KRW("KRW", 410),
    KWD("KWD", 414),
    KYD("KYD", 136),
    KZT("KZT", 398),
    LAK("LAK", 418),
    LBP("LBP", 422),
    LKR("LKR", 144),
    LRD("LRD", 430),
    LSL("LSL", 426),
    LTL("LTL", 440),
    LYD("LYD", 434),
    MAD("MAD", 504),
    MDL("MDL", 498),
    MGA("MGA", 969),
    MKD("MKD", 807),
    MMK("MMK", 104),
    MNT("MNT", 496),
    MOP("MOP", 446),
    MRO("MRO", 478),
    MUR("MUR", 480),
    MVR("MVR", 462),
    MWK("MWK", 454),
    MXN("MXN", 484),
    MXV("MXV", 979),
    MYR("MYR", 458),
    MZN("MZN", 943),
    NAD("NAD", 516),
    NGN("NGN", 566),
    NIO("NIO", 558),
    NOK("NOK", 578),
    NPR("NPR", 524),
    NZD("NZD", 554),
    OMR("OMR", 512),
    PAB("PAB", 590),
    PEN("PEN", 604),
    PGK("PGK", 598),
    PHP("PHP", 608),
    PKR("PKR", 586),
    PLN("PLN", 985),
    PYG("PYG", 600),
    QAR("QAR", 634),
    RON("RON", 946),
    RSD("RSD", 941),
    RUB("RUB", 643),
    RWF("RWF", 646),
    SAR("SAR", 682),
    SBD("SBD", 90),
    SCR("SCR", 690),
    SDG("SDG", 938),
    SEK("SEK", 752),
    SGD("SGD", 702),
    SHP("SHP", 654),
    SLL("SLL", 694),
    SOS("SOS", 706),
    SRD("SRD", 968),
    SSP("SSP", 728),
    STD("STD", 678),
    SYP("SYP", 760),
    SZL("SZL", 748),
    THB("THB", 764),
    TJS("TJS", 972),
    TMT("TMT", 934),
    TND("TND", 788),
    TOP("TOP", 776),
    TRY("TRY", 949),
    TTD("TTD", 780),
    TWD("TWD", 901),
    TZS("TZS", 834),
    UAH("UAH", 980),
    UGX("UGX", 800),
    USD("USD", 840),
    USN("USN", 997),
    USS("USS", 998),
    UYI("UYI", 940),
    UYU("UYU", 858),
    UZS("UZS", 860),
    VEF("VEF", 937),
    VND("VND", 704),
    VUV("VUV", 548),
    WST("WST", 882),
    XAF("XAF", 950),
    XAG("XAG", 961),
    XAU("XAU", 959),
    XBA("XBA", 955),
    XBB("XBB", 956),
    XBC("XBC", 957),
    XBD("XBD", 958),
    XCD("XCD", 951),
    XDR("XDR", 960),
    XFU("XFU", null),
    XOF("XOF", 952),
    XPD("XPD", 964),
    XPF("XPF", 953),
    XPT("XPT", 962),
    XSU("XSU", 994),
    XTS("XTS", 963),
    XUA("XUA", 965),
    XXX("XXX", 999),
    YER("YER", 886),
    ZAR("ZAR", 710),
    ZMW("ZMW", 967),
    ZWD("ZWD", 932);

    private static final Map<String, Currency> ALPHA_MAP;
    static {
        Map<String, Currency> temp = new HashMap<>();
        for (Currency value : values()) {
            temp.put(value.alphaCode, value);
        }
        ALPHA_MAP = Collections.unmodifiableMap(temp);
    }

    private static final Map<Integer, Currency> NUMERIC_MAP;
    static {
        Map<Integer, Currency> temp = new HashMap<>();
        for (Currency value : values()) {
            temp.put(value.numericCode, value);
        }
        NUMERIC_MAP = Collections.unmodifiableMap(temp);
    }

    public final String alphaCode;
    public final Integer numericCode;

    Currency(String alphaCode, Integer numericCode) {
        this.alphaCode = alphaCode;
        this.numericCode = numericCode;
    }

    public static Currency parseAlphaCode(String alphaCode) {
        return ALPHA_MAP.get(alphaCode);
    }

    public static Currency parseNumericCode(Integer numericCode) {
        return NUMERIC_MAP.get(numericCode);
    }
}
