package DTO;

public class STORES {
    public int SNUM;
    public String SNAME;
    public String SLOCATION;
    public int CNUM;

    public int getSNUM() {
        return SNUM;
    }

    public void setSNUM(int SNUM) {
        this.SNUM = SNUM;
    }

    public String getSNAME() {
        return SNAME;
    }

    public void setSNAME(String SNAME) {
        this.SNAME = SNAME;
    }

    public String getSLOCATION() {
        return SLOCATION;
    }

    public void setSLOCATION(String SLOCATION) {
        this.SLOCATION = SLOCATION;
    }

    public int getCNUM() {
        return CNUM;
    }

    public void setCNUM(int CNUM) {
        this.CNUM = CNUM;
    }

    @Override
    public String toString() {
        return "가게 [ " + "가게번호 : " + SNUM +
                ", 가게이름 : " + SNAME +
                ", 가게주소 : " + SLOCATION +
                ", 가게종류 : " + CNUM +
                " ]";
    }
}
