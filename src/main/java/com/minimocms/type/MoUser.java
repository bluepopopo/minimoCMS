package com.minimocms.type;

import com.minimocms.utils.IdUtil;

import java.io.Serializable;

public class MoUser implements Serializable {
    String username;
    String passHash;
    String _id;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public void setId(String id) {
        this._id = id;
    }

    public MoUser(){}

    public MoUser(String username, String passHash) {
        this.username = username;
        this.passHash = passHash;
        _id = IdUtil.createId();
    }

    public String getUsername() {
        return username;
    }

    public String id(){
        return _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPassHash() {
        return passHash;
    }

}
