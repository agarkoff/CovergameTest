package ru.misterparser.covergametest;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static ru.misterparser.covergametest.Consts.CSV_COUNT;
import static ru.misterparser.covergametest.Consts.CSV_DIRECTORY;
import static ru.misterparser.covergametest.Consts.THREAD_COUNT;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer, List<CsvObject>> map = new ConcurrentHashMap<>(); // ключ - id, значение - счётчик
        List<CsvObject> all = new CopyOnWriteArrayList<>();
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < CSV_COUNT; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try (Reader reader = Files.newBufferedReader(Paths.get(CSV_DIRECTORY.getAbsolutePath(), finalI + ".csv"))) {
                    CsvToBean<CsvObject> csvObjects = new CsvToBeanBuilder<CsvObject>(reader)
                            .withType(CsvObject.class)
                            .build();
                    for (CsvObject csvObject : csvObjects) {
                        map.merge(csvObject.getId(), new CopyOnWriteArrayList<>(Collections.singletonList(csvObject)), (t, u) -> {
                            CopyOnWriteArrayList<CsvObject> list = new CopyOnWriteArrayList<>(t);
                            list.addAll(u);
                            return list;
                        });
                        all.add(csvObject);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        assert map.values().stream().mapToLong(Collection::size).sum() == all.size();
        map.values().forEach(Collections::sort);
        List<CsvObject> result = new ArrayList<>();
        int i = 0;
        while (result.size() < 1000 && i < 20) {
            for (Integer key : map.keySet()) {
                List<CsvObject> list = map.get(key);
                if (i < list.size()) {
                    result.add(list.get(i));
                }
            }
            i++;
        }
        //Map<Integer, List<CsvObject>> collect = result.stream().collect(Collectors.groupingBy(CsvObject::getId));
        result.forEach(System.out::println);
    }
}
