package my.example;

import my.RotateImageView.RotateImageView;
import my.RotateImageView.RotateImageView.OnChangedListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ExampleActivity  extends Activity implements OnChangedListener{
	private TextView subMenuTextView;
	private String[] mainmenu;
	
	private int currentPosition = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
    }
	
	private void init(){
		mainmenu = getResources().getStringArray(R.array.mainmenu);
		
		subMenuTextView = (TextView)findViewById(R.id.submenu);
		subMenuTextView.setText(mainmenu[currentPosition]);
        
        //Roulette
        RotateImageView roulette = (RotateImageView)findViewById(R.id.image);
        roulette.setMaxRoulette(mainmenu.length);
        roulette.setBackgroundResource(R.drawable.test);
        roulette.setOnChangedListener(this);
                
         
	}

	@Override
	public void onChanged(RotateImageView view, int index) {
		currentPosition = index;
		subMenuTextView.setText(mainmenu[currentPosition]);
	}
}
