package nextus.naeilro.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chosw on 2016-09-24.
 */

public class CategoryData {

    public ArrayList<List<String>> category = new ArrayList<>();
    public List<String> do_01 = new ArrayList<>();  // 경기도
    public List<String> do_02 = new ArrayList<>();  // 강원도
    public List<String> do_03 = new ArrayList<>();  // 광역시
    public List<String> do_04 = new ArrayList<>();  // 서울
    public List<String> do_05 = new ArrayList<>();  // 충청도
    public List<String> do_06 = new ArrayList<>();  // 전라도
    public List<String> do_07 = new ArrayList<>();  // 경상도
    public List<String> do_08 = new ArrayList<>();  // 전체

    public CategoryData(){
        setData();


    }

    public void setData()
    {
        do_01.add("가평"); do_01.add("수원");
        do_02.add("정동진"); do_02.add("춘천"); do_02.add("정선"); do_02.add("평창");
        do_03.add("광주");do_03.add("대전");do_03.add("부산");do_03.add("울산");do_03.add("대구");do_03.add("인천");
        do_04.add("서울");
        do_05.add("단양");do_05.add("온양온천");do_05.add("대천");
        do_06.add("여수EXPO");do_06.add("전주");do_06.add("순천");do_06.add("남원");do_06.add("정읍");do_06.add("목포");do_06.add("곡성");do_06.add("군산");do_06.add("담양");do_06.add("보성");
        do_07.add("진주");do_07.add("포항");do_07.add("경주");do_07.add("안동");do_07.add("영주");

        category.add(do_01);
        category.add(do_02);
        category.add(do_03);
        category.add(do_04);
        category.add(do_05);
        category.add(do_06);
        category.add(do_07);

        for(int i=0; i<category.size(); i++)
        {
            do_08.addAll(category.get(i));
        }

        Collections.sort(do_08);
        category.add(do_08);

    }
}
