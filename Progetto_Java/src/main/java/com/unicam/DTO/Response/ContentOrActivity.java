package com.unicam.DTO.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentOrActivity {

    private Map<String, List<?>> contents;

    public ContentOrActivity(){
        this.contents = new HashMap<>();
    }

    public Map<String, List<?>> getContents() {
        return contents;
    }

    public void setContents(Map<String, List<?>> contents) {
        this.contents = contents;
    }
}