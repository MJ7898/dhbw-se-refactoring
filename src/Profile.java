package refactoring;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FarmerProfile extends AppCompatActivity {

    private static String farmerIDReal;
    private FirebaseFirestore mDatabase;
    private String anzahlFarmshops;
    private ArrayList<String> farmshopList = new ArrayList<>();
    private static final String FARMERID = "com.example.linkn.myapplication";
    private TextView farmerVorname, farmerNachname;
    private ListView farmerShops;


    public FarmerProfile(){

    }
    public static void start(Context context, String farmerIDID) {
        Intent intent = new Intent(context, FarmerProfile.class);
        intent.putExtra(FARMERID, farmerIDID);
        context.startActivity(intent);
        farmerIDReal = farmerIDID;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_profile);
        Intent intent = this.getIntent();
        getShopsAndMachines(farmerIDReal);
    }


    public void showShopsOrMachines(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, farmshopList);

        farmerShops.setAdapter(adapter);



    }

    protected void getShopsAndMachines(String farmerID){
        this.farmerIDReal = farmerID;
        farmerShops = findViewById(R.id.shopListItem);
        farmerVorname = findViewById(R.id.textViewFarmerVorname);
        farmerNachname = findViewById(R.id.textViewFarmerNachname);
        mDatabase = FirebaseFirestore.getInstance();

        DocumentReference farmerProfile = mDatabase.collection("Farmer").document(farmerID);
        farmerProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String farmerVornameDB = document.getString("firstname");
                farmerVorname.setText(farmerVornameDB);
                String farmerNachnameDB = document.getString("lastname");
                farmerNachname.setText(farmerNachnameDB);

                anzahlFarmshops = document.getString("farmerid");

                // guess the size/ length of the saved farmshops in database and will add it to an arraylist
                if(anzahlFarmshops != null){
                    for(int i = 0; i< Integer.parseInt(anzahlFarmshops);i++){
                        String farmerFarmshops = document.getString(Integer.toString(i));
                            farmshopList.add(farmerFarmshops);
                    }
                }
            }
        });
        showShopsOrMachines();
        Button okButton = (Button) findViewById(R.id.buttonOK);
        View.OnClickListener onClickListener = OnClickListener -> {
            startActivity(new Intent(FarmerProfile.this, MapsActivity.class));
        };
        okButton.setOnClickListener(onClickListener);
    }

}
