package com.ruyonga.xmlparser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.path.android.jobqueue.JobManager;
import com.ruyonga.xmlparser.Jobs.GetIdMessage;
import com.ruyonga.xmlparser.utils.Globals;
import com.ruyonga.xmlparser.utils.PostMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class MainActivity extends AppCompatActivity {

    ListView ll;
    boolean log = true;
    ProgressDialog pDialog;
    JobManager jobManager;
    MessageAdapter messageAdapter;
    List<String> ms = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        jobManager = new JobManager(getApplicationContext());
        try {

            // parse our XML
            new parseXmlAsync().execute(Globals.ID_URL);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Un comment this to delete all locally saved messages
         */
        //  SaveMessage.deleteAll(SaveMessage.class);

        //assign the listview
        ll = (ListView) findViewById(R.id.messagelist);
        /**
         * Load the previously saved messages as we fetch new ones
         */
        loadOrm();

        MultiDex.install(this);

    }

    /**
     * Every time a message is retrived and event is published and it will be resceieved by this
     * activity and call the loadOrm method, which will read a new message into the listview
     *
     * @param mg
     */
    @Subscribe
    public void onEvent(final PostMessage mg) {
        log("+++++++++: ReloadHomeActivity even received" + mg.message);
        ms.add(mg.message);
        //RELOAD Adapter
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadOrm();
                log("how main" + ms.size());

            }
        });


    }

    /**
     * register for the event to be publish
     */
    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    /**
     * When the activity is closed nuregister
     */
    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Call an asyn task to fetch the id,
     * loop through the IDs and for each Id, start a job that will run in the background
     * After the job is complete,kick off an event bus..to notify the subscribing class[main activity]
     * to refresh and display the message in a listview.
     */

    private class parseXmlAsync extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.fetching));
            pDialog.setIndeterminate(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... Url) {

//

            try {
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // Download the XML file
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                // Locate the Tag Name
                // nodelist = doc.getElementsByTagName("item");
                NodeList nodelists = doc.getElementsByTagName("item");
                log("nodelist====" + nodelists.getLength());
                for (int i = 0; i < nodelists.getLength(); i++) {
                    Node node = nodelists.item(i);

                    log("**items==" + nodelists.item(i).getTextContent());
                    GetIdMessage getIdMessage = new GetIdMessage(getApplicationContext(), nodelists.item(i).getTextContent());
                    jobManager.addJobInBackground(getIdMessage);

                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String item) {
            // your do stuff after parsing the XML
            pDialog.dismiss();
//            Toast.makeText(getApplication(), getString(R.string.found) + nodelists.getLength() + getString(R.string.ids)
//                    , Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Read locally saved messages
     */
    public void loadOrm() {

        if (ms.size() > 0) {
            messageAdapter = new MessageAdapter(ms, getApplicationContext());
            ll.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();
        } else {
            // Toast.makeText(getApplicationContext(), R.string.no_mesa, Toast.LENGTH_LONG).show();
        }
    }

    void log(String msg) {
        Log.v(this.getClass().getSimpleName(), msg);
    }
}
