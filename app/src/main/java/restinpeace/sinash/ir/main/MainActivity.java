package restinpeace.sinash.ir.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {

    Button btnConnect;
    EditText etAddress;
    String serverAddress;
    AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().setTitle("RESTInPeace");

        btnConnect = (Button) findViewById(R.id.main_btn_connect);
        etAddress = (EditText) findViewById(R.id.main_et_address);




        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddress.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter server address and try again.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //traditional way

                    //must contain protocol, else -> exception occurs
                    serverAddress = etAddress.getText().toString();
//                    new CallWebService().execute(serverAddress);

                    //loopj library
                    client.get(serverAddress, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            String result = null;
                            try {
                                result = new String(responseBody, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });



                }
            }
        });


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

    private class CallWebService extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {

            String urlString = params[0]; //called URL

            String resultToDisplay = "";

            InputStream in = null;

            //HTTP GET

            try{
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());

                resultToDisplay = IOUtils.toString(in);

            }
            catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }

//            Log.e("get", resultToDisplay);
            return resultToDisplay;
        }

        @Override
        protected void onPostExecute(String result) {

//            Log.e("GET", result.toString());
//            etAddress.setText(result);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
}
