package com.adcar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
//import net.youmi.android.appoffers.YoumiOffersManager;
//import net.youmi.android.appoffers.YoumiPointsManager;

public class RewardOffersSample
  extends Activity
{
  Button btnShowOffers;
  TextView tvPoints;
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.reward_offers);
    this.tvPoints = ((TextView)findViewById(R.id.tv_offers_points));
    ((Button)findViewById(R.id.fh)).getBackground().setAlpha(50);
    ((Button)findViewById(R.id.btn_show_offers)).getBackground().setAlpha(50);
    ((Button)findViewById(R.id.btn_refresh)).getBackground().setAlpha(50);
    this.btnShowOffers = ((Button)findViewById(R.id.btn_show_offers));
   this.btnShowOffers.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
      //  YoumiOffersManager.showOffers(RewardOffersSample.this, 0);
      }
    });
    findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RewardOffersSample.this.showPoints();
      }
    });
    findViewById(R.id.fh).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        RewardOffersSample.this.finish();
        Intent localIntent = new Intent(RewardOffersSample.this, MainActivity.class);
        RewardOffersSample.this.startActivity(localIntent);
      }
    });
  }
  
  protected void onResume()
  {
    super.onResume();
    showPoints();
  }
  
  void showPoints()
  {
    try
    {
      //this.tvPoints.setText(Integer.toString(YoumiPointsManager.queryPoints(this)));
      return;
    }
    catch (Exception localException) {}
  }
}


/* Location:              /root/game1/classes-dex2jar.jar!/com/adcar/RewardOffersSample.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */