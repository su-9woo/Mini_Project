package DTO;

public class TOPING {
    public String TOPINGID;
    public String TNAME;
    public int PRICE;
    public int CNUM;

    public String getTOPINGID() {
        return TOPINGID;
    }

    public void setTOPINGID(String TOPINGID) {
        this.TOPINGID = TOPINGID;
    }

    public String getTNAME() {
        return TNAME;
    }

    public void setTNAME(String TNAME) {
        this.TNAME = TNAME;
    }

    public int getPRICE() {
        return PRICE;
    }

    public void setPRICE(int PRICE) {
        this.PRICE = PRICE;
    }

    public int getCNUM() {
        return CNUM;
    }

    public void setCNUM(int CNUM) {
        this.CNUM = CNUM;
    }

    @Override
    public String toString() {
        return "토핑 [ " + "토핑번호 : " + TOPINGID +
                ", 토핑이름 : " + TNAME +
                ", 토핑가격 : " + PRICE +
                ", 가게종류 : " + CNUM +
                " ]";
    }
}
