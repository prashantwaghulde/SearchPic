package com.androidchallenge.searchpic.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidchallenge.searchpic.R;
import com.androidchallenge.searchpic.adapter.CardAdapter;
import com.androidchallenge.searchpic.interfaces.LoadMore;
import com.androidchallenge.searchpic.model.CardItem;
import com.androidchallenge.searchpic.network.FetchImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rx.functions.Func0;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final Activity activity = this;
    public static ArrayList<CardItem> items;
    public static ArrayList<CardItem> itemsServer;
    public static String searchText = "";
    public CardAdapter adapter;
    private ScrollView scrollView;
    public static TextView emptyTextView;
    public static boolean isOnLoadMore = false;
    private Context context;
    private static int pageNumber = 1;
    private static ImageView imageView1,imageView2,imageView3;
    private static TextView textView1,textView2,textView3;
    private static LinearLayout linearLayout1,linearLayout2,linearLayout3;

    public static RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        items = new ArrayList<>();

        setHomeScreenData();



        scrollView = (ScrollView)findViewById(R.id.sv_home_screen);
        scrollView.setVisibility(View.VISIBLE);
        emptyTextView =(TextView) findViewById(R.id.tv_empty_view);

        recyclerView = (RecyclerView) findViewById(R.id.rv_picture_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.GONE);
        adapter = new CardAdapter(recyclerView,MainActivity.this,items);
        recyclerView.setAdapter(adapter);
        adapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    public void run() {
                        items.add(null);
                        adapter.notifyItemInserted(items.size()-1);
                    }
                });
                isOnLoadMore = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        items.remove(items.size() -1);
                        adapter.notifyItemRemoved(items.size());
                        adapter.setRxData(searchText,pageNumber);
                        adapter.notifyDataSetChanged();
                    }
                },3000);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search_item);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        int options = searchView.getImeOptions();
        searchView.setImeOptions(options | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        searchView.setMaxWidth(Integer.MAX_VALUE);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                pageNumber =1;
                Toast.makeText(MainActivity.this, "QuerySubmitted", Toast.LENGTH_SHORT).show();
                searchText = s;
                items = new ArrayList<>();
                adapter.setRxData(s,pageNumber);
                adapter.setLoaded();
                recyclerView.setVisibility(View.VISIBLE);
                isOnLoadMore = false;
               // adapter.notifyDataSetChanged();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                if(s!="") {
//                    pageNumber = 1;
//                    adapter.setRxData(s, pageNumber);
//                    adapter.notifyDataSetChanged();
//                }
                return false;
            }
        });


        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                scrollView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.VISIBLE);
                emptyTextView.setText(getResources().getString(R.string.type_to_begin));

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
               // items = new ArrayList<>();
                adapter.removeItems();
                searchText="";
                pageNumber =1;
                //adapter.setData(items);
                //adapter.notifyDataSetChanged();
                scrollView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return true;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }


    public static rx.Observable<ArrayList<CardItem>> getListObservable(String s,int pageNo){
        return rx.Observable
                .defer(new Func0<rx.Observable<ArrayList<CardItem>>>() {
                    @Override
                    public rx.Observable<ArrayList<CardItem>> call() {
                       try {
                           return rx.Observable.just(backgroundTask(s,pageNo));
                       }catch (Exception e) {
                           e.printStackTrace();
                           return null;
                       }
                    }
                });
    }




    public static ArrayList<CardItem> backgroundTask(String s, int page){

            itemsServer = new ArrayList<>();
            itemsServer = FetchImage.getImageData(s, page);
            items.addAll(itemsServer);
            pageNumber = page + 1;

            return items;

    }




    public ArrayList<CardItem>getData(){

        items = new ArrayList<>();
        CardItem item1 = new CardItem("Clearing a path through the ocean; Moses-big","https://i.imgur.com/ln4vf37.jpg");
        CardItem item2 = new CardItem("Ocean Of Emotion (Poem)","https://i.imgur.com/GWIQD43.png");
        CardItem item3 = new CardItem("Rare water spout on the ocean","https://i.imgur.com/toSeosw.jpg");


        items.add(item1);
        items.add(item2);
        items.add(item3);

        return items;
    }

    public void setHomeScreenData(){

        linearLayout1 = (LinearLayout) findViewById(R.id.ll_card1);
        linearLayout2 = (LinearLayout) findViewById(R.id.ll_card2);
        linearLayout3 = (LinearLayout) findViewById(R.id.ll_card3);

        imageView1 = (ImageView) findViewById(R.id.iv_card_image1);
        imageView2 = (ImageView) findViewById(R.id.iv_card_image2);
        imageView3 = (ImageView) findViewById(R.id.iv_card_image3);

        textView1 = (TextView) findViewById(R.id.tv_card_view_item1);
        textView2 = (TextView) findViewById(R.id.tv_card_view_item2);
        textView3 = (TextView) findViewById(R.id.tv_card_view_item3);


        textView1.setText("Clearing a path through the ocean; Moses-big");
        textView2.setText("Ocean Of Emotion (Poem)");
        textView3.setText("Rare water spout on the ocean");

        Picasso.with(context).load("https://i.imgur.com/ln4vf37.jpg").resize(600,400).centerCrop().into(imageView1);
        Picasso.with(context).load("https://i.imgur.com/GWIQD43.png").resize(600,400).centerCrop().into(imageView2);
        Picasso.with(context).load("https://i.imgur.com/toSeosw.jpg").resize(600,400).centerCrop().into(imageView3);

        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ll_card1:
                 goToDetailScreen(imageView1,"https://i.imgur.com/ln4vf37.jpg","Clearing a path through the ocean; Moses-big");
                break;

            case R.id.ll_card2:
                goToDetailScreen(imageView2,"https://i.imgur.com/GWIQD43.png","Ocean Of Emotion (Poem)");
                break;

            case R.id.ll_card3:
                goToDetailScreen(imageView3,"https://i.imgur.com/toSeosw.jpg","Rare water spout on the ocean");
                break;
            default:
        }
    }



    public void goToDetailScreen(ImageView imageView,String imageUrl,String imageTxt){
        Intent intent = new Intent(context, ImageDetail.class);
        Bundle bundle = ActivityOptions.
                makeSceneTransitionAnimation(
                        activity,
                        imageView,
                        imageView.getTransitionName())
                .toBundle();
        intent.putExtra("image", imageUrl);
        intent.putExtra("title", imageTxt);

        context.startActivity(intent,bundle);
    }
}
