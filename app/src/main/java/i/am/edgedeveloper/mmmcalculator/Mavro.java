package i.am.edgedeveloper.mmmcalculator;


import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by OPEYEMI OLORUNLEKE on 6/7/2017.
 */

public class Mavro {

    private DecimalFormat formatter;

    private String date;
    private long money;
    private NumberToWords mToWords;
    private Date endTime;
    private Date beginTime;

    public Mavro() {
        formatter = new DecimalFormat("#,###");
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return formatter.format(money);
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getMoneyWords() {
        mToWords = new NumberToWords();
        String words = mToWords.convert((int) money);
        if (money > Integer.MAX_VALUE)
            return "";
        return words;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
}