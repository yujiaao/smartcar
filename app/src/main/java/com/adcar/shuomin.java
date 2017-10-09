package com.adcar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class shuomin
  extends Activity
{
  Button BT;
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.shuomin);
    setTitle("使用说明");
    this.BT = ((Button)findViewById(R.id.But));
    this.BT.getBackground().setAlpha(50);
    this.BT.setOnClickListener(new JK());
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.activity_main, paramMenu);
    return true;
  }
  
  class JK
    implements View.OnClickListener
  {
    JK() {}
    
    public void onClick(View paramView)
    {
      shuomin.this.finish();
      Intent localIntent = new Intent(shuomin.this, MainActivity.class);
      shuomin.this.startActivity(localIntent);
    }
  }
}


/* Location:              /root/game1/classes-dex2jar.jar!/com/adcar/shuomin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */