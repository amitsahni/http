package test.retrofit;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by clickapps on 31/8/17.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.button)
    Button getBtn;
    @BindView(R.id.button1)
    Button postBtn;
    @BindView(R.id.button2)
    Button putBtn;
    @BindView(R.id.button3)
    Button deleteBtn;

    MainActivityModel mainActivityModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivityModel.MainActivityModelFactory factory = new MainActivityModel.MainActivityModelFactory(getApplication());
        mainActivityModel = ViewModelProviders.of(this, factory).get(MainActivityModel.class);
        ButterKnife.bind(this);
        getBtn.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        putBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);


        // Get Observer
        mainActivityModel.getGet().observe(this, object -> {
            if (object != null) {
                Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // Post Observer
        mainActivityModel.getPost().observe(this, o -> {
            if (o == null) return;
            Toast.makeText(MainActivity.this, o.toString(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(Object object) {
        Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getLifecycleRegistry().removeObserver(mainActivityModel);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button) {
            // https://s3.amazonaws.com/uifaces/faces/twitter/calebogden/128.jpg
            // http://api.staging.moh.clicksandbox1.com:8080/upload/magazins/8/original/624996-pixelponew.jpg?1505885452
            mainActivityModel.get();
        } else if (id == R.id.button1) {
            mainActivityModel.post();
        } else if (id == R.id.button2) {
            mainActivityModel.put();
        } else if (id == R.id.button3) {
            mainActivityModel.delete();
        } else {
            Log.i(getLocalClassName(), "No clickHandled");
        }


    }
}
