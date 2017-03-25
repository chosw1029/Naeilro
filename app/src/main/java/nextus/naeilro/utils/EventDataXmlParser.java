package nextus.naeilro.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import nextus.naeilro.model.EventAPI;

/**
 * Created by chosw on 2017-01-03.
 */

public class EventDataXmlParser {

    private ArrayList<EventAPI> eventList = new ArrayList<>();
    private int itemCount = 12;
    private String key="AYfA0yQD4QNdFo%2FGK9SBlBh%2Fvb7pAk3hWEWZOYspVpY0mfbJbsGjqpZ1wjalXRHbhLqyBNxHVybiDJ%2FrdC5lHw%3D%3D";

    public EventDataXmlParser()
    {
    }

    public ArrayList<EventAPI> getEventList() { return eventList; }

    public void getContentID()
    {
        try {

            URL url = new URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=" +
                    key+"&contentTypeId=15&areaCode=&sigunguCode=&cat1=&cat2=&cat3=&listYN=Y&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&arrange=R&numOfRows="+itemCount+"&pageNo=1"); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is, "UTF-8"));

            int eventType = parser.getEventType();

            EventAPI eventAPI = null;
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("item"))
                        {
                            eventAPI = new EventAPI();
                        }
                        else if(startTag.equals("contentid"))
                        {
                            eventAPI.setContentid(parser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("item")) {
                            eventList.add(eventAPI);
                        }
                        break;
                }
                eventType = parser.next();
            }

        }catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        xmlParsing();
    }

    public void xmlParsing() {

        for(int i=0; i<eventList.size(); i++)
        {
            String queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?ServiceKey=" +
                    key+"&contentTypeId=15&contentId="+eventList.get(i).getContentid()+"&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&transGuideYN=Y";
            try {

                URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
                InputStream is = url.openStream();  //url위치로 입력스트림 연결

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    switch (eventType)
                    {
                        case XmlPullParser.START_TAG:
                            String startTag = parser.getName();
                            if(startTag.equals("item"));

                            if(startTag.equals("addr1"))
                            {
                                eventList.get(i).setAddr(parser.nextText());
                            }
                            else if(startTag.equals("firstimage"))
                            {
                                eventList.get(i).setImgUrl(parser.nextText());
                            }
                            else if(startTag.equals("title"))
                            {
                                eventList.get(i).setTitle(parser.nextText());
                            }
                            else if(startTag.equals("tel"))
                            {
                                eventList.get(i).setTel(parser.nextText());
                            }
                            else if(startTag.equals("mapX"))
                            {
                                eventList.get(i).setMapX(parser.nextText());
                            }
                            else if(startTag.equals("mapY"))
                            {
                                eventList.get(i).setMapY(parser.nextText());
                            }
                            else if(startTag.equals("overview"))
                            {
                                eventList.get(i).setOverview(parser.nextText());
                            }
                            else if(startTag.equals("homepage"))
                            {
                                eventList.get(i).setSite(parser.nextText());
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }
                    eventType = parser.next();
                }

            }catch(XmlPullParserException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        detailParsing();
    }

    public void detailParsing() {

        for(int i=0; i<eventList.size(); i++)
        {
            String queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro?ServiceKey=" +
                    key+"&contentTypeId=15&contentId="+eventList.get(i).getContentid()+"&MobileOS=ETC&MobileApp=TourAPI3.0_Guide&introYN=Y";
            try {

                URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
                InputStream is = url.openStream();  //url위치로 입력스트림 연결

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                int eventType = parser.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    switch (eventType)
                    {
                        case XmlPullParser.START_TAG:
                            String startTag = parser.getName();
                            if(startTag.equals("item"));

                            if(startTag.equals("eventenddate"))
                            {
                                eventList.get(i).setEndDate(parser.nextText());
                            }
                            else if(startTag.equals("eventstartdate"))
                            {
                                eventList.get(i).setStartDate(parser.nextText());
                            }
                            else if(startTag.equals("playtime"))
                            {
                                eventList.get(i).setPlayTime(parser.nextText());
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }
                    eventType = parser.next();
                }

            }catch(XmlPullParserException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
