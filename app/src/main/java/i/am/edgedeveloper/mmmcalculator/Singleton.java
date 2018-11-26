package i.am.edgedeveloper.mmmcalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OPEYEMI OLORUNLEKE on 6/2/2017.
 */

public class Singleton {

    List<Mavro>  mMavroList = new ArrayList<>();
    private static Singleton mSingleton;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (mSingleton == null){
            mSingleton = new Singleton();
        }
        return mSingleton;
    }

    public void addMavros(List<Mavro> mavros) {
        mMavroList.clear();
        mMavroList  = mavros;
    }

    public List<Mavro> getMavroList() {
        return mMavroList;
    }
}
