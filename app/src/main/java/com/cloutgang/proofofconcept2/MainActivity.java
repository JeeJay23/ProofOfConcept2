package com.cloutgang.proofofconcept2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.cloutgang.proofofconcept2.MESSAGE";

    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDataBase = FirebaseDatabase.getInstance().getReference();
    }

    public void sendMessage(View view)
    {
        // create intent
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        // get the EditText
        EditText editText = (EditText) findViewById(R.id.txtKey);
        // get string from the EditText
        String message = editText.getText().toString();
        // pass along string to new activity
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void SendToFireBase(View view)
    {
        String key = ((EditText)findViewById(R.id.txtKey)).getText().toString();
        String value = ((EditText)findViewById(R.id.txtValue)).getText().toString();

        // quick way of afhelajgfoaewjfoiawejfoijaweoijfe ojfoia ejfoiewj ofjewoifj weoiafjoiwej faowejfoiwje oijcessing child objects
//        mDataBase.child(key).setValue(value);

        // cleaner way
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(key);
        myRef.setValue(value);


        // Create hasmap for more complicated data
//        HashMap<String, String> dataMap = new HashMap<String, String>();
//        dataMap.put("Name", key);
//        dataMap.put("Email", value);

        // Use push for a generated key
//        mDataBase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful())
//                {
//                    Toast.makeText(
//                            MainActivity.this,
//                            "Data Saved!",
//                            Toast.LENGTH_LONG).show();
//
//                }
//                else
//                {
//                    Toast.makeText(
//                            MainActivity.this,
//                            "Error, something went wrong",
//                            Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });
    }


}
