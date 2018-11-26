package i.am.edgedeveloper.mmmcalculator;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import i.am.edgedeveloper.mmmcalculator.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final String INVESTMENT_KEY = "investment_key";
    private final String TENURE_KEY = "tenure_key";
    private final double bitcoinInterestRate = 0.50;
    private final double cashInterestRate = 0.30;
    private DateFormat dateFormat;
    private int myNumber = 0;
    private AppCompatRadioButton cash, bitcoin;
    private InterstitialAd mInterstitialAd;
    private Calendar mCalendar;
    private EditText investmentEditTxt, tenureEditTxt;
    private MavroAdapter adapter;
    private ActivityMainBinding binding;
    private TinyDB mDB_Investment, mDB_months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (binding.toolbar);
        setSupportActionBar(toolbar);

        mDB_Investment = new TinyDB(this);
        mDB_months = new TinyDB(this);

        cash = binding.cash;
        bitcoin = binding.bitcoin;
        tenureEditTxt = binding.tenure;
        investmentEditTxt = binding.investment;

        investmentEditTxt.setText(mDB_Investment.getString(INVESTMENT_KEY));
        tenureEditTxt.setText(mDB_months.getString(TENURE_KEY));

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new MavroAdapter(this);
        recyclerView.setAdapter(adapter);
        dateFormat = new SimpleDateFormat("MMM d yyyy");

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9297690518647609~8889402571");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        investmentEditTxt.addTextChangedListener(new NumberTextWatcherForThousand(investmentEditTxt));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9297690518647609/7273068576");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();

        adapter.addMavros(Singleton.getInstance().getMavroList());
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void calculate(View view) {

        if (TextUtils.isEmpty(tenureEditTxt.getText().toString()) || TextUtils.isEmpty(investmentEditTxt.getText().toString())) {
            Snackbar.make(binding.getRoot(), "" + getResources().getString(R.string.input_details), Snackbar.LENGTH_LONG).show();
            return;
        }

        mCalendar = Calendar.getInstance();
        adapter.mMavros.clear();
        adapter.notifyDataSetChanged();
        String monthsString = tenureEditTxt.getText().toString();
        mDB_months.putString(TENURE_KEY, monthsString);
        int months = Integer.parseInt(monthsString);

        double iRate;
        Mavro mavro;
        List<Mavro> mavros = new ArrayList<>();
        String moneyStringWithComma = investmentEditTxt.getText().toString();
        String moneyStringWithoutComma = moneyStringWithComma.replace(",", "");
        mDB_Investment.putString(INVESTMENT_KEY, moneyStringWithComma);
        double mumuMoney = Integer.parseInt(moneyStringWithoutComma);

        int residual = myNumber % 5;
        if (mInterstitialAd.isLoaded() && residual == 4) {
            mInterstitialAd.show();
        } else {
            if (cash.isChecked()) {
                iRate = cashInterestRate;
            } else {
                iRate = bitcoinInterestRate;
            }

            for (int i = 0; i < months; i++) {
                double profit = mumuMoney * iRate;
                mumuMoney += profit;
                mavro = new Mavro();

                mavro.setMoney((long) mumuMoney);

                mavro.setBeginTime(mCalendar.getTime());

                addMonth();

                String dateString = dateFormat.format(mCalendar.getTime());
                mavro.setDate(dateString);
                mavro.setEndTime(mCalendar.getTime());

                mavros.add(mavro);
            }
            Singleton.getInstance().addMavros(mavros);
            adapter.addMavros(mavros);
            requestNewInterstitial();

        }
        myNumber++;
    }

    public void addMonth() {
        mCalendar.add(Calendar.DAY_OF_MONTH, 30);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.facts:

                int residual = myNumber % 2;
                if (mInterstitialAd.isLoaded() && residual == 1) {
                    mInterstitialAd.show();
                } else {
                    startActivity(new Intent(this, AboutActivity.class));
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class NumberTextWatcherForThousand implements TextWatcher {

        EditText editText;


        public NumberTextWatcherForThousand(EditText editText) {
            this.editText = editText;


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                editText.removeTextChangedListener(this);
                String value = editText.getText().toString();


                if (value != null && !value.equals("")) {

                    if (value.startsWith(".")) {
                        editText.setText("0.");
                    }
                    if (value.startsWith("0") && !value.startsWith("0.")) {
                        editText.setText("");

                    }


                    String str = editText.getText().toString().replaceAll(",", "");
                    if (!value.equals(""))
                        editText.setText(getDecimalFormattedString(str));
                    editText.setSelection(editText.getText().toString().length());
                }
                editText.addTextChangedListener(this);
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                editText.addTextChangedListener(this);
            }
        }

        public String getDecimalFormattedString(String value) {
            StringTokenizer lst = new StringTokenizer(value, ".");
            String str1 = value;
            String str2 = "";
            if (lst.countTokens() > 1) {
                str1 = lst.nextToken();
                str2 = lst.nextToken();
            }
            String str3 = "";
            int i = 0;
            int j = -1 + str1.length();
            if (str1.charAt(-1 + str1.length()) == '.') {
                j--;
                str3 = ".";
            }
            for (int k = j; ; k--) {
                if (k < 0) {
                    if (str2.length() > 0)
                        str3 = str3 + "." + str2;
                    return str3;
                }
                if (i == 3) {
                    str3 = "," + str3;
                    i = 0;
                }
                str3 = str1.charAt(k) + str3;
                i++;
            }

        }

        public String trimCommaOfString(String string) {
            if (string.contains(",")) {
                return string.replace(",", "");
            } else {
                return string;
            }

        }
    }
}