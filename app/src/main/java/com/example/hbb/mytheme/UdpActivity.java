package com.example.hbb.mytheme;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hbb.mytheme.core.GetIP;

public class UdpActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button btnGetIp;
    private TextView tvIp;
    private TextView tvNetIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        initView();
    }

    private void initView() {
        btnGetIp = (Button) findViewById(R.id.getIp_btn);
        btnGetIp.setOnClickListener(this);
        tvIp = (TextView) findViewById(R.id.tv_ip);
        tvNetIp = (TextView) findViewById(R.id.tv_net_ip);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.getIp_btn:
                new Thread(runnable).start();
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String ip = bundle.getString("ip");
            if(!ip.isEmpty())
                tvIp.setText("内网IP："+ip);
            String netip = bundle.getString("netip");
            if(netip != null && !netip.isEmpty())
                tvNetIp.setText("外网IP："+netip);
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            Bundle data = new Bundle();
            String ip = GetIP.getHostIP();
            String netip = GetIP.GetNetIp();
            Log.d("tag",ip);
            Log.d("tag",netip);
            data.putString("ip",ip);
            data.putString("netip",netip);
            message.setData(data);
            handler.sendMessage(message);
        }
    };
}
