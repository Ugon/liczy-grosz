package pl.edu.agh.iisg.to.to2project.app.core.utils;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * @author Wojciech Pachuta.
 */
public class ObservableMerge {
    @SafeVarargs
    public static <T> ObservableList<T> merge(ObservableList<? extends T>... lists) {
        final ObservableList<T> list = FXCollections.observableArrayList();
        for (ObservableList<? extends T> l : lists) {
            list.addAll(l);
            l.addListener((ListChangeListener.Change<? extends T> c) -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        list.addAll(c.getAddedSubList());
                    }
                    if (c.wasRemoved()) {
                        list.removeAll(c.getRemoved());
                    }
                }
            });
        }
        return list;
    }
}
