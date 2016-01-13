package pl.edu.agh.iisg.to.to2project.domain.utils;

import javafx.beans.Observable;
import javafx.collections.*;
import javafx.util.Callback;

/**
 * @author Wojciech Pachuta.
 */
public class ObservableUtils {

    @SafeVarargs
    public static <T> ObservableList<T> transferElements(ObservableList<? extends T>... lists) {
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

    public static <T> ObservableList<T> observableList(ObservableSet<T> set) {
        final ObservableList<T> list = FXCollections.observableArrayList();
        transferElements(list, set);
        return list;
    }

    public static <T> ObservableList<T> observableList(ObservableSet<T> set, Callback<T, Observable[]> extractor) {
        final ObservableList<T> list = FXCollections.observableArrayList(extractor);
        transferElements(list, set);
        return list;
    }

    private static <T> void transferElements(ObservableList<T> list, ObservableSet<T> set){
        list.addAll(set);
        set.addListener((SetChangeListener<T>) c -> {
            if (c.wasAdded()) {
                list.add(c.getElementAdded());
            }
            if (c.wasRemoved()) {
                list.remove(c.getElementRemoved());
            }
        });
    }

}
