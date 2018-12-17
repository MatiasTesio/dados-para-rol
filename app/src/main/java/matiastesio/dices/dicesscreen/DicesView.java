package matiastesio.dices.dicesscreen;

import java.util.List;

public interface DicesView {

    void setDicesValue(String dices);
    void dismissDialog();
    void populateFromSharedPref(List<String> data);
}
