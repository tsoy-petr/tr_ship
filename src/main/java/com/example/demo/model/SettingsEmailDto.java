package com.example.demo.model;

public class SettingsEmailDto {

    private String smtpServer;
    private int smtpPort;
    private String email;
    private String user;
    private String passwordEmail;
    private String imo;
    private String voyNo;
    private String serverEmail;
    private String folderForSavingReports;
    private String emailCopy1;
    private String emailCopy2;
    private String emailCopy3;

    public SettingsEmailDto(String smtpServer, int smtpPort, String email, String user, String passwordEmail, String imo, String voyNo, String serverEmail, String folderForSavingReports,
                            String emailCopy1, String emailCopy2, String emailCopy3) {
        this.smtpServer = smtpServer;
        this.smtpPort = smtpPort;
        this.email = email;
        this.user = user;
        this.passwordEmail = passwordEmail;
        this.imo = imo;
        this.voyNo = voyNo;
        this.serverEmail = serverEmail;
        this.folderForSavingReports = folderForSavingReports;
        this.emailCopy1 = emailCopy1;
        this.emailCopy2 = emailCopy2;
        this.emailCopy3 = emailCopy3;
    }

    public String getEmailCopy1() {
        return emailCopy1;
    }

    public void setEmailCopy1(String emailCopy1) {
        this.emailCopy1 = emailCopy1;
    }

    public String getEmailCopy2() {
        return emailCopy2;
    }

    public void setEmailCopy2(String emailCopy2) {
        this.emailCopy2 = emailCopy2;
    }

    public String getEmailCopy3() {
        return emailCopy3;
    }

    public void setEmailCopy3(String emailCopy3) {
        this.emailCopy3 = emailCopy3;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordEmail() {
        return passwordEmail;
    }

    public void setPasswordEmail(String passwordEmail) {
        this.passwordEmail = passwordEmail;
    }

    public String getImo() {
        return imo;
    }

    public void setImo(String imo) {
        this.imo = imo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVoyNo() {
        return voyNo;
    }

    public void setVoyNo(String voyNo) {
        this.voyNo = voyNo;
    }

    public String getServerEmail() {
        return serverEmail;
    }

    public void setServerEmail(String serverEmail) {
        this.serverEmail = serverEmail;
    }

    public String getFolderForSavingReports() {
        return folderForSavingReports;
    }

    public void setFolderForSavingReports(String folderForSavingReports) {
        this.folderForSavingReports = folderForSavingReports;
    }

}
