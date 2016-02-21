package pl.edu.agh.iisg.to.to2project.app.stats.util;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.service.IBasicDataSource;
import pl.edu.agh.iisg.to.to2project.service.impl.InOutWindowMockImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Wojciech Dymek on 26.12.2015.
 */
@Service
public class AccountTreeProviderUtil {
    @Autowired
    private IBasicDataSource expensesDataSource;

    @Autowired
    private InOutWindowMockImpl dataSource;

    public void selectAccountsInMenu(MenuButton button, CheckMenuItem eventSource) {
        if (eventSource.getText().equalsIgnoreCase(PropertiesUtil.ALL)) {
            if (eventSource.isSelected())
                selectAll(button);
            else
                unselectAll(button);
            return;
        }

        if (countSelected(button) == expensesDataSource.getAccounts().size() && eventSource.isSelected()) {
            selectAll(button);
            return;
        }

        if (!eventSource.isSelected()) {
            button.getItems().forEach(item -> {
                if (item.getText().equalsIgnoreCase(PropertiesUtil.ALL)) {
                    ((CheckMenuItem)item).setSelected(false);
                }
            });
        }
    }

    public List<Account> getSelectedAccounts(MenuButton button) {
        if (getButtonText(button).equalsIgnoreCase(PropertiesUtil.ALL)) {
            return new LinkedList<>(expensesDataSource.getAccounts());
        }

        List<Account> result = new LinkedList<>();

        button.getItems().forEach(item -> {
            if (item instanceof CheckMenuItem) {
                if (((CheckMenuItem)item).isSelected()) {
                    result.add(getAccountByName(expensesDataSource.getAccounts(), item.getText()));
                }
            }
        });

        return result;
    }

    public Account getAccountByName(List<Account> accounts, String name){
        for (Account account : accounts) {
            if (account.toString().equalsIgnoreCase(name)) return account;
        }
        return null;
    }

    public String getButtonText(MenuButton button){
        long count = countSelected(button);
        if (count - 1 == expensesDataSource.getAccounts().size()) {
            return PropertiesUtil.ALL;
        }

        if (count == 0) {
            return PropertiesUtil.EMPTY;
        }

        if (count == 1) {
            for (MenuItem menuItem : button.getItems()) {
                if (((CheckMenuItem) menuItem).isSelected()) {
                    return menuItem.getText();
                }
            }
        }

        return PropertiesUtil.SOME;
    }

    private long countSelected(MenuButton button) {
        return button.getItems().stream().filter(item -> item instanceof CheckMenuItem)
                .filter(item -> ((CheckMenuItem) item).isSelected()).count();
    }

    private void selectAll(MenuButton button) {
        setCommonSelectValue(button, true);
    }

    private void unselectAll(MenuButton button) {
        setCommonSelectValue(button, false);
    }

    private void setCommonSelectValue(MenuButton button, boolean value) {
        button.getItems().forEach(item -> {
            if (item instanceof CheckMenuItem) {
                ((CheckMenuItem)item).setSelected(value);
            }
        });
    }
}
