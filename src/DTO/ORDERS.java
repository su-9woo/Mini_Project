package DTO;

public class ORDERS {
    public String ORDERID;
    public int TOTALPRICE;
    public String MID;
    public int FNUM;

    public String getORDERID() {
        return ORDERID;
    }

    public void setORDERID(String ORDERID) {
        this.ORDERID = ORDERID;
    }

    public int getTOTALPRICE() {return TOTALPRICE;}

    public void setTOTALPRICE(int TOTALPRICE) {
        this.TOTALPRICE = TOTALPRICE;
    }

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public int getFNUM() {
        return FNUM;
    }

    public void setFNUM(int FNUM) {
        this.FNUM = FNUM;
    }

    @Override
    public String toString() {
        return "주문(장바구니) [ " + " 금액 : " + TOTALPRICE +
                ", 회원아이디 : " + MID +
                " ]";
    }
}
