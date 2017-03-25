package nextus.naeilro.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by chosw on 2017-01-05.
 */

@Root
public class ItemList {
    @ElementList(entry = "item", inline = true)
    List<CityCode> cityCodes;

    public List<CityCode> getCityCodes() {
        return cityCodes;
    }
}
