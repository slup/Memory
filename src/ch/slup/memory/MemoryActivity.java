package ch.slup.memory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MemoryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memory);
        
        /*
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new MemoryAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(MemoryActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        */
    }
}