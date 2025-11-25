package in.gov.irms.train.enums;

public enum TrainType {

    OTHERS,
    GOODS;

    public static enum SUPERFAST {
        RAJDHANI,
        SHATABDI,
        DURONTO,
        VANDE_BHARAT,

    }

    public static enum EXPRESS {
        SUPERFAST_EXPRESS,
        MAIL_EXPRESS,
        GARIB_RATH,
        SAMPARK_KRANTI,
        HUMSAFAR,
        TEJAS,
        JAN_SHATABDI,
        DECCAN_QUEEN

    }

    public static enum LOCAL {
        MEMU,
        PASSENGER,
        INTERCITY
    }

    public static enum LUXURY {
        MAHARAJA_EXP,
        PALACE_ON_WHEELS,
        DECCAN_ODYSSEY,
        GOLDEN_CHARIOT

    }

    public static enum SPECIAL {
        SUVIDHA_EXP,
        TOURIST_TRAIN
    }
}
