package ladyjek.twiscode.com.ladyjek.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srx.widget.PullCallback;
import com.srx.widget.PullToLoadView;

import java.util.ArrayList;
import java.util.List;

import ladyjek.twiscode.com.ladyjek.Adapter.AdapterHistory;
import ladyjek.twiscode.com.ladyjek.Model.ApplicationData;
import ladyjek.twiscode.com.ladyjek.Model.ModelOrder;
import ladyjek.twiscode.com.ladyjek.R;
import ladyjek.twiscode.com.ladyjek.Utilities.ApplicationManager;
import ladyjek.twiscode.com.ladyjek.Utilities.SocketManager;

public class ActivityHistory extends FragmentActivity {

    private TextView noItems;
    private ImageView btnBack;
    private RecyclerView mRecyclerView;
    private PullToLoadView mPullToLoadView;

    Activity act;
    ApplicationManager applicationManager;
    SocketManager socketManager;

    AdapterHistory adapter;
    List<ModelOrder> data = new ArrayList<>();

    boolean allLoaded = false, isLoading = false;
    int cnt = 1, max = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        act = this;
        applicationManager = new ApplicationManager(act);
        socketManager = ApplicationData.socketManager;


        btnBack = (ImageView) findViewById(R.id.btnBack);
        mPullToLoadView = (PullToLoadView) findViewById(R.id.pullToLoadView);
        noItems = (TextView) findViewById(R.id.noItems);
        mRecyclerView = mPullToLoadView.getRecyclerView();

        adapter = new AdapterHistory(act,data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(act));


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mPullToLoadView.isLoadMoreEnabled(true);



        mPullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                if(cnt >= max){
                    allLoaded = true;
                    mPullToLoadView.setComplete();
                    mPullToLoadView.isLoadMoreEnabled(false);
                }
                else {
                    allLoaded = false;
                    SetData();
                    cnt++;
                    mPullToLoadView.setComplete();
                }

            }

            @Override
            public void onRefresh() {
                mPullToLoadView.setComplete();
            }

            @Override
            public boolean isLoading() {
                return false;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return allLoaded;
            }
        });

        //初始加载
        //mPullToLoadView.initLoad();

        SetData();



        if(data.size() > 0){
            noItems.setVisibility(View.GONE);
            mPullToLoadView.setVisibility(View.VISIBLE);
        }
        else{
            noItems.setVisibility(View.VISIBLE);
            mPullToLoadView.setVisibility(View.GONE);
        }








    }

    void SetData(){
        for(int i=0; i<15;i++){
            ModelOrder o = new ModelOrder();
            o.setName("joko "+i);
            o.setDistance("2 km");
            o.setPrice("Rp. 20.000");
            o.setTo("Jl. Tunjungan");
            data.add(o);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_splash_screen, menu);
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

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }


}
