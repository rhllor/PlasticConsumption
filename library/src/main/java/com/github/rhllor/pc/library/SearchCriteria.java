package com.github.rhllor.pc.library;

public class SearchCriteria {
    private String _key;
    private Object _value;
    private TypeSearch _typeSearch;

    public static enum TypeSearch {
        equal,
        greaterOrEqual,
        lessOrEqual
    }

    public SearchCriteria(String key, Object value){
        this._key = key;
        this._value = value;
        this._typeSearch = TypeSearch.equal;
    }

    public SearchCriteria(String key, Object value, TypeSearch type){
        this._key = key;
        this._value = value;
        this._typeSearch = type;
    }

    public String getKey() {
        return this._key;
    }

    public void setKey(String key) {
        this._key = key;
    }

    public Object getValue() {
        return this._value;
    }

    public void setValue(Object value) {
        this._value = value;
    }

    public TypeSearch getTypeSearch() {
        return this._typeSearch;
    }

    public void setTypeSearch(TypeSearch type) {
        this._typeSearch = type;
    }
}
