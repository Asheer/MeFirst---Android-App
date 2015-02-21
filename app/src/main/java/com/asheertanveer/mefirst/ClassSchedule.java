package com.asheertanveer.mefirst;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.support.v4.app.Fragment;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.Fragment;

public class ClassSchedule extends FragmentActivity {
    private FragmentTabHost mTabHost;

    private String username, password;
   // ArrayAdapter scheduleAdapter;
   MyCustomBaseAdapter scheduleAdapter;
    public ArrayList<String> classes = new ArrayList<String>();
    public ArrayList<String> classDescription = new ArrayList<String>();
    public ArrayList<String> units = new ArrayList<String>();

    ArrayList<SearchResults> searchResults = new ArrayList<SearchResults>();
    ArrayList<SearchResults> results = new ArrayList<SearchResults>();
    public int i;
    SearchResults sr;

    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        //searchResults = GetSearchResults();
        final ListView lv = (ListView) findViewById(R.id.scheduleList);
        scheduleAdapter = new MyCustomBaseAdapter(this,results);
        lv.setAdapter(scheduleAdapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

      //  scheduleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,schedule);
      //  String name = "Asheer";
        //schedule.add(name)    ;
        initWeb();
    }

    public void initWeb() {
        webview = new WebView(this);
        webview.setVisibility(View.GONE);
        webview.removeAllViews();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        //   webview.getSettings().setBlockNetworkImage(true);
      //  setContentView(webview);

        webview.loadUrl("https://home.cunyfirst.cuny.edu/oam/Portal_Login1.html");
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                login();
                String currentUrl = webview.getOriginalUrl();
                System.out.println("Page " + currentUrl);
                if(currentUrl.equals("https://home.cunyfirst.cuny.edu/psp/cnyepprd/EMPLOYEE/EMPL/h/?tab=DEFAULT")) {
                    webview.loadUrl("https://hrsa.cunyfirst.cuny.edu/psc/cnyhcprd/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL?PORTALPARAM_PTCNAV=HC_SSR_SSENRL_GRADE_GBL&EOPP.SCNode=HRMS&EOPP.SCPortal=EMPLOYEE&EOPP.SCName=HCCC_ENROLLMENT&EOPP.SCLabel=Enrollment&EOPP.SCPTfname=HCCC_ENROLLMENT&FolderPath=PORTAL_ROOT_OBJECT.CO_EMPLOYEE_SELF_SERVICE.HCCC_ENROLLMENT.HC_SSR_SSENRL_GRADE_GBL&IsFolder=false&PortalActualURL=https%3a%2f%2fhrsa.cunyfirst.cuny.edu%2fpsc%2fcnyhcprd%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL&PortalContentURL=https%3a%2f%2fhrsa.cunyfirst.cuny.edu%2fpsc%2fcnyhcprd%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL&PortalContentProvider=HRMS&PortalCRefLabel=View%20My%20Grades&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fhrsa.cunyfirst.cuny.edu%2fpsp%2fcnyhcprd%2f&PortalURI=https%3a%2f%2fhrsa.cunyfirst.cuny.edu%2fpsc%2fcnyhcprd%2f&PortalHostNode=HRMS&NoCrumbs=yes&PortalKeyStruct=yes");
                }

                if(currentUrl.equals("https://home.cunyfirst.cuny.edu/access/dummy.cgi"))
                    webview.loadUrl("https://hrsa.cunyfirst.cuny.edu/psc/cnyhcprd/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES_2.SSR_SSENRL_CART.GBL?Page=SSR_SSENRL_CART&Action=A");
                if(currentUrl.equals("https://hrsa.cunyfirst.cuny.edu/psc/cnyhcprd/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES_2.SSR_SSENRL_CART.GBL?Page=SSR_SSENRL_CART&Action=A")) {
                    getSchedule();
                }

                if(currentUrl.equals("https://home.cunyfirst.cuny.edu/oam/InvalidLogin.html")) {
                    if (Build.VERSION.SDK_INT >= 11) {
                        recreate();
                    } else {
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            }
        });
    }

    public void getSchedule() {


        for (i = 0; i < 5; i++) {

            webview.evaluateJavascript(String.format("javascript:document.getElementById('win0divE_CLASS_NAME$%d').textContent;", i), new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    JsonReader reader = new JsonReader(new StringReader(value));
                    // Must set lenient to parse single values
                    reader.setLenient(true);
                    try {
                        if (reader.peek() != JsonToken.NULL) {
                            if (reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if (msg != null) {
                                    sr = new SearchResults();
                                    //  classes.add(msg);
                                    //  System.out.println("SIZE YO" + classes.size());
                                    msg = msg.substring(0, msg.length()-1);
                                    sr.setName(msg);

                                    /*)
                                    sr.setCityState("San Francisco, CA");
                                    sr.setPhone("415-555-1234");
                                    results.add(sr);
                                    scheduleAdapter.notifyDataSetChanged();
                                    */
                                    //System.out.println("Size: " + sr.getName());
                                    System.out.println("Class:" + msg);
                                }
                            }
                        }
                    } catch (IOException e) {
                    }
                }

            });

            webview.evaluateJavascript(String.format("javascript:document.getElementById('win0divE_CLASS_DESCR$%d').textContent;", i), new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    JsonReader reader = new JsonReader(new StringReader(value));
                    // Must set lenient to parse single values
                    reader.setLenient(true);
                    try {
                        if (reader.peek() != JsonToken.NULL) {
                            if (reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if (msg != null) {
                                    System.out.println("Class" + msg);
                                    //classDescription.add(msg);
                                    msg = msg.substring(0, msg.length()-1);
                                    sr.setCitySta   te(msg);
                                       /*
                                        sr = new SearchReashesults();
                                        sr.setName(msg);
                                        sr.setCityState("San Francisco, CA");
                                        sr.setPhone("415-555-1234");
                                        results.add(sr);
                                        scheduleAdapter.notifyDataSetChanged();
                                        System.out.println("Size: " + sr.getName()); */
                                    // System.out.println("see " + msg);
                                }
                            }
                        }
                    } catch (IOException e) {
                    }

                }

            });


          //  if (results != null) {
            webview.evaluateJavascript(String.format("javascript:document.getElementById('win0divSTDNT_ENRL_SSVW_UNT_TAKEN$%d').textContent;", i), new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    JsonReader reader = new JsonReader(new StringReader(value));
                    // Must set lenient to parse single values
                    reader.setLenient(true);
                    try {
                        if (reader.peek() != JsonToken.NULL) {
                            if (reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if (msg != null) {
                                    System.out.println("Class" + msg);
                                    //classDescription.add(msg);
                                    msg = msg.substring(0, msg.length()-1);
                                    sr.setPhone(msg);
                                    results.add(sr);
                                    scheduleAdapter.notifyDataSetChanged();
                                       /*
                                        sr = new SearchReashesults();
                                        sr.setName(msg);
                                        sr.setCityState("San Francisco, CA");
                                        sr.setPhone("415-555-1234");
                                        results.add(sr);
                                        scheduleAdapter.notifyDataSetChanged();
                                        System.out.println("Size: " + sr.getName()); */
                                    // System.out.println("see " + msg);
                                }
                            }
                        }
                    } catch (IOException e) {
                    }

                }

            });
       }
    }

    public void updateList() {

        System.out.println("Updating.." + classes.size());

        for(int i = 0; i < classes.size(); i++) {
            String classNames = classes.get(i);
            String classDes = classDescription.get(i);
            sr = new SearchResults();
            sr.setName(classNames);
            sr.setCityState(classDes);
            sr.setPhone("415-555-1234");
            results.add(sr);
            scheduleAdapter.notifyDataSetChanged();
        }
    }
    public void login() {
        webview.evaluateJavascript(String.format("javascript:document.getElementById('cf-login').value = '%s';",username),null);
        webview.evaluateJavascript(String.format("javascript:document.getElementById('password').value='%s';",password), null);
        webview.evaluateJavascript("javascript:document.forms['loginform'].submit();", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}