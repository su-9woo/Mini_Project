package DTO;

public class MEMBERS {
    public String MID;
    public String MPW;
    public String MNAME;
    public String PHONE;
    public String ADDR;
    public int BALANCE;

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getMPW() {
        return MPW;
    }

    public void setMPW(String MPW) {
        this.MPW = MPW;
    }

    public String getMNAME() {
        return MNAME;
    }

    public void setMNAME(String MNAME) {
        this.MNAME = MNAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public int getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(int BALANCE) {
        this.BALANCE = BALANCE;
    }

    @Override
    public String toString() {
        return "회원정보[" +
                "회원아이디='" + MID +
                ", 회원비밀번호='" + MPW +
                ", 회원이름='" + MNAME +
                ", 회원전화번호='" + PHONE +
                ", 회원주소='" + ADDR +
                ", 회원잔액=" + BALANCE +
                ']';
    }
}
