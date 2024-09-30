package DTO;

public class FOOD {
    public int FNUM;
    public String FOODNAME;
    public int FOODPRICE;
    public int SNUM;

    public int getFNUM() {
        return FNUM;
    }

    public void setFNUM(int FNUM) {
        this.FNUM = FNUM;
    }

    public String getFOODNAME() {
        return FOODNAME;
    }

    public void setFOODNAME(String FOODNAME) {
        this.FOODNAME = FOODNAME;
    }

    public int getFOODPRICE() {
        return FOODPRICE;
    }

    public void setFOODPRICE(int FOODPRICE) {
        this.FOODPRICE = FOODPRICE;
    }

    public int getSNUM() {
        return SNUM;
    }

    public void setSNUM(int SNUM) {
        this.SNUM = SNUM;
    }

    @Override
    public String toString() {
        return "음식[" +
                "음식번호=" + FNUM +
                ", 음식이름='" + FOODNAME +
                ", 음식가격=" + FOODPRICE +
                ", 가게고유번호=" + SNUM +
                ']';
    }
}

