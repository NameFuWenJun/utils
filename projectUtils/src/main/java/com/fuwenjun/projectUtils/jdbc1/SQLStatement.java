package com.fuwenjun.projectUtils.jdbc1;

public class SQLStatement {
    private String sql;
    private Object [] params=null;
    
    public String getSql() {
        return sql;
    }

    public Object[] getParams() {
        return params;
    }
    //带参
    public SQLStatement(String sql, Object[] params) {
        super();
        this.sql = sql;
        this.params = params;
    }
    //不带参
    public SQLStatement(String sql) {
        super();
        this.sql = sql;
    }
    
    
    
    
}
