package project.dat250.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class FBinitialize {
    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream("./poll-app-96148-firebase-adminsdk-pbd6z-812999fa4c.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://poll-app-96148.firebaseio.com")
                    .build();
            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Firestore getFirebase(){
        return FirestoreClient.getFirestore();
    }
}