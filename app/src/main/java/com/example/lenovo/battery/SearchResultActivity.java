package com.example.lenovo.battery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        handleIntent(getIntent());
    }
    private void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearching(query);
        }
    }
    private void doSearching(String query){
        Toast.makeText(SearchResultActivity.this,query,Toast.LENGTH_SHORT).show();
    }  //搜索结果

    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        handleIntent(intent);
    }
}
