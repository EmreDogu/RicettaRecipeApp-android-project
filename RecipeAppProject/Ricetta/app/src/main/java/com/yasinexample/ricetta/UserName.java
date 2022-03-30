package com.yasinexample.ricetta;

//Emre Doğu
public class UserName {
    public String userid, firstname, lastname;

    public UserName(){ }

    public UserName (String userid, String firstname, String lastname) {
        this.userid = userid;
        this.firstname = firstname;
        this.lastname = lastname;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
