package com.laioffer.tinnews.ui.save;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.laioffer.tinnews.databinding.FragmentSaveBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.repositiry.NewsRepository;
import com.laioffer.tinnews.repositiry.NewsViewModelFactory;

import java.util.List;

public class SaveFragment extends Fragment {
    private FragmentSaveBinding binding;
    private SaveViewModel viewModel;

    public SaveFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSaveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SavedNewsAdapter savedNewsAdapter = new SavedNewsAdapter(new SavedNewsAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Article article) {
                Log.d("onOpenDetails", article.toString());
            }

            @Override
            public void onRemoveFavorite(Article article) {
                viewModel.deleteSavedArticle(article);
            }
        });
        binding.newsSavedRecyclerView.setAdapter(savedNewsAdapter);
        binding.newsSavedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        NewsRepository repository = new NewsRepository((getContext()));
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository))
                .get(SaveViewModel.class);
        viewModel.getAllSavedArticles()
                .observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
                    @Override
                    public void onChanged(List<Article> articles) {
                        Log.d("SavedFragment", "" + articles);
                        if (articles != null) {
                            savedNewsAdapter.setArticles(articles);
                        }
                    }
                });
    }
}