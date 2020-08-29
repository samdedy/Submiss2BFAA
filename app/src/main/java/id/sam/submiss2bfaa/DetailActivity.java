package id.sam.submiss2bfaa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import id.sam.submiss2bfaa.adapter.SectionsPagerAdapter;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class DetailActivity extends AppCompatActivity {
    private CircleImageView imgAvatar;
    private TextView txtName,txtUsername,txtBio,txtCompany,txtLocation,txtUrl,txtRepo,txtGist,txtFollowing,txtFollower;
    private ProgressBar progressBar;
    private ImageView imgCompany, imgLocation, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtUsername = findViewById(R.id.txtUsername);
        txtBio = findViewById(R.id.txtBio);
        txtCompany = findViewById(R.id.txtCompany);
        txtLocation = findViewById(R.id.txtLocation);
        txtUrl = findViewById(R.id.txtUrl);
        txtRepo = findViewById(R.id.txtRepo);
        txtGist = findViewById(R.id.txtGist);
        txtFollowing = findViewById(R.id.txtFollowing);
        txtFollower = findViewById(R.id.txtFollower);
        progressBar = findViewById(R.id.progressBar);
        imgCompany = findViewById(R.id.imgCompany);
        imgLocation = findViewById(R.id.imgLocation);
        imgUrl = findViewById(R.id.imgUrl);

        try {
            Bundle mBundle = new Bundle();
            mBundle.putString("username", getIntent().getStringExtra("detail"));
            FollowingFragment mFollowingFragment = new FollowingFragment();
            mFollowingFragment.setArguments(mBundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.viewPager, mFollowingFragment)
                    .commit();
        } catch (Exception e){
            e.printStackTrace();
        }

        getDetailUser(getIntent().getStringExtra("detail"));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        getSupportActionBar().setElevation(0);
    }

    private void getDetailUser(String username){
        progressBar.setVisibility(View.VISIBLE);

        String apiKey = "e90d44e430119303a084ae702255a673d94decf7";
        String url = "https://api.github.com/users/"+username;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "token "+apiKey);
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);

                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);

                    String avatar = responseObject.getString("avatar_url");
                    String name = responseObject.getString("name");
                    String username = responseObject.getString("login");
                    String bio = responseObject.getString("bio");
                    String company = responseObject.getString("company");
                    String location = responseObject.getString("location");
                    String url = responseObject.getString("html_url");
                    String repo = responseObject.getString("public_repos");
                    String gist = responseObject.getString("public_gists");
                    String following = responseObject.getString("following");
                    String followers = responseObject.getString("followers");

                    Picasso.get().load(avatar).transform(new CropCircleTransformation()).into(imgAvatar);
                    if (!name.equals("null")){txtName.setText(name);} else {txtName.setVisibility(View.GONE);};
                    if (!username.equals("null")){txtUsername.setText(username);} else {txtUsername.setVisibility(View.GONE);}
                    if (!bio.equals("null")){txtBio.setText(bio);}else {txtBio.setVisibility(View.GONE);};
                    if (!company.equals("null")){txtCompany.setText(company);}else {txtCompany.setVisibility(View.GONE);imgCompany.setVisibility(View.GONE);}
                    if (!location.equals("null")){txtLocation.setText(location);}else {txtLocation.setVisibility(View.GONE);imgLocation.setVisibility(View.GONE);}
                    if (!url.equals("null")){txtUrl.setText(url);}else {txtUrl.setVisibility(View.GONE);imgUrl.setVisibility(View.GONE);}
                    txtRepo.setText(repo);
                    txtGist.setText(gist);
                    txtFollowing.setText(following);
                    txtFollower.setText(followers);
                } catch (Exception e){
                    Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("DetailActivity", e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbidden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage =  statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}