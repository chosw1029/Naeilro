package nextus.naeilro.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by chosw on 2017-01-05.
 */

@Root(name = "item")
public class CityCode {
    @Element(name = "citycode") private String citycode;
    @Element(name = "cityname") private String cityname;

    public String getCityname() {
        return cityname;
    }

    public String getCitycode() {
        return citycode;
    }
}
