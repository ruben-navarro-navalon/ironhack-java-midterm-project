package com.ironhack.midtermProject.enums;

public enum State {
    ALAVA("Álava"),
    ALBACETE("Albacete"),
    ALICANTE("Alicante"),
    ALMERIA("Almería"),
    ASTURIAS("Asturias"),
    AVILA("Ávila"),
    BADAJOZ("Badajoz"),
    BALEARES("Baleares"),
    BARCELONA("Barcelona"),
    BURGOS("Burgos"),
    CACERES("Cáceres"),
    CADIZ("Cádiz"),
    CANTABRIA("Cantabria"),
    CASTELLON("Castellón"),
    CEUTA("Ceuta"),
    CIUDAD_REAL("Ciudad Real"),
    CORDOBA("Córdoba"),
    CUENCA("Cuenca"),
    GERONA("Gerona"),
    GRANADA("Granada"),
    GUADALAJARA("Guadalajara"),
    GUIPUZCOA("Guipuzcoa"),
    HUELVA("Huelva"),
    HUESCA("Huesca"),
    JAEN("Jaén"),
    LA_CORUNA("La Coruña"),
    LA_RIOJA("La Rioja"),
    LAS_PALMAS("Las Palmas"),
    LEON("León"),
    LERIDA("Lérida"),
    LUGO("Lugo"),
    MADRID("Madrid"),
    MALAGA("Málaga"),
    MELILLA("Melilla"),
    MURCIA("Murcia"),
    NAVARRA("Navarra"),
    ORENSE("Orense"),
    PALENCIA("Palencia"),
    PONTEVEDRA("Pontevedra"),
    SALAMANCA("Salamanca"),
    TENERIFE("Santa Cruz de Tenerife"),
    SEGOVIA("Segovia"),
    SEVILLA("Sevilla"),
    SORIA("Soria"),
    TARRAGONA("Tarragona"),
    TERUEL("Teruel"),
    TOLEDO("Toledo"),
    VALENCIA("Valencia"),
    VALLADOLID("Valladolid"),
    VIZCAYA("Vizcaya"),
    ZAMORA("Zamora"),
    ZARAGOZA("Zaragoza");

    private final String NAME;

    State(String name) {
        this.NAME = name;
    }

    public String getNAME() {
        return NAME;
    }
}
