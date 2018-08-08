package com.artproficiencyapp.model;

import java.io.Serializable;

/**
 * Created by admin on 08-Jul-17.
 */

public class CountryCodeModel implements Serializable {

    String _mCountryCode, _mCountryName;

    public String get_mCountryCode() {
        return _mCountryCode;
    }

    public void set_mCountryCode(String _mCountryCode) {
        this._mCountryCode = _mCountryCode;
    }

    public String get_mCountryName() {
        return _mCountryName;
    }

    public void set_mCountryName(String _mCountryName) {
        this._mCountryName = _mCountryName;
    }
}
