package pl.edu.agh.iisg.to.to2project.app.stats.util.entity.calendar;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The year view shows the months.
 *
 * @author Christian Schudt
 */
final class YearView extends DatePane {

    private static final String CSS_CALENDAR_YEAR_VIEW = "calendar-year-view";
    private static final String CSS_CALENDAR_MONTH_BUTTON = "calendar-month-button";


    public YearView(final CalendarView calendarView) {
        super(calendarView);

        getStyleClass().add(CSS_CALENDAR_YEAR_VIEW);

        // When the locale changes, update the contents (month names).
        calendarView.localeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                updateContent();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildContent() {

        // Get the number of months. I read, there are some lunar calendars, with more than 12 months.
        int numberOfMonths = calendarView.getCalendar().getMaximum(Calendar.MONTH) + 1;

        int numberOfColumns = 3;

        for (int i = 0; i < numberOfMonths; i++) {
            final int j = i;
            Button button = new Button();
            button.getStyleClass().add(CSS_CALENDAR_MONTH_BUTTON);

            // Make the button stretch.
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(Double.MAX_VALUE);
            setVgrow(button, Priority.ALWAYS);
            setHgrow(button, Priority.ALWAYS);

            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String columnProp = "gridpane-column";
                    String rowProp = "gridpane-row";
                    int column = (int)button.getProperties().get(columnProp);
                    int row = (int)button.getProperties().get(rowProp);
                    int month = row*numberOfColumns + column;
                    int year = Integer.parseInt(title.getValue());
                    Date date = null;
                    if (calendarView.isFromCalendar) {
                        date = new Date(year - 1900, month, 1);
                    } else {
                        Calendar mycal = new GregorianCalendar(year, month, 1);
                        int day = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                        date = new Date(year - 1900, month, day);
                    }
                    calendarView.selectedDate.set(date);
                }
            });
            int rowIndex = i % numberOfColumns;
            int colIndex = (i - rowIndex) / numberOfColumns;
            add(button, rowIndex, colIndex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateContent() {
        DateFormatSymbols symbols = new DateFormatSymbols(calendarView.localeProperty().get());
        String[] monthNames = symbols.getShortMonths();
        for (int i = 1; i < monthNames.length; i++) {
            Button button = (Button) getChildren().get(i - 1);
            button.setText(monthNames[i - 1]);
        }
        title.set(getDateFormat("yyyy").format(calendarView.getCalendar().getTime()));
    }
}
