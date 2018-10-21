package ru.misterparser.covergametest;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.RandomStringGenerator;

@SuppressWarnings("WeakerAccess")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvObject implements Comparable {

    private static final RandomStringGenerator RANDOM_STRING_GENERATOR = new RandomStringGenerator.Builder().build();

    @CsvBindByPosition(position = 0)
    private int id;

    @CsvBindByPosition(position = 1)
    private String name;

    @CsvBindByPosition(position = 2)
    private String condition;

    @CsvBindByPosition(position = 3)
    private String state;

    @CsvBindByPosition(position = 4)
    private float price;

    @Override
    public int compareTo(Object o) {
        return Float.compare(price, ((CsvObject) o).getPrice());
    }

    static CsvObject createRandom() {
        return new CsvObjectBuilder()
                .id(RandomUtils.nextInt(0, 35))
                .name(RandomStringUtils.randomAlphabetic(12))
                .condition(RandomStringUtils.randomAlphabetic(12))
                .state(RandomStringUtils.randomAlphabetic(12))
                .price(RandomUtils.nextFloat(50, 100))
                .build();
    }
}
