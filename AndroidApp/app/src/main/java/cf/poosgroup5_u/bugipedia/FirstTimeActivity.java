package cf.poosgroup5_u.bugipedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cf.poosgroup5_u.bugipedia.utils.AppUtils;

public class FirstTimeActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView mDots[];

    private SliderAdapter sliderAdapter;

    private Button mNextBtn;
    private Button mBackBtn;
    private int mCurrentPage;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout =  findViewById(R.id.dotsLayout);

        mNextBtn = findViewById(R.id.nextBtn);
        mBackBtn = findViewById(R.id.prevBtn);

        sliderAdapter = new SliderAdapter(this);

        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0 );

        mSlideViewPager.addOnPageChangeListener(viewListener);

    //COMMENTED THIS SECTION SO THAT MY BUTTONS DONT DO ANYTHING(DON'T GO BACK AND NEXT.)
        //OnClick Listeners

        // This shall be a 'Skip' or a 'Close' button
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSlideViewPager.setCurrentItem(mCurrentPage + 1);

                //tell everyone that the user has passed the slideshow
                AppUtils.setFirstTimeUse(false, context);
            }
        });

        // Layout of this is temporary, login button should be on the last slider page.
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                //tell everyone that the user has passed the slideshow
//                AppUtils.setFirstTimeUse(false, context);
            }
        });
    }

    //Enable slide dots
    public void addDotsIndicator(int position) {
        //mDots = new TextView[3];
        mDots = new TextView[4];

        mDotLayout.removeAllViews();

        for(int i=0; i < mDots.length ; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0 ){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage = i;

//COMMENTED THIS SECTION BECAUSE i ONLY NEED LOGIN/SEARCH DB INSTEAD OF BACK AND NEXT.
//            if(i == 0){
//                mNextBtn.setEnabled(true);
//                mBackBtn.setEnabled(false);
//                mBackBtn.setVisibility(View.INVISIBLE);
//
//                mNextBtn.setText("Next");
//                mBackBtn.setText("");

//            } else if ( i == mDots.length - 1){
//                mNextBtn.setEnabled(true);
//                mBackBtn.setEnabled(true);
//                mBackBtn.setVisibility(View.VISIBLE);
//
//                mNextBtn.setText("Finish");
//                mBackBtn.setText("Back");
//            } else {
//                mNextBtn.setEnabled(true);
//                mBackBtn.setEnabled(true);
//                mBackBtn.setVisibility(View.VISIBLE);

//                mNextBtn.setText("Next");
//                mBackBtn.setText("Back");
//            }

            for(i=0; i<mDots.length; i++) {
                mNextBtn.setEnabled(true);
                mBackBtn.setEnabled(true);
                mBackBtn.setVisibility(View.VISIBLE);

//                mNextBtn.setText("Next");
//                mBackBtn.setText("Back");

                mNextBtn.setText(getString(R.string.search_database));
                mBackBtn.setText(getString(R.string.login)); //I NEED TO SPECIFY THIS IN THE ENTER TEXT HERE.. THAT YOU CAN SEARCH DATABASE(CLARIFY ACRONYM)... OR LOGIN
                                                     //lOOK AT ALL THE 4 FEATURES YOU CAN DO :)
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };
}
