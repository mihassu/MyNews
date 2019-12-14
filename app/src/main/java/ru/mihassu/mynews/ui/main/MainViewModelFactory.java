package ru.mihassu.mynews.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mihassu.mynews.domain.repository.ArticleRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private ArticleRepository repository;

    public MainViewModelFactory(ArticleRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(repository);
        }
        return null;
    }
}
