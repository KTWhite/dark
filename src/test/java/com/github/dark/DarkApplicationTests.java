package com.github.dark;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class DarkApplicationTests {

    @Test
    void contextLoads() {
        String address = "中山北路南京大学仙林校区";
        List<String> list = new ArrayList<>();
        list.add("南京");
        list.add("大学");
        list.add("仙林校区");
        list.add("中山北路");
        list.stream().sorted(Comparator.comparing(a->address.indexOf(a))).forEach(System.out::println);
    }

}
