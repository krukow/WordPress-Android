package org.wordpress.android.ui.posts;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import org.wordpress.android.WordPress;
import org.wordpress.android.fluxc.model.PostModel;
import org.wordpress.android.fluxc.model.SiteModel;
import org.wordpress.android.fluxc.store.PostStore;
import org.wordpress.android.fluxc.store.SiteStore;

import java.util.List;

import javax.inject.Inject;

/**
 * https://developer.android.com/training/search/setup.html
 */

public class PostsListSearchActivity extends ListActivity {
    @Inject SiteStore mSiteStore;
    @Inject PostStore mPostStore;

    private SiteModel mSite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WordPress) getApplication()).component().inject(this);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mSite = mSiteStore.getSites().get(0);
            String query = intent.getStringExtra(SearchManager.QUERY);
            List<PostModel> matches = mPostStore.searchPostTitles(mSite, query);
            if (matches.isEmpty()) {
                setListAdapter(null);
            } else {
                updateList(matches);
            }
        }
    }

    private void updateList(@NonNull List<PostModel> newItems) {
        String[] listText = new String[newItems.size()];
        for (int i = 0; i < newItems.size(); ++i) {
            listText[i] = newItems.get(i).getTitle();
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listText);
        setListAdapter(adapter);
    }
}
