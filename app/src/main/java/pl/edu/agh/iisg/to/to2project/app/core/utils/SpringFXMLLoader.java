package pl.edu.agh.iisg.to.to2project.app.core.utils;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Wojciech Pachuta.
 */
@Component
@Scope("prototype")
public class SpringFXMLLoader {

    @Autowired
    private ApplicationContext context;

    private FXMLLoader loader;

    public Object load(final String fullyQualifiedResource) throws IOException {
            InputStream fxmlStream = getClass().getResourceAsStream(fullyQualifiedResource);
            loader = new FXMLLoader();
            loader.setControllerFactory(context::getBean);
            return loader.load(fxmlStream);
    }

    public Object getController(){
        return loader.getController();
    }
}
