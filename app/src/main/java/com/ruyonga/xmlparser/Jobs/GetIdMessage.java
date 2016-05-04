package com.ruyonga.xmlparser.Jobs;

import android.content.Context;
import android.util.Log;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import com.ruyonga.xmlparser.utils.Globals;
import com.ruyonga.xmlparser.utils.PostMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.greenrobot.event.EventBus;

/**
 * Created by ruyonga on 04/05/16.
 */
public class GetIdMessage extends Job {

    Context context;
    String Id;
    boolean log = true;


    public GetIdMessage(Context context, String id) {
        super(new Params(5).requireNetwork());
        this.context = context;
        this.Id = id;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        log("call messages====== ");

        //GET SERVER CALL URL
        String url = Globals.MESSAGE + Id;
        log("gc134 url:" + url);

        try {
            URL myurl = new URL(url);
            DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Download the XML file
            Document doc = db.parse(new InputSource(myurl.openStream()));
            doc.getDocumentElement().normalize();
            // Locate the Tag Name
            // nodelist = doc.getElementsByTagName("item");
            NodeList nodelists = doc.getElementsByTagName("value");

            PostMessage ps = new PostMessage();
            for (int i = 0; i < nodelists.getLength(); i++) {
                Node node = nodelists.item(i);
                log("**items==" + nodelists.item(i).getTextContent());
               ps.message = nodelists.item(i).getTextContent();


            }

            EventBus.getDefault().post(ps);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancel() {
        log("call cancelled");

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {

        return false;
    }

    void log(String msg) {
        Log.v(this.getClass().getSimpleName(), msg);
    }
}
