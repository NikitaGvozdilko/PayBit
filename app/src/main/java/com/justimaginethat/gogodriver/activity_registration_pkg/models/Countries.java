package com.justimaginethat.gogodriver.activity_registration_pkg.models;

import java.util.List;

public class Countries {

    public List<Country> countries;

    public class Country{
        public String code;
        public String name;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Country> getCountries() {
        return countries;
    }
}
