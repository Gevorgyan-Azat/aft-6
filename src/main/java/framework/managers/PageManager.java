package framework.managers;

import framework.pages.BasePage;

import java.util.HashMap;
import java.util.Map;

public class PageManager {

    private static PageManager INSTANCE = null;
    private Map<String, BasePage> mapPages = new HashMap<>();

    private PageManager() {
    }

    public static PageManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new PageManager();
        }
        return INSTANCE;
    }

    public <T extends BasePage> T getPage(Class<T> ex) {
        if (mapPages.isEmpty() || mapPages.get(ex.getName()) == null) {
            try {
                mapPages.put(ex.getName(), ex.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return (T) mapPages.get(ex.getName());
    }

    void clearMapPages() {
        mapPages.clear();
    }
}
