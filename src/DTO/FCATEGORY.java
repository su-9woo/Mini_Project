package DTO;

public class FCATEGORY {
    public int CNUM;
    public String CNAME;

    public int getCNUM() {
        return CNUM;
    }

    public void setCNUM(int CNUM) {
        this.CNUM = CNUM;
    }

    public String getCNAME() {
        return CNAME;
    }

    public void setCNAME(String CNAME) {
        this.CNAME = CNAME;
    }

    @Override
    public String toString() {
        return "카테고리{" +
                "카테고리번호=" + CNUM +
                ", 음식종류='" + CNAME + '\'' +
                '}';
    }
}

