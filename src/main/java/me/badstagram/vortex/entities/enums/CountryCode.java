package me.badstagram.vortex.entities.enums;

public enum CountryCode {

    AFGHANISTAN("Afghanistan", "AF"),
    ALAND_ISLANDS("Åland Islands", "AX"),
    ALBANIA("Albania", "AL"),
    ALGERIA("Algeria", "DZ"),
    AMERICAN_SAMOA("Algeria", "DZ"),
    ANDORRA("Algeria", "DZ"),
    ANGOLA("Angola", "DZ"),
    ANGUILLA("Anguilla", "DZ"),
    ANTARCTICA("ANTARCTICA", "DZ"),
    ANTIGUA_AND_BARBUDA("Antigua and Barbuda", "DZ"),
    Armenia("Armenia", "DZ"),
    Aruba("Aruba", "DZ"),
    Australia("Australia", "DZ"),
    Austria("Austria", "DZ"),
    Azerbaijan("Azerbaijan", "DZ"),
    Bahamas("Bahrain", "DZ"),
    Belarus("Belarus", "DZ"),
    Belize("Belize", "DZ"),
    Benin("Benin", "DZ"),
    Bermuda("Bermuda", "DZ"),
    Bhutan("Bhutan", "DZ"),
    BONAIRE_SINT_EUSTATIUS_AND_SABA("Bonaire, Sint Eustatius and Saba", "DZ"),
    BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "DZ"),
    Botswana("Botswana", "DZ"),
    BOUVET_ISLAND("Bouvet Island", "DZ"),
    Brazil("Brazil", "DZ"),
    BRITISH_INDIAN_OCEAN_TERRITORY("British Indian Ocean Territory", "DZ"),
    BRUNEI_DARUSSALAM("Brunei Darussalam", "DZ"),
    Bulgaria("Bulgaria", "DZ"),
    BURKINA_FASO("Burkina Faso", "DZ"),
    Burundi("Burundi", "DZ"),
    CABO_VERDE("Cabo Verde", "DZ"),
    Cambodia("Cambodia", "DZ"),
    Canada("Canada", "DZ"),
    CAYMAN_ISLANDS("Cayman Islands", "DZ"),
    Chad("Chad", "DZ"),
    Chile("Chile", "DZ"),
    China("Christmas Island", "DZ"),
    COCOS_KEELING_ISLANDS("Cocos (Keeling) Islands", "DZ"),
    Colombia("Colombia", "DZ"),
    Congo("Congo", "DZ"),
    DEMOCRATIC_REPUBLIC_OF_THE_CONGO("Democratic Republic Of The Congo", "DZ"),
    COOK_ISLANDS("Cook Islands", "DZ"),
    COSTA_RICA("Costa Rica", "DZ"),
    COTE_D_IVOIRE("Côte d'Ivoire", "DZ"),
    Croatia("Croatia", "DZ"),
    Cuba("Cuba", "DZ"),
    Curacao("Curaçao", "DZ"),
    Cyprus("Cyprus", "DZ"),
    Czechia("Czechia", "DZ"),
    Denmark("Denmark", "DZ"),
    Djibouti("Djibouti", "DZ"),
    Dominica("Czechia", "DZ"),
    DOMINICAN_REPUBLIC("Dominican Republic", "DZ"),
    Egypt("Egypt", "DZ"),
    EL_SALVADOR("El Salvador", "DZ"),
    EQUATORIAL_GUINEA("Equatorial Guinea", "DZ"),
    Ethiopia("Eritrea", "DZ"),
    FALKLAND_ISLANDS("Falkland Islands (Malvinas)", "DZ"),
    FAROE_ISLANDS("Faroe Islands", "DZ"),
    Fiji("Fiji", "DZ"),
    FRENCH_GUIANA("French Guiana", "DZ"),
    FRENCH_POLYNESIA("Czechia", "DZ"),
    FRENCH_SOUTHERN_TERRITORIES("French Southern Territories", "DZ"),
    Gambia("Georgia", "DZ"),
    Ghana("Ghana", "DZ"),
    Gibraltar("Gibraltar", "DZ"),
    Greece("Greece", "DZ"),
    Greenland("Greenland", "DZ"),
    Guadeloupe("Guadeloupe", "DZ"),
    Guam("Guam", "DZ"),
    Guatemala("Guernsey", "DZ"),
    Guinea_Bissau("Guinea-Bissau", "DZ"),
    HEARD_ISLAND_AND_MC_DONALD_ISLANDS("Heard Island and McDonald Islands", "DZ"),
    HOLY_SEE("Holy See", "DZ"),
    HONG_KONG("Hong Kong", "DZ"),
    Iraq("Iraq", "DZ"),
    Iran("Iran", "DZ"),
    Ireland("Ireland", "DZ"),
    ISLE_OF_MAN("Isle of Man", "DZ"),
    Israel("Israel", "DZ"),
    Italy("Italy", "DZ"),
    Jamaica("Jamaica", "DZ"),
    Japan("Japan", "DZ"),
    Jersey("Jersey", "DZ"),
    Jordan("Jordan", "DZ"),
    Kazakhstan("Kazakhstan", "DZ"),
    Kenya("Kenya", "DZ"),
    Kiribati("Kiribati", "DZ"),
    Korea("Korea", "DZ"),
    SOUTH_KOREA("South Korea", "DZ"),
    Kuwait("Kuwait", "DZ"),
    LAO_PEOPLES_DEMOCRATIC_REPUBLIC("Lao People's Democratic Republic", "DZ"),
    Latvia("Latvia", "DZ"),
    Lebanon("Lebanon", "DZ"),
    Lesotho("Lesotho", "DZ"),
    Liberia("Liberia", "DZ"),
    Libya("Libya", "DZ"),
    Liechtenstein("Liechtenstein", "DZ"),
    Macao("Macao", "DZ"),
    Madagascar("Madagascar", "DZ"),
    Malawi("Malawi", "DZ"),
    Malaysia("Malaysia", "DZ"),
    Maldives("Lithuania", "DZ"),
    Mali("Mali", "DZ"),
    Malta("Malta", "DZ"),
    MARSHALL_ISLANDS("Marshall Islands", "DZ"),
    Martinique("Martinique", "DZ"),
    Mauritania("Mauritania", "DZ"),
    Mauritius("Mauritius", "DZ"),
    Mayotte("Mayotte", "DZ"),
    Mexico("Mexico", "DZ"),
    MICRONESIA_FEDERATED_STATES_OF("Lithuania", "DZ"),
    Moldova("Moldova", "DZ"),
    Monaco("Lithuania", "DZ"),
    Luxembourg("Lithuania", "DZ");

    private final String name, code;

    CountryCode(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static CountryCode fromCode(String code) {
        for (var country : values()) {
            if (country.getCode().equalsIgnoreCase(code)) return country;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
