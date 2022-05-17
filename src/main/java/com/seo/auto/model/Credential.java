package main.java.com.seo.auto.model;

public class Credential {
    private String domain;
    private String usrName;
    private String pwd;

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Credential(String domain, String usrName, String pwd) {
        this.domain = domain;
        this.usrName = usrName;
        this.pwd = pwd;
    }

    public Credential() {}
}
