package uk.ac.nottingham.chen_chen.fractalviewer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import uk.ac.nottingham.chen_chen.fractalviewer.R;

/**
 * Main Activity
 * @Author: Chen Chen
 * First Android project created on April 2016 for Web Based Computing (H63JAV), University of Nottingham
 */

public class MainActivity extends Activity {
    FractalView fractalcanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fractalcanvas = (FractalView) findViewById(R.id.fractalCanvas);
        addListenerOnButton();
        addListenerOnShareButton();
    }
    // share image here
    public void addListenerOnShareButton(){
        Button btn_share = (Button) findViewById(R.id.Share_button);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Share_button:
                        // forward intent here
                        System.out.println("Share button pressed");
                        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), fractalcanvas.image, "title", null);
                        Uri bitmapURI = Uri.parse(bitmapPath);
                        System.out.println(bitmapURI);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, fractalcanvas.image);
                        startActivity(Intent.createChooser(intent, "Share"));
                        intent.putExtra(Intent.EXTRA_STREAM, bitmapURI);

                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void addListenerOnButton() {
                Button btn = (Button) findViewById(R.id.RST_button);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.RST_button:
                                System.out.println("reset pressed");
                                fractalcanvas.reset();
                                break;
                            default:
                                break;
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
}
