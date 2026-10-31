package com.hammatov.MyFirstTestAppSpringBoot.hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
public class HelloController {

    // Коллекции создаются при первом обращении: до первого вызова update-метода они равны null
    private ArrayList<String> arrayList;
    private HashMap<Integer, String> hashMap;

    // Счётчик для автоинкремента ключей HashMap (первый ключ равен 1)
    private int nextKey = 1;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        log.info("GET /hello: получен параметр name = '{}'", name);
        String result = String.format("Hello %s!", name);
        log.info("GET /hello: ответ клиенту: {}", result);
        return result;
    }

    // synchronized: контроллер существует в единственном экземпляре,
    // а запросы могут приходить из разных потоков одновременно
    @GetMapping("/update-array")
    public synchronized String updateArrayList(@RequestParam("s") String s) {
        log.info("GET /update-array: получен параметр s = '{}'", s);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            log.info("GET /update-array: первый вызов, создан пустой ArrayList<String>");
        }
        arrayList.add(s);
        log.info("GET /update-array: строка '{}' добавлена, элементов в ArrayList: {}", s, arrayList.size());
        return String.format("Строка '%s' добавлена в ArrayList. Элементов в списке: %d", s, arrayList.size());
    }

    @GetMapping("/show-array")
    public synchronized String showArrayList() {
        log.info("GET /show-array: запрос на вывод элементов ArrayList");
        if (arrayList == null || arrayList.isEmpty()) {
            log.info("GET /show-array: ArrayList ещё не создан или пуст");
            return "ArrayList пуст. Добавьте элементы через /update-array?s=текст";
        }
        log.info("GET /show-array: в ArrayList {} элемент(ов): {}", arrayList.size(), arrayList);
        return String.format("Элементы ArrayList (%d): %s", arrayList.size(), arrayList);
    }

    @GetMapping("/update-map")
    public synchronized String updateHashMap(@RequestParam("s") String s) {
        log.info("GET /update-map: получен параметр s = '{}'", s);
        if (hashMap == null) {
            hashMap = new HashMap<>();
            log.info("GET /update-map: первый вызов, создан пустой HashMap<Integer, String>");
        }
        int key = nextKey++;
        hashMap.put(key, s);
        log.info("GET /update-map: пара {}={} добавлена, элементов в HashMap: {}", key, s, hashMap.size());
        return String.format("Строка '%s' добавлена в HashMap под ключом %d. Элементов в HashMap: %d",
                s, key, hashMap.size());
    }

    @GetMapping("/show-map")
    public synchronized String showHashMap() {
        log.info("GET /show-map: запрос на вывод элементов HashMap");
        if (hashMap == null || hashMap.isEmpty()) {
            log.info("GET /show-map: HashMap ещё не создан или пуст");
            return "HashMap пуст. Добавьте элементы через /update-map?s=текст";
        }
        log.info("GET /show-map: в HashMap {} элемент(ов): {}", hashMap.size(), hashMap);
        return String.format("Элементы HashMap (%d): %s", hashMap.size(), hashMap);
    }

    // Написание «lenght» в URL сохранено, как в методичке
    @GetMapping("/show-all-lenght")
    public synchronized String showAllLenght() {
        int arraySize = (arrayList == null) ? 0 : arrayList.size();
        int mapSize = (hashMap == null) ? 0 : hashMap.size();
        log.info("GET /show-all-lenght: ArrayList = {}, HashMap = {}", arraySize, mapSize);
        return String.format("Элементов в ArrayList: %d, элементов в HashMap: %d", arraySize, mapSize);
    }

}
