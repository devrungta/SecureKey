import android.app.Application;
import com.google.firebase.FirebaseApp;
public class Securekey extends Application {
    @Override
    public void OnCreate(){
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
