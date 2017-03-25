package nextus.naeilro.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by chosw on 2017-01-05.
 */

@Root(strict = false)
public class CityData {

    @Element(name = "body")
    Items items;

    public static class Items
    {
        @Element(name = "items")
        ItemList itemList;

        public ItemList getItemList() {
            return itemList;
        }
    }

    public Items getItems() {
        return items;
    }
}


