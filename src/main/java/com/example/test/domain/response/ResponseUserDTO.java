package com.example.test.domain.response;

import com.example.test.domain.Company.ResponseCompanyDTO;

public class ResponseUserDTO {
    private String name;
    private String email;
    private String avatar;
    private ResponseCompanyDTO company;

    public String getName() {
        return name;
    }

    public ResponseUserDTO(String name, String email, String avatar) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }

    public ResponseUserDTO() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ResponseCompanyDTO getCompany() {
        return company;
    }

    public void setCompany(ResponseCompanyDTO company) {
        this.company = company;
    }

}
