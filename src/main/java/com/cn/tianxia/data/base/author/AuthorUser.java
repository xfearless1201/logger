package com.cn.tianxia.data.base.author;

import java.io.Serializable;
import java.util.Set;

public class AuthorUser implements Serializable {

    private static final long serialVersionUID = 7523551439779863422L;

    private Long uid;

    private String usernmae;

    private String name;

    private Set<String> perms;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsernmae() {
        return usernmae;
    }

    public void setUsernmae(String usernmae) {
        this.usernmae = usernmae;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getPerms() {
        return perms;
    }

    public void setPerms(Set<String> perms) {
        this.perms = perms;
    }

    @Override
    public String toString() {
        return "AuthorUser [uid=" + uid + ", usernmae=" + usernmae + ", name=" + name + ", perms=" + perms + "]";
    }

}
