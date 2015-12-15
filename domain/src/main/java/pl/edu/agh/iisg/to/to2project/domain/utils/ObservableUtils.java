package pl.edu.agh.iisg.to.to2project.domain.utils;

import javafx.collections.*;

/**
 * @author Wojciech Pachuta.
 */
public class ObservableUtils {

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

    public static <T> ObservableList<T> observableList(ObservableSet<T> set) {
        final ObservableList<T> list = FXCollections.observableArrayList();
        list.addAll(set);
        set.addListener((SetChangeListener<T>) c -> {
            if (c.wasAdded()) {
                list.add(c.getElementAdded());
            }
            if (c.wasRemoved()) {
                list.remove(c.getElementRemoved());
            }
        });
        return list;
    }

}
