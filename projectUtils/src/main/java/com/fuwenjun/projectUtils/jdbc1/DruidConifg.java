package com.fuwenjun.projectUtils.jdbc1;

import com.fuwenjun.projectUtils.jdbc.DataBase;

/**
 * Druid的配置类
 * @author fuwenjun01
 *
 */
public class DruidConifg {
    //数据库类型
    private DataBase database_type;
    //包括ip,端口以及数据库
    private String jdbc_url;
    
    private String userName;
    
    private String password;
    

    public String getJdbc_url() {
        return jdbc_url;
    }

    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataBase getDatabase_type() {
        return database_type;
    }

    public void setDatabase_type(DataBase database_type) {
        this.database_type = database_type;
    }

    public DruidConifg(DataBase database_type, String jdbc_url, String userName, String password) {
        super();
        this.database_type = database_type;
        this.jdbc_url = jdbc_url;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((database_type == null) ? 0 : database_type.hashCode());
        result = prime * result + ((jdbc_url == null) ? 0 : jdbc_url.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DruidConifg other = (DruidConifg) obj;
        if (database_type != other.database_type)
            return false;
        if (jdbc_url == null) {
            if (other.jdbc_url != null)
                return false;
        } else if (!jdbc_url.equals(other.jdbc_url))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

}
