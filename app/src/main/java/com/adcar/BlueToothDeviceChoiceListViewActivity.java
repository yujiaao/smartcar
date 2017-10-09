package com.adcar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToothDeviceChoiceListViewActivity extends Activity {
    private ListView lv;
    private String[] s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_bluetooth);
        lv = (ListView) findViewById(R.id.list_view);
       // Button btn = (Button) findViewById(R.id.show_btn);
        setTitle("已配对的蓝牙设备");

        final List<BluetoothDevice> devices = this.fillChoice();

        s = getNames(devices).toArray(new String[0]);
        ArrayAdapter adapter = new ArrayAdapter<BluetoothDevice>(this,android.R.layout.simple_list_item_2,devices){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TwoLineListItem row;
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem)inflater.inflate(android.R.layout.simple_list_item_2, null);
                }else{
                    row = (TwoLineListItem)convertView;
                }
                BluetoothDevice data = devices.get(position);
                row.getText1().setTextColor(Color.GRAY);
                row.getText1().setText(data.getName());
                row.getText2().setText(data.getAddress());

                return row;
            }
        };
        lv.setAdapter(adapter);

        //lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, s));
       // lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, s));

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //btn.setOnClickListener(
        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){


                        Toast.makeText(BlueToothDeviceChoiceListViewActivity.this, s[position]  + " is selected", Toast.LENGTH_SHORT).show();

                        Intent data = new Intent(BlueToothDeviceChoiceListViewActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(MainActivity.BLUETOOTH_DEVICE_MAC_ADDRESS, devices.get(position).getAddress());
                        data.putExtras(bundle);
                        BlueToothDeviceChoiceListViewActivity.this.setResult(RESULT_OK, data); //这理有2个参数(int resultCode, Intent intent)


                BlueToothDeviceChoiceListViewActivity.this.finish();
            }
        });
    }

    protected List<String> getNames(List<BluetoothDevice> devices){
        List<String> list = new ArrayList<String>();
        for(BluetoothDevice bt: devices)
            list.add(bt.getName()+ "("+bt.getAddress()+")");
        return list;
    }

    protected List<String> getAddress(List<BluetoothDevice> devices){
        List<String> list = new ArrayList<String>();
        for(BluetoothDevice bt: devices)
            list.add(bt.getAddress());
        return list;
    }


    protected List<BluetoothDevice> fillChoice() {
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        Set localSet = bt.getBondedDevices();
        List<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
        list.addAll(localSet);
        return list;
    }
}