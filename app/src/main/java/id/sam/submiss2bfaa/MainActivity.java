package id.sam.submiss2bfaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import id.sam.submiss2bfaa.adapter.UserAdapter;
import id.sam.submiss2bfaa.model.UserItems;

public class MainActivity extends AppCompatActivity {
    private UserAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        showLoading(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    showLoading(true);
                    if (TextUtils.isEmpty(newText)) return false;
                    setUser(newText);
                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.language:
                Toast.makeText(MainActivity.this, "Menu ini masih dalam pengembangan", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    private void showLoading(Boolean state){
        if (state){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setUser(final String usernames){
        final ArrayList<UserItems> listItems = new ArrayList<>();

        String apiKey = "e90d44e430119303a084ae702255a673d94decf7";
        String url = "https://api.github.com/search/users?q="+usernames;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token "+apiKey);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("items");

                    for (int i=0; i<list.length(); i++){
                        JSONObject user = list.getJSONObject(i);
                        UserItems userItems = new UserItems();
                        userItems.setId(user.getInt("id"));
                        userItems.setUsername(user.getString("login"));
                        userItems.setUrl(user.getString("html_url"));
                        userItems.setAvatar(user.getString("avatar_url"));
                        listItems.add(userItems);
                    }
                    adapter.setData(listItems);
                    showLoading(false);
                } catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }
}